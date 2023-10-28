package apiGateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//klasa za konfigurisanje koji mikroservis zelimo da pogodimo da eliminisemo onaj dodatak. 
//U ovoj klasi je metoda gateway i vraca ovaj RouteLocator koji je interfejs, a Bean je da spring kontejner pravi objekat jedan ovog RouteLocator
//Ovaj Bean je koristan jer on obavlja posao rutiranja.
//
@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gateway(RouteLocatorBuilder builder) {
		
		return	builder.routes().route(p -> p.path("/currency-exchange/**").uri("lb://currency-exchange"))
				.route(p -> p.path("/currency-conversion/**").uri("lb://currency-conversion"))
				.route(p -> p.path("/users/**").uri("lb://users"))
				.route(p -> p.path("/crypto-wallet/**").uri("lb://crypto-wallet"))
				.route(p -> p.path("/crypto-conversion/**").uri("lb://crypto-conversion"))
				.route(p -> p.path("/bank-account/**").uri("lb://bank-account"))
				.route(p -> p.path("/transfer-service/**").uri("lb://transfer-service"))
				.route(p -> p.path("/trade-service/**").uri("lb://trade-service"))
				.route(p -> p.path("/crypto-exchange/**").uri("lb://crypto-exchange")).build();
	}
	//username:user a lozinka ne znam... morali bismo se prijaviti da dobijemo tu provjeru i da se prijavimo pa da tek onda vidimo podatke.

}
