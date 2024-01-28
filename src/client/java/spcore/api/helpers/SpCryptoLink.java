package spcore.api.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpCryptoLink {
    private static final Pattern pattern = Pattern.compile("^sp-crypto:\\/\\/(.*)\\?:\\$");

    public static String parse(String sprl) {
        Matcher matcher = pattern.matcher(sprl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
