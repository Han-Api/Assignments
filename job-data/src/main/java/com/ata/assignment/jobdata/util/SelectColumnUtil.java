package com.ata.assignment.jobdata.util;

import com.ata.assignment.jobdata.model.JobDataModel;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@UtilityClass
public class SelectColumnUtil {

    private static final Logger log = LogManager.getLogger(SelectColumnUtil.class);

    private static final String SEPARATOR = ",";

    public static final List<String> ALLOWED_SELECT_COLUMNS = List.of(
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

    private static final Map<String, BiConsumer<JsonGenerator, JobDataModel>> colToBiConsumerMap = Map.ofEntries(
            Map.entry("TIMESTAMP", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeStringField("Timestamp", jobDataModel.getTimestamp().toString());
                }
            }),
            Map.entry("EMPLOYER", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeStringField("Employer", jobDataModel.getEmployer());
                }
            }),
            Map.entry("LOCATION", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeStringField("Location", jobDataModel.getEmployer());
                }
            }),
            Map.entry("JOB_TITLE", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeStringField("Job Title", jobDataModel.getJobTitle());
                }
            }),
            Map.entry("YEARS_AT_EMPLOYER", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeNumberField("Years at Employer", jobDataModel.getYearsAtEmployer());
                }
            }),
            Map.entry("YEARS_OF_EXPERIENCE", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeNumberField("Years of Experience", jobDataModel.getYearsOfExperience());
                }
            }),
            Map.entry("SALARY", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeNumberField("Salary", jobDataModel.getSalary());
                }
            }),
            Map.entry("SIGNING_BONUS", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeNumberField("Signing Bonus", jobDataModel.getSigningBonus());
                }
            }),
            Map.entry("ANNUAL_BONUS", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeNumberField("Annual Bonus", jobDataModel.getAnnualBonus());
                }
            }),
            Map.entry("ANNUAL_STOCK_VALUE_BONUS", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeNumberField("Annual Stock Value/Bonus", jobDataModel.getAnnualStockValueBonus());
                }
            }),
            Map.entry("GENDER", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeStringField("Gender", jobDataModel.getGender().name());
                }
            }),
            Map.entry("ADDITIONAL_COMMENTS", new BiConsumer<JsonGenerator, JobDataModel>() {
                @SneakyThrows
                @Override
                public void accept(JsonGenerator jsonGenerator, JobDataModel jobDataModel) {
                    jsonGenerator.writeStringField("Additional Comments", jobDataModel.getAdditionalComments());
                }
            })
    );


    public static ObjectMapper get(String columns) throws NotFoundException, CannotCompileException {

        var mapper = new ObjectMapper();
        if ("ALL_COLUMNS".equals(columns) || StringUtils.isBlank(columns)) {
            return mapper;
        }

        var selectColumns = Arrays.stream(columns.split(SEPARATOR))
                .map(String::toUpperCase)
                .peek(col->log.info("selecting : {}", col))
                .filter(isValidDetail()).map(colToBiConsumerMap::get)
                .toList();

        var serializer = new JobDataModelSerializer();
        serializer.setGenerateColumnList(selectColumns);

        SimpleModule module = new SimpleModule();
        module.addSerializer(JobDataModel.class, serializer);
        mapper.registerModule(module);

        return mapper;

    }

    private Predicate<String> isValidDetail() {

        return (colName) -> {

            if (!colToBiConsumerMap.containsKey(colName.toUpperCase())) {
                log.error("Unknown select column name : {}", colName);
                return false;
            }

            return true;
        };
    }

    @Setter
    public class JobDataModelSerializer extends StdSerializer<JobDataModel> {

        private List<BiConsumer<JsonGenerator, JobDataModel>> generateColumnList;

        public JobDataModelSerializer() {
            this(null);
        }

        public JobDataModelSerializer(Class<JobDataModel> t) {
            super(t);
        }

        @Override
        public void serialize(
                JobDataModel value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

            jgen.writeStartObject();
            generateColumnList.forEach(c -> c.accept(jgen, value));
            jgen.writeEndObject();
        }
    }

}
