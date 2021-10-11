package at.htl.optician.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "invoice_item")
public class InvoiceItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Invoice invoice;

    private int qtyInStock;

    private double totalPrice;

    public InvoiceItem() {
    }

    public InvoiceItem(Invoice invoice, Product product, int qtyInStock) {
        this.invoice = invoice;
        this.product = product;
        this.qtyInStock = qtyInStock;
        this.totalPrice = product.getPrice() * qtyInStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.totalPrice = this.qtyInStock * product.getPrice();
    }

    public int getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(int qtyInStock) {
        this.qtyInStock = qtyInStock;
        this.totalPrice = this.qtyInStock * this.product.getPrice();
    }

    @Override
    public String toString() {
        return product.getId() + " " + product.getEanCode() + " " + qtyInStock;
    }
}
