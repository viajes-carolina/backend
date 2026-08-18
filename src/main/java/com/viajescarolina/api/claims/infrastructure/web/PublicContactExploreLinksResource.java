package com.viajescarolina.api.claims.infrastructure.web;

import com.viajescarolina.api.claims.application.dto.ContactExploreLinkDTO;
import com.viajescarolina.api.claims.application.usecase.ListContactExploreLinksUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Path("/api/public/v1/contact/explore-links")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Contact Explore Links", description = "Enlaces de exploración, soporte y Libro de Reclamaciones")
public class PublicContactExploreLinksResource {

    private final ListContactExploreLinksUseCase listContactExploreLinksUseCase;

    @Inject
    public PublicContactExploreLinksResource(ListContactExploreLinksUseCase listContactExploreLinksUseCase) {
        this.listContactExploreLinksUseCase = listContactExploreLinksUseCase;
    }

    @GET
    @Operation(summary = "Listar Enlaces de Exploración de Contacto", description = "Retorna los enlaces directos a Reclamaciones, FAQ y Mapa de oficina")
    public List<ContactExploreLinkDTO> listLinks() {
        return listContactExploreLinksUseCase.execute();
    }
}
