package com.project.app.services;

import com.project.app.entities.Profile;
import com.project.app.exceptions.ProfileIdentifierException;
import com.project.app.repositories.ProfileRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile findProfileByIdentifier(Long profileId) {
        if (profileId == null) {
            throw new ProfileIdentifierException("Profile ID '" + profileId + "' doesn't exists");
        }
        return profileRepository.findById(profileId).get();
    }

    public Profile updateProfile(Profile updatedProfile, Long profileId, Principal principal) {

       // updatedProfile.setProfilePicture(encoder()); for easier testing
        System.out.println(updatedProfile.getUser().getUsername());
        if (principal.getName().equals(updatedProfile.getUser().getUsername())) {

            Profile profile = findProfileByIdentifier(profileId);
            profile = updatedProfile;
            if (profile.getProfilePicture()!=null &&
                    pictureDecoder(updatedProfile.getProfilePicture(), profileId) != null) {
               // FileOutputStream fileOutputStream = pictureDecoder(updatedProfile.getProfilePicture(), profileId);
                profile.setProfilePicture(profileId + ".jpg");
            }
            return profileRepository.save(profile);
        }
        return new Profile();
    }

  /*  private String encoder() {
        String filePath = "C:\\Users\\user\\IdeaProjects\\FirstApp\\src\\main\\resources\\The_Earth_seen_from_Apollo_17.jpg";
        byte[] fileContent = new byte[0];
        String encodedString = null;
        try {
            fileContent = FileUtils.readFileToByteArray(new File(filePath));
            encodedString = Base64.getEncoder().encodeToString(fileContent);
            System.out.println(encodedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedString;
    }*/

    public Iterable<Profile> findAllProfiles() {
        return profileRepository.findAll();
    }

    public <S extends Profile> S saveProfile(S entity) {
        return profileRepository.save(entity);
    }

    private FileOutputStream pictureDecoder(String base64Picture, Long id) {
        try (FileOutputStream fos = new FileOutputStream( id + ".jpg")) {
            fos.write(Base64.getDecoder().decode(base64Picture));
            return fos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
