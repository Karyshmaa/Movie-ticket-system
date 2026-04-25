package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.dto.ShowRequestDTO;
import com.kary.moviebooking.dto.ShowResponseDTO;
import com.kary.moviebooking.entity.Movie;
import com.kary.moviebooking.entity.Screen;
import com.kary.moviebooking.entity.Show;
import com.kary.moviebooking.entity.Theater;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.repository.MovieRepository;
import com.kary.moviebooking.repository.ScreenRepository;
import com.kary.moviebooking.repository.ShowRepository;
import com.kary.moviebooking.repository.TheaterRepository;
import com.kary.moviebooking.service.Interface.ShowSeatService;
import com.kary.moviebooking.service.Interface.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final ShowSeatService showSeatService;

    @Override
    @Transactional
    public ShowResponseDTO createShow(ShowRequestDTO request) {

        // ✅ all DB lookups in service, not controller
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + request.getMovieId()));

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found: " + request.getTheaterId()));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found: " + request.getScreenId()));

        Show show = new Show();
        show.setMovie(movie);
        show.setTheater(theater);
        show.setScreen(screen);
        show.setShowTime(request.getShowTime());

        Show savedShow = showRepository.save(show);

        // ✅ auto create show seats after show is saved
        showSeatService.createShowSeats(savedShow);

        return toDTO(savedShow);
    }

    @Override
    public ShowResponseDTO getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + id));
        return toDTO(show);
    }

    @Override
    public List<ShowResponseDTO> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowResponseDTO> getShowsByMovieId(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found: " + movieId);
        }
        return showRepository.findByMovieId(movieId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowResponseDTO> getShowsByTheaterId(Long theaterId) {
        if (!theaterRepository.existsById(theaterId)) {
            throw new ResourceNotFoundException("Theater not found: " + theaterId);
        }
        return showRepository.findByTheaterId(theaterId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ShowResponseDTO toDTO(Show show) {
        return ShowResponseDTO.builder()
                .id(show.getId())
                .movieTitle(show.getMovie().getTitle())
                .screenName(show.getScreen().getName())
                .theaterName(show.getTheater().getName())
                .location(show.getTheater().getLocation())
                .showTime(show.getShowTime())
                .build();
    }
}