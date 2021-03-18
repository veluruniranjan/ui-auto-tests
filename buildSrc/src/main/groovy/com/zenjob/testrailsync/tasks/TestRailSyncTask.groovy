package com.zenjob.testrailsync.tasks

import com.zenjob.testrailsync.executors.TestRailSynchronizer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class TestRailSyncTask extends DefaultTask {

    @TaskAction
    def sync() {
        TestRailSyncPluginExtension config = project.extensions.testRailSync
        assert config.syncConfigFilePath: "TestRail sync file wasn't defined"

        File settingFile = new File(config.syncConfigFilePath)

        if (settingFile.exists() && settingFile.isFile()) {
            new TestRailSynchronizer().configure(settingFile).synchronize()
        } else {
            throw new Exception("Reports file ${settingFile.absolutePath} not found")
        }
    }
}
