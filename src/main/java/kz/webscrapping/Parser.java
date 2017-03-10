package kz.webscrapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KBadashvili on 009 09.03.17.
 */
public class Parser {

    private String str;

    public Parser(String[] args) {
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
            for (int i = 0; i < args.length; i++) {
                /*
                * -c — добавляет человека с заданными параметрами в конец allPeople, выводит id (index) на экран
                 */
                if (args[i].matches("[-][path]")) {
                    str = params.get("path").get(0);
                }
            }
        }
    }
}
