// Responsabilidade: Receber JSON, converter e chamar o Service.

package br.com.mycomedytube.api;

import br.com.mycomedytube.dto.LoginDTO;
import br.com.mycomedytube.dto.UserCreateDTO;
import br.com.mycomedytube.infra.Secured;
import br.com.mycomedytube.model.User;
import br.com.mycomedytube.service.TokenService;
import br.com.mycomedytube.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private UserService userService;

    @Inject
    private TokenService tokenService;

    @POST
    @Path("/register")
    public Response create(UserCreateDTO create) {
        try {
            if (!create.password().equals(create.confirmPassword())) {
                throw new RuntimeException("The passwords don't match");
            }

            User newUser = new User();
            newUser.setName(create.name());
            newUser.setEmail(create.email());

            String passwordHash = BCrypt.hashpw(create.password(), BCrypt.gensalt());
            newUser.setPassword(passwordHash);

            userService.register(newUser);

            return Response.status(Response.Status.CREATED).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginDTO login) {
        User user = userService.searchByEmail(login.email());

        if (user != null && (BCrypt.checkpw(login.password(), user.getPassword()))) {
            String token = tokenService.generateToken(user);
            return Response.ok(Map.of("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("logout")
    @Secured
    public Response logout(@Context HttpServletRequest request) {
        String token = (String) request.getAttribute("raw_token");
        tokenService.invalidadeToken(token);

        return Response.noContent().build();
    }
}
