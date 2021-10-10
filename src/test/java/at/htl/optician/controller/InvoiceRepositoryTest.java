package at.htl.optician.controller;

import at.htl.optician.entity.Invoice;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.logging.Logger.Level.INFO;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InvoiceRepositoryTest {
    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    CustomerRepository customerRepository;

    @Inject
    Logger logger;

    @Test
    @Order(100)
    void repoExists() {
        assertThat(invoiceRepository).isNotNull();
    }

    @Test
    @Order(110)
    @Transactional
    void saveInvoiceOk() {
        customerRepository.resetAndInit();
        invoiceRepository.reset();
        logger.log(INFO, invoiceRepository.listAll());

        Invoice susisInvoice = new Invoice(null, customerRepository.findById(1L), LocalDateTime.now());
        invoiceRepository.persist(susisInvoice);

        assertThat(invoiceRepository.listAll()).contains(susisInvoice);
    }

    @Test
    @Order(120)
    @Transactional
    void deleteInvoiceOk() {
        Invoice franzInvoice = new Invoice(null, customerRepository.findById(2L), LocalDateTime.now());
        Invoice fitzInvoice = new Invoice(null, customerRepository.findById(3L), LocalDateTime.now());
        invoiceRepository.persist(franzInvoice);
        invoiceRepository.persist(fitzInvoice);
        logger.log(INFO, invoiceRepository.listAll());

        invoiceRepository.deleteById(franzInvoice.getId());

        logger.log(INFO, invoiceRepository.listAll());
        assertThat(invoiceRepository.listAll())
                .doesNotContain(franzInvoice);
        assertThat(invoiceRepository.listAll().size()).isEqualTo(2);
    }

    @Test
    @Order(130)
    @Transactional
    void deleteInvoiceTwice() {
        Invoice franzSecondInvoice = new Invoice(null, customerRepository.findById(2L), LocalDateTime.now());
        invoiceRepository.persist(franzSecondInvoice);
        logger.log(INFO, invoiceRepository.listAll());

        invoiceRepository.deleteById(franzSecondInvoice.getId());
        invoiceRepository.deleteById(franzSecondInvoice.getId());

        logger.log(INFO, invoiceRepository.listAll());
        assertThat(invoiceRepository
                .listAll())
                .doesNotContain(franzSecondInvoice);
        assertThat(invoiceRepository.listAll().size()).isEqualTo(2);
    }

    @Test
    @Order(140)
    @Transactional
    void updateInvoice() {
        Invoice franzInvoice = new Invoice(null, customerRepository.findById(2L), LocalDateTime.now());
        invoiceRepository.persist(franzInvoice);
        logger.log(INFO, invoiceRepository.listAll());

        Invoice franzInvoiceUpdated = new Invoice(franzInvoice.getId(), customerRepository.findById(1L),
                LocalDateTime.of(2020, 11, 11, 11, 30));

        invoiceRepository.update(franzInvoiceUpdated);

        logger.log(INFO, invoiceRepository.listAll());
        assertThat(invoiceRepository.listAll().size()).isEqualTo(3);
        assertThat(invoiceRepository.listAll().stream().map(Invoice::getPurchaseDateTime)).contains(LocalDateTime.of(2020, 11, 11, 11, 30));
        assertThat(invoiceRepository.listAll().stream().map(Invoice::getCustomer)).doesNotContain(customerRepository.findById(2L));
    }

    @Test
    @Order(150)
    @Transactional
    void updateInvoiceNotInRepositoryShouldNotBePersisted() {
        logger.log(INFO, invoiceRepository.listAll());

        Invoice unknownInvoice = new Invoice(100L, customerRepository.findById(2L), LocalDateTime.now());

        invoiceRepository.update(unknownInvoice);
        assertThat(invoiceRepository.listAll().size()).isEqualTo(3);
        assertThat(invoiceRepository
                .listAll()
                .stream()
                .map(Invoice::getCustomer))
                .doesNotContain(customerRepository.findById(2L));
    }

    @Test
    @Order(160)
    @Transactional
    void findById() {
        Invoice franzInvoice = new Invoice(null, customerRepository.findById(2L), LocalDateTime.now());
        invoiceRepository.persist(franzInvoice);
        logger.log(INFO, invoiceRepository.listAll());

        assertThat(invoiceRepository.findById(franzInvoice.getId())).isEqualTo(franzInvoice);
    }

    @Test
    @Order(170)
    void findByIdNotInRepoShouldReturnNull() {
        assertThat(invoiceRepository.findById(100L)).isNull();
    }
}