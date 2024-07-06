package dev.fernando.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.fernando.dscatalog.dto.ProductDTO;
import dev.fernando.dscatalog.services.ProductService;
import dev.fernando.dscatalog.services.exceptions.DatabaseException;
import dev.fernando.dscatalog.services.exceptions.ResourceNotFoundException;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;

    private Long existingId;
    private Long dependentId;
    private Long nonExistingId;

    @BeforeEach
    void setup() throws Exception {

        existingId = 1L;
        dependentId = 2L;
        nonExistingId = 1000L;

        productDTO = new ProductDTO(existingId, "teste", "teste", 2.0, null, null);
        page = new PageImpl<>(List.of(productDTO));
        when(productService.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
        when(productService.findById(existingId)).thenReturn(productDTO);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        
        when(productService.update(ArgumentMatchers.eq(existingId), any())).thenReturn(productDTO);
        when(productService.update(ArgumentMatchers.eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        when(productService.store(ArgumentMatchers.any())).thenReturn(productDTO);
        
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependentId);
        doNothing().when(productService).delete(existingId);
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/products/{id}", nonExistingId)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void deleteShouldReturnBadRequestWhenIdIsDependent() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/products/{id}", dependentId)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/products/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/products")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findByIdShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }

    @Test
    void storeShouldReturnCreated() throws Exception {

        String json = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/products")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {

        String json = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/products/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
    }
    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String json = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
