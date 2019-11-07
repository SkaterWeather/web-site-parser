import service.ProductToJson;
import service.WebParser;

public class Main {
    public static void main(String[] args){
        WebParser parser = new WebParser();
        parser.parse();
        ProductToJson.convertIntoFile();
    }
}
