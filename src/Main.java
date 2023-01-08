import java.io.*;
import java.nio.file.Path;
import java.util.stream.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        CosineSimilarity textClassifier = new CosineSimilarity();

        Stream<Path> dataFiles = Files.walk(Paths.get("data"))
                .filter(Files::isRegularFile)
                .filter(p -> !p.toString().equals("data\\mystery.txt"));

        Map<String, String> data = new HashMap<>();

        dataFiles.forEach(p -> {
            String lang = p.getParent().getFileName().toString().substring(5);
            String content = data.getOrDefault(lang, "");
            try {
                content += Files.readAllLines(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            data.put(lang, content);
        });

        Map<String, Map<String, Integer>> ngramMap = new HashMap<>();
        data.forEach(
                (lang, content) -> ngramMap.put(lang,
                textClassifier.ngrams(
                        textClassifier.cleanText(content), 2)
                )
        );

        String mystery = Files.readAllLines(Paths.get("data\\mystery.txt")).toString();
        Map<String, Integer> mysteryNgrams = textClassifier.ngrams(textClassifier.cleanText(mystery), 2);
        Map<String, Double> similarityMap = new HashMap<>();

        ngramMap.forEach((lang, ngrams) -> similarityMap.put(lang, textClassifier.cosineSimilarity(ngrams, mysteryNgrams)));

        System.out.println(similarityMap);
        // get the max value from the map
        double max = similarityMap.values().stream().max(Double::compare).get();
        String lang_code = similarityMap.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .findFirst()
                .get().getKey();

        // convert the language code to a language name
        String lang_name = Arrays.stream(Locale.getISOLanguages()).map(Locale::new)
                .filter(l -> l.getLanguage().equals(lang_code))
                .findFirst()
                .get().getDisplayLanguage();

        System.out.println("The language that the mystery text is written in is: "
                + lang_name
                + " with cosine similarity of "
                + max);
    }
}