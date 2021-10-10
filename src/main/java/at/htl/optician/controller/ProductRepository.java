package at.htl.optician.controller;

import at.htl.optician.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    @Inject
    EntityManager em;

    @Transactional
    public void deleteByEanCode(Long eanCode) {
        delete("ean_code",eanCode);
    }

    public Product findByEanCode(Long ean) {
        return find("ean_code", ean).stream().findFirst().orElse(null);
    }

    @Transactional
    public void update(Product product) {
        if (findById(product.getId()) != null) {
            em.merge(product);
        }
    }

    @Transactional
    public void reset() {
        em.createQuery("delete from InvoiceItem ").executeUpdate();
        em.createQuery("delete from Product ").executeUpdate();
    }

    @Transactional
    public void resetAndInit() {
        reset();
        persist(new Product(null, 4960999667454L, "Kontaktlinsen", "Weiche Kontaktlinsen", 40.3,  50));
        persist(new Product(null, 4005500256052L, "Brillenputztuch", "",
                3.3,  100));
        persist(new Product(null, 5901234123457L, "Woodfella Brille 930", "Brille mit Bügel aus Holz",
                130.20, 40));
    }
}
