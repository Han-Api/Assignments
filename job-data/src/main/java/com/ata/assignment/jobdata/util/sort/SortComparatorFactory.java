package com.ata.assignment.jobdata.util.sort;

import com.ata.assignment.jobdata.model.JobDataModel;
import com.ata.assignment.jobdata.model.SortOperator;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;

@UtilityClass
public class SortComparatorFactory {
    private static final Logger log = LogManager.getLogger(SortComparatorFactory.class);

    public static Comparator<JobDataModel> get(List<SortOperator> sortCols) {

        return sortCols.stream()
                .map(SortComparatorFactory::get)
                .reduce((o1, o2) -> 0, Comparator::thenComparing);

    }

    public static Comparator<JobDataModel> get(SortOperator sortCol) {

        var col = sortCol.getColumn().toUpperCase();
        var dir = sortCol.getDirection().toUpperCase();

        Comparator<JobDataModel> sortComparator = switch (col) {

            case "TIMESTAMP" -> Comparator.comparing(JobDataModel::getTimestamp,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "EMPLOYER" -> Comparator.comparing(JobDataModel::getEmployer,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "LOCATION" -> Comparator.comparing(JobDataModel::getLocation,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "YEARS_AT_EMPLOYER" -> Comparator.comparing(JobDataModel::getYearsAtEmployer,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "YEARS_OF_EXPERIENCE" -> Comparator.comparing(JobDataModel::getYearsOfExperience,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "SALARY" -> Comparator.comparing(JobDataModel::getSalary,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "SIGNING_BONUS" -> Comparator.comparing(JobDataModel::getSigningBonus,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "ANNUAL_BONUS" -> Comparator.comparing(JobDataModel::getAnnualBonus,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "ANNUAL_STOCK_VALUE_BONUS" -> Comparator.comparing(JobDataModel::getAnnualStockValueBonus,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "GENDER" -> Comparator.comparing(JobDataModel::getGender,
                    Comparator.nullsFirst(Comparator.naturalOrder()));
            case "ADDITIONAL_COMMENTS" -> Comparator.comparing(JobDataModel::getAdditionalComments,
                    Comparator.nullsFirst(Comparator.naturalOrder()));

            default -> {
                log.warn("Unsupported column[{}] for sort.", sortCol);
                yield (o1, o2) -> 0;
            }
        };

        if (SortDirectionEnum.DESC.name().equals(dir)) {
            return sortComparator.reversed();
        } else {
            return sortComparator;
        }
    }

}
