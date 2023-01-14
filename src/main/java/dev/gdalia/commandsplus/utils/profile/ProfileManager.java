package dev.gdalia.commandsplus.utils.profile;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ProfileManager {

    @Getter
    @Setter
    private static ProfileManager instance;

    private final Cache<String, Profile> profiles = CacheBuilder.newBuilder()
            .concurrencyLevel(6)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private Map<String, Profile> getImmutableProfileList() {
        return Map.copyOf(profiles.asMap());
    }

    private boolean containsKey(String name) {
        return profiles.getIfPresent(name.toLowerCase()) != null;
    }

    private boolean containsUser(UUID uuid) {
        return getImmutableProfileList().values()
                .stream()
                .anyMatch(profile -> profile.playerUUID().equals(uuid));
    }

    private Profile pullProfile(String name) {
        return profiles.getIfPresent(name);
    }

    private Profile pullProfile(UUID uuid) {
        return getImmutableProfileList().values()
                .stream()
                .filter(profile -> profile.playerUUID().equals(uuid))
                .findAny()
                .orElse(null);
    }

    private void pushProfile(String name, Profile profile) {
        profiles.put(name.toLowerCase(), profile);
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

    public Optional<Profile> getProfile(UUID uuid) {
        if (containsUser(uuid)) {
            Optional<Profile> profile = Optional.of(pullProfile(uuid));
            long MAX_DURATION = TimeUnit.NANOSECONDS.convert(10, TimeUnit.MINUTES);
            long duration = Instant.now().getNano() - profile.get().pulledOut().getNano();

            if (duration >= MAX_DURATION)
                profile = createProfile(uuid);

            return profile;
        }

        return createProfile(uuid);
    }

    public CompletableFuture<Profile> getProfileAsync(String username) {
        return CompletableFuture.supplyAsync(() -> getProfile(username).orElseThrow());
    }

    public CompletableFuture<Profile> getProfileAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getProfile(uuid).orElseThrow());
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
        Optional<Profile> profile = MojangUtils.fetchAshconProfile(name);
        
        if (profile.isPresent()) {
            Optional<Profile> oldProfile = Optional.ofNullable(pullProfile(name));
            if (oldProfile.isPresent() && profile.get().signature().equals(oldProfile.get().signature())
                    && profile.get().value().equals(oldProfile.get().value()))
                return oldProfile;

            pushProfile(name, profile.get());
        }

        return profile;
    }

    public Optional<Profile> createProfile(UUID uuid) {
        Optional<Profile> profile = MojangUtils.fetchAshconProfile(uuid.toString());

        if (profile.isPresent()) {
            Optional<Profile> oldProfile = Optional.ofNullable(pullProfile(profile.get().playerName()));
            if (oldProfile.isPresent() && profile.get().signature().equals(oldProfile.get().signature())
                    && profile.get().value().equals(oldProfile.get().value()))
                return oldProfile;

            pushProfile(profile.get().playerName(), profile.get());
        }

        return profile;
    }
}
