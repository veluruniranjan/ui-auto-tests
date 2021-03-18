# UI automation tests

## Configuration

To configure the development environment, use `environment.properties` file, or environment variables.

### Example

```
showExecution=true
```
`showExecution` parameter specifies if test run execution window will be "visually" opened or not

## Run


Tests are running inside docker containers, so Docker should be running locally   
To run tests of specific project
```
./gradlew runCVSTests
```
Next options are available currently:

| Project          | Task        | 
| -----------------| :----------:|
| core             | runZVSTests |
| Company App      | runCVSTests |
| Contract manager | runCMGTests |

### Parameters

As we have applications running on several environments (QA and STAGING) tests could be run on different environments too.
Use `env` parameter for that. Possible values are: `qa` (default one) and `staging` to run on qa or staging environment accordingly.  
Example:
```
./gradlew runCVSTests -Denv=qa
```

There is also `cucumber.options` parameter which allows to run specific test cases by testRail id tag (@tidXXX) or any other tag.  
Example:

```
./gradlew runCVSTests -Dcucumber.options='--tags @tid1234'
```
In order to run specific type of tests (smoke, regression, etc.), there are specific tags. 
```
./gradlew runCVSTests -DtestType=SMOKE
```
Possible options (case sensitive): `SMOKE`, `REGRESSION` and `FUNCTIONAL`

## TestRail synchronization

TestRail synchronization is made using plugin located in `buildSrc` folder. 
More details about plugin itself and it's usage are [here](buildSrc/README.md)

### Configuration

#### TestRail credentials

To be able to sync with TestRail, TestRail url and TestRail user credentials should be set in `environment.properties` file, or passed as environment variables.  

Example (environment.properties file):
```
TESTRAIL_URL=https://zenjob.testrail.io
TESTRAIL_EMAIL=[your.email]@zenjob.com
TESTRAIL_PASSWORD=[your.password]
```
#### build.gradle

Next section should be added somewhere to the `build.gradle`:
```
testRailSync {
    syncConfigFilePath = file("src/main/resources/testRailSyncData.json")
    reportsPath = "build/reports"
}
```
`reportsPath` - path of the folder, where cucumber results json files are stored   
`syncConfigFilePath` parameter is .json file with TestRail project configuration of next format:

```json
[
  {
    "project": 6,
    "suites": [8],
    "folder": "src/main/groovy/com/zenjob/apps/uitests/zvs/features"
  },
  {
    "project": 3,
    "suites": [11],
    "folder": "src/main/groovy/com/zenjob/apps/uitests/company/features"
  }
]
```

Every object of configuration is the setting for specific project.  
`project` - is the id of TestRail project  
`suites` - the list of suites ids  
`folder` - relative path to the folder where feature files will be located


### Usage

#### Synchronise test-cases with TestRail

__To pull test-cases from TestRail:__
```
./gradlew getTestCases
```
This script will pull all test-cases from TestRail according to the `testRailSyncData.json` configuration file


__To push test-cases back to TestRail:__

In case if some test-cases (*a.k.a* features) were changed in the code, they could be pushed back to TestRail.

To push all testCases:
```
./gradlew updateTestCases
```
If only some of test cases were changed, they could be listed using test-case ids (`@tidXXX` tag, where `XXX` is the id itself) in `cases` parameter:
```
./gradlew updateTestCases --cases="111, 222, 3333"
```
The same goes for "sections". If "background" description was changed, it could be updated in TestRail separately using section id (`@sidXXX` tag, where `XXX` is the id itself):
```
./gradlew updateTestCases --sections="44, 555"
```
Also `cases` and `sections` parameters could be used together in case if only some of the changes need to be pushed to TestRail:
```
./gradlew updateTestCases --cases="111" --sections="44"
```

#### Push results to TestRail

After test run task is finished, there is `results.json` file with run results automatically created in `build/reports` folder. In order to push results to the TestRail, there is a `pushResultsToTestRail` gradle task.
This task requires `runId` parameter to be set in order to push results to the specific TestRail run. 

Example:
```
./gradlew postTestResults --runId=38
```