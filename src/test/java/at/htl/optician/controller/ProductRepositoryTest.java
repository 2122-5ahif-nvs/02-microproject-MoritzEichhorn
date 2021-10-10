package at.htl.optician.controller;

import at.htl.optician.entity.Product;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.logging.Logger.Level.INFO;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTest {
    @Inject
    ProductRepository productRepository;

    @Inject
    Logger logger;

    @Test
    @Order(100)
    void repoExists() {
        assertThat(productRepository).isNotNull();
    }

    @Test
    @Order(110)
    @Transactional
    void saveProductOk() {
        productRepository.reset();
        Product glasses = new Product(null, 5901234123457L, "Woodfella Brille 930", "Brille mit Bügel aus Holz",
                130.20, 40);

        logger.log(INFO, productRepository.listAll());

        productRepository.persist(glasses);

        assertThat(productRepository.listAll()).contains(glasses);
    }

    @Test
    @Order(120)
    @Transactional
    void deleteProductOk() {
        Product contactLenses = new Product(null, 4960999667454L, "Kontaktlinsen", "Weiche Kontaktlinsen", 40.3,  50);
        Product glassesCleaningCloth = new Product(null, 4005500256052L, "Brillenputztuch", "",
                3.3,  100);
        productRepository.persist(contactLenses);
        productRepository.persist(glassesCleaningCloth);
        logger.log(INFO, productRepository.listAll());

        productRepository.deleteById(contactLenses.getId());
        assertThat(productRepository.listAll())
                .doesNotContain(contactLenses);
        assertThat(productRepository.listAll().size()).isEqualTo(2);
    }

    @Test
    @Order(130)
    @Transactional
    void deleteProductTwice() {
        logger.log(INFO, productRepository.listAll());
        productRepository.deleteByEanCode(4005500256052L);
        productRepository.deleteByEanCode(4005500256052L);

        logger.log(INFO, productRepository.listAll());
        assertThat(productRepository
                .listAll()
                .stream()
                .map(Product::getName))
                .doesNotContain("Brillenputztuch");
        assertThat(productRepository.listAll().size()).isEqualTo(1);
    }

    @Test
    @Order(140)
    @Transactional
    void updateProduct() {
        Product contactLenses = new Product(null, 4960999667454L, "Kontaktlinsen", "Weiche Kontaktlinsen", 40.3,  50);
        Product glassesCleaningCloth = new Product(null, 4005500256052L, "Brillenputztuch", "",
                3.3,  100);
        productRepository.persist(contactLenses);
        productRepository.persist(glassesCleaningCloth);
        logger.log(INFO, productRepository.listAll());

        Product lensesUpdated = new Product(contactLenses.getId(), 4960999667454L, "Kontaktlinsen", "Weiche Kontaktlinsen", 44.3,  30);
        productRepository.update(lensesUpdated);

        logger.log(INFO, productRepository.listAll());
        assertThat(productRepository.listAll().size()).isEqualTo(3);
        assertThat(productRepository.listAll().stream().map(Product::getQuantity)).contains(30);
        assertThat(productRepository.listAll().stream().map(Product::getPrice)).contains(44.3);
        assertThat(productRepository.listAll().stream().map(Product::getQuantity)).doesNotContain(50);
        assertThat(productRepository.listAll().stream().map(Product::getPrice)).doesNotContain(40.3);
    }

    @Test
    @Order(160)
    @Transactional
    void findById() {
        Product glassesCase = new Product(null,4012345678994L, "Brillenetui", "von RayBan", 15.49,  80);
        productRepository.persist(glassesCase);
        logger.log(INFO, productRepository.listAll());

        assertThat(productRepository.findById(glassesCase.getId())).isEqualTo(glassesCase);
    }

    @Test
    @Order(170)
    void findByEanNotInRepoShouldReturnNull() {
        assertThat(productRepository.findByEanCode(4012345678987L)).isNull();
    }
}

