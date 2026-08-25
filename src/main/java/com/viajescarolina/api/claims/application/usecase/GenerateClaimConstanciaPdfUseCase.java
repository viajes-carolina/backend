package com.viajescarolina.api.claims.application.usecase;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.viajescarolina.api.claims.domain.ClaimRecord;
import com.viajescarolina.api.claims.domain.ClaimRepository;
import com.viajescarolina.api.settings.domain.OfficeLocation;
import com.viajescarolina.api.settings.domain.OfficeRepository;
import com.viajescarolina.api.settings.domain.SettingsRepository;
import com.viajescarolina.api.settings.domain.SiteSettings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

/**
 * Genera la constancia de registro (PDF) de una hoja de reclamación, con la identidad legal
 * del proveedor (BC settings) y el resumen completo del reclamo (BC claims). Renderizado vía
 * openhtmltopdf a partir de una plantilla XHTML estricta armada con StringBuilder (no hace
 * falta un motor de plantillas para un único documento).
 */
@ApplicationScoped
public class GenerateClaimConstanciaPdfUseCase {

    private static final Locale LOCALE_PE = Locale.forLanguageTag("es-PE");
    // Formato explícito por patrón (no ofLocalizedDateTime(FormatStyle.LONG)): ese estilo
    // requiere un ZoneId para el nombre de la zona horaria, y aquí solo se dispone de un
    // ZoneOffset (OffsetDateTime), lo que provocaría DateTimeException en tiempo de ejecución.
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, HH:mm", LOCALE_PE);

    private final ClaimRepository claimRepository;
    private final SettingsRepository settingsRepository;
    private final OfficeRepository officeRepository;

    @Inject
    public GenerateClaimConstanciaPdfUseCase(ClaimRepository claimRepository,
                                              SettingsRepository settingsRepository,
                                              OfficeRepository officeRepository) {
        this.claimRepository = claimRepository;
        this.settingsRepository = settingsRepository;
        this.officeRepository = officeRepository;
    }

    /**
     * @return el PDF en bytes si el claimCode existe y documentNumber coincide exactamente con
     * el titular del reclamo; Optional.empty() en cualquier otro caso (claimCode inexistente o
     * documentNumber no coincidente) — el llamador debe traducir eso a un 404 uniforme, sin
     * distinguir la causa, para no filtrar si un claimCode existe o no.
     */
    public Optional<byte[]> execute(String claimCode, String documentNumber) {
        if (claimCode == null || claimCode.isBlank() || documentNumber == null || documentNumber.isBlank()) {
            return Optional.empty();
        }

        Optional<ClaimRecord> claimOpt = claimRepository.findByCode(claimCode.trim());
        if (claimOpt.isEmpty()) {
            return Optional.empty();
        }

        ClaimRecord claim = claimOpt.get();
        if (claim.getDocumentNumber() == null || !claim.getDocumentNumber().equalsIgnoreCase(documentNumber.trim())) {
            return Optional.empty();
        }

        SiteSettings settings = settingsRepository.findSiteSettings().orElse(null);
        OfficeLocation office = officeRepository.findOffice().orElse(null);

        String html = buildHtml(claim, settings, office);
        return Optional.of(renderPdf(html));
    }

    private byte[] renderPdf(String html) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF de constancia: " + e.getMessage(), e);
        }
    }

    private String buildHtml(ClaimRecord claim, SiteSettings settings, OfficeLocation office) {
        String legalCompanyName = esc(settings != null && settings.getLegalCompanyName() != null
                ? settings.getLegalCompanyName() : "Viajes Carolina");
        String taxId = esc(settings != null && settings.getTaxId() != null ? settings.getTaxId() : "N/D");
        String address = esc(buildOfficeAddress(office));
        String registeredAt = claim.getCreatedAt() != null
                ? claim.getCreatedAt().withOffsetSameInstant(ZoneOffset.of("-05:00")).format(DATE_FORMATTER)
                : "N/D";

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<!DOCTYPE html>");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\"/>");
        sb.append("<title>Constancia de Registro - ").append(esc(claim.getClaimCode())).append("</title>");
        sb.append("<style>");
        sb.append("body { font-family: Helvetica, Arial, sans-serif; font-size: 11px; color: #1f2937; margin: 32px; }");
        sb.append("h1 { font-size: 18px; margin-bottom: 4px; color: #111827; }");
        sb.append("h2 { font-size: 13px; margin: 18px 0 8px 0; padding-bottom: 4px; border-bottom: 1px solid #d1d5db; color: #111827; }");
        sb.append(".subtitle { font-size: 11px; color: #4b5563; margin-bottom: 18px; }");
        sb.append(".folio-box { border: 1px solid #d1d5db; border-radius: 4px; padding: 10px 14px; margin-bottom: 18px; background-color: #f9fafb; }");
        sb.append(".folio-code { font-size: 16px; font-weight: bold; color: #111827; }");
        sb.append("table { width: 100%; border-collapse: collapse; margin-bottom: 4px; }");
        sb.append("td { padding: 4px 6px; vertical-align: top; }");
        sb.append("td.label { width: 34%; color: #6b7280; font-weight: bold; }");
        sb.append(".block { border: 1px solid #e5e7eb; border-radius: 4px; padding: 8px 10px; margin-bottom: 6px; white-space: pre-wrap; }");
        sb.append(".footer { margin-top: 26px; font-size: 9px; color: #9ca3af; border-top: 1px solid #e5e7eb; padding-top: 8px; }");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<h1>").append(legalCompanyName).append("</h1>");
        sb.append("<div class=\"subtitle\">RUC: ").append(taxId).append(" &#8212; ").append(address).append("</div>");

        sb.append("<div class=\"folio-box\">");
        sb.append("<div>Constancia de Registro &#8212; Libro de Reclamaciones Virtual</div>");
        sb.append("<div class=\"folio-code\">Código de Folio: ").append(esc(claim.getClaimCode())).append("</div>");
        sb.append("<div>Fecha de registro: ").append(esc(registeredAt)).append("</div>");
        sb.append("<div>Estado actual: ").append(esc(claim.getStatus())).append("</div>");
        sb.append("</div>");

        sb.append("<h2>Identificación del Consumidor Reclamante</h2>");
        sb.append("<table>");
        appendRow(sb, "Nombre completo", claim.getFullName());
        appendRow(sb, "Documento", (claim.getDocumentType() != null ? claim.getDocumentType() : "") + " " + nvl(claim.getDocumentNumber()));
        appendRow(sb, "Correo electrónico", claim.getEmail());
        appendRow(sb, "Teléfono", claim.getPhone());
        appendRow(sb, "Domicilio", claim.getAddress());
        if (claim.isMinor()) {
            appendRow(sb, "Representante (menor de edad)", nvl(claim.getParentName()));
            appendRow(sb, "Documento del representante", nvl(claim.getParentDocument()));
        }
        sb.append("</table>");

        sb.append("<h2>Identificación del Bien Contratado</h2>");
        sb.append("<table>");
        appendRow(sb, "Tipo de bien contratado", claim.getContractedType());
        appendRow(sb, "Servicio relacionado", nvl(claim.getRelatedService()));
        appendRow(sb, "Código de reserva", nvl(claim.getReservationCode()));
        appendRow(sb, "Fecha del servicio", claim.getServiceDate() != null ? claim.getServiceDate().toString() : "N/D");
        appendRow(sb, "Monto reclamado",
                claim.getClaimedAmount() != null ? formatAmount(claim.getClaimedAmount()) + " " + nvl(claim.getCurrency()) : "N/D");
        appendRow(sb, "Canal de respuesta preferido", nvl(claim.getResponseChannel()));
        sb.append("</table>");
        sb.append("<div class=\"block\">").append(esc(claim.getDescription())).append("</div>");

        sb.append("<h2>Detalle de la Reclamación</h2>");
        sb.append("<table>");
        appendRow(sb, "Tipo", claim.getClaimType());
        sb.append("</table>");
        sb.append("<div class=\"block\">").append(esc(claim.getConsumerDetail())).append("</div>");

        sb.append("<h2>Pedido del Consumidor</h2>");
        sb.append("<div class=\"block\">").append(esc(claim.getConsumerRequest())).append("</div>");

        sb.append("<div class=\"footer\">");
        sb.append("Documento generado electrónicamente conforme al Código de Protección y Defensa del Consumidor del Perú (Ley N.º 29571). ");
        sb.append("Esta constancia acredita únicamente el registro del reclamo en el Libro de Reclamaciones Virtual; no implica la aceptación del mismo por parte del proveedor.");
        sb.append("</div>");

        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    private String buildOfficeAddress(OfficeLocation office) {
        if (office == null) return "N/D";
        StringBuilder addr = new StringBuilder();
        if (office.getAddressLine() != null) addr.append(office.getAddressLine());
        if (office.getDistrict() != null) addr.append(", ").append(office.getDistrict());
        if (office.getCity() != null) addr.append(", ").append(office.getCity());
        if (office.getCountry() != null) addr.append(", ").append(office.getCountry());
        return addr.length() > 0 ? addr.toString() : "N/D";
    }

    private String formatAmount(BigDecimal amount) {
        return amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private void appendRow(StringBuilder sb, String label, String value) {
        sb.append("<tr><td class=\"label\">").append(esc(label)).append("</td><td>").append(esc(nvl(value))).append("</td></tr>");
    }

    private String nvl(String value) {
        return value != null && !value.isBlank() ? value : "N/D";
    }

    private static String esc(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
