package com.dev.prepaid.config;

import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  private ApiInfo apiInfo() {
    ApiInfo apiInfo = new ApiInfo("My REST API",
        "Some custom description of API.",
        "API TOS",
        "Terms of service",
        new Contact("Name", "www.example.com", "myeaddress@company.com"),
        "License of API",
        "API license URL",
        Collections.emptyList());
    return apiInfo;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)//
        .select()//
        .apis(RequestHandlerSelectors.any())//
        .paths(Predicates.not(PathSelectors.regex("/error")))//
        .build()//
        .apiInfo(apiInfo())//
        .useDefaultResponseMessages(false)//
        .securitySchemes(Collections.singletonList(new ApiKey("Bearer %token", "Authorization", "Header")))
        .genericModelSubstitutes(Optional.class);
  }


}
