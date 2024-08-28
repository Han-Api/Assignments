package com.example.demo.controller;

import com.example.demo.dto.EventDto;
import com.example.demo.exception.InvalidParameterException;
import com.example.demo.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {


    private final DemoService demoService;


    @GetMapping("/event/all")
    public ResponseEntity<List<EventDto>> getAll() {

        return ResponseEntity.status(HttpStatus.OK).body(demoService.getAll());
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(demoService.getEventByID(id));
    }


    @GetMapping("/event/top3")
    public ResponseEntity<List<EventDto>> getTopThree(@RequestParam String by) {

        if (Stream.of("cost", "duration").noneMatch(by::equalsIgnoreCase)) {
            throw new InvalidParameterException("[%s] column is not allowed in top three query.".formatted(by));
        }

        return ResponseEntity.status(HttpStatus.OK).body(demoService.getTopThreeByColumn(by));
    }


}
