package com.project.rating.RatingService.services;

import com.project.rating.RatingService.entities.Rating;

import java.util.List;

public interface RatingService {

    // create
    Rating create(Rating rating);

    List<Rating> getRatings();

    List<Rating> getRatingByUserId(String userId);

    List<Rating> getRatingByHotelId(String hotelId);
}
