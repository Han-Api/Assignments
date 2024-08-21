package com.ata.assignment.jobdata;

import com.ata.assignment.jobdata.model.JobDataModel;
import com.ata.assignment.jobdata.model.JobDataRaw;
import com.ata.assignment.jobdata.service.JobDataService;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@RestController
public class JobDataFacade {

    private static final Logger log = LogManager.getLogger(JobDataFacade.class);
    private final JobDataService jobDataService;

    public JobDataFacade(JobDataService jobDataService) {
        this.jobDataService = jobDataService;
    }

    @GetMapping("/heartbeat")
    public String heartbeat() {
        return "I'm alive @ " + new Date().getTime();
    }

    @GetMapping("/jobData")
    public String fetch(@RequestParam(value = "filters", defaultValue = "NO_FILTER") String filters,
                        @RequestParam(value = "columns", defaultValue = "ALL_COLUMNS") String columns,
                        @RequestParam(value = "sorts", defaultValue = "NO_SORT") String sorts
    ) {

        try {

            final List<JobDataModel> filteredList = jobDataService.getFilteredJobs(filters);
            final List<JobDataModel> sortedList = jobDataService.getSortedJobs(filteredList, sorts);

            return jobDataService.getSelectedColumnsJobs(sortedList, columns);

        } catch (IOException | NotFoundException | CannotCompileException e) {
            log.error("Error in processing request.", e);
        }

        return "Can not fetch data.";
    }

}
