package com.zenjob.testrailsync.executors

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.ObjectMapper
import com.zenjob.testrailsync.obj.TestCaseResult
import com.zenjob.testrailsync.TestRailApiWrapper
import com.zenjob.testrailsync.obj.parsing.Feature
import com.zenjob.testrailsync.translators.FeaturesToTestCaseResultsTranslator
import net.masterthought.cucumber.Configuration


class TestRailResultPusher {

    static List<TestCaseResult> parseResultsFromJson(String reportsPath, File jsonFile) {
        List<Feature> features = jsonResultsToFeatures(reportsPath, jsonFile)

        return new FeaturesToTestCaseResultsTranslator().parseFeaturesToTestRailResults(features)
    }


    static void pushResults(List<TestCaseResult> results, long runId) {
        // logging
        results.each { TestCaseResult tc ->
            String statusOutputTemplate = tc.status ? 'passed' : 'failed'
            println "The test case with id: ${tc.id} ${statusOutputTemplate}"
        }


        TestRailApiWrapper api = new TestRailApiWrapper()

        // add results to test run
        // We would not create new run every time when we're running tests, so we'll use a parameter to provide already created runId
        api.addResultsForCases(runId, results)
    }

     private static List<Feature> jsonResultsToFeatures(String reportsPath, File json) {
        Configuration configuration = new Configuration(new File(reportsPath), "projectName")
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        InjectableValues values = new InjectableValues.Std().addValue(Configuration.class, configuration)

        mapper.setInjectableValues(values)

        return mapper.readValue(json, Feature[].class)
    }
}
