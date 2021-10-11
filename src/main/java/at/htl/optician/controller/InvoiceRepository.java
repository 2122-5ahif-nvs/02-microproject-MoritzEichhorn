package at.htl.optician.controller;

import at.htl.optician.entity.Customer;
import at.htl.optician.entity.Invoice;
import at.htl.optician.entity.InvoiceItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jdk.jshell.spi.ExecutionControl;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.logging.Logger.Level.INFO;

@ApplicationScoped
public class InvoiceRepository implements PanacheRepository<Invoice> {
    @Inject
    EntityManager em;

    @Inject
    CustomerRepository customerRepository;

    public String getInvoiceStatistics(LocalDateTime start, LocalDateTime end) {
        Object[] results = em.createQuery("select count(distinct ii.invoice.customer), sum(ii.qtyInStock), sum(ii.totalPrice) / sum(ii.qtyInStock)  from InvoiceItem ii " +
                "where ii.invoice.purchaseDateTime between :start and :end", Object[].class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        double avgPrice = 0;
        long quantity = 0;
        // check if we sold anything during this period, otherwise avg price won't be set, because of division by 0
        if((long)results[0] != 0) {
            avgPrice = (double)results[2];
            quantity = (long)results[1];
        }

        return String.format("Number of customers: %d; Products sold: %d; Avg product price: %.2f", (long)results[0], quantity, avgPrice);
    }

    @Transactional
    public void update(Invoice invoice) {
        if (findById(invoice.getId()) != null) {
            em.merge(invoice);
        }
    }


    @Transactional
    public void reset() {
        em.createQuery("delete from Invoice").executeUpdate();
    }

    @Transactional
    public void resetAndInit() {
        customerRepository.resetAndInit();
        reset();
        List<Customer> customers = customerRepository.listAll();
        persist(new Invoice(null, customers.get(0), LocalDateTime.now()));
        persist(new Invoice(null, customers.get(1), LocalDateTime.now()));
        // save(new Invoice(null, customers.get(2), LocalDateTime.now()));
    }
}
