package com.zenjob.testrailsync.obj.parsing

import net.masterthought.cucumber.json.support.Status

class Step {
    String name
    Result result
}

class Result {
    Long duration
    Status status
}

