package dev.elephantcode.temperatureprovider

import client.CitiesAverageTemperatureClient
import client.CitiesAverageTemperatureClientConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@Import(CitiesAverageTemperatureClientConfig)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemperatureProviderIntegrationTest extends Specification {

    @Autowired
    CitiesAverageTemperatureClient client

    def 'should load file on context startup'() {
        when:
        def response = client.getAnnualAverageTemperature("Warszawa")

        then:
        response.statusCode.is2xxSuccessful()
        verifyAll(response.body) {
            it.size() == 4
            it.find { it.year() == "2018" }.averageTemperature() == 1.10
            it.find { it.year() == "2019" }.averageTemperature() == 2.20
            it.find { it.year() == "2020" }.averageTemperature() == 3.30
            it.find { it.year() == "2021" }.averageTemperature() == 4.40
        }
    }

    def 'should find average temperature regardless of casing'() {
        when:
        def response = client.getAnnualAverageTemperature(city)

        then:
        response.statusCode.is2xxSuccessful()
        verifyAll(response.body) {
            it.size() == 4
            it.find { it.year() == "2018" }.averageTemperature() == 1.10
            it.find { it.year() == "2019" }.averageTemperature() == 2.20
            it.find { it.year() == "2020" }.averageTemperature() == 3.30
            it.find { it.year() == "2021" }.averageTemperature() == 4.40
        }

        where:
        city << ["Warszawa", "warszawa", "WARSZAWA", "WaRsZaWa"]
    }

    def 'should validate request'() {
        when:
        def response = client.getAnnualAverageTemperatureAndExpectError(city)

        then:
        response.statusCode.value() == 400
        verifyAll(response.body) {
            it.timestamp() != null
            it.message() == "Invalid request"
            it.code() == 'BAD_REQUEST'
        }

        where:
        city << [" ", "definitely-too-long-value-definitely-too-long-value-definitely-too-long-value-definitely-too-long-value"]
    }
}
