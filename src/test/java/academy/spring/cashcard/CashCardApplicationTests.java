package academy.spring.cashcard;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
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

    @Test
    void shouldNotReturnACashCardWithAnUnknownId() throws Exception {
        this.mvc.perform(get("/cashcards/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void shouldCreateANewCashCard() throws Exception {
        String location = this.mvc.perform(post("/cashcards")
                .with(csrf())
                .contentType("application/json")
                .content("""
                        {
                            "amount" : 250.00,
                            "owner" : "carol2"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");

        this.mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.owner").value("carol2"));
    }

    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() throws Exception {
        this.mvc.perform(get("/cashcards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$..owner").value(hasItem("sarah1")));
    }
}