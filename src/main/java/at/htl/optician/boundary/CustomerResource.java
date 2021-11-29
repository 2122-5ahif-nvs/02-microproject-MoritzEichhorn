package at.htl.optician.boundary;

import at.htl.optician.boundary.dtos.InvoiceItemDto;
import at.htl.optician.controller.CustomerRepository;
import at.htl.optician.controller.InvoiceItemRepository;
import at.htl.optician.entity.Customer;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.jboss.logging.Logger.Level.INFO;

@Path("customers")
@Tag(name = "Customers")
public class CustomerResource {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Inject
    Logger logger;

    @Operation(
            summary = "Register a customer",
            description = "As an employee I want to be able to register new customers."
    )
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response insert(Customer customer, @Context UriInfo uriInfo) {
        customerRepository.persist(customer);
        UriBuilder uriBuilder = uriInfo
                .getAbsolutePathBuilder()
                .path(customer.getId().toString());

        logger.log(INFO, uriInfo
                .getAbsolutePathBuilder()
                .path(customer.getId().toString()).build());

        return Response.created(uriBuilder.build()).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Customer> getAll() {
        return customerRepository.listAll();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getByID(@PathParam long id)
    {
        Customer c = customerRepository.findById(id);
        if(c == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(c).build();
        }
    }

    @GET
    @Path("getHistory/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getHistory(@PathParam long id)
    {
        return Response.ok(invoiceItemRepository.getByCustomer(id)
                .stream()
                    .map(i -> new InvoiceItemDto(i.getProduct().getName(), i.getInvoice().getPurchaseDateTime(),
                        i.getQuantity(), i.getQuantity()))
                                .collect(Collectors.toList())
                )
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response update(@PathParam long id, Customer customer) {
        Customer c = customerRepository.findById(id);
        if(c == null ) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        customer.setId(id);

        customerRepository.update(customer);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam long id) {
        customerRepository.deleteById(id);
        return Response.ok().build();
    }

    @PATCH
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response updatePatch(Customer customer) {
        Customer c = customerRepository.findById(customer.getId());
        if(c == null ) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        customerRepository.update(customer);
        return Response.ok().build();
    }
}