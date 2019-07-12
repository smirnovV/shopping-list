package ru.smirnovv.shoppingList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционный тест для {@link ShoppingListController}.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingListControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Before
    public void deleteShoppingList() {
        shoppingListRepository.deleteAll();
    }

    @Test
    public void shouldReturnShoppingList() throws Exception {
        Purchase purchaseA = shoppingListRepository.save(new Purchase("Purchase A"));
        Purchase purchaseB = shoppingListRepository.save(new Purchase("Purchase B"));
        Purchase purchaseC = shoppingListRepository.save(new Purchase("Purchase C"));

        mockMvc.perform(get("/shoppinglist"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].title").value(is(purchaseA.getTitle())))
                .andExpect(jsonPath("$.content[1].id").exists())
                .andExpect(jsonPath("$.content[1].title").value(is(purchaseB.getTitle())))
                .andExpect(jsonPath("$.content[2].id").exists())
                .andExpect(jsonPath("$.content[2].title").value(is(purchaseC.getTitle())))
                .andExpect(jsonPath("$.totalElements").value(is(3)));
    }

    @Test
    public void shouldReturnActualShoppingList() throws Exception {
        Purchase purchaseA = shoppingListRepository.save(new Purchase("Purchase A"));
        Purchase purchaseB = shoppingListRepository.save(new Purchase("Purchase B"));
        Purchase purchaseC = shoppingListRepository.save(new Purchase("Purchase C"));

        mockMvc.perform(post("/shoppinglist/{id}", purchaseB.getId())
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());

        mockMvc.perform(get("/shoppinglist/actual"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].title").value(is(purchaseA.getTitle())))
                .andExpect(jsonPath("$.content[1].id").exists())
                .andExpect(jsonPath("$.content[1].title").value(is(purchaseC.getTitle())))
                .andExpect(jsonPath("$.totalElements").value(is(2)));
    }

    @Test
    public void shouldAddNewPurchase() throws Exception {
        mockMvc.perform(post("/shoppinglist")
                .param("title", "apple")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(is("apple")))
                .andExpect(jsonPath("$.actual").value(is(true)));
    }

    @Test
    public void shouldNotAddPurchaseWhenTitleIsNotProvided() throws Exception {
        mockMvc.perform(post("/shoppinglist")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/shoppinglist")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Required String parameter 'title' is not present")));
    }

    @Test
    public void shouldNotAddPurchaseWhenTitleIsEmpty() throws Exception {
        mockMvc.perform(post("/shoppinglist")
                .param("title", "")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/shoppinglist")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid title! The title must be no longer than 50 characters and not empty")));
    }

    @Test
    public void shouldNotAddNewPurchaseWhenLengthTitleGreaterMaxLength() throws Exception {
        mockMvc.perform(post("/shoppinglist")
                .param("title", "TestTestTestTestTestTestTestTestTestTestTestTestTest")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/shoppinglist")))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid title! The title must be no longer than 50 characters and not empty")));
    }

    @Test
    public void shouldReturnPurchase() throws Exception {
        Purchase purchaseA = shoppingListRepository.save(new Purchase("Purchase A"));

        mockMvc.perform(get("/shoppinglist/{id}", purchaseA.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(is("Purchase A")));
    }

    @Test
    public void shouldPurchaseNotFoundException() throws Exception {
        mockMvc.perform(get("/shoppinglist/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/shoppinglist/10")))
                .andExpect(jsonPath("$.status").value(is(404)))
                .andExpect(jsonPath("$.message").value(
                        is("Purchase 10 not found.")));
    }

    @Test
    public void shouldNotChangePeriod() throws Exception {
        Purchase purchaseA = shoppingListRepository.save(new Purchase("apple"));

        mockMvc.perform(put("/shoppinglist/{id}", purchaseA.getId())
                .param("period", "5")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(is("apple")))
                .andExpect(jsonPath("$.actual").value(is(true)))
                .andExpect(jsonPath("$.period").value(is(5)));
    }

    @Test
    public void shouldNotChangePeriodWhenPeriodNotPositive() throws Exception {
        Purchase purchaseA = shoppingListRepository.save(new Purchase("apple"));

        mockMvc.perform(put("/shoppinglist/{id}", purchaseA.getId())
                .param("period", "-1")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value(is("/shoppinglist/" + purchaseA.getId())))
                .andExpect(jsonPath("$.status").value(is(400)))
                .andExpect(jsonPath("$.message").value(
                        is("Invalid period! The period must be not negative.")));
    }

    @Test
    public void shouldDeletePurchase() throws Exception {
        Purchase purchaseA = shoppingListRepository.save(new Purchase("Purchase A"));
        Purchase purchaseB = shoppingListRepository.save(new Purchase("Purchase B"));
        Purchase purchaseC = shoppingListRepository.save(new Purchase("Purchase C"));

        mockMvc.perform(delete("/shoppinglist/{id}", purchaseA.getId()))
                .andExpect(status().isOk());

        assertEquals(shoppingListRepository.count(), 2);
        assertTrue(shoppingListRepository.findById(purchaseB.getId()).isPresent());
        assertTrue(shoppingListRepository.findById(purchaseC.getId()).isPresent());
    }

    @Autowired
    private ShoppingListService shoppingListService;

    @Test
    public void shouldChangeActualWhenPeriodHasPassed() throws Exception {
        Purchase purchase = new Purchase("apple");
        purchase.setPeriod(2L);
        purchase.setActual(false);
        Date today = new Date();
        purchase.setDate(new Date(today.getTime() - 4 * 3600 * 1000 * 24));

        Purchase purchaseA = shoppingListRepository.save(purchase);

        mockMvc.perform(get("/shoppinglist/{id}", purchaseA.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(is("apple")))
                .andExpect(jsonPath("$.actual").value(is(true)));
    }
}
