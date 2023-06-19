package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {

	@FXML
	private Label myMessage;
	@FXML
	private Label topList;
	@FXML
	private TextField number;
	@FXML
	private Button clickMe;
	
	int num;
	
	public void submit(ActionEvent event) {
				
		try {
			num = Integer.parseInt(number.getText());
			myMessage.setText("Top " + num);
			int topWords = num;
			
			
			// print n top words by line
			Main.listSortedUniqueWordsAndCount.subList(0, topWords).forEach(System.out::println);
			
			String list = "";
						
			for(int i = 0; i < topWords; i++) {
				list += " '" + Main.listSortedUniqueWordsAndCount.get(i).toString().replaceAll("=", "': ") + "\n";
			}
			
			topList.setText(list);
						
		}
		catch (NumberFormatException e) {
			myMessage.setText("Enter number.");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}
