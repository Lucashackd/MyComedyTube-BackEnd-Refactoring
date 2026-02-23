package br.com.mycomedytube.infra;

import br.com.mycomedytube.model.UserRole;
import jakarta.ws.rs.NameBinding;

import javax.management.relation.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured {
    UserRole[] value() default {};
}
