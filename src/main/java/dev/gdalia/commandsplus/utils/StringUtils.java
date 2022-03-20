package dev.gdalia.commandsplus.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * 
 * 
 * Created to manage characters for players when examenating a temporary punishment for a player.
 * This should be functioning while players are using it correct.
 * Permitted users: Gdalia.
 * 
 * @authors Voigon, OfirTIM.
 */
public class StringUtils {

    public static Duration phraseToDuration(String phrase, ChronoUnit... supportedTimeUnits) {
    	return phraseToDuration(phrase, supportedTimeUnits.length != 0 ? Arrays.asList(supportedTimeUnits) : null);
    }
    
    public static Duration phraseToDuration(String phrase, Collection<ChronoUnit> supportedTimeUnits) {
        long seconds = 0;
        String num = "";
        for (int i = 0; i < phrase.length(); i++) {
            char c = phrase.charAt(i);
            if (Character.isDigit(c)) {
                num += c;
            } else {
                ChronoUnit lastUnit = getFromChar(c);
                if (supportedTimeUnits != null && !supportedTimeUnits.isEmpty() && !supportedTimeUnits.contains(lastUnit))
                    continue;

                seconds += lastUnit.getDuration().getSeconds() * Integer.parseInt(num);
                num = "";
            }
        }
        return Duration.ofSeconds(seconds);
    }

    public static ChronoUnit getFromChar(char c) {
        return switch (c) {
            case 's' -> ChronoUnit.SECONDS;
            case 'm' -> ChronoUnit.MINUTES;
            case 'h' -> ChronoUnit.HOURS;
            case 'd' -> ChronoUnit.DAYS;
            case 'w' -> ChronoUnit.WEEKS;
            case 'M' -> ChronoUnit.MONTHS;
            case 'y' -> ChronoUnit.YEARS;
            default -> throw new IllegalStateException("Unexpected char: " + c);
        };
    }
    
	public static boolean isUniqueId(String uuidAsString) {
		try {
			UUID.fromString(uuidAsString);
			return true;
		} catch (IllegalArgumentException e1) {
			return false;
		}
	}

    public static String createTimeFormatter(Instant instant, String format) {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                .ofPattern(format)
                .withZone(ZoneId.systemDefault());
        return DATE_TIME_FORMATTER.format(instant);
    }
}