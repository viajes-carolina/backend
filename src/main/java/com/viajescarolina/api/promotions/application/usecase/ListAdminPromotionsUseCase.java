package com.viajescarolina.api.promotions.application.usecase;

import com.viajescarolina.api.media.domain.MediaAsset;
import com.viajescarolina.api.media.domain.MediaRepository;
import com.viajescarolina.api.promotions.application.dto.PromotionDTO;
import com.viajescarolina.api.promotions.domain.AdminPromotionFilter;
import com.viajescarolina.api.promotions.domain.HomeFeaturedPolicy;
import com.viajescarolina.api.promotions.domain.Promotion;
import com.viajescarolina.api.promotions.domain.PromotionCatalogCounters;
import com.viajescarolina.api.promotions.domain.PromotionRepository;
import com.viajescarolina.api.promotions.domain.PromotionSourceFilter;
import com.viajescarolina.api.promotions.domain.PromotionStatusFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * El catálogo de promociones del panel, una página cada vez.
 *
 * <p>La búsqueda, los filtros, el orden y el recorte los hace la base de datos: el panel
 * nunca descarga las 32 filas (ni las que vengan) para quedarse con 15.</p>
 */
@ApplicationScoped
public class ListAdminPromotionsUseCase {

    /**
     * Techo duro del tamaño de página. Existe para que un {@code ?size=10000} —a mano, por un
     * script o por un bug del cliente— no se convierta en "devuélvemelo todo" y deshaga en una
     * URL justo lo que la paginación viene a evitar.
     */
    public static final int MAX_PAGE_SIZE = 100;

    private final PromotionRepository promotionRepository;
    private final MediaRepository mediaRepository;

    @Inject
    public ListAdminPromotionsUseCase(
            PromotionRepository promotionRepository,
            MediaRepository mediaRepository) {
        this.promotionRepository = promotionRepository;
        this.mediaRepository = mediaRepository;
    }

    /**
     * Una página del catálogo del panel.
     *
     * @param items   las promociones de esta página, ya mapeadas.
     * @param total   cuántas filas cumplen los filtros (alimenta la paginación).
     * @param page    página devuelta, base 0, ya normalizada.
     * @param size    filas por página realmente aplicadas, ya acotadas a {@link #MAX_PAGE_SIZE}.
     * @param summary contadores del catálogo COMPLETO, sin filtros.
     */
    public record PromotionsPageResponse(
            List<PromotionDTO> items,
            long total,
            int page,
            int size,
            PromotionsCatalogSummary summary) {}

    /**
     * Las cuatro métricas de la cabecera del panel. Describen todo el catálogo aunque haya un
     * filtro puesto: si se calcularan sobre las filas devueltas dirían "3 promociones" con 32
     * en la base de datos.
     */
    public record PromotionsCatalogSummary(
            long total,
            long featuredInHome,
            long publishedOnFacebook,
            long hidden) {}

    public PromotionsPageResponse execute(int page, int size, String search, String status, String source, String featured) {
        int paginaEfectiva = Math.max(0, page);
        int tamanoEfectivo = Math.clamp(size, 1, MAX_PAGE_SIZE);

        // Única resolución de la portada en toda la petición: sirve a la vez para el filtro
        // featured=SI|NO, para la marca featuredInHome de cada fila y para el contador de la
        // cabecera. Un solo cálculo, ningún riesgo de que las tres versiones difieran.
        List<Long> idsEnPortada = promotionRepository.findHomeFeaturedIds();

        AdminPromotionFilter filtro = new AdminPromotionFilter(
                search,
                parseEnum(PromotionStatusFilter.class, status, "status"),
                parseEnum(PromotionSourceFilter.class, source, "source"),
                parseFeatured(featured),
                idsEnPortada);

        List<Promotion> pagina = promotionRepository.findAdminPage(filtro, paginaEfectiva, tamanoEfectivo);
        long total = promotionRepository.countAdminPage(filtro);
        PromotionCatalogCounters contadores = promotionRepository.countCatalog();

        // Batch-resolve las fotos destacadas UNA sola vez para las filas de ESTA página, en vez
        // de una query por promoción dentro del .map().
        Map<Long, MediaAsset> mediaById = ListFeaturedPromotionsUseCase.resolveMediaMap(pagina, mediaRepository);
        Set<Long> portada = Set.copyOf(idsEnPortada);

        List<PromotionDTO> items = pagina.stream()
                .map(p -> ListFeaturedPromotionsUseCase.mapToDTO(p, mediaById, portada))
                .toList();

        PromotionsCatalogSummary resumen = new PromotionsCatalogSummary(
                contadores.total(),
                // Cuántos huecos de portada están realmente ocupados: HomeFeaturedPolicy.SLOTS
                // si hay activas de sobra, menos si no las hay.
                idsEnPortada.size(),
                contadores.publishedOnFacebook(),
                contadores.hidden());

        return new PromotionsPageResponse(items, total, paginaEfectiva, tamanoEfectivo, resumen);
    }

    /**
     * Traduce el valor de un parámetro de filtro a su enum. Vacío o ausente = sin filtrar; un
     * valor desconocido es un 400, no un "devuélvelo todo" silencioso que pintaría en pantalla
     * un resultado sin relación con lo que se pidió.
     */
    private static <E extends Enum<E>> E parseEnum(Class<E> tipo, String valor, String parametro) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(tipo, valor.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Valor no válido para '" + parametro + "': " + valor);
        }
    }

    /** {@code SI} / {@code NO} sobre {@link HomeFeaturedPolicy}; vacío = todas. */
    private static Boolean parseFeatured(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return switch (valor.trim().toUpperCase(Locale.ROOT)) {
            case "SI", "SÍ" -> Boolean.TRUE;
            case "NO" -> Boolean.FALSE;
            default -> throw new BadRequestException("Valor no válido para 'featured': " + valor + " (use SI o NO)");
        };
    }
}
