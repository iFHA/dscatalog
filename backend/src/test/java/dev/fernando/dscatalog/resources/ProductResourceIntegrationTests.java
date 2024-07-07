package dev.fernando.dscatalog.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.fernando.dscatalog.dto.ProductDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        productDTO = new ProductDTO();
        productDTO.setName("teste");
    }

    @Test
    void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/products")
            .accept(MediaType.APPLICATION_JSON)
            .param("sort", "name")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(countTotalProducts))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Macbook Pro"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("PC Gamer"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String json = mapper.writeValueAsString(productDTO);
        mockMvc.perform(
            MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
            .accept(MediaType.APPLICATION_JSON)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String json = mapper.writeValueAsString(productDTO);
        mockMvc.perform(
            MockMvcRequestBuilders.put("/products/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }
}
