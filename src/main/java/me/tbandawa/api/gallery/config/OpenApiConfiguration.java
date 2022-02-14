package me.tbandawa.api.gallery.config;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

public class OpenApiConfiguration {
	
	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Image Upload APIs")
	            .description("<b>Spring Boot Image Uploading sample application</b><p>"
	            		+ "Feautures:"
	            		+ "<ul>"
	            		+ "<li>multiple image upload</li>"
	            		+ "<li>exception handling</li>"
	            		+ "<li>api documentation</li>"
	            		+ "</ul>")
	            .version("v0.0.1")
	            .license(new License().name("Apache 2.0").url("http://springdoc.org")))
	            .externalDocs(new ExternalDocumentation()
	            .description("GitHub Page")
	            .url("https://github.com/tbandawa/spring-image-upload"));
	}

}
