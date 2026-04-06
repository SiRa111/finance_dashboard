package zovryn.finance_dashboard;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Finance Dashboard API",
        version = "1.0.0",
        description = "REST API for personal finance tracking and dashboard management with role-based access control",
        contact = @Contact(
            name = "Finance Dashboard Team",
            url = "https://example.com"
        ),
        license = @License(
            name = "MIT",
            url = "https://opensource.org/licenses/MIT"
        )
    )
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Enter JWT token. Example: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
)
public class FinanceDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceDashboardApplication.class, args);
	}

}
