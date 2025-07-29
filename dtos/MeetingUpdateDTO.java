package com.Unite.UniteMobileApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingUpdateDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String agenda;
    private String notes;
}
