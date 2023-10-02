package example.cashcard;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class CashCardSpringSecurityTests {

	@Autowired
	JwtEncoder jwtEncoder;

	@Autowired
	private MockMvc mvc;

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
	}

}
