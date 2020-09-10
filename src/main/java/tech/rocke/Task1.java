package tech.rocke;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Task1 {
    public static final String DEFAULT_FILE_PATH = "src/main/resources/test.txt";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, input a file path and search word: " +
                "(If the path is empty, will be used  the default value )");
        String path = scanner.nextLine();
        System.out.println("Please, input a search word: " +
                "(If the word is empty, will be used the default value )");
        String searchWord = scanner.nextLine();
        if (path.isEmpty()){
            path = DEFAULT_FILE_PATH;
        }if (searchWord.isEmpty()){
            searchWord = "lorem";
        }

        try (WordSearch wordSearch = new WordSearch(path)){
            wordSearch.search(searchWord);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class WordSearch implements AutoCloseable{
    private String fileName;
    private FileReader fileReader;
    private Scanner scanner;

    public WordSearch(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.fileReader = new FileReader(fileName);
        this.scanner = new Scanner(this.fileReader);
    }

    public void search(String word) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        while (scanner.hasNextLine()) {
            Text text = new Text(scanner.nextLine())
                    .validate()
                    .split()
                    .search(word);
            result.append(text.getText()).append("\n");
            count += text.getCount();
        }
        System.out.println(getResult(result.toString(),count));
    }

    public String getResult(String text, int count) {
        String result = "\t SEARCH RESULT: \n" +
                "The given word is present for " + count + " times in the file\n\n" +
                "\t TEXT WITH FOUND WORDS DENOTE WITH <<<  >>>:\n\n" + text + "\n";
        return result;
    }


    @Override
    public void close() throws Exception {
            fileReader.close();
    }
}

class Text{
    private String text;
    private String validatedText;
    private String [] words;
    private int startIndex;
    private int count;

    public Text(String text) {
        this.text = text;
    }

    public Text(Text text) {
        this.text = text.text;
        this.validatedText = text.validatedText;
        this.words = text.words;
        this.startIndex = text.startIndex;
        this.count = text.count;
    }

    private Text createResult(String searchWord){
        startIndex = text.toLowerCase().indexOf(searchWord.toLowerCase(),startIndex);
        int endIndex = startIndex + searchWord.length();
        String foundWord = "<<<" + text.substring(startIndex, endIndex) + ">>>";
        text = text.substring(0, startIndex) + foundWord + text.substring(endIndex);
        startIndex = endIndex + 6;
        return this;
    }

    public Text validate() {
        this.validatedText = this.text.replaceAll("[.,/\\-;:]", "");
        return this;
    }

    public Text split() {
        this.words = this.validatedText.split(" ");
        return this;
    }

    public Text search(String searchWord){
        for (String word : words) {
            if (word.equalsIgnoreCase(searchWord)) {
                createResult(searchWord);
                plusCount();
            }
        }
        clearIndex();
        return new Text(this);
    }

    public void clearIndex(){
        this.startIndex = 0;
    }

    private void plusCount(){
        this.count++;
    }

    public String getText() {
        return text;
    }

    public int getCount() {
        return count;
    }
}
