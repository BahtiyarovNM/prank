import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by kolya on 09.04.17.
 */
public class Matrix {
    private int SIZE = 100;
    private Set<String> parsed = new HashSet<>();
    private int[][] matrix = new int[SIZE][SIZE];
    private Map<Integer, String> pages = new HashMap<>();

    public Set<String> add(String page, Set<String> linksOnPage) {
        parsed.add(page);
        Optional<Integer> indexOption = getIndex(page);
        if (isFull() && !indexOption.isPresent()) return new HashSet<>();
        int index = indexOption.orElseGet(() -> addPage(page));
        Set<String> newSubpages = new HashSet<>();
        for (String subpage : linksOnPage) {
            Optional<Integer> subIndexOpt = getIndex(subpage);
            if (isFull() && !subIndexOpt.isPresent()) continue;
            int subIndex = subIndexOpt.orElseGet(() -> addPage(subpage));
            matrix[index][subIndex] = 1;
            if (!subIndexOpt.isPresent()) {
                newSubpages.add(subpage);
            }
        }

        return newSubpages;
    }

    private Integer addPage(String page) {
        int size = pages.size();
        pages.put(size, page);
        matrix[size] = new int[SIZE];
        return size;
    }

    public boolean isExist(String page) {
        return pages.values().stream().anyMatch(p -> p.equals(page));
    }

    public boolean isParsed(String page) {
        return parsed.contains(page);
    }

    public boolean isFull() {
        return pages.size() >= SIZE;
    }

    public void print() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("matrix.txt"));
        } catch (FileNotFoundException e) {
            return;
        }
        for (Integer key : pages.keySet().stream().sorted().collect(Collectors.toList())) {
            pw.println(key + " " + pages.get(key));
        }
        StringBuilder builder = new StringBuilder();
        for (int[] row : matrix) {
            for (int column : row) {
                pw.write(column + " ");
            }
            pw.println();
        }
        pw.close();
    }

    public List<Integer> getTo(int index) {
        List<Integer> to = new ArrayList<>();
        IntStream.range(0, SIZE).forEach(row -> {
            if (matrix[row][index] == 1) {
                to.add(row);
            }
        });
        return to;
    }

    public List<Integer> getFrom(int index) {
        List<Integer> from = new ArrayList<>();
        for (int col : matrix[index]) {
            if (matrix[index][col] == 1) {
                from.add(col);
            }
        }
        return from;
    }

    public int getSize() {
        return this.SIZE;
    }

    public String getPage(int index) {
        return pages.get(index);
    }

    private Optional<Integer> getIndex(String page) {
        return pages.entrySet().stream()
                .filter(p -> p.getValue().equals(page))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
