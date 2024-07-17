package client


import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CitiesAverageTemperatureClientConfig {

    @Bean
    CitiesAverageTemperatureClient citiesAverageTemperatureClient(TestRestTemplate testRestTemplate) {
        return new CitiesAverageTemperatureClient(testRestTemplate)
    }
}
