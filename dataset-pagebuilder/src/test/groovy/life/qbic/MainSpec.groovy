package life.qbic

import spock.lang.Specification

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

    def "example: test a function in a different class"() {
        when:
        def b = Main.min(1,2)

        then:
        b == 1
    }

}
