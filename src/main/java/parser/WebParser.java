package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import model.Product;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebParser {
    private Document document;
    private List<String> links;
    private Map<String, Product> productMap;
    private static final String CONTEXT_PATH = "https://www.aboutyou.de";

    public WebParser(String url) {
        this.links = Collections.emptyList();
        this.productMap = new HashMap<>();

        Optional<Document> document = getDocumentFromUrl(url);
        document.ifPresent(value -> this.document = value);
    }

    public void parse() {
        links = parsePageIntoProductLinks();
        int i = 1;
        for (String link : links) {
            System.out.println("link got[" + i++ + "]: " + CONTEXT_PATH + link);
            //todo: or call method here
        }
    }

    private List<String> parsePageIntoProductLinks() {
        String query = "#app > section "
                + "> section.RowSection-gi00w3-0.StaticSplitSection__StyledRowSection-sc-19tq43e-2.gEzrfo "
                + "> div.StaticSplitSection__SectionEnd-sc-19tq43e-1.chMWOM "
                + "> div:nth-child(4) > div";
        String productRowClass = "MixedTileRowContainer-sc-1n50fuf-0 hzvjNO";
        Elements mainContainerRows = document.body().selectFirst(query).children();

        List<String> listOfLinks = new ArrayList<>();
        for (Element row : mainContainerRows) {
            if (row.hasClass(productRowClass)) {
                for (Element productTile : row.children()) {
                    listOfLinks.add(productTile.attr("href"));
                    //todo: optional: call recursive funcion here or in parse() method
                }
            }
        }
        return listOfLinks;
    }

    private Product parseProductFromLink(String link) {
        Optional<Document> document = getDocumentFromUrl(link);
        document.ifPresent(value -> this.document = value);
        return new Product();
    }

    private Optional<Document> getDocumentFromUrl(String url) {
        try {
            return Optional.of(Jsoup.connect(url)
                    .userAgent("Chrome/51.0.2704.103")
                    .timeout(3000)
                    .method(Connection.Method.GET)
                    .execute()
                    .parse());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
