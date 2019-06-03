package com.project.app.services;

import com.project.app.entities.Profile;
import com.project.app.entities.ProfileNegativeRating;
import com.project.app.entities.ProfilePositiveRating;
import com.project.app.entities.Rating;
import com.project.app.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private RatingRepository ratingRepository;
    private ProfileService profileService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, ProfileService profileService) {
        this.ratingRepository = ratingRepository;
        this.profileService = profileService;
    }

    public Rating addLike(Long profileIdentifier, Rating updatedRating, String userName){

        Profile profile = profileService.findProfileByIdentifier(profileIdentifier);
        updatedRating.setProfileRating(profile);

            if (!updatedRating.getLikeSourceUsername().equals(profile.getUser().getUsername())) {
                Long profileLike = 0L;
                if (profile.getLikes() == null){
                    profileLike++;
                    profile.setLikes(profileLike);
                    updatedRating.setLikeSourceUsername(updatedRating.getLikeSourceUsername());
                    System.out.println("IF //////////////////////////");
                } else {
                    profileLike = profile.getLikes();
                    profileLike++;
                    profile.setLikes(profileLike);
                    updatedRating.setLikeSourceUsername(updatedRating.getLikeSourceUsername());
                    System.out.println("ELSE /////////////////////////");
                }
            }

        return ratingRepository.save(updatedRating);
    }

    public Rating addDislike(Long profileIdentifier, Rating rating, String userName){

        Profile profile = profileService.findProfileByIdentifier(profileIdentifier);

            Long profileDislike = profile.getDislikes();
            profileDislike++;
            profile.setLikes(profileDislike);

        return ratingRepository.save(rating);
    }

    public <S extends Rating> S saveRating(S entity) {
        return ratingRepository.save(entity);
    }
}
