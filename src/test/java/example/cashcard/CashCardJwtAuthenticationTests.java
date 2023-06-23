package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CashCardJwtAuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnProfileUsingAuthenticationPrincipalDetails() throws Exception {
        mockMvc.perform(get("/security/profile/principal")
                        .with(jwt().jwt((jwt) -> jwt
                                .claim("scope", "CARD-OWNER")
                                .subject("sarah1")))

                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("sarah1")));
    }

    @Test
    void shouldReturnProfileUsingAuthentication() throws Exception {
        mockMvc.perform(get("/security/profile/authentication")
                        .with(jwt().jwt((jwt) -> jwt
                                .claim("scope", "CARD-OWNER")
                                .subject("sarah1")))
                )
                .andExpect(content().string(containsString("sarah1")));
    }

    @Test
    void shouldReturnProfileUsingSecurityContext() throws Exception {
        mockMvc.perform(get("/security/profile/authentication")
                        .with(jwt().jwt((jwt) -> jwt
                                .claim("scope", "CARD-OWNER")
                                .subject("sarah1")))
                )
                .andExpect(content().string(containsString("sarah1")));
    }
}
