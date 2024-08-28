package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EventDto {
    private String id;
    private String name;
    private Integer cost;
    private Integer duration;
}