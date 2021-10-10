package at.htl.optician.controller;

import at.htl.optician.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {
    @Inject
    EntityManager em;

    @Transactional
    public void update(Customer customer) {
        if (findById(customer.getId()) != null) {
            em.merge(customer);
        }
    }

    @Transactional
    public void reset() {
        em.createQuery("delete from InvoiceItem").executeUpdate();
        em.createQuery("delete from Invoice").executeUpdate();
        em.createQuery("delete from Customer").executeUpdate();
    }

    @Transactional
    public void resetAndInit() {
        reset();
        persist(new Customer(null, "Susi", LocalDate.of(2000, 10, 10),
                4040, "Linz", "Ziegeleistraße 20"));
        persist(new Customer(null, "Franz", LocalDate.of(2002, 11, 9),
                4203, "Altenberg", "Langlus 11a"));
        persist(new Customer(null, "Fritz", LocalDate.of(1974, 7, 1),
                4020, "Stadt der Brille", "Brillenstraße"));
    }
}
