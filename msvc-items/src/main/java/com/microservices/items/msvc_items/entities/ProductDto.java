package com.microservices.items.msvc_items.entities;

//CLASE PARA OBTENER LOS DATOS DE UN PRODUCTO DESDE EL OTRO SERVICIO

import java.time.LocalDate;

public class ProductDto {

    private Long id;
    private String name;
    private  Double price;
    private LocalDate createdAt;
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
