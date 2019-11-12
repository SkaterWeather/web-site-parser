package service;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ParserHelper {

    private Element getProductBuyBox(Document document) {
        return document.body().selectFirst("#app > section > section")
                .attr("data-test-id", "StaticSplitSection")
                .children().first().children().last().children()
                .attr("data-test-id", "BuyBox")
                .first().children().first().children().first();
    }

    public Elements getMainContainerRows(Document document) {
        String queryForMainContainerGrid = "#app > section "
                + "> section.gi00w3-0.sc-19tq43e-2.kVYixp "
                + "> div.sc-19tq43e-1.fIkTJO "
                + "> div:nth-child(4) > div";
        return document.body().selectFirst(queryForMainContainerGrid).children();
    }

    public String getProductName(Document document) {
        return getProductBuyBox(document)
                .children().get(1)
                .children().last().text();
    }

    public String getProductBrand(Document document) {
         return getProductBuyBox(document)
                 .children().get(1)
                 .selectFirst("img").attributes().get("alt");
    }

    public String getProductBasePrice(Document document) {
        return getProductBuyBox(document).children().get(2).select("span").last().text();
    }

    public String getProductSalePrice(Document document) {
        Elements priceField = getProductBuyBox(document).children().get(2).select("span");
        if (priceField.size() == 2) {
            return priceField.first().text();
        }
        return "-";
    }

    public Elements getProductColorLinks(Document document) {
        Elements colorLinks = getProductBuyBox(document)
                .children().get(4)
                .children().first().children();
        //check if there's button -> then there's list
        if (colorLinks.select("button").size() > 0) {
            return colorLinks.first().children().first()
                    .children().first()
                    .children().not("button").first()
                    .children().first().children().select("a");
        }
        return colorLinks;
    }

    public String getProductArticle(Document document) {
        Elements productDetailedBoxTabPanel = document.body()
                .selectFirst("#app > section > section > div")
                .attr("data-test-id", "ProductDetailBox")
                .children().first().children().last().children();
        for (Element panel : productDetailedBoxTabPanel) {
            Element productDetails = panel.children().first().children().first()
                    .getElementsByAttributeValueContaining("data-test-id", "ProductDetails")
                    .first();
            if (productDetails != null) {
                List<Node> articleField = productDetails.children().first().children().last().childNodes();
                return articleField.get(articleField.size() - 1).toString();
            }
        }
        return "-";
    }
}
