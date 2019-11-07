package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Product;
import storage.Storage;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebParser {
    private List<String> productLinksFromMainPage;
    private ParserHelper parserHelper;
    private int totalRequests;
    private static final String PARSE_URL = "/maenner/bekleidung";
    private static final String CONTEXT_PATH = "https://www.aboutyou.de";

    public WebParser() {
        this.productLinksFromMainPage = new ArrayList<>();
        this.parserHelper = new ParserHelper();
        this.totalRequests = 0;
    }

    public void parse() {
        System.out.print("Process: ");
        parsePageIntoProductLinks();
        for (String link : productLinksFromMainPage) {
            parseProductFromLink(link);
            System.out.print("#");
        }
        System.out.println(" Done");
        System.out.println("\nTOTAL REQUESTS: " + totalRequests);
        System.out.println("TOTAL PRODUCTS EXTRACTED: " + Storage.productMap.size());
    }

    private void parsePageIntoProductLinks() {
        Document document = getDocumentFromUrl(PARSE_URL).orElseThrow();
        String productRowClass = "MixedTileRowContainer-sc-1n50fuf-0 hzvjNO";

        Elements mainContainerRows = parserHelper.getMainContainerRows(document);
        for (Element row : mainContainerRows) {
            if (row.hasClass(productRowClass)) {
                for (Element productTile : row.children()) {
                    //List productLinksFromMainPage is redundant,
                    //We can already call here parseProductFromLink()
                    productLinksFromMainPage.add(productTile.attr("href"));
                }
            }
        }
    }

    //recursive function
    private void parseProductFromLink(String link) {
        //exit condition
        if (Storage.productMap.containsKey(link)) {
            return;
        }
        Document document = getDocumentFromUrl(link).orElseThrow();

        Product product = new Product();
        product.setArticleID(parserHelper.getProductArticle(document));
        product.setName(parserHelper.getProductName(document));
        product.setBrand(parserHelper.getProductBrand(document));
        product.setBasePrice(parserHelper.getProductBasePrice(document));
        product.setSalePrice(parserHelper.getProductSalePrice(document));

        Elements colorLinks = parserHelper.getProductColorLinks(document);
        product.setColor(colorLinks.first().text());
        Storage.productMap.put(link, product);

        //recursively get and parse other color versions of that product
        for (Element colorLink : colorLinks) {
            parseProductFromLink(colorLink.attr("href"));
        }
    }

    private Optional<Document> getDocumentFromUrl(String url) {
        try {
            Document document = Jsoup.connect(CONTEXT_PATH + url)
                    .userAgent("Chrome/51.0.2704.103")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .method(Connection.Method.GET)
                    .execute()
                    .parse();
            this.totalRequests++;
            return Optional.of(document);
        } catch (IOException e) {
            System.out.println("Can't connect to " + CONTEXT_PATH + url);
            System.out.println("Please try again!");
        }
        return Optional.empty();
    }
}
