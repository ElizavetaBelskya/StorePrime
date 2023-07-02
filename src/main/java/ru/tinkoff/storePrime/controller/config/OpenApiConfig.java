package ru.tinkoff.storePrime.controller.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.tinkoff.storePrime.security.config.TokenSecurityConfig.AUTHENTICATION_URL;
import static ru.tinkoff.storePrime.security.filters.JwtAuthenticationFilter.USERNAME_PARAMETER;
import static ru.tinkoff.storePrime.security.filters.JwtRevokeFilter.REVOKE_TOKEN_URL;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .addSecurityItem(buildSecurity())
                .paths(buildAuthenticationPath())
                .components(buildComponents())
                .info(buildInfo());
    }

    private Paths buildAuthenticationPath() {
        return new Paths()
                .addPathItem(AUTHENTICATION_URL, buildAuthenticationPathItem())
                .addPathItem(REVOKE_TOKEN_URL, buildLogoutPathItem());
    }


    private PathItem buildAuthenticationPathItem() {
        return new PathItem().post(
                new Operation()
                        .addTagsItem("Authentication")
                        .requestBody(buildAuthenticationRequestBody())
                        .responses(buildAuthenticationResponses()));
    }

    private RequestBody buildAuthenticationRequestBody() {
        return new RequestBody().content(
                new Content()
                        .addMediaType("application/x-www-form-urlencoded",
                                new MediaType()
                                        .schema(new Schema<>()
                                                .$ref("EmailAndPassword"))));
    }

    private ApiResponses buildAuthenticationResponses() {
        return new ApiResponses()
                .addApiResponse("200",
                        new ApiResponse()
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .$ref("Tokens")))))
                .addApiResponse("401",
                        new ApiResponse()
                                .description("Unauthorized")
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .$ref("Error")))));
    }

    private PathItem buildLogoutPathItem() {
        return new PathItem().post(
                new Operation()
                        .addTagsItem("Authentication")
                        .requestBody(buildLogoutRequestBody())
                        .responses(buildLogoutResponses()));
    }

    private RequestBody buildLogoutRequestBody() {
        return new RequestBody().content(new Content());
    }

    private ApiResponses buildLogoutResponses() {
        return new ApiResponses()
                .addApiResponse("200",
                        new ApiResponse()
                                .description("Logout successful")
                )
                .addApiResponse("400",
                        new ApiResponse()
                                .description("Bad Request")
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .$ref("Error")))));
    }



    private SecurityRequirement buildSecurity() {
        return new SecurityRequirement().addList(BEARER_AUTH);
    }


    private Info buildInfo() {
        return new Info().title("StorePrime API").version("0.1");
    }

    private Components buildComponents() {
        Schema<?> error = new Schema<>().type("object").description("")
                .addProperty("timestamp", new Schema<>().type("integer"))
                .addProperty("status", new Schema<>().type("integer"))
                .addProperty("error", new Schema<>().type("string"))
                .addProperty("path", new Schema<>().type("string"));

        Schema<?> emailAndPassword = new Schema<>()
                .type("object").description("The email and password")
                .addProperty(USERNAME_PARAMETER, new Schema<>().type("string"))
                .addProperty("password", new Schema<>().type("string"));

        Schema<?> tokens = new Schema<>().type("object").description("")
                .addProperty("accessToken", new Schema<>().type("string"))
                .addProperty("refreshToken", new Schema<>().type("string"));

        SecurityScheme securityScheme = new SecurityScheme()
                .name(BEARER_AUTH)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("FWT");

        return new Components()
                .addSchemas("EmailAndPassword", emailAndPassword)
                .addSchemas("Tokens", tokens)
                .addSchemas("Error", error)
                .addSecuritySchemes(BEARER_AUTH, securityScheme);
    }


}
