package at.htl.optician.boundary;

import at.htl.optician.boundary.dtos.ProductDto;
import at.htl.optician.boundary.dtos.ProductInfoDto;
import at.htl.optician.controller.CustomerRepository;
import at.htl.optician.controller.InvoiceItemRepository;
import at.htl.optician.controller.InvoiceRepository;
import at.htl.optician.controller.ProductRepository;
import at.htl.optician.entity.Customer;
import at.htl.optician.entity.Invoice;
import at.htl.optician.entity.InvoiceItem;
import at.htl.optician.entity.Product;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.jboss.logging.Logger.Level.ERROR;

@Path("products")
@Tag(name = "Product")
public class ProductResource {
    @Inject
    ProductRepository productRepository;

    @Inject
    CustomerRepository customerRepository;

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Inject
    Logger logger;

    @Operation(
            summary = "Add one or multiple products to the inventory.",
            description = "As an employee I want to be able to add new products to the inventory." +
                    "If one or more products are invalid (for example invalid EAN-Code) none of the given" +
                    "products will be stored"
    )
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response insert(Product[] products) {
        Arrays.stream(products).forEach(p -> {
            productRepository.persist(p);
        });

        return Response.noContent().build();
    }

    @Operation(
            summary = "Buy a product",
            description = "As a customer I want to be able to buy products."
    )
    @POST
    @Path("buy")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response buyProduct(
            @RequestBody(content = {
                    @Content(schema = @Schema(
                            properties = {
                                    @SchemaProperty(name = "customerId", implementation = long.class),
                                    @SchemaProperty(name = "products", type = SchemaType.ARRAY,
                                            implementation = ProductDto.class)
                            }))}) JsonObject jsonBuy) {
        Customer buyer = customerRepository.findById((long) jsonBuy.getInt("customerId"));
        if (buyer == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Customer not found").build();
        }
        JsonArray jsonProducts = jsonBuy.getJsonArray("products");
        Invoice invoice = new Invoice(null, buyer, LocalDateTime.now());
        invoiceRepository.persist(invoice);
        Long invoiceId = invoice.getId();

        AtomicReference<Boolean> boughtProducts = new AtomicReference<>();

        jsonProducts.forEach(v -> {
            JsonObject p = v.asJsonObject();
            try {
                Product product = productRepository.findById(Long.valueOf(p.getString("id")));
                int quantity = p.getInt("quantity");

                if(product.getQuantity() >= quantity) {
                    invoiceItemRepository.persist(new InvoiceItem(invoice, product, quantity));
                    product.setQuantity(product.getQuantity() - quantity);
                    boughtProducts.set(true);
                } else {
                    logger.logf(ERROR, "Not enough of %d in store!", product.getEanCode());
                }
            } catch(IllegalArgumentException ex) {
                logger.log(ERROR, ex.getMessage());
            }
        });

        if(!boughtProducts.get()) {
            invoiceRepository.deleteById(invoiceId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(URI.create("/api/invoices/" + invoiceId)).build();
    }

    @Operation(
            summary = "Get the information of one product"
    )
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getProduct(@PathParam("id") Long id) {
        Product p = productRepository.findById(id);
        if(p == null) {
            return Response.status(404).build();
        }
        return Response
                .ok(new ProductInfoDto(p.getEanCode(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity()))
                .build();
    }
}
