package com.zenjob.testrailsync.obj

class TestCase {
    Long id
    CaseType type
    String title
    String text

    TestCase() {
    }

    TestCase setId(Long id) {
        this.id = id
        return this
    }

    TestCase setTitle(String title) {
        this.title = title
        return this
    }

    TestCase setText(String text) {
        this.text = text
        return this
    }

    TestCase setType(CaseType type) {
        this.type = type
        return this
    }

    Long getId() {
        return id
    }

    String getTitle() {
        return title
    }

    String getText() {
        return text
    }

    CaseType getType() {
        return type
    }


}
