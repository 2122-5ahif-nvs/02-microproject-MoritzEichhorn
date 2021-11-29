package at.htl.optician.controller;

import at.htl.optician.entity.Invoice;
import at.htl.optician.entity.InvoiceItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class InvoiceItemRepository implements PanacheRepository<InvoiceItem> {
    @Inject
    InvoiceRepository invoiceRepository;

    private Invoice getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId);

        if (invoice == null) {
            throw new IllegalArgumentException("Couldn't find invoice with id " + invoiceId);
        }

        return invoice;
    }

    @Transactional
    public void deleteByProductId(Long invoiceId, Long productId) {
        Invoice invoice = getInvoiceById(invoiceId);

        Optional<InvoiceItem> toDelete = invoice.getInvoiceItems()
                .stream()
                .filter(l -> l.getProduct().getId().equals(productId))
                .findFirst();

        if(toDelete.isPresent()) {
            invoice.getInvoiceItems().remove(toDelete.get());
            delete(toDelete.get());
        }
    }

    @Transactional
    public void update(InvoiceItem invoiceItem) {
        if (findById(invoiceItem.getId()) != null) {
            em.merge(invoiceItem);
        }
    }

    @Inject
    EntityManager em;

    @Transactional
    public void reset() {
        em.createQuery("delete from InvoiceItem").executeUpdate();
    }


    public List<InvoiceItem> getByCustomer(Long id) {
        return find("invoice.customer.id", id).stream().collect(Collectors.toList());
    }
}
