// Responsabilidade: Receber JSON, converter e chamar o Service.

package br.com.mycomedytube.api;

import br.com.mycomedytube.model.Video;
import br.com.mycomedytube.service.VideoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideoResource {

    @Inject
    private VideoService service;

    @GET
    public List<Video> list() {
        return service.listAll();
    }

    @POST
    public Response create(Video video) {
        try {
            Video newVideo = service.upload(video);

            // Return HTTP 201 (CREATED)
            return Response.status(Response.Status.CREATED).entity(newVideo).build();
        } catch (RuntimeException e) {

            // Return HTTP 400 (BAD REQUEST) if validation fails
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
