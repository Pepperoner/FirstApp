package com.project.app.services;

import com.project.app.entities.Profile;
import com.project.app.exceptions.ProfileIdentifierException;
import com.project.app.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile findProfileByIdentifier(Long profileId){

        if (profileId == null){
            throw new ProfileIdentifierException("Profile ID '"+ profileId +"' doesn't exists");
        }
        return profileRepository.findById(profileId).get();
    }

    public Profile updateProfile(Profile updatedProfile, Long profileId){

        Profile profile = findProfileByIdentifier(profileId);
        profile = updatedProfile;
        return profileRepository.save(profile);
    }

    public Iterable<Profile> findAllProfiles(){
        return profileRepository.findAll();
    }

    public <S extends Profile> S saveProfile(S entity) {
        return profileRepository.save(entity);
    }
}
