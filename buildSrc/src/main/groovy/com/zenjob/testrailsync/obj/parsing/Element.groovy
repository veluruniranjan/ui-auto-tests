package com.zenjob.testrailsync.obj.parsing

class Element {
    String id
    String name
    String type
    String description
    String keyword
    List<Step> steps
    List<Tag> tags

    private static final String SCENARIO_TYPE = "scenario"

    boolean isScenario() {
        return type.equalsIgnoreCase(SCENARIO_TYPE)
    }

}
