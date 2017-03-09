package kz.webscrapping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Created by KBadashvili on 006 06.03.17.
 */
public class WebScrapper {

    public static void main(String[] args) throws Exception {
        try (final WebClient webClient = new WebClient()) {

            // Создаем файлы для экспорта
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/Java/Tentorium/export.csv"), "Cp1251"));
            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/Java/Tentorium/export2.csv"), "Cp1251"));
            BufferedWriter writerItem = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/Java/Tentorium/exportItems.csv"), "Cp1251"));

            // Заголовки в csv
            writerItem
                    .append("Catalog").append(";")
                    .append("Name").append(";")
                    .append("Description").append(";")
                    .append("Ingredients").append(";")
                    .append("Use").append(";")
                    .append("Instruction").append(";")
                    .append("Documentation").append(";")
                    .append("Image").append(";");
            writerItem.newLine();

            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            // Начинаем со страницы магазина
            HtmlPage page = webClient.getPage("https://tentorium.ru/internet-magazin");
            // Страницы подкатегорий
            HtmlPage pages, productpage;

//            final String pageAsXml = page.asXml();
//            Assert.assertTrue(pageAsXml.contains("<div class=\"col-lg-3 col-md-4 col-sm-6 col-xs-12\">"));
//            System.out.println(pageAsXml);

            List<HtmlElement> catalog = page.getByXPath(".//div[@class='col-lg-3 col-md-4 col-sm-6 col-xs-12']");

            if(catalog.isEmpty()){
                System.out.println("No items found !");
            } else {
                /*
                * Первый уровень - каталог
                 */
                System.out.println("Понеслась!");
                for(HtmlElement htmlItem : catalog){
                    HtmlElement title = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='organizm_category_item']/b")); // Заголовок
                    //get image
                    HtmlImage image = ((HtmlImage) htmlItem.getFirstByXPath(".//img")); // Картинка
                    //get links
                    HtmlAnchor link = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a")); // URL для перехода на второй уровень

                    // Создаем директории и называем их именами категорий. Будем сохранять туда изображения
                    try {
                        if (Files.exists(Paths.get("D:/Java/Tentorium/" + title.asText().replace("/", " и")))) {
                            System.out.println("Директория \"" + title.asText().replace("/", " и") + "\" уже создана");
                        } else {
                            Files.createDirectory(Paths.get("D:/Java/Tentorium/" + title.asText().replace("/", " и")));
                        }

                    } catch (Exception e) {
                        System.out.println("Попытка создать директорию... Что-то пошло не так...");
                        e.printStackTrace();
                    }

                    /*
                    * Второй уровень - товары
                     */
                    pages = link.click();
                    List<HtmlElement> products = pages.getByXPath(".//div[@class='loop-product col-lg-3 col-md-4 col-sm-6 col-xs-12']");
                    for (HtmlElement product : products) {
                        HtmlAnchor productLink = ((HtmlAnchor) product.getFirstByXPath(".//a")); // URL для перехода на третий уровень
                        HtmlElement productTitle = ((HtmlElement) product.getFirstByXPath(".//h3")); // Название
                        HtmlElement productDescription = ((HtmlElement) product.getFirstByXPath(".//p[@class='excerpt']")); // Описание
                        HtmlImage productImage = ((HtmlImage) product.getFirstByXPath(".//img")); // Картинка

//                        writer2.append(productTitle.asText())
//                                .append(";")
//                                .append(productDescription.asText())
//                                .append(";")
//                                .append(productImage.getAttribute("src"))
//                                .append(";");
//                        writer2.newLine();

                        /*
                        * Третий уровень - продукт
                         */
                        productpage = productLink.click();
                        List<HtmlElement> items = productpage.getByXPath(".//body[@class='is_not_distributor']");
                        for (HtmlElement item : items) {
                            HtmlElement itemTitle = ((HtmlElement) item.getFirstByXPath(".//div[@class='product_title_row']/h1")); // Название
                            HtmlElement itemDescription = ((HtmlElement) item.getFirstByXPath(".//div[@class='product_info_col']/div/p")); // Описание
                            List<HtmlElement> itemFunction = item.getByXPath(".//div[@class='product_info_col']/div[@class='organism_function_wrap']/a"); // Области применения
                            HtmlElement itemIngredients = ((HtmlElement) item.getFirstByXPath(".//div[@class='product_info_col']/div[@class='product_tabs_wrap']/div[@id='ingredients']")); // Описание
                            HtmlElement itemUse = ((HtmlElement) item.getFirstByXPath(".//div[@class='product_info_col']/div[@class='product_tabs_wrap']/div[@id='use']")); // Применение
                            HtmlElement itemDocumentation = ((HtmlElement) item.getFirstByXPath(".//div[@class='product_info_col']/div[@class='product_tabs_wrap']/div[@id='documentation']/a")); // Документация
                            HtmlImage itemImage = ((HtmlImage) item.getFirstByXPath(".//div[@class='product-images']/a/img")); // Картинка

                            if (itemImage == null) {
                                System.out.println("У продукта \"" + itemTitle.asText() + "\" нет зображения");
                            }

                            // Загоняем в строку список из "области применения".
                            String attributes = "";
                            for (HtmlElement function : itemFunction) {
                                attributes += (function.getAttribute("data-title")) + ", ";
                            }

                            try {
                                if (Files.exists(Paths.get("D:/Java/Tentorium/" + title.asText().replace("/", " и") + "/" + itemTitle.asText() + ".png"))) {
                                    System.out.println("Файл: \"" + itemTitle.asText() + ".png" +"\" уже создан!" );
                                }

                                File imageFile = new File("D:/Java/Tentorium/" + title.asText().replace("/", " и") + "/" + itemTitle.asText() + ".png");
                                itemImage.saveAs(imageFile);

                                writerItem
                                        .append(title.asText())
                                        .append(";")
                                        .append(itemTitle.asText())
                                        .append(";")
                                        .append(itemDescription.asText().replace(";", ".").replace("\n", "").replace("\r", "")) // удаляем переходы на новую строку
                                        .append(";")
                                        .append(attributes.replace("\n", "").replace("\r", ""))
                                        .append(";");
                                if (itemIngredients == null) {
                                    System.out.println("У продукта \"" + itemTitle.asText() + "\" нет ингридиентов");
                                    writerItem.append("none")
                                            .append(";");
                                } else {
                                    writerItem.append(itemIngredients.asText().replace(";", ".").replaceAll("   ", ", ").replace("\n", "").replace("\r", ", "))
                                            .append(";");
                                }
                                if (itemUse == null) {
                                    System.out.println("У продукта \"" + itemTitle.asText() + "\" нет инструкции");
                                    writerItem.append("none")
                                            .append(";");
                                } else {
                                    writerItem.append(itemUse.asText().replace(";", ".").replace("\n", "").replace("\r", ""))
                                            .append(";");
                                }
                                if (itemDocumentation == null) {
                                    System.out.println("У продукта \"" + itemTitle.asText() + "\" нет документации");
                                    writerItem.append("none")
                                            .append(";");
                                } else {
                                    writerItem.append(itemDocumentation.getAttribute("href"))
                                            .append(";");
                                }
                                writerItem.append(imageFile.getPath())
                                        .append(";");
                                writerItem.newLine();
                            } catch (NullPointerException e) {
                                writerItem.newLine(); // переходим на новую строку
                                System.out.println("Какая-то хрень c продуктом " + itemTitle.asText());
                            }
                        }
                    }
                }
                // Обязательно закрываем потоки
                writer.close();
                writer2.close();
                writerItem.close();
            }
            webClient.close();
        }
    }
}
