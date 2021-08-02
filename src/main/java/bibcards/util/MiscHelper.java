package bibcards.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MiscHelper {

    public static String getCurrentTimeAsString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm-");
        LocalDateTime now = LocalDateTime.now();
        String result = dtf.format(now)
                .replaceAll("/", "_")
                .replaceAll(":", "_");
        return result;
    }

    public static String decodeValue(String value) {
        try {
            if (value == null)
                return value;
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

}
