package example.cashcard;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/security")
public class ProfileController {

    JwtEncoder encoder;

    public ProfileController(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @GetMapping("/profile/principal")
    public ResponseEntity<String> getOwnerProfileUsingAuthenticationPrincipal(@AuthenticationPrincipal Jwt principal) {
        Jwt.Builder jwtBuilder = Jwt.withTokenValue(principal.getTokenValue());
        jwtBuilder.headers(headers -> headers.putAll(principal.getHeaders()));
        jwtBuilder.claim("scope", principal.getClaimAsString("scope"));
        jwtBuilder.claim("sub", principal.getClaimAsString("sub"));
        Jwt jwt = jwtBuilder.build();

        String username = jwt.getClaimAsString("sub");
        String role = jwt.getClaimAsString("scope");
        String response = String.format("Welcome %s, this is your profile, with authority: %s", username, role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/authentication")
    public ResponseEntity<String> getOwnerProfileUsingAuthentication(Authentication authentication) {
        String response = String.format("Welcome %s, this is your profile.", authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/context")
    public ResponseEntity<String> getOwnerProfileUsingSecurityContext(@CurrentSecurityContext SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        String response = String.format("Welcome %s, this is your profile.", authentication.getName());
        return ResponseEntity.ok(response);
    }
}
