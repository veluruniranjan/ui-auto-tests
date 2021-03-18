package com.zenjob.testrailsync.scripts

import com.zenjob.testrailsync.executors.TestRailSynchronizer


def casesParameter = System.getProperty("cases").replaceAll(" ","")
def sectionsParameter = System.getProperty("sections").replaceAll(" ","")

def cases = casesParameter.isEmpty()? null: casesParameter.split(',').collect { Long.valueOf(it) }
def sections = sectionsParameter.isEmpty()? null: sectionsParameter.split(',').collect { Long.valueOf(it) }

TestRailSynchronizer.pushback(cases, sections)
