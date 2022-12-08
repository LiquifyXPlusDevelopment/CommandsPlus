package dev.gdalia.commandsplus.utils.profile;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ProfileManager {

    @Getter
    @Setter
    private static ProfileManager instance;

    private final Map<String, Profile> profiles = new HashMap<>();

    private boolean containsKey(String name) {
        return profiles.containsKey(name);
    }

    private Profile pullProfile(String name) {
        return profiles.get(name);
    }

    private void pushProfile(String name, Profile profile) {
        profiles.put(name, profile);
    }

    private void removeProfile(String name) {
        profiles.remove(name);
    }

    /**
     * Checking if the profile is in the cash,
     * <p>
     * if the profile is in the cash,
     * <p>
     * the system will get the profile,
     * <p>
     * and will check if 10 minutes has passed from the last pull,
     * <p>
     * if 10 minutes has passed the system will try to create a new profile,
     * <p>
     * if the profile is in the cash and the profile that the system just pulled out is the same,
     * <p>
     * the system will return the profile from the cash.
     * <p>
     * ********************************************************
     * <p>
     * If the profile is not in the cash,
     * <p>
     * the system will create a new one,
     * <p>
     * and will return the one that he just created.
     *
     * @param name the targeted player name of the profile.
     * @return a profile object.
     */
    public Optional<Profile> getProfile(String name) {
        if (containsKey(name)) {
            Optional<Profile> profile = Optional.of(pullProfile(name));
            long MAX_DURATION = TimeUnit.NANOSECONDS.convert(10, TimeUnit.MINUTES);
            long duration = Instant.now().getNano() - profile.get().pulledOut().getNano();

            if (duration >= MAX_DURATION)
                profile = createProfile(name);

            return profile;
        }

        return createProfile(name);
    }

    /**
     * Getting a new profile from mojang utils,
     * <p>
     * after that checking if the profile is not null,
     * <p>
     * if the profile is not null,
     * <p>
     * the system will get the profile from the cash,
     * <p>
     * if the 'oldProfile' is not null and the value and signature of the new profile is the same of the 'oldProfile',
     * <p>
     * the system will return the 'oldProfile'.
     * <p>
     * ********************************************
     * <p>
     * If the new profile value and signature is not the same,
     * <p>
     * the system will check if the profile is contains in the cash,
     * <p>
     * if it contains the system will remove it from the cash,
     * <p>
     * and then the system will add the new profile to the cash and will return the new profile.
     *
     * @param name the targeted player name of the profile.
     * @return a player profile.
     */
    public Optional<Profile> createProfile(String name) {
        Optional<Profile> profile = MojangUtils.fetchProfile(name);
        
        if (profile.isPresent()) {
            Optional<Profile> oldProfile = Optional.ofNullable(pullProfile(name));
            if (oldProfile.isPresent() && profile.get().signature().equals(oldProfile.get().signature())
                    && profile.get().value().equals(oldProfile.get().value()))
                return oldProfile;

            if (containsKey(name)) removeProfile(name);
            pushProfile(name, profile.get());
        }

        return profile;
    }
}
