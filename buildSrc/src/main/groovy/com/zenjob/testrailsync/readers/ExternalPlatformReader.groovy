package com.zenjob.testrailsync.readers

import com.zenjob.testrailsync.schemas.ExternalPlatformSchema

interface ExternalPlatformReader {
    ExternalPlatformSchema read(Map<String, List<Long>> filters)
}