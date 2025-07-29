package com.Unite.UniteMobileApp.dtos;

import lombok.Data;

import java.time.LocalDateTime;



;

@Data
public class MeetingUpdateRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String agenda;
    private String notes;
}