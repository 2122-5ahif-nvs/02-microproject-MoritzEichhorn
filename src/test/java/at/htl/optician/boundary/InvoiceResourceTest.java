package at.htl.optician.boundary;

import at.htl.optician.controller.CustomerRepository;
import at.htl.optician.controller.InvoiceItemRepository;
import at.htl.optician.controller.InvoiceRepository;
import at.htl.optician.controller.ProductRepository;
import at.htl.optician.entity.Invoice;
import at.htl.optician.entity.InvoiceItem;
import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@QuarkusTest
public class InvoiceResourceTest {
    @Inject
    ProductRepository productRepository;

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    CustomerRepository customerRepository;

    @Inject
    InvoiceItemRepository invoiceItemRepository;
    @Inject
    Logger logger;

    @BeforeEach
    @Transactional
    void beforeEach() {
        customerRepository.resetAndInit();
        productRepository.resetAndInit();
        invoiceRepository.resetAndInit();
        invoiceItemRepository.reset();

        List<Invoice> invoices = invoiceRepository.listAll();
        invoiceItemRepository.persist(new InvoiceItem(invoices.get(0), productRepository.findByEanCode(4005500256052L), 1));
        invoiceItemRepository.persist(new InvoiceItem(invoices.get(1), productRepository.findByEanCode(4960999667454L), 2));
    }

    @Karate.Test
    Karate testGetStatistics() {
        return Karate.run("invoice-resource.feature").relativeTo(getClass());
    }
}
