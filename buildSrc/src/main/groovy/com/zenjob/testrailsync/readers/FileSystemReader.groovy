package com.zenjob.testrailsync.readers

import com.zenjob.testrailsync.schemas.FileSystemSchema

class FileSystemReader implements ExternalPlatformReader {
    FileSystemSchema fss = new FileSystemSchema()

    FileSystemSchema read(Map<String, List<Long>> filters) {

        fss.baseDir.eachDir { File projectDir ->
            FileSystemSchema.Folder projectFolder = new FileSystemSchema.Folder(projectDir.name)
            fss.folders.add(projectFolder)

            projectDir.eachDir { File folderDir ->
                FileSystemSchema.Folder folder = new FileSystemSchema.Folder(folderDir.name)
                folder.setFile(folderDir.canonicalFile)

                projectFolder.children.add(folder)

                folder.resolveChildren()
                folder.resolveFeatureFiles()
            }
        }
        fss
    }
}
