package com.project.app.services;

import com.project.app.entities.Profile;
import com.project.app.entities.User;
import com.project.app.exceptions.ProfileIdentifierException;
import com.project.app.exceptions.ProfileNotFoundException;
import com.project.app.repositories.ProfileRepository;
import com.project.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Base64;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository=userRepository;
    }

    public Profile findProfileByIdentifier(Long profileId) {
        if (profileId == null) {
            throw new ProfileIdentifierException("Profile ID '" + profileId + "' doesn't exists");
        }
        return profileRepository.findById(profileId).get();
    }

    public Profile updateProfile(Profile updatedProfile, String principalName) {
        User currentUser = userRepository.findByUsername(principalName);

        fixUpdatedProfileId(updatedProfile, currentUser);

        fixUpdatedProfileUser(updatedProfile, currentUser);

        fixUpdatedProfileRatings(updatedProfile, currentUser);

        // updatedProfile.setProfilePicture(encoder()); for easier testing
       // System.out.println(updatedProfile.getUser().getUsername());

        if (principalName.equals(updatedProfile.getUser().getUsername())) {
            Profile profile = findProfileByIdentifier(updatedProfile.getId());
            profile = updatedProfile;

            if (profile.getProfilePicture()!=null &&
                    pictureDecoder(updatedProfile.getProfilePicture(), updatedProfile.getId()) != null) {
               // FileOutputStream fileOutputStream = pictureDecoder(updatedProfile.getProfilePicture(), profileId);
                profile.setProfilePicture(updatedProfile.getId() + ".jpg");
            }
            return profileRepository.save(profile);
        }
        return new Profile();
    }

    private void fixUpdatedProfileRatings(Profile updatedProfile, User currentUser) {
        if(!profileRepository.findById(currentUser.getId()).get().getRatings().equals(updatedProfile.getRatings())){
            updatedProfile.setRatings(profileRepository.findById(currentUser.getId()).get().getRatings());
        }
    }

    private void fixUpdatedProfileUser(Profile updatedProfile, User currentUser) {
        if(!currentUser.equals(updatedProfile.getUser())){
            updatedProfile.setUser(currentUser);
        }
    }

    private void fixUpdatedProfileId(Profile updatedProfile, User currentUser) {
        if(!currentUser.getId().equals(updatedProfile.getId())){
            updatedProfile.setId(currentUser.getId());
        }
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
