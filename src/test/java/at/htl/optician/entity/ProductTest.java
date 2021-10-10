package at.htl.optician.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
    @Test
    void testEanCodeIsTooShortShouldThrowException() {
        String message = "";
        try {
            Product p = new Product(null, 1L, "Woodfella Brille 930", "Brille mit Bügel aus Holz", 130.20, 40);
        } catch(IllegalArgumentException ex) {
            message = ex.getMessage();
        }

        assertThat(message).isEqualTo("Invalid EanCode length. Expected: " + Product.EAN_CODE_LENGTH + ", Actual: 1");
    }

    @Test
    void testEanCodeIsInvalidShouldThrowException() {
        String message = "";
        try {
            Product p = new Product(null, 1234567890123L, "Woodfella Brille 930", "Brille mit Bügel aus Holz", 130.20, 40);
        } catch(IllegalArgumentException ex) {
            message = ex.getMessage();
        }

        assertThat(message).isEqualTo("EanCode is not valid!");
    }

    @Test
    void testEanCodeIsValidToStringShouldBeCorrect() {
        String message = "";
        Product p = null;
        try {
            p = new Product(null, 5901234123457L, "Woodfella Brille 930", "Brille mit Bügel aus Holz", 130.20, 40);
        } catch(IllegalArgumentException ex) {
            message = ex.getMessage();
        }

        assertThat(message).isEqualTo("");
        assertThat(p).isNotNull();
        assertThat(p.toString()).isEqualTo("5901234123457 Woodfella Brille 930 Brille mit Bügel aus Holz 130.2 40");
    }
}
