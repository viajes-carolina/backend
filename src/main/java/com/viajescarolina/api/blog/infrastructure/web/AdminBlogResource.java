package com.viajescarolina.api.blog.infrastructure.web;

import com.viajescarolina.api.blog.application.dto.*;
import com.viajescarolina.api.blog.application.usecase.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/admin/v1/blog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"SUPER_ADMIN", "CONTENT_EDITOR", "ADVISOR"})
@Tag(name = "Admin Blog CMS", description = "Endpoints administrativos para la gestión de artículos y categorías del blog")
public class AdminBlogResource {

    @Inject
    ListAdminBlogPostsUseCase listAdminBlogPostsUseCase;

    @Inject
    CreateBlogPostUseCase createBlogPostUseCase;

    @Inject
    UpdateBlogPostUseCase updateBlogPostUseCase;

    @Inject
    DeleteBlogPostUseCase deleteBlogPostUseCase;

    @Inject
    ListBlogCategoriesUseCase listBlogCategoriesUseCase;

    @Inject
    CreateBlogCategoryUseCase createBlogCategoryUseCase;

    @Inject
    UpdateBlogCategoryUseCase updateBlogCategoryUseCase;

    @Inject
    DeleteBlogCategoryUseCase deleteBlogCategoryUseCase;

    @Inject
    GetPublicBlogHeroUseCase getPublicBlogHeroUseCase;

    @Inject
    UpdateBlogHeroUseCase updateBlogHeroUseCase;

    @Inject
    GetPublicBlogLibraryUseCase getPublicBlogLibraryUseCase;

    @Inject
    UpdateBlogLibraryUseCase updateBlogLibraryUseCase;

    // --- Artículos (Posts) ---

    @GET
    @Path("/posts")
    @Operation(summary = "Listar artículos para el panel CMS con filtros")
    public Response listPosts(
            @QueryParam("status") String status,
            @QueryParam("search") String search,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("50") int size) {
        List<BlogPostDTO> posts = listAdminBlogPostsUseCase.execute(status, search, page, size);
        return Response.ok(posts).build();
    }

    @POST
    @Path("/posts")
    @Operation(summary = "Crear un nuevo artículo en el CMS")
    public Response createPost(@Valid CreateOrUpdateBlogPostRequest req) {
        BlogPostDTO created = createBlogPostUseCase.execute(req);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/posts/{id}")
    @Operation(summary = "Actualizar un artículo existente")
    public Response updatePost(@PathParam("id") Long id, @Valid CreateOrUpdateBlogPostRequest req) {
        BlogPostDTO updated = updateBlogPostUseCase.execute(id, req);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/posts/{id}")
    @Operation(summary = "Archivar o eliminar un artículo")
    public Response deletePost(@PathParam("id") Long id) {
        deleteBlogPostUseCase.execute(id);
        return Response.noContent().build();
    }

    // --- Categorías (Categories) ---

    @GET
    @Path("/categories")
    @Operation(summary = "Listar todas las categorías del blog para el CMS")
    public Response listCategories() {
        List<BlogCategoryDTO> categories = listBlogCategoriesUseCase.execute(true);
        return Response.ok(categories).build();
    }

    @POST
    @Path("/categories")
    @Operation(summary = "Crear una nueva categoría del blog")
    public Response createCategory(@Valid CreateOrUpdateBlogCategoryRequest req) {
        BlogCategoryDTO created = createBlogCategoryUseCase.execute(req);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/categories/{id}")
    @Operation(summary = "Actualizar una categoría del blog")
    public Response updateCategory(@PathParam("id") Long id, @Valid CreateOrUpdateBlogCategoryRequest req) {
        BlogCategoryDTO updated = updateBlogCategoryUseCase.execute(id, req);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/categories/{id}")
    @Operation(summary = "Desactivar una categoría del blog")
    public Response deleteCategory(@PathParam("id") Long id) {
        deleteBlogCategoryUseCase.execute(id);
        return Response.noContent().build();
    }

    // --- Copy editable de la página pública /blog ---

    @GET
    @Path("/hero")
    @Operation(summary = "Obtener configuración del hero de la página /blog", description = "Retorna la configuración editorial actual del hero")
    public Response getHero() {
        BlogHeroDTO dto = getPublicBlogHeroUseCase.execute();
        return Response.ok(dto).build();
    }

    @PUT
    @Path("/hero")
    @Operation(summary = "Actualizar configuración del hero de la página /blog", description = "Actualiza los textos del hero")
    public Response updateHero(BlogHeroDTO dto) {
        BlogHeroDTO updated = updateBlogHeroUseCase.execute(dto);
        return Response.ok(updated).build();
    }

    @GET
    @Path("/library")
    @Operation(summary = "Obtener configuración de la biblioteca de la página /blog", description = "Retorna la configuración editorial actual de la biblioteca")
    public Response getLibrary() {
        BlogLibraryDTO dto = getPublicBlogLibraryUseCase.execute();
        return Response.ok(dto).build();
    }

    @PUT
    @Path("/library")
    @Operation(summary = "Actualizar configuración de la biblioteca de la página /blog", description = "Actualiza los textos de la biblioteca")
    public Response updateLibrary(BlogLibraryDTO dto) {
        BlogLibraryDTO updated = updateBlogLibraryUseCase.execute(dto);
        return Response.ok(updated).build();
    }
}
