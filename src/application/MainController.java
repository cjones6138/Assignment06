package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {

	/**
	 * Initiate items for stage
	 */
	@FXML
	private Label myMessage;
	@FXML
	private Label topList;
	@FXML
	private TextField number;
	@FXML
	private Button clickMe;
	
	int num;
	
	/**
	 * Controller main for GUI
	 * 
	 * Receive number of top words from user.
	 * Call get method from Main.
	 * Display top words and count to GUI textbox.
	 * 
	 * @param event
	 */
	public void submit(ActionEvent event) {
				
		try {
			num = Integer.parseInt(number.getText());
			myMessage.setText("Top " + num);
			int topWords = num;
			ArrayList<String> topArray = Main.get(topWords);
			
			String list = "";
						
			for(int i = 0; i < topWords; i++) {
				list += " '" + topArray.get(i) + "\n";
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
