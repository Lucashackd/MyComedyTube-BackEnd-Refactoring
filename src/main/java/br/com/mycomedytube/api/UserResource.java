// Responsabilidade: Receber JSON, converter e chamar o Service.

package br.com.mycomedytube.api;

import br.com.mycomedytube.dto.UserCreateDTO;
import br.com.mycomedytube.model.User;
import br.com.mycomedytube.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

public class UserResource {

    @Inject
    private UserService userService;

    @POST
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

            return Response.status(Response.Status.CREATED).entity(newUser).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
