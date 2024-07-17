package client

import dev.elephantcode.temperatureprovider.api.AnnualAverageTemperatureDto
import dev.elephantcode.temperatureprovider.api.ApiErrorHandler
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder

class CitiesAverageTemperatureClient {

    private final static String GET_AVERAGE_TEMP_PATH = "/api/cities/%s/temperature/average"

    private final TestRestTemplate testRestTemplate

    CitiesAverageTemperatureClient(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate
    }

    ResponseEntity<List<AnnualAverageTemperatureDto>> getAnnualAverageTemperature(String city) {
        def uri = UriComponentsBuilder.fromPath(GET_AVERAGE_TEMP_PATH.formatted(city))
                .build().toUri()

        def request = RequestEntity.get(uri)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build()

        testRestTemplate.exchange(request, new ParameterizedTypeReference<List<AnnualAverageTemperatureDto>>() {})
    }

    ResponseEntity<ApiErrorHandler.ErrorResponse> getAnnualAverageTemperatureAndExpectError(String city) {
        def uri = UriComponentsBuilder.fromPath(GET_AVERAGE_TEMP_PATH.formatted(city))
                .build().toUri()

        def request = RequestEntity.get(uri)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build()

        testRestTemplate.exchange(request, ApiErrorHandler.ErrorResponse)
    }
}
