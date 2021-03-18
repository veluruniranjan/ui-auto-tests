package com.zenjob.testrailsync

import com.zenjob.testrailsync.obj.TestCaseResult
import com.zenjob.testrailsync.obj.parsing.Element
import com.zenjob.testrailsync.obj.parsing.Feature
import com.zenjob.testrailsync.obj.parsing.Result
import com.zenjob.testrailsync.obj.parsing.Step
import com.zenjob.testrailsync.obj.parsing.Tag
import com.zenjob.testrailsync.translators.FeaturesToTestCaseResultsTranslator
import net.masterthought.cucumber.json.support.Status
import spock.lang.Specification

class FeaturesToTestCaseResultsTranslatorSpec extends Specification {

    def "Parse multiple features"() {
        given:
        List<Feature> features = [
                new Feature(
                        id: "login",
                        name: "login",
                        description: "login feature",
                        keyword: "Feature",
                        elements: Arrays.asList(new Element(
                                id: "login",
                                name: "login",
                                description: "login feature",
                                keyword: "scenario",
                                type: "scenario",
                                steps: [new Step(
                                        name: "go to page",
                                        result: new Result(status: Status.PASSED)
                                ), new Step(
                                        name: "fill form",
                                        result: new Result(status: Status.PASSED)
                                ), new Step(
                                        name: "submit login",
                                        result: new Result(status: Status.PASSED)
                                )],
                                tags: [
                                        new Tag(
                                                name: "@tid100"
                                        )
                                ]
                        ))
                ),
                new Feature(
                        id: "order-creation",
                        name: "Order creation",
                        description: "",
                        keyword: "Feature",
                        elements: [
                                new Element(
                                        id: "order-creation;create-simple-order",
                                        name: "Create simple order",
                                        description: "",
                                        keyword: "scenario",
                                        type: "scenario",
                                        steps: [new Step(
                                                name: "open order page",
                                                result: new Result(status: Status.PASSED)
                                        ), new Step(
                                                name: "submit order",
                                                result: new Result(status: Status.PASSED)
                                        )],
                                        tags: [
                                                new Tag(
                                                        name: "@tid200"
                                                )
                                        ]
                                ),
                                new Element(
                                        id: "order-creation;create-order-with-two-shifts",
                                        name: "Create order with two shifts",
                                        description: "",
                                        keyword: "scenario",
                                        type: "scenario",
                                        steps: [new Step(
                                                name: "open login page",
                                                result: new Result(status: Status.PASSED)
                                        ), new Step(
                                                name: "submit order with two shifts",
                                                result: new Result(status: Status.FAILED)
                                        )],
                                        tags: [
                                                new Tag(
                                                        name: "@tid201"
                                                )
                                        ]
                                )]
                )
        ]

        when:
        def result = new FeaturesToTestCaseResultsTranslator().parseFeaturesToTestRailResults(features)

        then:
        result.size() == 3
        result.each {
            [
                    new TestCaseResult(
                            id: 100,
                            status: true
                    ),
                    new TestCaseResult(
                            id: 200,
                            status: true
                    ),
                    new TestCaseResult(
                            id: 201,
                            status: false
                    )
            ].contains(it)
        }

    }

    def "Parse empty Feature"() {
        given:
        List<Feature> features = new ArrayList<>()

        when:
        def result = new FeaturesToTestCaseResultsTranslator().parseFeaturesToTestRailResults(features)

        then:
        result.isEmpty()
    }

    def "Parse Feature without test-cases related to TestRail"() {
        given:
        List<Feature> features = [
                new Feature(
                        id: "login",
                        name: "login",
                        description: "login feature",
                        keyword: "Feature",
                        elements: Arrays.asList(new Element(
                                id: "login",
                                name: "login",
                                description: "login feature",
                                keyword: "scenario",
                                type: "scenario",
                                steps: [new Step(
                                        name: "go to page",
                                        result: new Result(status: Status.PASSED)
                                ), new Step(
                                        name: "fill form",
                                        result: new Result(status: Status.PASSED)
                                ), new Step(
                                        name: "submit login",
                                        result: new Result(status: Status.PASSED)
                                )],
                                tags: [
                                        new Tag(
                                                name: "@SMOKE"
                                        )
                                ]
                        ))
                )
        ]

        when:
        def result = new FeaturesToTestCaseResultsTranslator().parseFeaturesToTestRailResults(features)

        then:
        result.isEmpty()
    }

    def "Scenario result with all steps passed"() {
        given:
        def element = new Element(
                id: "login",
                name: "login",
                description: "login feature",
                keyword: "scenario",
                type: "scenario",
                steps: [new Step(
                        name: "go to page",
                        result: new Result(status: Status.PASSED)
                ), new Step(
                        name: "fill form",
                        result: new Result(status: Status.PASSED)
                ), new Step(
                        name: "submit login",
                        result: new Result(status: Status.PASSED)
                )],
                tags: [
                        new Tag(
                                name: "@SMOKE"
                        )
                ]
        )

        when:
        def result = new FeaturesToTestCaseResultsTranslator().isScenarioPassed(element)

        then:
        assert result
    }

    def "Scenario result if steps are skipped"() {
        given:
        def element = new Element(
                id: "login",
                name: "login",
                description: "login feature",
                keyword: "scenario",
                type: "scenario",
                steps: [new Step(
                        name: "go to page",
                        result: new Result(status: Status.SKIPPED)
                ), new Step(
                        name: "fill form",
                        result: new Result(status: Status.SKIPPED)
                ), new Step(
                        name: "submit login",
                        result: new Result(status: Status.SKIPPED)
                )],
                tags: [
                        new Tag(
                                name: "@tid520"
                        )
                ]
        )

        when:
        def result = new FeaturesToTestCaseResultsTranslator().isScenarioPassed(element)

        then:
        assert !result

    }

    def "Scenario result if one of the steps failed"() {
        given:
        def element = new Element(
                id: "login",
                name: "login",
                description: "login feature",
                keyword: "scenario",
                type: "scenario",
                steps: [new Step(
                        name: "go to page",
                        result: new Result(status: Status.PASSED)
                ), new Step(
                        name: "fill form",
                        result: new Result(status: Status.FAILED)
                ), new Step(
                        name: "submit login",
                        result: new Result(status: Status.PASSED)
                )],
                tags: [
                        new Tag(
                                name: "@SMOKE"
                        )
                ]
        )

        when:
        def result = new FeaturesToTestCaseResultsTranslator().isScenarioPassed(element)

        then:
        assert !result
    }

    def "Scenario result if one of the steps skipped"() {
        given:
        def element = new Element(
                id: "login",
                name: "login",
                description: "login feature",
                keyword: "scenario",
                type: "scenario",
                steps: [new Step(
                        name: "go to page",
                        result: new Result(status: Status.PASSED)
                ), new Step(
                        name: "fill form",
                        result: new Result(status: Status.SKIPPED)
                ), new Step(
                        name: "submit login",
                        result: new Result(status: Status.PASSED)
                )],
                tags: [
                        new Tag(
                                name: "@SMOKE"
                        )
                ]
        )

        when:
        def result = new FeaturesToTestCaseResultsTranslator().isScenarioPassed(element)

        then:
        assert !result
    }
}
