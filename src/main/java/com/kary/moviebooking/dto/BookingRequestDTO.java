package com.kary.moviebooking.dto;

import lombok.Data;
//import org.antlr.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Data
public class BookingRequestDTO {

    @NotNull
    private Long showId;

    @NotNull
    private Long userId;
}

