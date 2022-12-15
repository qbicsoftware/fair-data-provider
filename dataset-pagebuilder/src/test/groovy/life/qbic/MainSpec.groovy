package life.qbic

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
class MainSpec extends Specification {

    def "see if the spock tests work"() {

        when:
        def a = 2

        then:
        a == 2

    }

    def "read from test resource"() {

        given:
        List<String> testJson = Files.readAllLines(
                Path.of(this.getClass().getResource("/example.json").toURI()))

        when:
        var numberOfLines = testJson.size()

        then:
        numberOfLines == 3

    }

}
