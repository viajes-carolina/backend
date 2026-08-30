package com.viajescarolina.api.blog.infrastructure.web;

import com.viajescarolina.api.blog.application.dto.BlogCategoryDTO;
import com.viajescarolina.api.blog.application.dto.BlogLibraryDTO;
import com.viajescarolina.api.blog.application.dto.BlogHeroDTO;
import com.viajescarolina.api.blog.application.dto.BlogPostDetailResponse;
import com.viajescarolina.api.blog.application.dto.PublicBlogResponse;
import com.viajescarolina.api.blog.application.usecase.GetBlogPostBySlugUseCase;
import com.viajescarolina.api.blog.application.usecase.GetPublicBlogLibraryUseCase;
import com.viajescarolina.api.blog.application.usecase.GetPublicBlogHeroUseCase;
import com.viajescarolina.api.blog.application.usecase.ListBlogCategoriesUseCase;
import com.viajescarolina.api.blog.application.usecase.ListPublicBlogPostsUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/public/v1/blog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public Blog", description = "Endpoints públicos para el blog de viajes, categorías y artículos")
public class PublicBlogResource {

    @Inject
    ListPublicBlogPostsUseCase listPublicBlogPostsUseCase;

    @Inject
    GetBlogPostBySlugUseCase getBlogPostBySlugUseCase;

    @Inject
    ListBlogCategoriesUseCase listBlogCategoriesUseCase;

    @Inject
    GetPublicBlogHeroUseCase getPublicBlogHeroUseCase;

    @Inject
    GetPublicBlogLibraryUseCase getPublicBlogLibraryUseCase;

    @GET
    @Operation(summary = "Listar artículos publicados del blog con filtros, búsqueda y paginación")
    public Response getBlog(
            @QueryParam("category") String category,
            @QueryParam("search") String search,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("9") int size) {
        PublicBlogResponse response = listPublicBlogPostsUseCase.execute(category, search, page, size);
        return Response.ok(response).build();
    }

    @GET
    @Path("/categories")
    @Operation(summary = "Listar categorías activas del blog")
    public Response getCategories() {
        List<BlogCategoryDTO> list = listBlogCategoriesUseCase.execute(false);
        return Response.ok(list).build();
    }

    @GET
    @Path("/posts/{slug}")
    @Operation(summary = "Obtener detalle de un artículo por slug y artículos relacionados")
    public Response getPostBySlug(@PathParam("slug") String slug) {
        BlogPostDetailResponse response = getBlogPostBySlugUseCase.execute(slug);
        return Response.ok(response).build();
    }

    // --- Copy editable de la página pública /blog ---

    @GET
    @Path("/hero")
    @Operation(summary = "Obtener sección hero para la página pública /blog", description = "Retorna la configuración editorial del hero del blog")
    public Response getHero() {
        BlogHeroDTO dto = getPublicBlogHeroUseCase.execute();
        return Response.ok(dto).build();
    }

    @GET
    @Path("/library")
    @Operation(summary = "Obtener biblioteca para la página pública /blog", description = "Retorna la configuración editorial del bloque de biblioteca")
    public Response getLibrary() {
        BlogLibraryDTO dto = getPublicBlogLibraryUseCase.execute();
        return Response.ok(dto).build();
    }
}
