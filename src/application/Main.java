package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;

public class Main extends Application {

	public static List<Entry<String, Integer>> listSortedUniqueWordsAndCount;

	public static void main(String[] args) throws IOException {
		// read poem from file to list
		File file = new File("TheRavenPoemWithHTMLTags.txt");
		List<String> poemList = readPoem(file);
		
		// create Map to store words and count of how many times the words appear
		Map<String, Integer> mapUniqueWordsAndCount = uniqueWords(poemList);
				
		// establish sorted List of unique words with count
		listSortedUniqueWordsAndCount = sortMapUniqueWords(mapUniqueWordsAndCount);
				
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.setTitle("My Title");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static List<String> readPoem(File file) throws IOException {
		
		// read in txt document
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		// create variable to read line
		String line = null;
		
		// create variable to parse lines by word and store words in List
		List<String> words = new LinkedList<>();
		List<String> wordList = new ArrayList<>();
				
		// loop through BufferedReader by reading each line to a string
		while((line = br.readLine()) != null) {
			// if line read from BufferedReader is not an empty line,
			// parse line into words and add to List of words
			if(!(line.isBlank())) {
				// remove html tags and punctuation from txt document
								
				words = Arrays.asList(line.toUpperCase().split("\\s+"));
				for(int i = 0; i < words.size(); i++) {
					if(words.get(i) !=  "") {
						wordList.add(words.get(i));
					}					
				}
			}
		}
		
		br.close();
		
		// create variable to parse poem from document
		String poem = "";
		String search1 = "<P>", search2 = "</P>";
		int sub1 = 0, sub2 = 0;

		// search wordList for p tags and concat to poem as string
		for(int i = 0; i < wordList.size(); i++) {
			
			// search for open p tags and find index for subList start
			if(wordList.get(i).equals(search1)) {
				sub1 = i;
			}
			
			//search for close p tags and find index for subList end
			if(wordList.get(i).equals(search2)) {
				sub2 = i;
			}
			
			//concat poem to variable from wordList
			if(sub1 > 0 && sub2 > 0) {
				poem += wordList.subList(sub1 + 1, sub2 - 1).toString().replaceAll("\\p{IsPunctuation}", "").replaceAll("<.*?>", "");
				sub1 = 0;
				sub2 = 0;
			}
		}
		
		// establish poemList to take poem in as List
		List<String> poemList = Arrays.asList(poem.toUpperCase().split("\\s+"));
		return poemList;
		
	}
	
	public static Map<String, Integer> uniqueWords(List<String> poemList) {
		
		// create List to store unique words
		List<String> uniqueWordList = new ArrayList<String>();
		
		// loop through wordList to search for unique words
		// store unique words in uniqueWordList
		for(int i = 0; i < poemList.size(); i++) {
			String temp;
			if(!uniqueWordList.contains(poemList.get(i))) {
				temp = poemList.get(i);
				uniqueWordList.add(temp);
			}
		}

		// create Map to store words and count of how many times the words appear
		Map<String, Integer> mapUniqueWordsAndCount = new TreeMap<>(Collections.reverseOrder());
		
		// loop through uniqueWordList
		for(int i = 0; i < uniqueWordList.size(); i++) {
			int count = 0;
			String uniqueWord = uniqueWordList.get(i);
			
			// loop through wordList
			for(int j = 0; j < poemList.size(); j++) {
				String word = poemList.get(j);
				
				// compare each uniqueWordList to each word in wordList
				// count each time the unique word appears in the document
				if(word.equals(uniqueWord)) {
					count++;
				}
			}
			
			// store unique words and how many times they appear in the document as key and value
			mapUniqueWordsAndCount.put(uniqueWord, count);			
		}
		return mapUniqueWordsAndCount;	
	}
	
	public static List<Entry<String, Integer>> sortMapUniqueWords(Map<String, Integer> mapUniqueWordsAndCount)	{
		
		// establish sorted List of unique words with count
		List<Entry<String, Integer>> listSortedUniqueWordsAndCount = new ArrayList<>(mapUniqueWordsAndCount.entrySet());
		listSortedUniqueWordsAndCount.sort(Entry.comparingByValue(Comparator.reverseOrder()));
		return listSortedUniqueWordsAndCount;
		
	}
}
