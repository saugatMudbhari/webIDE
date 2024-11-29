package com.share;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Saugat");
        myContact.setEmail("test.test@gmail.com");

        Info information = new Info()
                .title("this is for the shareIde")
                .version("1.0")
                .description("This API exposes endpoints that I have developed till now.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
