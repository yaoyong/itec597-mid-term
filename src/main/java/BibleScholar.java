import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class BibleScholar {

    private static final int COUNT = 12;

    /**
     * <pre>
     * 根据kjv.txt和stopwords.txt，定位出现频率最高的12个单词及最低的12个单词
     * lord:7886
     * god:3823
     * ......
     * </pre>
     */
    public String[] resolve() throws FileNotFoundException{
        Set<String> stopWords = new HashSet<>();
        //读取stopwords.txt
        try (Scanner scw = new Scanner(getClass().getResourceAsStream("main/resources/stopwords.txt"))) {
            while (scw.hasNextLine())
                stopWords.add(scw.nextLine().trim());
        }

        Map<String, Integer> map = new HashMap<>();
        //读取kjv.txt
        try (Scanner scb = new Scanner(getClass().getResourceAsStream("main/resources/kjv.txt"))) {
            while (scb.hasNext()) {
                String word = scb.next();
                int len = word.length(), begin = 0, end = len - 1;
                while (begin < len && !Character.isAlphabetic(word.charAt(begin)))
                    begin++;
                if (begin == len)
                    continue;

                while (end > 0 && !Character.isAlphabetic(word.charAt(end)))
                    end--;
                String converted = word.substring(begin, end + 1).toLowerCase();
                if (stopWords.contains(converted))
                    continue;

                map.put(converted, map.getOrDefault(converted, 0) + 1);
            }
        }

        Comparator<Entry<String, Integer>> comp = (a, b) -> a.getValue().equals(b.getValue()) ?
            a.getKey().compareTo(b.getKey()) : a.getValue() - b.getValue();
        PriorityQueue<Entry<String, Integer>> minPQ = new PriorityQueue<>(COUNT, comp);
        //统计次数最少的list
        PriorityQueue<Entry<String, Integer>> maxPQ = new PriorityQueue<>(COUNT, comp.reversed());
        //统计次数最多的list
        for (Entry<String, Integer> entry : map.entrySet()) {
            if (minPQ.size() < COUNT) {
                minPQ.offer(entry);
            } else if (comp.compare(entry, minPQ.peek()) > 0) {
                minPQ.poll();
                minPQ.offer(entry);
            }

            if (maxPQ.size() < COUNT) {
                maxPQ.offer(entry);
            } else if (comp.compare(entry, maxPQ.peek()) < 0) {
                maxPQ.poll();
                maxPQ.offer(entry);
            }
        }

        final String[] result = new String[COUNT * 2];
        for (int i = COUNT - 1; i >= 0; i--) {
            Entry<String, Integer> entry = minPQ.poll();
            if (entry == null) break;
            result[i] = entry.getKey() + ":" + entry.getValue();
        }

        for (int i = COUNT; i < COUNT * 2; i++) {
            Entry<String, Integer> entry = maxPQ.poll();
            if (entry == null) break;
            result[i] = entry.getKey() + ":" + entry.getValue();
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        final long now = System.currentTimeMillis();
        Stream.of(new BibleScholar().resolve()).forEach(System.out::println);
        System.out.printf("Time Spent: %.2fS%n", (System.currentTimeMillis() - now) / 1000.0);
        //打印执行时间
    }
}
