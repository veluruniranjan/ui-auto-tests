package com.zenjob.testrailsync.tasks

import com.zenjob.testrailsync.executors.TestRailSynchronizer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class UpdateTestRailCasesTask extends DefaultTask {

    @Input
    @Optional
    List<Long> cases = null
    @Input
    @Optional
    List<Long> sections = null

    @Option(option = "cases", description = "Ids of TestRail cases, which need to be updated")
    void setCases(String cases) {
        this.cases = cases.isEmpty() ? null : cases.split(',').collect { Long.valueOf(it) }
    }

    @Option(option = "sections", description = "Ids of TestRail sections (features), which need to be updated")
    void setSections(String sections) {
        this.sections = sections.isEmpty() ? null : sections.split(',').collect { Long.valueOf(it) }
    }

    @TaskAction
    void updateTestRailCases() {
        TestRailSyncPluginExtension config = project.extensions.testRailSync
        assert config.syncConfigFilePath: "TestRail sync file wasn't defined"

        File settingFile = new File(config.syncConfigFilePath)
        if (settingFile.exists() && settingFile.isFile()) {
            new TestRailSynchronizer().configure(settingFile).pushback(cases, sections)
        } else {
            throw new Exception("Reports file ${settingFile.absolutePath} not found")
        }
    }

}
