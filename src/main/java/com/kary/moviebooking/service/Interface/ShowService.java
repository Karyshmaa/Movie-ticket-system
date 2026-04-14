package com.kary.moviebooking.service.Interface;

import com.kary.moviebooking.dto.ShowResponseDTO;
import com.kary.moviebooking.entity.Show;

import java.util.List;

public interface ShowService {

    ShowResponseDTO getShowById(Long id);
    List<ShowResponseDTO> getAllShows();
    ShowResponseDTO createShow(Show show);
}
