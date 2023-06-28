package example.cashcard;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CashCardOwnerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() throws Exception {
        mockMvc.perform(get("/owners")
                        .with(jwt().jwt((jwt) -> jwt
                                .claim("scope", "CARD-OWNER")
                                .subject("sarah1")))
                )
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cashCards.length()").value(3))
                .andExpect(jsonPath("$[0].cashCards..id").value(containsInAnyOrder(99, 100, 101)))
                .andExpect(jsonPath("$[0].cashCards..amount").value(containsInAnyOrder(123.45, 1.00, 150.00)));
    }
}
