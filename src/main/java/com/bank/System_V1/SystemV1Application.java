package com.bank.System_V1;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Sab3 Islamic Bank System",
				description = "BackEnd REST Apis for the Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Omar Medhat",
						email = "mr.omarmedhat@gmail.com"
//						url = "https://github.com/Omar-Bank"
				),
				license = @License(
						name = "Omar Medhat"
						// url = github
				)
		)
		// externalDocs = @ExternalDocumentation(
		// description
		// url
		// )
)
public class SystemV1Application {

	public static void main(String[] args) {
		SpringApplication.run(SystemV1Application.class, args);
	}

}
