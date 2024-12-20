package com.microservices.items.msvc_items.entities;

public class ItemEntity {
    private ProductDto productDto;
    private int quantity;

    public ItemEntity(ProductDto productDto, int quantity) {
        this.productDto = productDto;
        this.quantity = quantity;
    }



    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getTotal(){
        return productDto.getPrice()*this.quantity;
    }
}
