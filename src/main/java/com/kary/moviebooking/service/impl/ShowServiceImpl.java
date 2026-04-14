package com.kary.moviebooking.service.Impl;


import com.kary.moviebooking.dto.ShowResponseDTO;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.repository.ShowRepository;
import com.kary.moviebooking.service.Interface.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowServiceImpl implements ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatService showSeatService;

    @Override
    public ShowResponseDTO createShow(Show show) {

        System.out.println("CREATE SHOW CALLED");

        Show savedShow = showRepository.save(show);

        showSeatService.createShowSeats(savedShow);

        return mapToDTO(savedShow);
    }
    private ShowResponseDTO mapToDTO(Show show) {
        ShowResponseDTO dto = new ShowResponseDTO();

        dto.setId(show.getId());
        dto.setMovieTitle(show.getMovie().getTitle());
        dto.setScreenName(show.getScreen().getName());
        dto.setTheaterName(show.getTheater().getName());
        dto.setLocation(show.getTheater().getLocation());
        dto.setShowTime(show.getShowTime());

        return dto;
    }

    @Override
    public ShowResponseDTO getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        return mapToDTO(show);
    }

    @Override
    public java.util.List<ShowResponseDTO> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}
