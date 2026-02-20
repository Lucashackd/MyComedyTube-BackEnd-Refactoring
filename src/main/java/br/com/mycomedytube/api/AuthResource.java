// Responsabilidade: Receber JSON, converter e chamar o Service.

package br.com.mycomedytube.api;

import br.com.mycomedytube.dto.LoginDTO;
import br.com.mycomedytube.model.User;
import br.com.mycomedytube.service.TokenService;
import br.com.mycomedytube.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    @POST
    @Path("/login")
    public Response login(@Valid LoginDTO login) {
        User user = userService.searchByEmail(login.email());

        if (user != null && (BCrypt.checkpw(login.password(), user.getPassword()))) {
            String token = tokenService.generateToken(user);
            return Response.ok().entity("{\"token\": \"" + token + "\"}").build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
