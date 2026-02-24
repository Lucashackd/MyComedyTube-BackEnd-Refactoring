package br.com.mycomedytube.infra;

import br.com.mycomedytube.model.UserRole;
import br.com.mycomedytube.service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    private TokenService tokenService;

    @Context
    private ResourceInfo resourceInfo; // Helps to read which method the user is trying to access

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION); // Take the Authorization header

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer".length()).trim(); // Remove "Bearer "

            Claims claims = tokenService.validateToken(token);
            if (claims != null) {
                /*
                 * Valid token! Extract the data
                 */
                String email = claims.getSubject();
                String roleString = claims.get("role", String.class);
                UserRole userRole = UserRole.valueOf(roleString);

                /*
                 * Insert the user properties in the requisition for later use
                 */
                requestContext.setProperty("user_email", email);
                requestContext.setProperty("User_role", userRole.name());
                requestContext.setProperty("raw_token", token);

                checkPermissions(userRole, requestContext);
                return;
            }
        }

        // If got in here, then token does not exist, is invalid or expired
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("Access denied. Token invalid or absent")
                .build());
    }

    public void checkPermissions(UserRole userRole, ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod(); // Find which method from the resource the user called
        Secured secured = method.getAnnotation(Secured.class); // Get the @Secured annotation of this method
        if (secured == null) {
            // If the method does not have the annotation, finds out if the whole class has
            secured = resourceInfo.getResourceClass().getAnnotation(Secured.class);
        }

        // If the annotation needs some specific role...
        if (secured != null && secured.value().length > 0) {
            boolean hasPermission = Arrays.asList(secured.value()).contains(userRole);

            if (!hasPermission) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("You do not have permission to access this resource")
                        .build());
            }
        }
    }
}
