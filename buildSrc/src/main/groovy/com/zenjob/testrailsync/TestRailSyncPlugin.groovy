package com.zenjob.testrailsync

import com.zenjob.testrailsync.tasks.SetTestResultsTask
import com.zenjob.testrailsync.tasks.TestRailSyncPluginExtension
import com.zenjob.testrailsync.tasks.TestRailSyncTask
import com.zenjob.testrailsync.tasks.UpdateTestRailCasesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestRailSyncPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.ext.SetTestResultsTask = SetTestResultsTask
        project.ext.TestRailSyncTask = TestRailSyncTask
        project.ext.UpdateTestRailCasesTask = UpdateTestRailCasesTask

        project.extensions.create('testRailSync', TestRailSyncPluginExtension)

        project.task('postTestResults', type: SetTestResultsTask) {
            description = "Post test results to the TestRail test run specified by id"
            group = "TestRail Sync"
        }

        project.task('getTestCases', type: TestRailSyncTask) {
            description = "Pulls test cases from TestRail and create feature files in the project"
            group = "TestRail Sync"
        }

        project.task('updateTestCases', type: UpdateTestRailCasesTask) {
            description = "Update specific test cases from code to TestRail"
            group = "TestRail Sync"
        }
    }
}
