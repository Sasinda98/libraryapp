//package com.gsr.library.libraryapp.configurations;
//
//import io.swagger.v3.oas.models.ExternalDocumentation;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import org.springdoc.core.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
////http://localhost:8080/v2/api-docs
////http://localhost:8080/swagger-ui.html
//public class SwaggerConfiguration {
////    @Bean
////    public GroupedOpenApi api(){
////        return new Docket(DocumentationType.SWAGGER_2)
////                .select()
////                .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.any())
////                .build()
////                .useDefaultResponseMessages(false)  //prevent swagger from putting default response messages.
////                .pathMapping("/")
////                .apiInfo(apiInfo());
////    }
////    @Bean
////    public GroupedOpenApi publicApi() {
////        return GroupedOpenApi.builder()
////                .group("library-books")
////                .pathsToMatch("/**")
////                .build();
////    }
//
//    @Bean
//    public OpenAPI springShopOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("Library API")
//                        .description("API that models library.")
//                        .version("v0.0.1")
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
//    }
//
//}
