package kz.webscrapping;

import com.gargoylesoftware.htmlunit.html.HtmlImage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by KBadashvili on 007 07.03.17.
 */
public class FileWriter {

    private String filePath;
    private String fileName;
    private String encoding = "Cp1251";
    private String delimiter = ";";
    private volatile BufferedWriter writerItem;

    public BufferedWriter getWriterItem() {
        return writerItem;
    }

    public void setWriterItem(BufferedWriter writerItem) {
        this.writerItem = writerItem;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    private int n = 0; // Счетчик, чтобы заголовки не вставлялись после каждого раздела

    void create(String catalogTitle) throws IOException {
        try {
            while (n == 0) {
                writerItem
                        .append("Catalog").append(delimiter)
                        .append("Name").append(delimiter)
                        .append("ShortDescription").append(delimiter)
                        .append("Description").append(delimiter)
                        .append("Attributes").append(delimiter)
                        .append("Ingredients").append(delimiter)
                        .append("Use").append(delimiter)
                        .append("Documentation").append(delimiter)
                        .append("Image").append(delimiter);
                writerItem.newLine();
                this.n += 1;
            }
            // Создаем директории и называем их именами категорий. Будем сохранять туда изображения
            try {
                if (Files.exists(Paths.get(this.filePath + catalogTitle.replace("/", " и")))) {
                    System.out.println("Директория \"" + catalogTitle.replace("/", " и") + "\" уже создана ");
                } else {
                    System.out.println("Создаем директорию \"" + catalogTitle.replace("/", " и") + "\"    " + Paths.get(this.filePath + catalogTitle.replace("/", " и")));
                    Files.createDirectory(Paths.get(this.filePath + catalogTitle.replace("/", " и")));
                }
            } catch (Exception e) {
                System.out.println("Попытка создать директорию... Что-то пошло не так...");
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println("Кодировка не поддерживается");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Проблема с записью в файл или на диск");
            e.printStackTrace();
        }
    }

    synchronized void append(String productCatalogName,
                       String productName,
                       String productShortDescription,
                       String productDescription,
                       String productAttributes,
                       String productIngredients,
                       String productUsage,
                       String productDocumentation,
                       HtmlImage productImage,
                       String delimiter) throws IOException {

        try {

            if (Files.exists(Paths.get(this.filePath + productCatalogName.replace("/", " и") + "/" + productName + ".png"))) {
                System.out.println("Файл: \"" + productName + ".png" +"\" уже создан!" );
            }

            File imageFile = new File(this.filePath + productCatalogName.replace("/", " и") + "/" + productName + ".png");
            if (productImage != null) {
                productImage.saveAs(imageFile);
            }

            System.out.println("Записываем данные в " + fileName);
            writerItem.append(productCatalogName)
                    .append(delimiter)
                    .append(productName).append(delimiter)
                    .append(productShortDescription).append(delimiter)
                    .append(productDescription).append(delimiter)
                    .append(productAttributes).append(delimiter)
                    .append(productIngredients).append(delimiter)
                    .append(productUsage).append(delimiter)
                    .append(productDocumentation).append(delimiter)
                    .append(imageFile.getPath()).append(delimiter);
            writerItem.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void close() throws IOException {
        this.writerItem.close();
        System.out.println("Закрываем поток...");
    }
}