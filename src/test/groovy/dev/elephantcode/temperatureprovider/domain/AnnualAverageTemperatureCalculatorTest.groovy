package dev.elephantcode.temperatureprovider.domain

import dev.elephantcode.temperatureprovider.infrastructure.CsvLineParser
import dev.elephantcode.temperatureprovider.infrastructure.TemperatureFileReaderAdapter
import spock.lang.Specification

class AnnualAverageTemperatureCalculatorTest extends Specification {

    AverageTemperatureRepository repository = Mock()
    TemperatureFileReader temperatureFileReader = new TemperatureFileReaderAdapter(new CsvLineParser())

    AnnualAverageTemperatureCalculator calculator = new AnnualAverageTemperatureCalculator(repository, temperatureFileReader)

    def 'should process file'() {
        given:
        File file = new File("src/test/resources/example_file.csv")

        when:
        calculator.process(file)

        then:
        1 * repository.putAll("warszawa", _)
        1 * repository.putAll("gdańsk", _)

    }
}
