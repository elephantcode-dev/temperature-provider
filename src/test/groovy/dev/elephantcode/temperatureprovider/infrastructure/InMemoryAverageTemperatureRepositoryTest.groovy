package dev.elephantcode.temperatureprovider.infrastructure

import dev.elephantcode.temperatureprovider.domain.AnnualAverageTemperature
import spock.lang.Specification

class InMemoryAverageTemperatureRepositoryTest extends Specification {

    InMemoryAverageTemperatureRepository repository = new InMemoryAverageTemperatureRepository()

    def 'values added with putAll should be available in findAll'() {
        given:
        def temperature = new AnnualAverageTemperature("2048", BigDecimal.TWO)

        when:
        repository.putAll("test", [temperature])

        and:
        def result = repository.findAll("test")

        then:
        result.size() == 1
        verifyAll(result.first) {
            it.year() == temperature.year()
            it.averageTemperature() == temperature.averageTemperature()
        }
    }

    def 'should return empty list when entry not found'() {
        when:
        def result = repository.findAll("non-existing")

        then:
        result.isEmpty()
    }

    def 'should override existing proves on putAll'() {
        given:
        def existingCityAProbe = new AnnualAverageTemperature("2048", BigDecimal.TWO)
        def existingCityBProbe = new AnnualAverageTemperature("2025", BigDecimal.ONE)

        and:
        repository.putAll("cityA", [existingCityAProbe])
        repository.putAll("cityB", [existingCityBProbe])

        and:
        def updateOfCityAProbe = new AnnualAverageTemperature("2048", BigDecimal.TEN)

        when:
        repository.putAll("cityA", [updateOfCityAProbe])

        then:
        def result = repository.findAll("cityA")
        result.size() == 1
        verifyAll(result.first) {
            it.averageTemperature() == updateOfCityAProbe.averageTemperature()
            it.year() == it.year()
        }

        and:
        def cityBResult = repository.findAll("cityB")
        cityBResult.size() == 1
        verifyAll(cityBResult.first) {
            it.averageTemperature() == existingCityBProbe.averageTemperature()
            it.year() == existingCityBProbe.year()
        }

    }
}
