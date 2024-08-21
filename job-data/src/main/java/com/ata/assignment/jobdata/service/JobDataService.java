package com.ata.assignment.jobdata.service;

import com.ata.assignment.jobdata.dao.JobDataDao;
import com.ata.assignment.jobdata.model.*;
import com.ata.assignment.jobdata.util.*;
import com.ata.assignment.jobdata.util.filter.OperationPredicateFactory;
import com.ata.assignment.jobdata.util.sort.SortComparatorFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
public class JobDataService {

    private static final Logger log = LogManager.getLogger(JobDataService.class);
    private final JobDataDao jobDataDao;

    public JobDataService(JobDataDao jobDataDao) {
        this.jobDataDao = jobDataDao;
    }

    public List<JobDataModel> getFilteredJobs(String filters) throws IOException {

        List<FetchOperator> filterCols = FilterOperatorUtil.get(filters);

        // fetch full data with filter
        log.info(() -> filterCols);

        if (filterCols.isEmpty()) {
            return jobDataDao.readCSV();
        } else {
            return jobDataDao.readCSV().stream().filter(OperationPredicateFactory.get(filterCols)).toList();
        }
    }

    public List<JobDataModel> getSortedJobs(List<JobDataModel> dataList, String sorts) throws IOException {

        List<SortOperator> sortOperations = SortOperatorUtil.get(sorts);
        List<JobDataModel> sortedList;
        if (!sortOperations.isEmpty()) {
            sortedList = dataList.stream().sorted(SortComparatorFactory.get(sortOperations)).toList();
        } else {
            sortedList = dataList;
            log.info("No Sorting needed.");
        }

        return sortedList;
    }

    public String getSelectedColumnsJobs(List<JobDataModel> dataList, String columns) throws IOException, NotFoundException, CannotCompileException {

        return SelectColumnUtil.get(columns).writeValueAsString(dataList);
    }

}
