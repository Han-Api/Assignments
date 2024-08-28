package com.example.demo.service;


import com.example.demo.dao.DemoDao;
import com.example.demo.dto.EventDto;
import com.example.demo.exception.EventNotFoundException;
import com.example.demo.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoDao demoDao;




    public EventDto getEventByID(String id) {

        return demoDao.findByID(id).orElseThrow(() -> new EventNotFoundException("Not Found event by id[%s]".formatted(id)));
    }

    public List<EventDto> getAll() {

        return demoDao.getAll();

    }
    public List<EventDto> getTopThreeByColumn(String by) {

        return switch (by.toLowerCase()) {
            case "cost" -> demoDao.findFirst3ByOrderByCostDesc();
            case "duration" -> demoDao.findFirst3ByOrderByDurationDesc();
            default ->
                    throw new InvalidParameterException("[%s] column is not allowed in top three query.".formatted(by));
        };

    }


}
