package com.ata.assignment.jobdata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobDataModel {
    LocalDateTime timestamp;
    String employer;
    String location;
    String jobTitle;
    Double yearsAtEmployer;
    Double yearsOfExperience;
    Long salary;
    Long signingBonus;
    Long annualBonus;
    Long annualStockValueBonus;
    GenderEnum gender;
    String additionalComments;

}
