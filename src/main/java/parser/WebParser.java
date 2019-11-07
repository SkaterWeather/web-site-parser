package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import model.Product;
import storage.Storage;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

// problem link: https://www.aboutyou.de/p/nike-sportswear/sweatshirt-club-4362842

public class WebParser {
    private List<String> links;
    private int totalRequests;

    //https://www.aboutyou.de/maenner/bekleidung
    private static final String PARSE_URL = "/maenner/bekleidung";
    private static final String CONTEXT_PATH = "https://www.aboutyou.de";

    public WebParser() {
        this.links = Collections.emptyList();
        this.totalRequests = 0;
    }

    public void parse() {
        links = parsePageIntoProductLinks();
        int i = 1;
        for (String link : links) {
            System.out.println("link got[" + i++ + "]: " + CONTEXT_PATH + link);
            //todo: or call method here
            parseProductFromLink(link);
        }
        System.out.println("\nTOTAL REQUESTS: " + totalRequests);
        System.out.println("TOTAL PRODUCTS AMOUNT: " + Storage.productMap.size());
    }

    private List<String> parsePageIntoProductLinks() {
        Optional<Document> document = getDocumentFromUrl(PARSE_URL);
        String query = "#app > section "
                + "> section.RowSection-gi00w3-0.StaticSplitSection__StyledRowSection-sc-19tq43e-2.gEzrfo "
                + "> div.StaticSplitSection__SectionEnd-sc-19tq43e-1.chMWOM "
                + "> div:nth-child(4) > div";
        String productRowClass = "MixedTileRowContainer-sc-1n50fuf-0 hzvjNO";
        Elements mainContainerRows = document.get().body().selectFirst(query).children();

        List<String> listOfLinks = new ArrayList<>();
        for (Element row : mainContainerRows) {
            if (row.hasClass(productRowClass)) {
                for (Element productTile : row.children()) {
                    listOfLinks.add(productTile.attr("href"));
                    //todo: optional: call recursive funcion here or in parseProduct() method
                }
            }
        }
        return listOfLinks;
    }

    private void parseProductFromLink(String link) {
        if (Storage.productMap.containsKey(link)) {
            return;
        }

        Optional<Document> document = getDocumentFromUrl(link);
        String queryForName = "#app > section > section:nth-child(4) > section > div.StaticSplitSection__SectionEnd-sc-19tq43e-1.hlmeZf > div > div > div > div.BuyBoxConnected__StyledBrandAndProductName-sc-1ybtkva-0.koieQZ > div.BrandAndProductName__ProductName-iay39c-1.fgjQqH";
        String queryForPrice = "#app > section > section:nth-child(4) > section > div.StaticSplitSection__SectionEnd-sc-19tq43e-1.hlmeZf > div > div > div > div.BuyBox__StyledPriceBoxExtended-sc-12rq7nw-0.fAMDmm.PriceBoxExtended__Wrapper-sc-1t8547r-1.fWALgU > div.PriceBoxExtended__StyledPriceBox-sc-1t8547r-0.iogkTg";

        String queryForBrand = "#app > section > section:nth-child(4) > section > div.StaticSplitSection__SectionEnd-sc-19tq43e-1.hlmeZf > div > div > div > div.BuyBoxConnected__StyledBrandAndProductName-sc-1ybtkva-0.koieQZ > div:nth-child(1) > a > img";
        String queryForColorsList = "#app > section > section:nth-child(4) > section > div.StaticSplitSection__SectionEnd-sc-19tq43e-1.hlmeZf > div > div > div > div.BuyBox__OverlayWrapper-sc-12rq7nw-3.bnzdui > div.ThumbnailsList__FlexWrap-sc-1befil5-0.gkIDsC";

        Product product = new Product();

        //setArticle
        Element productDetailedBox = document.get().body().selectFirst("#app > section > section > div").attr("data-test-id", "ProductDetailBox");
        Elements listBelow = productDetailedBox.children().first().children().last().children();

        for (Element panel : listBelow) {
            Element productDetails = panel.children().first().children().first().getElementsByAttributeValueContaining("data-test-id", "ProductDetails").first();
            if (productDetails != null) {
                List<Node> articleNodes = productDetails.children().first().children().last().childNodes();
                product.setArticleID(articleNodes.get(articleNodes.size() - 1).toString());
            }
        }

        //setName
        product.setName(document.get().body().select(queryForName).text());

        //setBrand
        String brandName = document.get().body().selectFirst(queryForBrand).attributes().get("alt");
        product.setBrand(brandName);

        //setPrice
        Elements priceField = document.get().body().selectFirst(queryForPrice).children();
        if (priceField.size() == 2) {
            product.setBasePrice(priceField.get(1).text());
            product.setSalePrice(priceField.get(0).text());
        } else {
            product.setBasePrice(priceField.first().text());
            product.setSalePrice("-");
        }

        //setColor
        Elements colorLinks = document.get().body().selectFirst(queryForColorsList).select("a");
        product.setColor(colorLinks.first().text());

        Storage.productMap.put(link, product);
        System.out.println("DONE > PRODUCT CREATED");

        //recursively get other color versions of that product
        for (Element colorLink : colorLinks) {
            parseProductFromLink(colorLink.attr("href"));
        }
    }

    private Optional<Document> getDocumentFromUrl(String url) {
        try {
            //todo: make increment after parse() , don't use Optional<>
            this.totalRequests++;
            return Optional.of(Jsoup.connect(CONTEXT_PATH + url)
                    .userAgent("Chrome/51.0.2704.103")
                    .cookie("auth", "token")
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
