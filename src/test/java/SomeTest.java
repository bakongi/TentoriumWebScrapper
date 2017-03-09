import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by KBadashvili on 006 06.03.17.
 */
public class SomeTest {
    private WebClient webClient;

    @Before
    public void init() throws Exception {
        webClient = new WebClient();
    }

    @After
    public void close() throws Exception {
        webClient.close();
    }

    @Test
    public void givenBaeldungArchive_whenRetrievingArticle_thenHasH1()
            throws Exception {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        String url = "http://www.baeldung.com/full_archive";
        HtmlPage page = webClient.getPage(url);
        String xpath = "(//ul[@class='car-monthlisting']/li)[1]/a";
        HtmlAnchor latestPostLink
                = (HtmlAnchor) page.getByXPath(xpath).get(0);
        HtmlPage postPage = latestPostLink.click();

        List<HtmlHeading1> h1
                = postPage.getByXPath("//h1");

        Assert.assertTrue(h1.size() > 0);

        for (HtmlHeading1 h : h1) {
            System.out.println(h.toString());
        }
    }
}
