import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kolya on 08.04.17.
 */
public class LinkParser {

    private static final String HTTP_PROTOCOL = "http://";
    private static final String HTTPS_PROTOCOL = "https://";
    private static final String SAME_PAGE_ELEM = "#";

    public Set<String> start(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(Constants.SITE_URL + url).get();
        } catch (IOException e) {
            return new HashSet<String>();
        }
        Elements links = doc.select("a[href]");
        return links.stream()
                .map(l -> l.attr("href"))
                .map(l -> l.startsWith("/") ? Constants.SITE_URL + l : l)
                .filter(this::filter)
                .map(l -> {
                    try {
                        return  new URL(l).getPath();
                    } catch (MalformedURLException e) {
                        return  null;
                    }
                })
                .filter(Objects::nonNull)
                .map(l -> l.equals("") ? "/" : l)
                .collect(Collectors.toSet());
    }

    private boolean filter(String check) {
        return check.startsWith(Constants.SITE_URL) || !(check.startsWith(HTTP_PROTOCOL) || check.startsWith(HTTPS_PROTOCOL))
                && !check.contains(SAME_PAGE_ELEM);
    }
}
