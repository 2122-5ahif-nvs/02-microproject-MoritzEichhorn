package at.htl.optician.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceItemTest {
    @Test
    void testToStringFromSingleInvoice() {
        Customer c = new Customer(1L, "Moritz", LocalDate.of(2002, 10, 18), 4203, "Altenberg", "Langlus 11a");

        Invoice invoice = new Invoice(1L, c, LocalDateTime.of(2020, 11, 8, 16,30));
        Product product = new Product(1L, 5901234123457L, "Woodfella Brille 930", "Brille mit Bügel aus Holz", 130.20, 40);
        InvoiceItem invoiceItem = new InvoiceItem(invoice, product, 20);
        invoice.getInvoiceItems().add(invoiceItem);

        assertThat(invoiceItem.toString()).isEqualTo("1 5901234123457 20");
    }
}
