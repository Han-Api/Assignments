package com.ata.assignment.jobdata.model;

import com.ata.assignment.jobdata.util.sort.SortDirectionEnum;
import lombok.Data;

@Data
public class SortOperator {

    private String column;
    private String direction;

    public SortOperator(String column, String direction) {
        this.column = column;
        this.direction = direction;
    }

    public SortOperator(String... detail) {
        this.column = detail[0].toUpperCase();
        this.direction = detail.length == 2 ? detail[1].toUpperCase() : SortDirectionEnum.ASC.name();
    }
}
