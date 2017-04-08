import java.util.LinkedList;
import java.util.Set;

/**
 * Created by kolya on 09.04.17.
 */
public class Crawler {
    private LinkParser parser;
    private LinkedList<String> q = new LinkedList<>();

    public Crawler(LinkParser parser) {
        this.parser = parser;
    }

    public Matrix crawl() {
        Matrix matrix = new Matrix();
        q.add("/");
        while (!q.isEmpty()) {
            System.out.println("In queue: " + q.size());
            crawl(q.get(0), matrix);
            q.removeFirst();
        }

        matrix.print();
        return matrix;
    }

    private void crawl(String url, Matrix matrix) {
        if (matrix.isParsed(url) || (matrix.isFull() && !matrix.isExist(url))) return;
        System.out.println("Starting : " + url);
        Set<String> pages = parser.start(url);
        System.out.println("Founded: " + pages.size());
        pages = matrix.add(url, pages);
        q.addAll(pages);
    }
}
