package com.ata.assignment.jobdata.util.filter;

import com.ata.assignment.jobdata.model.FetchOperator;
import com.ata.assignment.jobdata.model.JobDataModel;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class OperationPredicateFactory {
    private static final Logger log = LogManager.getLogger(OperationPredicateFactory.class);

    public static Predicate<JobDataModel> get(List<FetchOperator> filterCols) {

        return filterCols.stream()
                .map(OperationPredicateFactory::get)
                .reduce(x -> true, Predicate::and);

    }

    public static Predicate<JobDataModel> get(FetchOperator filterCol) {

        return (j) -> {

            var col = filterCol.getColumn().toUpperCase();
            var op = filterCol.getOperation().toUpperCase();
            var value = filterCol.getValue();

            return switch (col) {
                case "JOB_TITLE" -> doStringOperation(j.getJobTitle(), value, op);
                case "SALARY" -> doLongNumberOperation(j.getSalary(), value, op);
                case "GENDER" -> doGenderOperation(j.getGender().name(), value, op);
                case "EMPLOYER" -> doStringOperation(j.getEmployer(), value, op);
                case "LOCATION" -> doStringOperation(j.getLocation(), value, op);
                case "YEARS_AT_EMPLOYER" -> doDoubleOperation(j.getYearsAtEmployer(), value, op);
                case "YEARS_OF_EXPERIENCE" -> doDoubleOperation(j.getYearsOfExperience(), value, op);
                case "SIGNING_BONUS" -> doLongNumberOperation(j.getSigningBonus(), value, op);
                case "ANNUAL_BONUS" -> doLongNumberOperation(j.getAnnualBonus(), value, op);
                case "ANNUAL_STOCK_VALUE_BONUS" -> doLongNumberOperation(j.getAnnualStockValueBonus(), value, op);
                case "ADDITIONAL_COMMENTS" -> doStringOperation(j.getAdditionalComments(), value, op);

                default -> {
                    log.warn("Unsupported column[{}] for filter.", col);
                    yield true;
                }
            };

        };
    }

    private static boolean doStringOperation(String repoStrValue, String value, String op) {
        if (null == repoStrValue) {
            return false;
        }

        if (FilterOperationsEnum.EQ.name().equals(op)) {
            return repoStrValue.equals(value);
        } else if (FilterOperationsEnum.NEQ.name().equals(op)) {
            return !repoStrValue.equals(value);
        } else if (FilterOperationsStringEnum.SW.name().equals(op)) {
            return repoStrValue.startsWith(value);
        } else if (FilterOperationsStringEnum.EW.name().equals(op)) {
            return repoStrValue.endsWith(value);
        } else if (FilterOperationsStringEnum.CT.name().equals(op)) {
            return repoStrValue.contains(value);
        } else {
            log.warn("The String operation[{}] for filter is not supported.", op);
            return true;
        }
    }

    private static boolean doLongNumberOperation(Long repoNumberValue, String value, String op) {
        if (null == repoNumberValue) {
            return false;
        }

        long longValue;
        try {
            longValue = Long.parseLong(value);
        } catch (Exception e) {
            log.error("Value to filtered is not a parsable number.", e);
            return true;
        }

        if (FilterOperationsEnum.EQ.name().equals(op)) {
            return repoNumberValue == longValue;
        } else if (FilterOperationsEnum.NEQ.name().equals(op)) {
            return repoNumberValue != longValue;
        } else if (FilterOperationsNumberEnum.LT.name().equals(op)) {
            return repoNumberValue < longValue;
        } else if (FilterOperationsNumberEnum.LTE.name().equals(op)) {
            return repoNumberValue <= longValue;
        } else if (FilterOperationsNumberEnum.GT.name().equals(op)) {
            return repoNumberValue > longValue;
        } else if (FilterOperationsNumberEnum.GTE.name().equals(op)) {
            return repoNumberValue >= longValue;
        } else {
            log.warn("The Number operation[{}] for filter is not supported.", op);
            return true;
        }

    }


    private static boolean doDoubleOperation(Double repoNumberValue, String value, String op) {
        if (null == repoNumberValue) {
            return false;
        }

        double numberValue;
        try {
            numberValue = Double.parseDouble(value);
        } catch (Exception e) {
            log.error("Value to filtered is not a parsable number.", e);
            return true;
        }

        if (FilterOperationsEnum.EQ.name().equals(op)) {
            return repoNumberValue == numberValue;
        } else if (FilterOperationsEnum.NEQ.name().equals(op)) {
            return repoNumberValue != numberValue;
        } else if (FilterOperationsNumberEnum.LT.name().equals(op)) {
            return repoNumberValue < numberValue;
        } else if (FilterOperationsNumberEnum.LTE.name().equals(op)) {
            return repoNumberValue <= numberValue;
        } else if (FilterOperationsNumberEnum.GT.name().equals(op)) {
            return repoNumberValue > numberValue;
        } else if (FilterOperationsNumberEnum.GTE.name().equals(op)) {
            return repoNumberValue >= numberValue;
        } else {
            log.warn("The Number operation[{}] for filter is not supported.", op);
            return true;
        }

    }

    private static boolean doGenderOperation(String repoGenderValue, String value, String op) {
        if (null == repoGenderValue) {
            return false;
        }

        if (FilterOperationsEnum.EQ.name().equals(op)) {
            return repoGenderValue.equals(value);
        } else if (FilterOperationsEnum.NEQ.name().equals(op)) {
            return !repoGenderValue.equals(value);
        } else {
            log.warn("The Gender operation[{}] for filter is not supported.", op);
            return true;
        }
    }
}
