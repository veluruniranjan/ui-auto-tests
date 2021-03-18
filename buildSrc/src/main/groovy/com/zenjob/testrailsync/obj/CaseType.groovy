package com.zenjob.testrailsync.obj

enum CaseType {
    REGRESSION(9),
    SMOKE(11),
    FUNCTIONAL(7),
    E2E(6)

    int id

    CaseType(int id) {
        this.id = id
    }

    public static CaseType find(int id) {
        CaseType type
        values().find { c ->
            if(c.id == id) {
                type = c
                return true
            }
        }
        return type
    }
}
