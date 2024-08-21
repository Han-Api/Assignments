package com.ata.assignment.jobdata.util;

import com.ata.assignment.jobdata.model.SortOperator;
import com.ata.assignment.jobdata.util.sort.SortDirectionEnum;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class SortOperatorUtil {

    private static final Logger log = LogManager.getLogger(SortOperatorUtil.class);

    private static final String COL_SEPARATOR = ",";
    private static final String DETAIL_SEPARATOR = ":";

    public static final List<String> ALLOWED_SORT_DIRECTION_STRING = Arrays.stream(SortDirectionEnum.values()).map(Enum::name).toList();
    public static final List<String> ALLOWED_SORT_COLUMNS = List.of(
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
    );

    public static List<SortOperator> get(String sortsParam) {

        if ("NO_SORT".equals(sortsParam) || StringUtils.isBlank(sortsParam)) {
            return Collections.emptyList();
        }

        return Arrays.stream(sortsParam.split(COL_SEPARATOR))
                .map(c -> c.split(DETAIL_SEPARATOR))
                .filter(isValidDetail())
                .map(SortOperator::new)
                .toList();
    }

    private Predicate<String[]> isValidDetail() {

        return (d) -> {
            if (null == d || !(d.length == 1 || d.length == 2 ) || Arrays.stream(d).anyMatch(String::isEmpty)) {
                return false;
            }

            var colName = d[0].toUpperCase();
            var direction = d.length == 2 ? d[1].toUpperCase() : null;

            if (!ALLOWED_SORT_COLUMNS.contains(colName)) {
                log.error("Unknown sort column name : {}", colName);
                return false;
            } else if (!StringUtils.isBlank(direction) && !ALLOWED_SORT_DIRECTION_STRING.contains(direction)) {
                log.error("Unknown direction : {} ", direction);
                return false;
            }

            return true;
        };
    }
}
