package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class CashCardApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnACashCardWhenDataIsSaved() throws Exception {
        mockMvc.perform(get("/cashcards/99"))
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.amount").value(123.45));
    }

    @Test
    void shouldNotReturnACashCardWithAnUnknownId() throws Exception {
        mockMvc.perform(get("/cashcards/1000"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    @DirtiesContext
    void shouldCreateANewCashCard() throws Exception {
        String location = mockMvc.perform(post("/cashcards")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                 {
                                     "amount" : 250.00,
                                     "owner" : "sarah1"
                                 }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(250.00));
    }

    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() throws Exception {
        mockMvc.perform(get("/cashcards"))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$..id").value(containsInAnyOrder(99, 100, 101)))
                .andExpect(jsonPath("$..amount").value(containsInAnyOrder(123.45, 1.00, 150.00)));
    }
}