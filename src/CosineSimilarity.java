import java.util.*;
import java.util.stream.*;

public class CosineSimilarity {
    public static String cleanText(String str){
        return str.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
    }

    public static Map<String, Integer> ngrams(String str, int n) {
        List<String> ngrams = new ArrayList<>();
        IntStream.range(0, str.length() - n + 1).forEach(i -> ngrams.add(str.substring(i, i + n)));
        Map<String, Integer> ngramMap = new HashMap<>();
        ngrams.forEach(ngram -> ngramMap.put(ngram, ngramMap.getOrDefault(ngram, 0) + 1));
        ngramMap.keySet().removeIf(k -> k.matches("\\s+"));
        return ngramMap;
    }

    public static double cosineSimilarity(Map<String, Integer> m1, Map<String, Integer> m2) {
        Set<String> intersection = new HashSet<>(m1.keySet());
        intersection.retainAll(m2.keySet());

        double dotProduct = intersection.stream().mapToDouble(i -> m1.get(i) * m2.get(i)).sum();

        double d1 = m1.keySet().stream().mapToDouble(i -> Math.pow(m1.get(i), 2)).sum();
        double d2 = m2.keySet().stream().mapToDouble(i -> Math.pow(m2.get(i), 2)).sum();
        double denominator = Math.sqrt(d1) * Math.sqrt(d2);

        return dotProduct / denominator;
    }

}


