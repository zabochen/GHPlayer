package ua.ck.ghplayer.utils;

public class TimeUtils {

    public static String milliSecondsConverter(long milliSeconds) {
        StringBuilder buffer = new StringBuilder();

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            buffer.append(hours + ":");
        }

        buffer.append(minutes + ":");

        if (seconds < 10) {
            buffer.append("0" + seconds);
        } else {
            buffer.append(seconds);
        }

        return buffer.toString();
    }

}
