package service;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ParserHelper {
    private String queryForProductBuyBoxWrapper = "#app > section "
            + "> section:nth-child(4) > section "
            + "> div.sc-19tq43e-1.ewlgdA "
            + "> div > div > div ";

    public Elements getMainContainerRows(Document document) {
        String queryForMainContainerGrid = "#app > section "
                + "> section.gi00w3-0.sc-19tq43e-2.kVYixp "
                + "> div.sc-19tq43e-1.fIkTJO "
                + "> div:nth-child(4) > div";
        return document.body().selectFirst(queryForMainContainerGrid).children();
    }

    public String getProductName(Document document) {
        String queryForName = queryForProductBuyBoxWrapper
                + "> div.sc-1ybtkva-0.PQpcS "
                + "> div.iay39c-1.faQObc";
        return document.body().selectFirst(queryForName).text();
    }

    public String getProductBrand(Document document) {
        String queryForBrandImage = queryForProductBuyBoxWrapper
                + "> div.sc-1ybtkva-0.PQpcS "
                + "> div:nth-child(1) > a > img";
         return document.body().selectFirst(queryForBrandImage).attributes().get("alt");
    }

    public String getProductBasePrice(Document document) {
        String queryForPriceField = queryForProductBuyBoxWrapper
                + "> div.sc-12rq7nw-0.hQOIjj.sc-1t8547r-1.iuFFwF"
                + "> div.sc-1t8547r-0.shMKG";
        Elements priceField = document.body().selectFirst(queryForPriceField).children();
        if (priceField.size() == 2) {
            return priceField.get(1).text();
        }
        return priceField.first().text();
    }

    public String getProductSalePrice(Document document) {
        String queryForPriceField = queryForProductBuyBoxWrapper
                + "> div.sc-12rq7nw-0.hQOIjj.sc-1t8547r-1.iuFFwF"
                + "> div.sc-1t8547r-0.shMKG";
        Elements priceField = document.body().selectFirst(queryForPriceField).children();
        if (priceField.size() == 2) {
            return priceField.first().text();
        }
        return "-";
    }

    public Elements getProductColorLinks(Document document) {
        String queryForColorsList = queryForProductBuyBoxWrapper
                + "> div.sc-12rq7nw-3.ivQbqZ"
                + "> div.sc-1befil5-0.eROFI";
        return document.body().selectFirst(queryForColorsList).select("a");
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
