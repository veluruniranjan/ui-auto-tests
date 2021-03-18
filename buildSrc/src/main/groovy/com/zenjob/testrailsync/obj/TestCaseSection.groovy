package com.zenjob.testrailsync.obj

class TestCaseSection {
    Long id
    String name
    String description

    TestCaseSection() {
    }

    TestCaseSection setId(Long id) {
        this.id = id
        return this
    }

    TestCaseSection setName(String name) {
        this.name = name
        return this
    }

    TestCaseSection setDescription(String description) {
        this.description = description
        return this
    }

    Long getId() {
        return id
    }

    String getTitle() {
        return name
    }

    String getText() {
        return description
    }
}
