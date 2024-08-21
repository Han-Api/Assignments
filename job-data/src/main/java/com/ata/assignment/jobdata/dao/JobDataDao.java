package com.ata.assignment.jobdata.dao;

import com.ata.assignment.jobdata.model.GenderEnum;
import com.ata.assignment.jobdata.model.JobDataModel;
import com.ata.assignment.jobdata.model.JobDataRaw;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class JobDataDao {
    private static final Logger log = LogManager.getLogger(JobDataDao.class);

    private final ObjectMapper objectMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm:ss");

    public JobDataDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Cacheable("readCSV")
    public List<JobDataModel> readCSV() {

        try {
            List<JobDataRaw> rawList = objectMapper.readValue(new File("src/main/resources/salary_datasets/salary_survey-3.json"), new TypeReference<List<JobDataRaw>>() {
            });

            return rawList.stream().map(r -> {
                        var model = new JobDataModel();

                        try {
                            model.setTimestamp(LocalDateTime.parse(r.getTimestamp(), formatter));
                            model.setLocation(r.getLocation());
                            model.setJobTitle(r.getJobTitle());
                            model.setYearsAtEmployer(getYears(r.getYearsAtEmployer()));
                            model.setYearsOfExperience(getYears(r.getYearsOfExperience()));
                            model.setSalary(getMoney(r.getSalary()));
                            model.setSigningBonus(getMoney(r.getSigningBonus()));
                            model.setAnnualBonus(getMoney(r.getAnnualBonus()));
                            model.setAnnualStockValueBonus(getMoney(r.getAnnualStockValueBonus()));
                            model.setGender(getGender(r.getGender()));
                            model.setAdditionalComments(r.getAdditionalComments());

                            return model;
                        } catch (Exception e) {
//                            log.error("Garbage Data would not be loaded : " + r.toString(), e);
                            log.debug("Skipped Garbage Data would not be loaded : " + r.toString());
                            return null;
                        }
                    }
            ).filter(Objects::nonNull).toList();

        } catch (IOException e) {
            log.error("Can not read RAW Data from json file.");
        }

        return Collections.emptyList();

    }

    private GenderEnum getGender(String genderString) {
        try {
            return GenderEnum.valueOf(genderString);
        } catch (IllegalArgumentException e) {
            return GenderEnum.Others;
        }
    }

    private Long getMoney(String moneyString) throws ParseException {
        if(StringUtils.isBlank(moneyString)){
            return null;
        } else {
            return DecimalFormat.getInstance().parse(moneyString).longValue();
        }
    }

    private Double getYears(String yearString) {

        if(StringUtils.isBlank(yearString)){
            return null;
        } else {
            return Double.parseDouble(yearString);
        }
    }

}
