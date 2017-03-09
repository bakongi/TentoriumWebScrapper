package kz.webscrapping;

import java.io.*;

/**
 * Created by KBadashvili on 007 07.03.17.
 */
public class FileWriter {

    private String filename = "d:\\export.csv";

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    private String charset = "Cp1251";


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public FileWriter (String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.filename), this.charset));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding...");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("File not found...");
            e.printStackTrace();
        }
    }
}
