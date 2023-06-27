package com.project.user.service.UserService.services.impl;

import com.project.user.service.UserService.entities.Hotel;
import com.project.user.service.UserService.entities.Rating;
import com.project.user.service.UserService.entities.User;
import com.project.user.service.UserService.exceptions.ResourceNotFoundException;
import com.project.user.service.UserService.external.services.HotelService;
import com.project.user.service.UserService.repositories.UserRepository;
import com.project.user.service.UserService.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given ID not found on server: "+userId));

        Rating[] list = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{} ",list);

        List<Rating> ratings = Arrays.stream(list).toList();

        List<Rating> ratingList = ratings.stream().map(rating -> {

//            Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;

        }).toList();

        user.setRatings(ratingList);

        return user;
    }
}
