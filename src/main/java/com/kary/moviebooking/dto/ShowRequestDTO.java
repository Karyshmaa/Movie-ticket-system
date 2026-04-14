package com.kary.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowRequestDTO {
    private Long movieId;
    private Long screenId;
    private Long theaterId;
    private LocalDateTime showTime;
}
