package example.cashcard;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.web.servlet.MockMvc;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
public class CashCardSpringSecurityTests {

	@Autowired
	JwtEncoder jwtEncoder;

	@Autowired
	private MockMvc mvc;

	@Test
	void shouldNotAllowTokensWithAnInvalidAudience() throws Exception {
		String token = mint((claims) -> claims.audience(List.of("https://wrong")));
		this.mvc.perform(get("/cashcards/1000").header("Authorization", "Bearer " + token))
				.andExpect(status().isUnauthorized())
				.andExpect(header().string("WWW-Authenticate", containsString("aud claim is not valid")));

	}

	@Test
	void shouldNotAllowTokensThatAreExpired() throws Exception {
		String token = mint((claims) -> claims
				.issuedAt(Instant.now().minusSeconds(3600))
				.expiresAt(Instant.now().minusSeconds(3599))
		);
		this.mvc.perform(get("/cashcards/1000").header("Authorization", "Bearer " + token))
				.andExpect(status().isUnauthorized())
				.andExpect(header().string("WWW-Authenticate", containsString("Jwt expired")));
	}

	@Test
	void shouldShowAllTokenValidationErrors() throws Exception {
		String expired = mint((claims) -> claims
				.audience(List.of("https://wrong"))
				.issuedAt(Instant.now().minusSeconds(3600))
				.expiresAt(Instant.now().minusSeconds(3599))
		);
		this.mvc.perform(get("/cashcards").header("Authorization", "Bearer " + expired))
				.andExpect(status().isUnauthorized())
				.andExpect(header().exists("WWW-Authenticate"))
				.andExpect(jsonPath("$.errors..description").value(
						containsInAnyOrder(containsString("Jwt expired"), containsString("aud claim is not valid"))));
	}

	private String mint() {
		return mint(consumer -> {});
	}

	private String mint(Consumer<JwtClaimsSet.Builder> consumer) {
		JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
				.issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(100000))
				.subject("sarah1")
				.issuer("http://localhost:9000")
				.audience(Arrays.asList("cashcard-client"))
				.claim("scp", Arrays.asList("cashcard:read", "cashcard:write"));
		consumer.accept(builder);
		JwtEncoderParameters parameters = JwtEncoderParameters.from(builder.build());
		return this.jwtEncoder.encode(parameters).getTokenValue();
	}


	@TestConfiguration
	static class TestJwtConfiguration {
		@Bean
		JwtEncoder jwtEncoder(@Value("classpath:authz.pub") RSAPublicKey pub,
							  @Value("classpath:authz.pem") RSAPrivateKey pem) {
			RSAKey key = new RSAKey.Builder(pub).privateKey(pem).build();
			return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(key)));
		}

		@Bean
		JwtDecoder jwtDecoder(@Value("classpath:authz.pub") RSAPublicKey pub) {
			NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(pub).build();
			OAuth2TokenValidator<Jwt> defaults = JwtValidators.createDefaultWithIssuer("http://localhost:9000");
			OAuth2TokenValidator<Jwt> audience = new JwtClaimValidator<List<Object>>(JwtClaimNames.AUD,
					(aud) -> !Collections.disjoint(aud, Collections.singleton("cashcard-client")));
			jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(defaults, audience));
			return jwtDecoder;
		}
	}

}
