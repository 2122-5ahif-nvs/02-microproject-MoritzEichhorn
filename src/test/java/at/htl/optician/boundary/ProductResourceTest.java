package at.htl.optician.boundary;

import at.htl.optician.controller.CustomerRepository;
import at.htl.optician.controller.InvoiceItemRepository;
import at.htl.optician.controller.InvoiceRepository;
import at.htl.optician.controller.ProductRepository;
import at.htl.optician.entity.Product;
import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ProductResourceTest {
    @Inject
    ProductRepository productRepository;

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    CustomerRepository customerRepository;

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Inject Logger logger;

    @Test
    void testBuyProduct() {
        invoiceItemRepository.reset();
        customerRepository.resetAndInit();
        invoiceRepository.reset();
        productRepository.resetAndInit();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder p1 = Json.createObjectBuilder();
        p1.add("quantity", 1);
        p1.add("id", productRepository.findByEanCode(4005500256052L).getId().toString());
        arrayBuilder.add(p1.build());

        JsonObjectBuilder p2 = Json.createObjectBuilder();
        p1.add("quantity", 1);
        p1.add("id", productRepository.findByEanCode(5901234123457L).getId().toString());
        arrayBuilder.add(p1.build());

        JsonObjectBuilder fullBuy = Json.createObjectBuilder();
        fullBuy.add("customerId", customerRepository.listAll().get(0).getId());
        fullBuy.add("products", arrayBuilder.build());

        Response response = given()
                .contentType("application/json")
                .body(fullBuy.build().toString())
            .when()
                .post("products/buy")
            .then()
                .statusCode(201)
                .extract()
                .response();

        logger.info(response);

        long invoiceId = Long.parseLong(response.header("Location").split("/")[response.header("Location").split("/").length - 1]);

        assertThat(invoiceRepository.findById(invoiceId).getInvoiceItems().size()).isEqualTo(2);
    }

    @Test
    void testBuyProductCustomerNotFoundShouldReturn400() {
        invoiceItemRepository.reset();
        customerRepository.resetAndInit();
        invoiceRepository.reset();
        productRepository.resetAndInit();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder p1 = Json.createObjectBuilder();
        p1.add("quantity", 1);
        p1.add("id", productRepository.findByEanCode(4005500256052L).getId().toString());
        arrayBuilder.add(p1.build());

        JsonObjectBuilder p2 = Json.createObjectBuilder();
        p1.add("quantity", 1);
        p1.add("id", productRepository.findByEanCode(5901234123457L).getId().toString());
        arrayBuilder.add(p1.build());

        JsonObjectBuilder fullBuy = Json.createObjectBuilder();
        fullBuy.add("customerId", 100);
        fullBuy.add("products", arrayBuilder.build());

        Response response = given()
                .contentType("application/json")
                .body(fullBuy.build().toString())
                .when()
                .post("products/buy")
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Karate.Test
    Karate productTest() {
        return Karate.run("product-resource.feature").relativeTo(getClass());
    }
}
