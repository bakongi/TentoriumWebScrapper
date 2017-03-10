package kz.webscrapping;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KBadashvili on 009 09.03.17.
 */
public class Parser {

    private String path;
    private String filename;
    private Encoder encoder = new Encoder();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Parser(String[] args) throws UnsupportedEncodingException {
        /*
        * Подсмотрел здесь http://stackoverflow.com/questions/7341683/parsing-arguments-to-a-java-command-line-program
        */
        final Map<String, List<String>> params = new HashMap<>();

        List<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return;
                }

                options = new ArrayList<>();
                params.put(a.substring(1), options);
            }
            else if (options != null) {
                options.add(a);
            }
            else {
                System.err.println("Illegal parameter usage");
                return;
            }
        }

        if (args.length > 0) {
            System.setOut(new java.io.PrintStream(System.out, true, "Cp1251"));
            for (int i = 0; i < args.length; i++) {
                /*
                * -p path
                 */
                if (args[i].matches("[-][p]")) {
                    System.out.println(encoder.convert("Директория, куда будут сохраняться данные: " + params.get("p").get(0)));
                    this.path = params.get("p").get(0);
                } else if (args[i].matches("[-][f]")) {
                    System.out.println(encoder.convert("Файл с информацией: " + params.get("f").get(0) + ".csv"));
                    this.filename = params.get("f").get(0);
                }
            }
        } else {
            System.out.println(encoder.convert("Отсутствуют аргументы. Используйте следующие команды:"));
            System.out.println(encoder.convert("    -d D:/Folder/"));
            System.out.println(encoder.convert("    -f Export"));
        }
    }
}
