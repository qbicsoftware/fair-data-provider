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
    var exampleFilePath = (Path.of(this.getClass().getResource("/jsonld_example.json").toURI()))

    def "read from test resource"() {

        given:
        List<String> testJson = Files.readAllLines(exampleFilePath)
        when:
        var numberOfLines = testJson.size()

        then:
        numberOfLines == 20
    }

//    def "execute create method"() {
//        when:
//        Main.main("create","-f",exampleFilePath.toString())
//        then:
//        1 * CommandLineInput.create()
//    }

    def "read json file"() {
        given:
        String compareModel = "[@context:http://schema.org, @type:Dataset, @id:iri, dct:conformsTo:https://bioschemas.org/profiles/Dataset/1.0 RELEASE-2022_07_14, identifier:ngs123456, keywords:[@id:https://example/terms/rna.com, @type:DefinedTerm, inDefinedTermSet:https://example/terms.com, name:rna], url:https://example.com, description:A dataset description needs to be at least 50 chars long or otherwise google will throw an error which prevents the dataset for getting a rich result. Therefore: this is a very very interesting and cool dataset and completely deserves to be FAIR meaning findable, accessible, interoperable, reusable and it should be used by many other people because it is so awesome., name:some ngs dataset, license:https://example/url.com, creator:[@context:https://schema.org, @type:Organization, name:QBiC]]"
        when:
        var dataModel = FileHandler.readJsonFile(exampleFilePath.toFile())
        then:
        dataModel.toMapString() == compareModel
        noExceptionThrown()
    }





}
