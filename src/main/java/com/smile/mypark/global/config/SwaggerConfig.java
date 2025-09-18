package com.smile.mypark.global.config;

import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smile.mypark.global.annotation.AuthUser;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Value("${app.url}")
	private String url;

	static {
		SpringDocUtils.getConfig().addAnnotationsToIgnore(AuthUser.class);
	}

	@Bean
	public OpenAPI openAPI() {
		String securitySchemeName = "bearerAuth";

		Server server = new Server()
			.url(url)
			.description("server");

		return new OpenAPI()
			.info(new Info().title("MyPark API").version("1.0"))
			.addServersItem(server)
			.components(new Components()
				.addSecuritySchemes(securitySchemeName,
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				)
			)
			.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
	}
}