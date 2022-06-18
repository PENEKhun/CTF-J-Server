package com.penekhun.ctfjserver.Config;

import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.Exception.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("CTF-Backend-Public")
                .displayName("Public API")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public OpenAPI api() {

        Components responseComponents = new Components().addSecuritySchemes("bearer-key",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
        for (ErrorCode errCode : ErrorCode.values()) {
            Object example = new ErrorResponse(errCode.getErrorCode(), errCode.name(), errCode.getMessage());
            ApiResponse apiResponse = new ApiResponse().content(
                    new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                            new MediaType().example(example)));

            responseComponents.addResponses(String.format("%s.%s", ErrorCode.class.getSimpleName(), errCode), apiResponse);
        }

        return new OpenAPI()
                .components(responseComponents)
                .info(new Info()
                        .title("CTF-Backend API!")
                        .version("v1")
                        .description("hi..."));
    }
}
