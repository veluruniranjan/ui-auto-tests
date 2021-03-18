package com.zenjob.testrailsync.schemas

import com.zenjob.testrailsync.obj.CaseType
import groovy.io.FileType

class FileSystemSchema implements ExternalPlatformSchema {
    static class FeatureFile {
        String id
        String name
        Scenario background = null // no background by default
        List<Scenario> scenarios = []

        FeatureFile(String name) {
            this.name = name
        }
    }

    static class Scenario {
        String name
        String id
        String content
        CaseType type

        Scenario(id, name, content, type) {
            this.id = id
            this.name = name
            this.content = content
            this.type = type
        }
    }

    static class Folder {
        String name

        File file
        Folder parent
        List<Folder> children = []

        List<FeatureFile> featureFiles = []

        Folder(String name) {
            this.name = name
        }

        void resolveChildren() {
            if (file == null) println 'File is null'
            File dir = file
            dir.eachDir {
                Folder childFolder = new Folder(it.name)
                this.children << childFolder
                childFolder.file = it
                childFolder.resolveChildren()
            }
        }

        void resolveFeatureFiles() {
            File dir = file
            dir.eachFile(FileType.FILES) {
                FeatureFile featureFile = new FeatureFile(it.name)
                featureFiles << featureFile
            }
            children.each {
                it.resolveFeatureFiles()
            }
        }
    }
    File baseDir
    List<Folder> folders = []
}
