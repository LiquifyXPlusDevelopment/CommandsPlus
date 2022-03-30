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
 * @author Voigon, OfirTIM.
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

    public static String formatTime(Duration duration) {
        StringBuilder sb = new StringBuilder();

        long months = duration.toDays() / 30;
        long weeks = (duration.toDaysPart() / 7) - (months * 30);
        long days = duration.toDaysPart() - (weeks * 7);
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        //Appends months
        if (sb.isEmpty()) sb.append(months == 0 ? "" : months + "M");
        else sb.append(months == 0 ? "" : ", " + months + "M");

        // Appends weeks
        if (sb.isEmpty()) sb.append(weeks == 0 ? "" : weeks + "w");
        else sb.append(weeks == 0 ? "" : ", " + weeks + "w");

        // Appends days
        if (sb.isEmpty()) sb.append(days == 0 ? "" : days + "d");
        else sb.append(days == 0 ? "" : ", " + days + "d");

        // Appends hours
        if (sb.isEmpty()) sb.append(hours == 0 ? "" : hours + "h");
        else sb.append(hours == 0 ? "" : ", " + hours + "h");

        // Appends minutes
        if (sb.isEmpty()) sb.append(minutes == 0 ? "" : minutes + "m");
        else sb.append(minutes == 0 ? "" : ", " + minutes + "m");

        // Appends seconds
        if (sb.isEmpty()) sb.append(seconds == 0 ? "" : seconds + "s");
        else sb.append(seconds == 0 ? "" : ", " + seconds + "s");

        return sb.append("").toString();
    }
}