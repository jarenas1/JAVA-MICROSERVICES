package com.microservices.products.msvc_products.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "products ")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    @Column(name = "created_at")
    private LocalDate createdAt;

    public ProductEntity(Long id, LocalDate createdAt, String name, Double price) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.price = price;
    }

    public ProductEntity() {
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
