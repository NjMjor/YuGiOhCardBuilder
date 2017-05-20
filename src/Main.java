import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.annotation.Resource;

/**
 * Created by mlade on 16-May-17.
 */

public class Main extends Application
{
    @FXML
    private TextField cardName;

    @FXML
    private Button Confirm;

    @FXML
    private Text tcgMessage;

    @Override public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
        primaryStage.setTitle("Yu-Gi-Oh! Card Builder");
        primaryStage.setScene(new Scene(root, 454, 288));
        primaryStage.show();
    }



    public void confirmButtonClicked()
    {
        boolean success;
        Card card = null;
        try
        {
            card = new Card(cardName.getText());
            success = true;
        }

        catch(Exception exc)
        {
            success = false;
        }

        if(success)
        {
            cardName.setStyle("-fx-text-fill: green;");
            TCG t = new TCG();

            try
            {
                t.generateCard(card);
                tcgMessage.setStyle("-fx-opacity: 0;");
            }
            catch(Exception e)
            {
                tcgMessage.setStyle("-fx-opacity: 1; -fx-fill: red");
            }
        }
        else
        {
            cardName.setText("Error Fetching Data");
            cardName.setStyle("-fx-text-fill: red;");
        }



    }

    public void cardNameClicked()
    {
        cardName.setStyle("-fx-text-fill: black;");
        cardName.clear();
    }

    public static void main (String args[]) throws Exception
    {
        launch(args);

        //String tcgPath = System.getProperty("user.home") + "\\Desktop\\TCG\\TCGEditor.exe";
        //Process tcg = Runtime.getRuntime().exec(tcgPath);
    }
}
