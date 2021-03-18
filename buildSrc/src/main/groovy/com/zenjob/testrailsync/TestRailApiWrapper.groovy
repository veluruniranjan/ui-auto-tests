package com.zenjob.testrailsync

import api.APIClient
import com.zenjob.testrailsync.config.Configuration
import groovy.json.JsonBuilder


class TestRailApiWrapper {
    APIClient client

    Object getProject(long projectId) {
        client.sendGet("get_project/${projectId}")
    }

    List<Object> getSuitesForProject(long projectId) {
        client.sendGet("get_suites/${projectId}")
    }

    List<Object> getSectionsForProjectAndSuite(long projectId, long suiteId) {
        client.sendGet("get_sections/${projectId}&suite_id=${suiteId}")
    }

    List<Object> getTestCasesForSection(long projectId, long suiteId, long sectionId) {
        client.sendGet("get_cases/${projectId}&suite_id=${suiteId}&section_id=${sectionId}")
    }

    void sendTestCases(long testCaseId, Object data) {
        client.sendPost("update_case/${testCaseId}", data)
    }

    void sendTestCasesSections (long testCasesSectionId, Object data) {
        client.sendPost("update_section/${testCasesSectionId}", data)
    }

    void addResultsForCases(long runId, List testCaseResults) {

        def data = [
            results:
                testCaseResults.collect({[
                    case_id: it.id,
                    status_id: it.status ? 1 : 5
                ]})
        ]

        Object json = new JsonBuilder(data)

        client.sendPost("add_results_for_cases/${runId}", json)
    }

    long addRunForProjectAndSuite(long projectId, long suiteId, String runName) {
        def data = [
            suite_id: suiteId,
            name: runName,
            include_all: true
        ]

        Object json = new JsonBuilder(data)
        Object response = client.sendPost("add_run/${projectId}", json)

        long runId = response.id

        runId
    }

    TestRailApiWrapper() {
        client = new APIClient(Configuration.getConfigProperty('TESTRAIL_URL'))
        client.setUser(Configuration.getConfigProperty('TESTRAIL_EMAIL'))
        client.setPassword(Configuration.getConfigProperty('TESTRAIL_PASSWORD'))
    }
}
