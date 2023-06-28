package example.cashcard;

import org.springframework.security.core.annotation.CurrentSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CurrentSecurityContext(expression = "hasAuthority('CARD_ADMIN')")
public @interface CashCardRequiredCardOwner {
}