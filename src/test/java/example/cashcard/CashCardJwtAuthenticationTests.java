package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CashCardJwtAuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnProfileUsingAuthenticationPrincipalDetailsNoMockUser() throws Exception {
        mockMvc.perform(get("/security/profile/principal")
                        .with(jwt().jwt((jwt) -> jwt
                                .claim("scope", "CARD-OWNER")
                                .subject("sarah1")))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("sarah1")));
    }


    @Test
    @WithMockUser(authorities = "SCOPE_CARD-OWNER")
    void shouldReturnProfileUsingAuthenticationPrincipalDetails() throws Exception {
        MvcResult result = mockMvc.perform(post("/security/token")
                        .with(httpBasic("sarah1", "abc123")))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        mockMvc.perform(get("/security/profile/principal")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("sarah1")));
    }

    @Test
    @WithMockUser(username = "sarah1", password = "abc123", authorities = "SCOPE_CARD-OWNER")
    void shouldReturnProfileUsingAuthentication() throws Exception {
        mockMvc.perform(get("/security/profile/authentication"))
                .andExpect(content().string(containsString("sarah1")));
    }

    @Test
    @WithMockUser(username = "sarah1", password = "abc123", authorities = "SCOPE_CARD-OWNER")
    void shouldReturnProfileUsingSecurityContext() throws Exception {
        mockMvc.perform(get("/security/profile/authentication"))
                .andExpect(content().string(containsString("sarah1")));
    }
}
