package se.leiden.asedajvf.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(info = @Info(title = "AsedaJvs API",version = "0.1",description = "API INFORMATION"))

public class SwaggerConfig {
}
