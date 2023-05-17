package me.tbandawa.api.gallery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfiguration {
	
	private static final String SCHEME_NAME = "Bearer Authentication";
    private static final String SCHEME = "bearer";
	
	@Bean
	public OpenAPI springGalleryOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Image Upload APIs")
	            .description("<b>Spring Boot Image Uploading sample application</b>")
	            .version("v0.0.1")
	            .license(new License().name("Apache 2.0").url("http://springdoc.org")))
	            .externalDocs(new ExternalDocumentation()
	            .description("GitHub Page")
	            .url("https://github.com/tbandawa/spring-image-upload"))
	            .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, createSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME));
	}
 
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME);
    }
}
