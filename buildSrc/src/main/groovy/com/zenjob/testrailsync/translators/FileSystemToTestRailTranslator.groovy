package com.zenjob.testrailsync.translators

import com.zenjob.testrailsync.obj.CaseType
import com.zenjob.testrailsync.obj.TestCase
import com.zenjob.testrailsync.obj.TestCaseSection
import groovy.io.FileType


import java.util.regex.Matcher
import java.util.regex.Pattern

class FileSystemToTestRailTranslator {

    static List<File> dirs

    FileSystemToTestRailTranslator(List<File> dirs) {
        this.dirs = dirs
    }
    static Pattern testCasePattern = Pattern.compile("(?m)\\s*(@(\\w{1,}))?[\\n\\r ]*@tid(\\d{1,})[\\n\\r ]*Scenario: ([^\\n\\r]{1,})(((?!@tid)[^\\s@]*\\s*)*)")
    static Pattern sectionPattern = Pattern.compile("(?m)@sid(\\d{1,})[\\n\\r]*Feature: ([^\\n\\r]{1,})[\\n\\r\\s]*Background:\\s*(((?!@tid).*\\s*)*)")

    static List<TestCase> getTestCasesToSync(List<Long> casesToPush) {
        List<TestCase> cases = new ArrayList<>()
        //read all files from Features directory
        dirs.each { dir ->
            dir.eachFileRecurse(FileType.FILES) { file ->
                Matcher testCaseMatcher = testCasePattern.matcher(file.text)
                while (testCaseMatcher.find()) {
                    //parse every match to TestCase object, which will be used for TestRail synchronisation
                    if (casesToPush) {
                        if (casesToPush.contains(Long.valueOf(testCaseMatcher.group(3)))) {
                            TestCase testCase = new TestCase()
                                    .setId(Long.valueOf(testCaseMatcher.group(3)))
                                    .setTitle(testCaseMatcher.group(4))
                                    .setText(cleanUpText(testCaseMatcher.group(5)))
                            if (testCaseMatcher.group(2)) {
                                testCase.setType(testCaseMatcher.group(2) as CaseType)
                            }
                            cases.add(testCase)
                        }
                    } else {
                        //if no specific cases are set, all get synchronised
                        TestCase testCase = new TestCase()
                                .setId(Long.valueOf(testCaseMatcher.group(3)))
                                .setTitle(testCaseMatcher.group(4))
                                .setText(cleanUpText(testCaseMatcher.group(5)))
                        if (!testCaseMatcher.group(2).isEmpty()) {
                            testCase.setType(testCaseMatcher.group(2) as CaseType)
                        }
                        cases.add(testCase)
                    }
                }

            }

        }
        assert !cases.isEmpty(): "No cases found for synchronisation!"
        return cases
    }

    static List<TestCaseSection> getSectionsToSync(List<Long> sectionsToPush) {
        List<TestCaseSection> sections = new ArrayList<>()
        dirs.each { dir ->
            dir.eachFileRecurse(FileType.FILES) { file ->
                Matcher sectionMatcher = sectionPattern.matcher(file.text)
                while (sectionMatcher.find()) {
                    //parse every match to TestCaseSection object, which will be used for TestRail synchronisation
                    if (sectionsToPush) {
                        if (sectionsToPush.contains(Long.valueOf(sectionMatcher.group(1)))) {
                            TestCaseSection section = new TestCaseSection()
                                    .setId(Long.valueOf(sectionMatcher.group(1)))
                                    .setName(sectionMatcher.group(2))
                                    .setDescription(cleanUpText(sectionMatcher.group(3)))
                            sections.add(section)
                        }
                    } else {
                        //if no specific sections are set, all get synchronised
                        TestCaseSection section = new TestCaseSection()
                                .setId(Long.valueOf(sectionMatcher.group(1)))
                                .setName(sectionMatcher.group(2))
                                .setDescription(cleanUpText(sectionMatcher.group(3)))
                        sections.add(section)
                    }
                }
            }

        }
        assert !sections.isEmpty() || !sectionsToPush: "No section found for synchronisation!"
        return sections
    }

    private static String cleanUpText(String text) {
        //replace all the spaces in the beginning and \n in the end
        return text.replaceAll(" (?![^\\s].*)[ \n]", "")
                .replaceAll("\\n*\$", "")
                .replaceAll("\r", "")
                .replaceAll("^\\n", "")
    }

}
