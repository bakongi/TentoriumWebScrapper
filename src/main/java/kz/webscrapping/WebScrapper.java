package kz.webscrapping;

/**
 *  I know that this code is *****
 * Created by KBadashvili on 006 06.03.17.
 */
public class WebScrapper {

    public static void main(String[] args) throws Exception {
        Scrapper scrapper = new Scrapper();
        scrapper.setFilePath("D:/Java/Tentorium/");
        scrapper.setFileName("ExportedItems" + ".csv");
        scrapper.setEncoding("Cp1251");
        scrapper.run();
    }
}
