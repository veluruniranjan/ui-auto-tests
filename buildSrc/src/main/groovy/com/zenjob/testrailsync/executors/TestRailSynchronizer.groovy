package com.zenjob.testrailsync.executors


import com.zenjob.testrailsync.obj.Project
import com.zenjob.testrailsync.obj.TestCase
import com.zenjob.testrailsync.obj.TestCaseSection
import com.zenjob.testrailsync.readers.TestRailReader
import com.zenjob.testrailsync.schemas.FileSystemSchema
import com.zenjob.testrailsync.schemas.TestRailSchema
import com.zenjob.testrailsync.TestRailApiWrapper
import com.zenjob.testrailsync.translators.FileSystemToTestRailTranslator
import com.zenjob.testrailsync.translators.TestRailToFileSystemTranslator
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper


class TestRailSynchronizer {
    List<Project> filters = []
    static List<File> featureFolders = []

    TestRailSynchronizer configure(File syncSettingFile) {
        def json = new JsonSlurper().parse(syncSettingFile)

        json.each { item -> this.filters.add(new Project(item))
        }
        filters.each { setting ->
            featureFolders.add(new File(setting.folder))
        }
        return this
    }


    void synchronize() {
        def testRailReader = new TestRailReader()

        TestRailToFileSystemTranslator tr2fsTranslator = new TestRailToFileSystemTranslator()
        TestRailSchema trs = testRailReader.read(filters)
        tr2fsTranslator = new TestRailToFileSystemTranslator(trs)
        List<FileSystemSchema> fss = tr2fsTranslator.translate()
        FileSystemSchemaExecutor fssExecutor = new FileSystemSchemaExecutor(fss)
        fssExecutor.execute()
    }

    static void pushback(List<Long> casesToPush, List<Long> sectionsToPush) {
        assert featureFolders: "Feature source folder wasn't set"

        List<TestCase> cases
        List<TestCaseSection> sections

        FileSystemToTestRailTranslator translator = new FileSystemToTestRailTranslator(featureFolders)

        if (sectionsToPush) {
            sections = translator.getSectionsToSync(sectionsToPush)
        }
        if (casesToPush) {
            cases = translator.getTestCasesToSync(casesToPush)
        }
        if (!sectionsToPush && !casesToPush) {
            cases = translator.getTestCasesToSync()
            sections = translator.getSectionsToSync()
        }

        TestRailApiWrapper api = new TestRailApiWrapper()
        cases.each {
            def data = [
                    title         : it.title,
                    custom_gherkin: it.text
            ]
            if (it.type) {
                data.put("type_id", it.type.id)
            }
            Object json = new JsonBuilder(data)
            println("Test case #${it.id} sync with data: ${json}")
            api.sendTestCases(it.id, json)
        }
        sections.each {
            def data = [
                    name       : it.name,
                    description: it.description
            ]
            Object json = new JsonBuilder(data)
            println("Test cases section #${it.id} sync with data: ${json}")
            api.sendTestCasesSections(it.id, json)
        }
    }
}
