package com.matias.shoppingmicroservice.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
public class InvoiceItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive(message = "El stock debe ser mayor que cero")
    private Double quantity;
    private Double price;

    @Column(name = "product_id")
    private Long productId;

    @Transient
    private Double subTotal;

    public Double getSubTotal() {
        if (this.price > 0 && this.quantity > 0) {
            return this.quantity * this.price;
        } else {
            return (double) 0;
        }
    }

    public InvoiceItemEntity() {
        this.quantity = (double) 0;
        this.price = (double) 0;

    }

}
