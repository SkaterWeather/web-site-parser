import parser.WebParser;

public class Main {
    private static final String PARSE_URL = "https://www.aboutyou.de/maenner/bekleidung";

    public static void main(String[] args) {
        WebParser parser = new WebParser(PARSE_URL);
        parser.parse();
    }
}
