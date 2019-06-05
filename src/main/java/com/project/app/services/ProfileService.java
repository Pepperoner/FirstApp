package com.project.app.services;

import com.project.app.entities.LikebleProfile;
import com.project.app.entities.Profile;
import com.project.app.entities.User;
import com.project.app.exceptions.ProfileIdentifierException;
import com.project.app.repositories.ProfileRepository;
import com.project.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.*;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
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

            Profile profileFromDB = findProfileByIdentifier(updatedProfile.getId());

            if (updatedProfile.getInformation() != null) {
                profileFromDB.setInformation(updatedProfile.getInformation());
            }
            if (updatedProfile.getSkills() != null) {
                profileFromDB.setSkills(updatedProfile.getSkills());
            }

            if (profileFromDB.getProfilePicture() != null &&
                    pictureDecoder(updatedProfile.getProfilePicture(), updatedProfile.getId()) != null) {
                // FileOutputStream fileOutputStream = pictureDecoder(updatedProfile.getProfilePicture(), profileId);
                profileFromDB.setProfilePicture(updatedProfile.getId() + ".jpg");
            }
            return profileRepository.save(profileFromDB);
        }
        return new Profile();
    }

    private void fixUpdatedProfileRatings(Profile updatedProfile, User currentUser) {
        if (!profileRepository.findById(currentUser.getId()).get().getRatings().equals(updatedProfile.getRatings())) {
            updatedProfile.setRatings(profileRepository.findById(currentUser.getId()).get().getRatings());
        }
    }

    private void fixUpdatedProfileUser(Profile updatedProfile, User currentUser) {
        if (!currentUser.equals(updatedProfile.getUser())) {
            updatedProfile.setUser(currentUser);
        }
    }

    private void fixUpdatedProfileId(Profile updatedProfile, User currentUser) {
        if (!currentUser.getId().equals(updatedProfile.getId())) {
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

    public Iterable<LikebleProfile> getAllProfilesWithLikeOpportunity(String principalName) {
        User currentUser = userRepository.findByUsername(principalName);

        Profile currentProfile = profileRepository.findById(currentUser.getId()).get();

        List<LikebleProfile> likebleProfiles = new ArrayList<>();

        profileRepository.findAll().iterator()
                .forEachRemaining(profile -> {
                    if (isProfileRaringEmptyAndNotEqualsCurrent(currentProfile, profile, profile.getRatings().isEmpty())) {
                        getProfileWithLikeAvailability(likebleProfiles, profile, true);
                        return;
                    } else if (isProfileRatingIncludeLikeFromCurrent(currentProfile, profile)) {
                        getProfileWithLikeAvailability(likebleProfiles, profile, false);
                        return;
                    } else if (isProfileRaringNotEmptyAndNotEqualsCurrent(currentProfile, profile, profile.getRatings().isEmpty())) {
                        getProfileWithLikeAvailability(likebleProfiles, profile, true);
                    }
                });
        return likebleProfiles;
    }

    private void getProfileWithLikeAvailability(List<LikebleProfile> likebleProfiles, Profile profile, boolean b) {
        likebleProfiles.add(LikebleProfile.builder()
                .profileId(profile.getId())
                .picture(profile.getProfilePicture())
                .fullName(profile.getUser().getFullName())
                .jobTitle(profile.getUser().getJobTitle())
                .isAbleToLike(b)
                .build());
    }

    private boolean isProfileRatingIncludeLikeFromCurrent(Profile currentProfile, Profile profile) {
        return !profile.getRatings().isEmpty() && !profile.equals(currentProfile) &&
                profile.getRatings().stream()
                        .anyMatch(rating -> currentProfile.getUser().getUsername().equals(rating
                                .getRatingSourceUsername()));
    }

    private boolean isProfileRaringEmptyAndNotEqualsCurrent(Profile currentProfile, Profile profile, boolean empty) {
        return empty && !profile.equals(currentProfile);
    }

    private boolean isProfileRaringNotEmptyAndNotEqualsCurrent(Profile currentProfile, Profile profile, boolean empty) {
        return !empty && !profile.equals(currentProfile);
    }

    public <S extends Profile> S saveProfile(S entity) {
        return profileRepository.save(entity);
    }

    private FileOutputStream pictureDecoder(String base64Picture, Long id) {
        try (FileOutputStream fos = new FileOutputStream(id + ".jpg")) {
            fos.write(Base64.getDecoder().decode(base64Picture));
            return fos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
