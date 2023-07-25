package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;

public class Main extends Application {

	public static List<Entry<String, Integer>> listSortedUniqueWordsAndCount;

	/**
	 * Main method.
	 * Instantiate File object with file name.
	 * Instantiate List object using readPoem method to parse poem from file.
	 * 
	 * Instantiate Map object using uniqueWords method to parse Key of unique words with Value of number unique words occur in poem.
	 * 
	 * Instantiate List object using sortMapUniqueWords method to sort unique words descending from most frequent.
	 * 
	 * Ask user what size list to console log list at that size.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		createTable();
		
		// read poem from file to list
		File file = new File("TheRavenPoemWithHTMLTags.txt");
		List<String> poemList = readPoem(file);
		
		// create Map to store words and count of how many times the words appear
		uniqueWords(poemList);
				
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
	
	/**
	 * Read poem method.
	 * 
	 * Takes in File object and returns List object.
	 * 	List will contain only the poem in all caps and no punctuation.
	 * 
	 * @param File
	 * @return List
	 * @throws IOException
	 */
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
	
	/**
	 * Unique words method.
	 * 
	 * Takes in List object and insert into database.
	 * 	Key : Unique Words.
	 * 	Value : Unique Word Occurrence.
	 * 
	 * @param List
	 * @return void
	 */
	public static void uniqueWords(List<String> poemList) {
		
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
		
		try {
			Connection conn = getConnection();
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
				PreparedStatement posted = conn.prepareStatement(
						"INSERT INTO word(words, occurrence) "
						+ "Values ('" + uniqueWord + "', '" + count + "')");
				posted.executeUpdate();
			}
			
		} catch (Exception e) {
			System.out.println(e);			
		}
		finally {
			System.out.println("Insert Words and Occurrence Completed");
		}	
	}
	
	/**
	 * Get ArrayList of Top Words from database method.
	 * 
	 * Receive number of words from user interface.
	 * Return ArrayList of indicated number of top words.
	 * 
	 * @param int
	 * @return List
	 */
	public static ArrayList<String> get(int topWords) throws Exception{
		
		
		try {
			Connection conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement(
					"SELECT words, occurrence "
					+ "FROM word "
					+ "ORDER BY occurrence DESC "
					+ "LIMIT " + topWords);
			ResultSet result = statement.executeQuery();
			
			ArrayList<String> array = new ArrayList<String>();
			
			while(result.next()) {
				String temp1 = result.getString("words");
				String temp2 = result.getString("occurrence");
				
				System.out.print(temp1 + " " + temp2 + "\n");
				
				array.add(temp1 + " " + temp2);
			}
			System.out.println("All records have been selected!");
			return array;
		} catch(Exception e) {
			System.out.println(e);
		}		
		return null;
		
	}
	
	/**
	 * Create table to store unique words and counts
	 * 
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
		try {
			Connection conn = getConnection();
			PreparedStatement create = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS word("
					+ "id int NOT NULL AUTO_INCREMENT, "
					+ "words varchar(255), "
					+ "occurrence int, "
					+ "PRIMARY KEY(id))");
			create.executeUpdate();						
		} catch(Exception e) {System.out.println(e);}
		finally {
			System.out.println("Table Created.");
			};
	}
	
	/**
	 * Establish connection to database.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		try {
			String url = "jdbc:mysql://localhost:3306/word_occurrences";
			String username = "cjones";
			String password = "password123";
			
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected");
			return conn;
		} catch(Exception e) {System.out.println(e);}
		
		return null;
		
	}
}
