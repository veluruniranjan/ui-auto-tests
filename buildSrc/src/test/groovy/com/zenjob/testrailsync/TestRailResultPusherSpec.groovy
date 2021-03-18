package com.zenjob.testrailsync

import com.zenjob.testrailsync.executors.TestRailResultPusher
import com.zenjob.testrailsync.obj.parsing.Feature
import spock.lang.Specification

class TestRailResultPusherSpec extends Specification {

    def "Parse results to feature objects"() {
        when:
        def result = new TestRailResultPusher().jsonResultsToFeatures("build/reports", new File("src/test/resources/push/$file"))

        then:
        result instanceof List<Feature>
        !result.isEmpty()

        where:
        file                        | _
        "failedFeature.json"        | _
        "fullRep.json"              | _
        "pushTest.json"             | _
        "singleScenarioFailed.json" | _
        "singleScenarioPassed.json" | _

    }

}
