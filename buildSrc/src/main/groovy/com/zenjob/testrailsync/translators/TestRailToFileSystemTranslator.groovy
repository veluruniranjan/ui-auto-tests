package com.zenjob.testrailsync.translators

import com.zenjob.testrailsync.schemas.FileSystemSchema
import com.zenjob.testrailsync.schemas.TestRailSchema


class TestRailToFileSystemTranslator {
    TestRailSchema trs

    TestRailToFileSystemTranslator(TestRailSchema trs) {
        this.trs = trs
    }

    List<FileSystemSchema> translate() {
        List<FileSystemSchema> fssResult = []
        trs.projects.each { TestRailSchema.Project trsProject ->
            FileSystemSchema fss = new FileSystemSchema()
            fss.baseDir = new File(trsProject.folder)
            trsProject.suites.each { TestRailSchema.Suite trsSuite ->
                FileSystemSchema.Folder suiteFolder = new FileSystemSchema.Folder(trsSuite.name)

                // add suite to project
                fss.folders.add(suiteFolder)

                copyHierarchy(trsSuite.sections, suiteFolder.children, suiteFolder)
            }
            fssResult.add(fss)
        }
        return fssResult
    }

    private static void copyHierarchy(def currentSections, def currentFolders, FileSystemSchema.Folder baseFolder) {
        currentSections.each { def currentSection ->
            FileSystemSchema.Folder currentFolder = new FileSystemSchema.Folder(currentSection.name)
            currentFolder.setParent(baseFolder)
            currentFolders.add(currentFolder)
            copyHierarchy(currentSection.sections, currentFolder.children, currentFolder)

            // add feature files
            if (currentSection.testCases.size() > 0) {
                FileSystemSchema.FeatureFile featureFile = new FileSystemSchema.FeatureFile(currentSection.name)
                featureFile.id = currentSection.id

                // if there are test cases for section
                // and there is a description (not every feature has to have a background
                // then the description of that section is the gherkin background
                if (currentSection.testCases && currentSection.description != null) {
                    featureFile.background = new FileSystemSchema.Scenario(null, null, currentSection.description, null)
                }

                currentSection.testCases.each {
                    FileSystemSchema.Scenario scenario = new FileSystemSchema.Scenario(it.id, it.name, it.content, it.type)
                    featureFile.scenarios.add(scenario)
                }

                currentFolder.featureFiles.add(featureFile)
            }
        }
    }

}
