package br.com.mycomedytube.infra;

import br.com.mycomedytube.service.TokenService;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    private TokenService tokenService;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Take the Authorization header
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Verify if has the Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext);
            return;
        }

        // Extract the token and validates it
        String token = authHeader.substring(7); // Remove "Bearer "
        String email = tokenService.validateToken(token);

        if (email == null) abort(requestContext);

        /*
        Pro tip: Inject the user in the request to use later
        requestContext.setProperty("user_email", email);
        */
    }

    public void abort(ContainerRequestContext context) {
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
