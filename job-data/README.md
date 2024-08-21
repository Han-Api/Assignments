# Query from static list with only one GET API - JobData

## Time Spent : 3-4 after working days

## Requirement :: Backend Exercise : Expose an API for querying job data

The goal of this exercise is to design a read-only API (REST) that returns one or more records from static set of job data.



User Story: As a developer I want to

list job data via API GET request

    Filter by one or more fields/attributes (e.g. /job_data?salary[gte]=120000) (Show only filtered row. Expected filter able column: job title, salary, gender )

    Filter by a sparse fields/attributes (e.g. /job_data?fields=job_title,gender,salary) (Show only filtered column)

    Sort by one or more fields/attributes (e.g. /job_data?sort=job_title&sort_type=DESC)

A few quick notes on submitting the Backend Exercise

    Don't worry about any web application concerns other than serializing JSON and returning via a GET request.
    Feel free to design the URL structure and JSON schema that you believe creates the best client/consumer experience. We want to see how you initiate a new project, project structure and design. 


## Guides

Sample : GET URL

    jobData?filters=job_title:sw:S,ADDITIONAL_COMMENTS:ew:.,salary:eq:12000,location:sw:Clem&sorts=salary,years_Of_Experience:DESC&columns=job_title,salary,timestamp,salary


## :::: Field List ::::

"TIMESTAMP"
, "EMPLOYER"
, "LOCATION"
, "JOB_TITLE"
, "YEARS_AT_EMPLOYER"
, "YEARS_OF_EXPERIENCE"
, "SALARY"
, "SIGNING_BONUS"
, "ANNUAL_BONUS"
, "ANNUAL_STOCK_VALUE_BONUS"
, "GENDER"
, "ADDITIONAL_COMMENTS"

### Filtering [filters=]
1. Support all columns except "TIMESTAMP".
2. String fields, support     EQ, NEQ, SW, EW, CT
3. Number/Double fields, support     EQ, NEQ, LT, LTE, GT, GTE
4. Gender, support EQ, NEQ

### Sorts [sorts=]
1. Support All fields.
2. default ASC, support ASC and DESC

### Column select [columns=]
1. Support All fields.
2. Field may repeat.

