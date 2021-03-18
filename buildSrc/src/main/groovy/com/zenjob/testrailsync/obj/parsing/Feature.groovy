package com.zenjob.testrailsync.obj.parsing

import groovy.transform.ToString

@ToString
class Feature {
    String id
    String name
    String description
    String keyword
    List<Element> elements
    List<Tag> tags
}
