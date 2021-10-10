package at.htl.optician.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceTest {
    @Test
    void testToStringFromSingleInvoice() {
        Customer c = new Customer(1L, "Moritz", LocalDate.of(2002, 10, 18), 4203, "Altenberg", "Langlus 11a");

        Invoice invoice = new Invoice(1L, c, LocalDateTime.of(2020, 11, 8, 16,30));

        assertThat(invoice.toString()).isEqualTo("1 1 " + LocalDateTime.of(2020, 11, 8, 16,30).toString());
    }
}
