package com.zenjob.testrailsync.scripts

import com.zenjob.testrailsync.executors.TestRailSynchronizer

TestRailSynchronizer synchronizer = new TestRailSynchronizer()
synchronizer.configure()
            .synchronize()
