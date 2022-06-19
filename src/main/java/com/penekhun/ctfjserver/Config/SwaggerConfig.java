package com.penekhun.ctfjserver.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.sql.Timestamp;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Timestamp.class, Long.class)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CTF Backend API docs")
                .description("this is API description ~")
                .version("1.0")
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
