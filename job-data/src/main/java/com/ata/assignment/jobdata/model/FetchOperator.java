package com.ata.assignment.jobdata.model;

import lombok.Data;

@Data
public class FetchOperator {

    private String column;
    private String operation;
    private String value;

    public FetchOperator(String column, String operation, String value) {
        this.column = column;
        this.operation = operation;
        this.value = value;
    }
}
