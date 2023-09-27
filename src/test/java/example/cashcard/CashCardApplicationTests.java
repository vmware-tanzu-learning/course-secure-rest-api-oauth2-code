package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "sarah1", authorities = {"SCOPE_cashcard:read", "SCOPE_cashcard:write"})
class CashCardApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturnACashCardWhenDataIsSaved() throws Exception {
        this.mvc.perform(get("/cashcards/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.owner").value("sarah1"));
    }

    @WithMockUser(username="esuez5", authorities = {"SCOPE_cashcard:read", "SCOPE_cashcard:write"})
    @Test
    @DirtiesContext
    void shouldCreateANewCashCard() throws Exception {
        String location = this.mvc.perform(post("/cashcards")
                .with(csrf())
                        .contentType("application/json")
                        .content("""
                        {
                            "amount" : 250.00
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");

        this.mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(250.00))
                .andExpect(jsonPath("$.owner").value("esuez5"));
    }

    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() throws Exception {
        this.mvc.perform(get("/cashcards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$..owner").value(everyItem(equalTo("sarah1"))));
    }
}