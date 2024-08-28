package com.example.demo.dao;

import com.example.demo.dto.EventDto;
import com.example.demo.entity.Events;
import com.example.demo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DemoDao {

    private final EventRepository eventRepository;

    public Optional<EventDto> findByID(String id) {

        eventRepository.findAll().forEach(System.out::println);

        var optResult = eventRepository.findById(id);
        return optResult.map(this::entityToDto);

    }


    public List<EventDto> getAll() {

        return getDtoListFromOptional(Optional.of(eventRepository.findAll()));
    }

    public List<EventDto> findFirst3ByOrderByCostDesc() {

        return getDtoListFromOptional(eventRepository.findFirst3ByOrderByCostDesc());
    }


    public List<EventDto> findFirst3ByOrderByDurationDesc() {

        return getDtoListFromOptional(eventRepository.findFirst3ByOrderByDurationDesc());
    }

    public List<EventDto> getDtoListFromOptional(Optional<List<Events>> entityOpt) {

        return entityOpt
                .orElse(Collections.emptyList()).stream()
                .map(this::entityToDto).toList();
    }


    public EventDto entityToDto(Events entity) {

        return EventDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cost(entity.getCost())
                .duration(entity.getDuration())
                .build();
    }

}
