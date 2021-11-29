package at.htl.optician.boundary.dtos;

import java.time.LocalDateTime;

public class InvoiceItemDto {
    private String productName;
    private LocalDateTime date;
    private int quantity;
    private double totalPrice;

    public InvoiceItemDto() {
    }

    public InvoiceItemDto(String productName, LocalDateTime date, int quantity, double totalPrice) {
        this.productName = productName;
        this.date = date;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
