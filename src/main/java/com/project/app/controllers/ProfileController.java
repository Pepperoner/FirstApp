package com.project.app.controllers;

import com.project.app.entities.Profile;
import com.project.app.entities.Rating;
import com.project.app.services.ProfileService;
import com.project.app.services.RatingService;
import com.project.app.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.security.Principal;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin
public class ProfileController {

    private ProfileService profileService;
    private RatingService ratingService;
    private ValidationErrorService validationErrorService;

    @Autowired
    public ProfileController(ProfileService profileService, RatingService ratingService, ValidationErrorService validationErrorService) {
        this.profileService = profileService;
        this.ratingService = ratingService;
        this.validationErrorService = validationErrorService;
    }

    @PostMapping("/{profileId}")
    public ResponseEntity<?> addLikeOrDislikeToProfile(@Valid @RequestBody Rating rating,
                                                       BindingResult result, @PathVariable Long profileId, Principal principal){
        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        Rating addRating = ratingService.addLikeOrDislike(profileId, rating, principal.getName());

        return new ResponseEntity<>(addRating, HttpStatus.CREATED);
    }

    //@PostMapping("/{profileId}")
    //public ResponseEntity<?> addLikeOrDislikeToProfile(@Valid @RequestBody Rating rating,
    //                                        BindingResult result, @PathVariable Long profileId, Principal principal){
    //    ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);
    //    if (errorMap != null) return errorMap;

    //    Rating addRating = profileService.addLikeOrDislike(profileId, rating, principal.getName());

    //    return new ResponseEntity<>(addRating, HttpStatus.CREATED);
    //}

    @GetMapping("/{profileId}")
    public ResponseEntity<?> getProfileById(@PathVariable Long profileId){
        Profile profile = profileService.findProfileByIdentifier(profileId);
        return new ResponseEntity<>(profile,HttpStatus.OK);
    }

    @PatchMapping("/{profileId}")
    public ResponseEntity<?> updateProfileById(@Valid @RequestBody Profile profileEntity, BindingResult result,
                                               @PathVariable Long profileId, Principal principal){

        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);
        if(errorMap != null) return errorMap;


        Profile updatedProfile = profileService.updateProfile(profileEntity, profileId, principal);

        return new ResponseEntity<>(updatedProfile,HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Profile> getAllProjects(){
        return profileService.findAllProfiles();
    }


}
