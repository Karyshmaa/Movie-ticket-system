package com.kary.moviebooking.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowResponseDTO {

    private Long id;
    private String movieTitle;
    private String screenName;
    private String theaterName;
    private String location;
    private LocalDateTime showTime;
}
