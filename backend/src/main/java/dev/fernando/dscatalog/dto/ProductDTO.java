package dev.fernando.dscatalog.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.fernando.dscatalog.entities.Category;
import dev.fernando.dscatalog.entities.Product;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private Instant date;
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }
    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
        if (!entity.getCategories().isEmpty()) {
            entity.getCategories().forEach(this::addEntityCategory);
        }
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public Instant getDate() {
        return date;
    }
    public void setDate(Instant date) {
        this.date = date;
    }
    public List<CategoryDTO> getCategories() {
        return Collections.unmodifiableList(categories);
    }
    public void addCategory(CategoryDTO categoryDTO) {
        this.categories.add(categoryDTO);
    }
    public void addEntityCategory(Category entity) {
        this.addCategory(new CategoryDTO(entity));
    }
}
