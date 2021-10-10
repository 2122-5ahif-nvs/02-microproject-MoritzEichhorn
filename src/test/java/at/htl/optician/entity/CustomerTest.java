package at.htl.optician.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {
    @Test
    void testToStringFromSingleCustomer() {
        Customer customer = new Customer(1L, "Max Mustermann", LocalDate.of(2002, 1,1),
                4203, "Altenberg", "Langlus 11a");
        assertThat(customer.toString()).isEqualTo("1 Max Mustermann 2002-01-01 Langlus 11a 4203 Altenberg");
    }
}
