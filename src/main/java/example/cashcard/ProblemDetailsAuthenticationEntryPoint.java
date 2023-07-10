package example.cashcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;

// (1)
/*
@Component
public class ProblemDetailsAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
*/

// (2) - This is the fix for the v3 SecurityConfig.java
/*
@Component
public class ProblemDetailsAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);

    }
}
*/


// (3) - This is the fix for the v4 SecurityConfig.java
@Component
public class ProblemDetailsAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper;

    public ProblemDetailsAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if (authException.getCause() instanceof JwtValidationException validation) {
            ProblemDetail detail = ProblemDetail.forStatus(401);
            detail.setType(URI.create("https://tools.ietf.org/html/rfc6750#section-3.1"));
            detail.setTitle(authException.getMessage());
            detail.setProperty("errors", validation.getErrors());

            // I need to add this in order to make it work. Is there somehow this is already added by Security???
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            this.mapper.writeValue(response.getWriter(), detail);
        }

        this.delegate.commence(request, response, authException);

    }
}