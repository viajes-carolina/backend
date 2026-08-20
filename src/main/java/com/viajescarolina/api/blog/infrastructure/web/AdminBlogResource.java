package com.viajescarolina.api.blog.infrastructure.web;

import com.viajescarolina.api.blog.application.dto.*;
import com.viajescarolina.api.blog.application.usecase.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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
    public Response createPost(CreateOrUpdateBlogPostRequest req) {
        BlogPostDTO created = createBlogPostUseCase.execute(req);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/posts/{id}")
    @Operation(summary = "Actualizar un artículo existente")
    public Response updatePost(@PathParam("id") Long id, CreateOrUpdateBlogPostRequest req) {
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
    public Response createCategory(CreateOrUpdateBlogCategoryRequest req) {
        BlogCategoryDTO created = createBlogCategoryUseCase.execute(req);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/categories/{id}")
    @Operation(summary = "Actualizar una categoría del blog")
    public Response updateCategory(@PathParam("id") Long id, CreateOrUpdateBlogCategoryRequest req) {
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
}
