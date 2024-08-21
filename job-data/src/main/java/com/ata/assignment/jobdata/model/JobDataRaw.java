package com.ata.assignment.jobdata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobDataRaw {

    @JsonProperty("Timestamp")
    String timestamp;
    @JsonProperty("Employer")
    String employer;
    @JsonProperty("Location")
    String location;
    @JsonProperty("Job Title")
    String jobTitle;
    @JsonProperty("Years at Employer")
    String yearsAtEmployer;
    @JsonProperty("Years of Experience")
    String yearsOfExperience;
    @JsonProperty("Salary")
    String salary;
    @JsonProperty("Signing Bonus")
    String signingBonus;
    @JsonProperty("Annual Bonus")
    String annualBonus;
    @JsonProperty("Annual Stock Value/Bonus")
    String annualStockValueBonus;
    @JsonProperty("Gender")
    String gender;
    @JsonProperty("Additional Comments")
    String additionalComments;

}
