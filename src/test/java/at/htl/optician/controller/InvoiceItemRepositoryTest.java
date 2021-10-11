package at.htl.optician.controller;

import at.htl.optician.entity.Invoice;
import at.htl.optician.entity.InvoiceItem;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.logging.Logger.Level.INFO;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvoiceItemRepositoryTest {
    @Inject
    Logger logger;

    @Inject
    ProductRepository productRepository;

    @Inject
    CustomerRepository customerRepository;

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Test
    @Order(100)
    void repoExists() {
        assertThat(invoiceItemRepository).isNotNull();
    }

    @Test
    @Order(110)
    @Transactional
    void insertInvoiceItemOk() {
        invoiceItemRepository.reset();
        customerRepository.resetAndInit();
        invoiceRepository.resetAndInit();
        productRepository.resetAndInit();
        
        logger.log(INFO, invoiceItemRepository.listAll());

        invoiceItemRepository.persist(new InvoiceItem(invoiceRepository.listAll().get(0), productRepository.findByEanCode(5901234123457L), 1));
        logger.log(INFO, invoiceItemRepository.listAll());
        assertThat(invoiceItemRepository.listAll().stream().map(InvoiceItem::getProduct)).contains(productRepository.findByEanCode(5901234123457L));
    }

    @Test
    @Order(120)
    @Transactional
    void deleteInvoiceItemOk() {
        List<Invoice> invoices = invoiceRepository.listAll();
        invoiceItemRepository.persist(new InvoiceItem(invoices.get(0), productRepository.findByEanCode(4005500256052L), 1));
        invoiceItemRepository.persist(new InvoiceItem(invoices.get(1), productRepository.findByEanCode(4960999667454L), 2));
        logger.log(INFO, invoiceItemRepository.listAll());

        invoiceItemRepository.deleteByProductId(invoices.get(0).getId(), productRepository.findByEanCode(4005500256052L).getId());

        logger.log(INFO, invoiceItemRepository.listAll());
        assertThat(invoiceItemRepository
                .listAll()
                .stream()
                .map(InvoiceItem::getProduct))
                .doesNotContain(productRepository.findByEanCode(4005500256052L));
        assertThat(invoiceItemRepository.listAll().size()).isEqualTo(2);
    }

    @Test
    @Order(130)
    @Transactional
    void deleteInvoiceItemTwice() {
        logger.log(INFO, invoiceItemRepository.listAll());

        List<Invoice> invoices = invoiceRepository.listAll();
        invoiceItemRepository.deleteByProductId(invoices.get(1).getId(), productRepository.findByEanCode(4960999667454L).getId());
        invoiceItemRepository.deleteByProductId(invoices.get(1).getId(), productRepository.findByEanCode(4960999667454L).getId());

        logger.log(INFO , invoiceItemRepository.listAll());
        assertThat(invoiceItemRepository.listAll().size()).isEqualTo(1);
    }

    @Test
    @Order(180)
    @Transactional
    void updateInvoiceItemOk() {
        List<Invoice> invoices = invoiceRepository.listAll();
        InvoiceItem glasses = new InvoiceItem(invoices.get(0), productRepository.findByEanCode(4005500256052L), 1);
        invoiceItemRepository.persist(glasses);
        invoiceItemRepository.persist(new InvoiceItem(invoices.get(1), productRepository.findByEanCode(4960999667454L), 2));

        InvoiceItem updated = new InvoiceItem(invoices.get(0), productRepository.findByEanCode(4005500256052L), 3);
        updated.setId(glasses.getId());
        logger.log(INFO, invoiceItemRepository.listAll());
        invoiceItemRepository.update(updated);

        logger.log(INFO, invoiceItemRepository.listAll());
        Optional<InvoiceItem> item = invoiceRepository.findById(invoices.get(0).getId()).getInvoiceItems().stream()
                .filter(i -> i.getProduct().getEanCode().equals(4005500256052L))
                .findFirst();

        assertThat(item).isPresent();
        item.ifPresent(InvoiceItem -> assertThat(InvoiceItem.getQtyInStock()).isEqualTo(3));

        assertThat(invoiceItemRepository.listAll().size()).isEqualTo(3);
    }
}
