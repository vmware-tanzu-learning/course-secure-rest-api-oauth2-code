package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class CashCardApplicationTests {

    @Autowired
    private MockMvc mockMvc;


    // Previous Tests
    /*
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
    void shouldCreateANewCashCard() throws Exception {


        String location = mockMvc.perform(post("/cashcards"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(250.00));
    }

    @Test
    void shouldReturnVoidWhenDeleteCashCardByIdRequest() throws Exception {
        mockMvc.perform(delete("/cashcards/101"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() throws Exception {
         mockMvc.perform(get("/cashcards"))
                 .andExpect(jsonPath("$.length()").value(3))
                 .andExpect(jsonPath("$..id").value(containsInAnyOrder(99, 100, 101)))
                 .andExpect(jsonPath("$..amount").value(containsInAnyOrder(123.45, 1.00, 150.00)));
    }

    @Test
    void shouldReturnAPageOfCashCards() throws Exception {
        mockMvc.perform(get("/cashcards?page=0&size=1"))
                .andExpect(jsonPath("$[*]").value(hasSize(1)));
    }

    @Test
    void shouldReturnASortedPageOfCashCards() throws Exception {
        mockMvc.perform(get("/cashcards?page=0&size=1&sort=amount,desc"))
                .andExpect(jsonPath("$[*]").value(hasSize(1)))
                .andExpect(jsonPath("$..amount").value(hasItem(150.00)));
    }

    @Test
    void shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() throws Exception {
        mockMvc.perform(get("/cashcards")
                )
                .andExpect(jsonPath("$[*]").value(hasSize(3)))
                .andExpect(jsonPath("$..amount").value(contains(1.00, 123.45, 150.00)));

    }

    @Test
    @DirtiesContext
    void shouldNotReturnACashCardWhenUsingBadCredentials() throws Exception {
        mockMvc.perform(get("/cashcards/99"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectUsersWhoAreNotCardOwners() throws Exception {
        mockMvc.perform(get("/cashcards/99"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotAllowAccessToCashCardsTheyDoNotOwn() throws Exception {
        mockMvc.perform(get("/cashcards/102"))
                .andExpect(status().isNotFound());
    }

    */

    /*
    @WithMockUser(username = "esuez")
    @Test
    void shouldReturnAllCashCardsByOwnerWhenListIsRequested() throws Exception {
        mockMvc.perform(get("/cashcards"))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$..id").value(containsInAnyOrder(400, 416, 423)))
                .andExpect(jsonPath("$..amount").value(containsInAnyOrder(233.75, 123.60, 52.00)));
    }
    */

    /*
    @Test
    void shouldReturnAllCashCardsByOwnerWhenListIsRequested() throws Exception {
        mockMvc.perform(get("/cashcards")
                        .with(jwt().jwt((jwt) -> jwt.claim("sub", "esuez"))))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$..id").value(containsInAnyOrder(400, 416, 423)))
                .andExpect(jsonPath("$..amount").value(containsInAnyOrder(233.75, 123.60, 52.00)));
    }
    */
}