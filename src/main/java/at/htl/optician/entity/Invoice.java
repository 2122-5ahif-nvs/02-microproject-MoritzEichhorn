package at.htl.optician.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@XmlRootElement
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;
    @Column(name = "purchase_date_time")
    private LocalDateTime purchaseDateTime;
    @OneToMany
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
        invoiceItems = new LinkedList<>();
    }

    public Invoice(Long id, Customer customer, LocalDateTime purchaseDateTime) {
        this.id = id;
        this.customer = customer;
        this.purchaseDateTime = purchaseDateTime;
        this.invoiceItems = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    public LocalDateTime getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(LocalDateTime purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    @Override
    public String toString() {
        return id + " " + customer.getId() + " " + purchaseDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id) && Objects.equals(customer, invoice.customer) && purchaseDateTime.equals(invoice.purchaseDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer);
    }
}
