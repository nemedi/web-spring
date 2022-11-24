package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class TasksConfiguration {
	
	@Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info().title("Tasks API")
            .version("1.0")
            .description("This is a sample CRUD Tasks API")
            .termsOfService("http://swagger.io/terms/")
            .license(new License().name("Apache 2.0")
                .url("http://springdoc.org")));
    }

}
