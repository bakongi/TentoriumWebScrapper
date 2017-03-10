package kz.webscrapping;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by KBadashvili on 010 10.03.17.
 */
public class Encoder {
    /*
    * Convert codepage to Cp1251
     */
    public String convert(String string) throws UnsupportedEncodingException {
        byte[] bytes = (string).getBytes(StandardCharsets.UTF_8); // Чтобы не было крякозябов в консоли
        return new String(bytes, "Cp1251");
    }
}
