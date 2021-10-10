package at.htl.optician.boundary;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(

        info = @Info(
                title="Eichhorn-Optician API",
                version = "1.0.3",
                description = "This is the API for the optician business used in a school project to use the learned" +
                        "techniques.")
)
public class ApiDocumentation extends Application {
}
