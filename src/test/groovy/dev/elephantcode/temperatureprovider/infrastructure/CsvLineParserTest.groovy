package dev.elephantcode.temperatureprovider.infrastructure

import spock.lang.Specification

class CsvLineParserTest extends Specification {

    CsvLineParser csvLineParser = new CsvLineParser()

    def 'should parse csv line'() {
        given:

        when:
        def result = csvLineParser.parseLine(csvLine)

        then:
        result.isPresent() == shouldParse
        result.ifPresent {
            verifyAll(it) {
                it.city() == city
                it.year() == year
                it.temperature() == probe
            }
        }

        where:
        csvLine                                  | shouldParse | city       | year   | probe
        "Warszawa;2024-09-26 11:40:28.793;-1.62" | true        | "Warszawa" | "2024" | new BigDecimal("-1.62")
        "Warszawa;2024-09-26 11:40:28.793;NaN"   | false       | null       | null   | null
        "Warszawa;2024-09-26 11:40:28.793;"      | false       | null       | null   | null
        "Warszawa;;1.62"                         | false       | null       | null   | null
        " ;2024-09-26 11:40:28.793;1.62"         | false       | null       | null   | null
        ""                                       | false       | null       | null   | null
        null                                     | false       | null       | null   | null
    }
}
