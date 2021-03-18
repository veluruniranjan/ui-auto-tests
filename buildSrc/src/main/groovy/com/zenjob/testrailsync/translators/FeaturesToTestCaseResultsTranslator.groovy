package com.zenjob.testrailsync.translators

import com.zenjob.testrailsync.obj.TestCaseResult
import com.zenjob.testrailsync.obj.parsing.Element
import com.zenjob.testrailsync.obj.parsing.Feature
import com.zenjob.testrailsync.obj.parsing.Tag

class FeaturesToTestCaseResultsTranslator {

    private final static String TEST_TAG_PREFIX = "@tid"

    List<TestCaseResult> parseFeaturesToTestRailResults(List<Feature> features) {
        List<TestCaseResult> testCaseResults = []

        features.each { Feature feature ->
            feature.elements.each { Element el ->
                int caseId = extractTestCaseIdFromTags(el.tags)
                if (el.isScenario() && caseId != 0) {
                    testCaseResults.add(new TestCaseResult(
                            id: caseId,
                            status: isScenarioPassed(el)
                    ))
                }
            }
        }

        return testCaseResults
    }

    private boolean isScenarioPassed(Element element) {
        boolean scenarioPassed = true
        element.steps.each {
            scenarioPassed &= it.result.status.isPassed()
        }
        return scenarioPassed
    }

    private int extractTestCaseIdFromTags(List<Tag> tags) {
        int id
        tags.find() {
            if(it.name.matches(/$TEST_TAG_PREFIX.*/)){
                id = it.name.substring(TEST_TAG_PREFIX.length()).toInteger()
            }
        }
        return id
    }

}
