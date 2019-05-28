package com.project.app.services;

import com.project.app.entities.Profile;
import com.project.app.entities.Rating;
import com.project.app.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private RatingRepository ratingRepository;
    private ProfileService profileService;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating addLikeOrDislike(Long profileIdentifier, Rating rating, String userName){

        //Profile profile = profileService.findProfileByIdentifier(profileIdentifier);
        //projectTask.setBacklog(backlog);
//
        //Integer backlogSequence = backlog.getPTSequence();
        //backlogSequence++;
        //backlog.setPTSequence(backlogSequence);

        return ratingRepository.save(rating);
    }
}
