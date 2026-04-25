package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.dto.TheaterRequestDTO;
import com.kary.moviebooking.dto.TheaterResponseDTO;
import com.kary.moviebooking.entity.Theater;
import com.kary.moviebooking.exception.ResourceNotFoundException;
import com.kary.moviebooking.repository.TheaterRepository;
import com.kary.moviebooking.service.Interface.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {
    private final TheaterRepository theaterRepository;

    @Override
    public TheaterResponseDTO createTheater(TheaterRequestDTO request) {
        Theater theater = new Theater();
        theater.setName(request.getName());
        theater.setLocation(request.getLocation());
        theaterRepository.save(theater);
        return toDTO(theater);
    }

    @Override
    public TheaterResponseDTO getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found: " + id));
        return toDTO(theater);
    }

    @Override
    public List<TheaterResponseDTO> getAllTheaters() {
        return theaterRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTheater(Long id) {
        if (!theaterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theater not found: " + id);
        }
        theaterRepository.deleteById(id);
    }

    private TheaterResponseDTO toDTO(Theater theater) {
        return TheaterResponseDTO.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .build();
    }
}
