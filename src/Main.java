import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by mlade on 16-May-17.
 */

public class Main extends Application
{
    @FXML
    private TextField cardName;

    @Override public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
        primaryStage.setTitle("Yu-Gi-Oh! Card Builder");
        primaryStage.setScene(new Scene(root, 454, 288));
        primaryStage.show();

    }

    public void confirmButtonClicked() throws Exception
    {
        Card card = new Card(cardName.getText());

        TCG t = new TCG();
        t.generateCard(card);
    }

    public static void main (String args[]) throws Exception
    {
        launch(args);

        //String tcgPath = System.getProperty("user.home") + "\\Desktop\\TCG\\TCGEditor.exe";
        //Process tcg = Runtime.getRuntime().exec(tcgPath);
    }
}
