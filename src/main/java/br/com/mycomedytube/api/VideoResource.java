// Responsabilidade: Receber JSON, converter e chamar o Service.

package br.com.mycomedytube.api;

import br.com.mycomedytube.dto.VideoCreateDTO;
import br.com.mycomedytube.infra.Secured;
import br.com.mycomedytube.model.User;
import br.com.mycomedytube.model.Video;
import br.com.mycomedytube.service.UserService;
import br.com.mycomedytube.service.VideoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideoResource {

    @Inject
    private VideoService videoService;

    @Inject
    private UserService userService;

    @GET
    public List<Video> list() {
        return videoService.listAll();
    }

    @POST
    @Secured
    public Response create(@Valid VideoCreateDTO create, @Context ContainerRequestContext requestContext) {
        try {
            Video newVideo = new Video();
            newVideo.setTitle(create.title());
            newVideo.setDescription(create.description());
            newVideo.setPath(create.path());

            String loggedUser = (String) requestContext.getProperty("user_email"); // Get from Context (AuthFilter)
            User videoOwner = userService.searchByEmail(loggedUser);
            newVideo.setUser(videoOwner);

            if (!create.thumbnail().trim().isEmpty()) newVideo.setThumbnail(create.thumbnail());
            else newVideo.setThumbnail(null);

            videoService.upload(newVideo);

            // Return HTTP 201 (CREATED)
            return Response.status(Response.Status.CREATED).entity(newVideo).build();
        } catch (RuntimeException e) {

            // Return HTTP 400 (BAD REQUEST) if validation fails
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
