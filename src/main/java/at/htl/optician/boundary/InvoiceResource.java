package at.htl.optician.boundary;

import at.htl.optician.controller.InvoiceItemRepository;
import at.htl.optician.controller.InvoiceRepository;
import at.htl.optician.entity.Invoice;
import at.htl.optician.entity.InvoiceItem;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.jboss.logging.Logger.Level.INFO;

@Path("invoices")
@Tag(name = "Invoice")
public class InvoiceResource {
    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    Logger logger;

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Operation(
            summary = "Get a description over a given period",
            description = "As an employee I want to be able to get a statistic over a given period,\n" +
                    "which shows how many different customers we had, how many products\n" +
                    "were sold during this period and the average price of the products"
    )
    @GET
    @Path("statistics")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response getStatistics(@QueryParam("s") String periodStart, @QueryParam("e") String periodEnd) {
        LocalDateTime start = LocalDateTime.parse(periodStart);
        LocalDateTime end = LocalDateTime.parse(periodEnd);
        return Response.ok(invoiceRepository.getInvoiceStatistics(start, end)).build();

    }
}
