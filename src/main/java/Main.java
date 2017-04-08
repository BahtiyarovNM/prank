
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by kolya on 07.04.17.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        LinkParser parser = new LinkParser();
        Crawler crawler = new Crawler(parser);
        Matrix matrix = crawler.crawl();
        PageRank pageRank = new PageRank();

        PrintWriter pw = new PrintWriter("pagerank.txt");
        List<Double> ranks = pageRank.calculate(matrix);
        Map<String, Double> result = new HashMap<>();
        IntStream.range(0, ranks.size())
                .forEach(r -> result.put(matrix.getPage(r), ranks.get(r)));
        result.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(e -> {
                    pw.print(e.getKey() + "\t");
                    pw.println(e.getValue());
                });
        pw.println("Execution time: " + pageRank.getTime());

        pw.close();

    }

}
