package com.kary.moviebooking.service.Impl;

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
    public Show createShow(Show show) {

        Show savedShow = showRepository.save(show);

        showSeatService.createShowSeats(savedShow);

        return savedShow;
    }
}
