package example.cashcard;


import org.springframework.security.core.annotation.CurrentSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@CurrentSecurityContext(expression = "authentication.authorities.contains('CARD-OWNER') || authentication.authorities.contains('CARD-ADMIN')")
public @interface CashCardRequiredCardOwner {
}
