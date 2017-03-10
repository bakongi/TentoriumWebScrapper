package kz.webscrapping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.util.List;

/**
 * Created by KBadashvili on 007 07.03.17.
 */
public class Scrapper implements Runnable {

    private String filePath;
    private String fileName;
    private String encoding = "Cp1251";

    private String catalogTitle = "";
    private String productTitle = "";
    private String productShortDescription = "";
    private String productDescription = "";
    private String productAttributes = "";
    private String productIngredients = "";
    private String productUse = "";
    private String productDocumentation = "";
    private String productImagePath = "";
    private String delimiter = ";";

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

    public String getCatalogTitle() {
        return catalogTitle;
    }

    public void setCatalogTitle(String productTitle) {
        this.catalogTitle = productTitle;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(String productAttributes) {
        this.productAttributes = productAttributes;
    }

    public String getProductIngredients() {
        return productIngredients;
    }

    public void setProductIngredients(String productIngredients) {
        this.productIngredients = productIngredients;
    }

    public String getProductUse() {
        return productUse;
    }

    public void setProductUse(String productUse) {
        this.productUse = productUse;
    }

    public String getProductDocumentation() {
        return productDocumentation;
    }

    public void setProductDocumentation(String productDocumentation) {
        this.productDocumentation = productDocumentation;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public HtmlImage getItemImage() {
        return itemImage;
    }

    public void setItemImage(HtmlImage itemImage) {
        this.itemImage = itemImage;
    }

    HtmlImage itemImage = null;

    @Override
    synchronized public void run() {
        FileWriter writer = null;
        try {
            writer = new FileWriter();
            writer.setFilePath(this.filePath);
            writer.setFileName(this.fileName);
            writer.setWriterItem(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.filePath + this.fileName), this.encoding)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.print("Процесс не может получить доступ к файлу, так как этот файл занят другим процессом");
            e.printStackTrace();
        }

        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            // Начинаем со страницы магазина
            HtmlPage page = webClient.getPage("https://tentorium.ru/internet-magazin");
            // Страницы подкатегорий
            HtmlPage pages, productpage;

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
//                    HtmlImage image = ((HtmlImage) htmlItem.getFirstByXPath(".//img")); // Картинка
                    //get links
                    HtmlAnchor link = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a")); // URL для перехода на второй уровень
                    this.catalogTitle = title.asText();

                    try {
                        if (writer != null) {
                            writer.create(this.catalogTitle); // this.catalogTitle - передаем название каталогов, чтобы папоки имели такое-же название
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*
                    * Второй уровень - товары
                     */
                    pages = link.click();
                    List<HtmlElement> products = pages.getByXPath(".//div[@class='loop-product col-lg-3 col-md-4 col-sm-6 col-xs-12']");
                    for (HtmlElement product : products) {
                        HtmlAnchor productLink = ((HtmlAnchor) product.getFirstByXPath(".//a")); // URL для перехода на третий уровень
//                        HtmlElement productTitle = ((HtmlElement) product.getFirstByXPath(".//h3")); // Название
                        HtmlElement productDescription = ((HtmlElement) product.getFirstByXPath(".//p[@class='excerpt']")); // Описание
//                        HtmlImage productImage = ((HtmlImage) product.getFirstByXPath(".//img")); // Картинка
                        this.productShortDescription = productDescription.asText().replace(";", ".").replace("\n", "").replace("\r", ""); // удаляем переходы на новую строку

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
                                File imageFile = new File(filePath + title.asText().replace("/", " и") + "/" + itemTitle.asText() + ".png");

                                this.productTitle = itemTitle.asText();
                                this.productDescription = itemDescription.asText().replace(";", ".").replace("\n", "").replace("\r", ""); // удаляем переходы на новую строку
                                this.productAttributes = attributes.replace("\n", "").replace("\r", "");
                                if (itemIngredients == null) {
                                    System.out.println("У продукта \"" + itemTitle.asText() + "\" нет ингредиентов");
                                    this.productIngredients = "none";
                                } else {
                                    this.productIngredients = itemIngredients.asText().replace(";", ".").replaceAll("   ", ", ").replace("\n", "").replace("\r", ", ");
                                }
                                if (itemUse == null) {
                                    System.out.println("У продукта \"" + itemTitle.asText() + "\" нет инструкции");
                                    this.productUse = "none";
                                } else {
                                    this.productUse = itemUse.asText().replace(";", ".").replace("\n", "").replace("\r", "");
                                }
                                if (itemDocumentation == null) {
                                    System.out.println("У продукта \"" + itemTitle.asText() + "\" нет документации");
                                    this.productDocumentation = "none";
                                } else {
                                    this.productDocumentation = itemDocumentation.getAttribute("href");
                                }
                                this.productImagePath = imageFile.getPath();

                                writer.append(this.catalogTitle,
                                        this.productTitle,
                                        this.productShortDescription,
                                        this.productDescription,
                                        this.productAttributes,
                                        this.productIngredients,
                                        this.productUse,
                                        this.productDocumentation,
                                        itemImage,
                                        this.delimiter);
                            } catch (NullPointerException e) {
                                System.out.println("Какая-то хрень c продуктом " + itemTitle.asText());
                            }
                        }
                    }
                }
            }
            writer.close();
            webClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
