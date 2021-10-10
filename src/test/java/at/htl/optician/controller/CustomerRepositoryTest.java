package at.htl.optician.controller;

import at.htl.optician.entity.Customer;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.transaction.*;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.logging.Logger.Level.INFO;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerRepositoryTest {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    Logger logger;

    @Test
    @Order(100)
    void repoExists() {
        assertThat(customerRepository).isNotNull();
    }

    @Test
    @Order(110)
    @Transactional
    void saveCustomerOk() {
        customerRepository.reset();
        Customer susi = new Customer(null, "Susi", LocalDate.of(2000, 10, 10),
                4040, "Linz", "Ziegeleistraße 20");

        logger.log(INFO, customerRepository.listAll());

        customerRepository.persist(susi);
        logger.log(INFO, customerRepository.listAll());
        assertThat(customerRepository.listAll()).contains(susi);
    }

    @Test
    @Order(120)
    @Transactional
    void deleteCustomerOk() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        Customer franz = new Customer(null, "Franz", LocalDate.of(2002, 11, 9),
                4203, "Altenberg", "Langlus 11a");
        Customer fritz = new Customer(null, "Fritz", LocalDate.of(1974, 7, 1),
                4020, "Stadt der Brille", "Brillenstraße");
        customerRepository.persist(franz);
        customerRepository.persist(fritz);
        logger.log(INFO, customerRepository.listAll());

        customerRepository.deleteById(franz.getId());

        logger.log(INFO, customerRepository.listAll());
        assertThat(customerRepository.listAll())
                .doesNotContain(franz);
        assertThat(customerRepository.listAll().size()).isEqualTo(2);
    }

    @Test
    @Order(130)
    @Transactional
    void deleteCustomerTwice() {
        logger.log(INFO, customerRepository.listAll());
        Optional<Customer> fritz = customerRepository.listAll().stream().filter(c -> c.getName().equals("Fritz"))
                .findFirst();

        if(fritz.isPresent()) {
            customerRepository.deleteById(fritz.get().getId());
            customerRepository.deleteById(fritz.get().getId());

            logger.log(INFO, customerRepository.listAll());
            assertThat(customerRepository
                    .listAll()
                    .stream()
                    .map(Customer::getName))
                    .doesNotContain("Fritz");
            assertThat(customerRepository.listAll().size()).isEqualTo(1);
        }
    }

    @Test
    @Order(140)
    @Transactional
    void updateCustomer() {
        Customer franz = new Customer(null, "Franz", LocalDate.of(2002, 11, 9),
                4203, "Altenberg", "Langlus 11a");
        Customer fritz = new Customer(null, "Fritz", LocalDate.of(1974, 7, 1),
                4020, "Stadt der Brille", "Brillenstraße");
        customerRepository.persist(franz);
        customerRepository.persist(fritz);
        logger.log(INFO, customerRepository.listAll());

        Customer franzUpdated = new Customer(franz.getId(), "Franzi", LocalDate.of(2002, 11, 9),
                4020, "Stadt der Brille", "Brillenstraße 15");


        customerRepository.update(franzUpdated);

        logger.log(INFO, customerRepository.listAll());
        assertThat(customerRepository.listAll().size()).isEqualTo(3);
        assertThat(customerRepository.findById(franzUpdated.getId()).getName()).isEqualTo("Franzi");
    }

    @Test
    @Order(150)
    @Transactional
    void updateCustomerNotInRepositoryShouldNotBePersisted() {
        logger.log(INFO, customerRepository.listAll());

        Customer unknownCustomer = new Customer(100L, "Worked", LocalDate.of(2002, 11, 9),
                4020, "Stadt der Brille", "Brillenstraße 15");

        customerRepository.update(unknownCustomer);
        assertThat(customerRepository.listAll().size()).isEqualTo(3);
        assertThat(customerRepository.listAll().stream().map(Customer::getName)).doesNotContain("Worked");
    }

    @Test
    @Order(160)
    @Transactional
    void findById() {
        Customer fredl = new Customer(null, "Fredl", LocalDate.of(2002, 10, 18),
                4203, "Altenberg", "Langlus 11a");
        customerRepository.persist(fredl);
        logger.log(INFO, customerRepository.listAll());

        assertThat(customerRepository.findById(fredl.getId())).isEqualTo(fredl);
    }

    @Test
    @Order(170)
    void findByIdNotInRepoShouldReturnNull() {
        Customer c = customerRepository.findById(100L);

        assertThat(c).isNull();
    }
}