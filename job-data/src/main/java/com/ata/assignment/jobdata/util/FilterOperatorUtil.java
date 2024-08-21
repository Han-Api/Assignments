package com.ata.assignment.jobdata.util;

import com.ata.assignment.jobdata.model.FetchOperator;
import com.ata.assignment.jobdata.model.GenderEnum;
import com.ata.assignment.jobdata.util.filter.FilterOperationsEnum;
import com.ata.assignment.jobdata.util.filter.FilterOperationsNumberEnum;
import com.ata.assignment.jobdata.util.filter.FilterOperationsStringEnum;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;

@UtilityClass
public class FilterOperatorUtil {

    private static final Logger log = LogManager.getLogger(FilterOperatorUtil.class);

    private static final String COL_SEPARATOR = ",";
    private static final String DETAIL_SEPARATOR = ":";

    public static final List<String> ALLOWED_OPERATION_STRING = Arrays.stream(FilterOperationsStringEnum.values()).map(Enum::name).toList();
    public static final List<String> ALLOWED_OPERATION_NUMBERS = Arrays.stream(FilterOperationsNumberEnum.values()).map(Enum::name).toList();
    public static final List<String> ALLOWED_OPERATION_GENDER = Arrays.stream(FilterOperationsEnum.values()).map(Enum::name).toList();
    private static final List<String> ALLOWED_VALUES_GENDER = Arrays.stream(GenderEnum.values()).map(Enum::name).toList();

    private static final Map<String, List<String>> colToOperMap = Map.ofEntries(
            Map.entry("JOB_TITLE", ALLOWED_OPERATION_STRING),
            Map.entry("SALARY", ALLOWED_OPERATION_NUMBERS),
            Map.entry("GENDER", ALLOWED_OPERATION_GENDER),
            Map.entry("EMPLOYER", ALLOWED_OPERATION_STRING),
            Map.entry("LOCATION", ALLOWED_OPERATION_STRING),
            Map.entry("YEARS_AT_EMPLOYER", ALLOWED_OPERATION_NUMBERS),
            Map.entry("YEARS_OF_EXPERIENCE", ALLOWED_OPERATION_NUMBERS),
            Map.entry("SIGNING_BONUS", ALLOWED_OPERATION_NUMBERS),
            Map.entry("ANNUAL_BONUS", ALLOWED_OPERATION_NUMBERS),
            Map.entry("ANNUAL_STOCK_VALUE_BONUS", ALLOWED_OPERATION_NUMBERS),
            Map.entry("ADDITIONAL_COMMENTS", ALLOWED_OPERATION_STRING)
    );

    public static List<FetchOperator> get(String filtersParam) {

        if ("NO_FILTER".equals(filtersParam) || StringUtils.isBlank(filtersParam)) {
            return Collections.emptyList();
        }

        return Arrays.stream(filtersParam.split(COL_SEPARATOR))
                .map(c -> c.split(DETAIL_SEPARATOR))
                .filter(isValidDetail())
                .map(details -> new FetchOperator(details[0], details[1], details[2]))
                .toList();

    }

    private Predicate<String[]> isValidDetail() {

        return (d) -> {
            if (null == d || d.length != 3 || Arrays.stream(d).anyMatch(String::isEmpty)) {
                return false;
            }

            var colName = d[0].toUpperCase();
            var operation = d[1].toUpperCase();
            var value = d[2];

            if (!colToOperMap.containsKey(colName)) {
                log.error("Unknown filter column name : {}", colName);
                return false;
            } else if (!colToOperMap.get(colName).contains(operation)) {
                log.error("Unknown operation : {} for this column name : {}", operation, colName);
                return false;
            } else if (ALLOWED_OPERATION_NUMBERS == colToOperMap.get(colName) && !NumberUtils.isParsable(value)) {
                log.error("Not Valid Number Value : {} ", value);
                return false;
            } else if ("GENDER".equals(colName) && !ALLOWED_VALUES_GENDER.contains(value)) {
                log.error("Unknown Gender Value : {} ", value);
                return false;
            }

            return true;
        };
    }
}
