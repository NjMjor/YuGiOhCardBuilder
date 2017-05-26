import javafx.application.Application;

import javafx.application.Platform;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;

import javafx.fxml.FXML;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;

/**
 * Created by mlade on 16-May-17.
 */

class Preferences implements Serializable
{
    public String cardDirectory;

    Preferences(String directory)
    {
        this.cardDirectory = directory;
    }

}

public class Main extends Application
{
    private static String cardDirectory;

    @FXML
    private TextField cardName;

    private static boolean  success = false;

    class CardThread extends Thread
    {
        Card card = null;
        @Override
        public void run()
        {
            try
            {
                card = new Card(cardName.getText());
                success = true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            //UPDATES GUI FROM ANOTHER THREAD AND I DONT GIVE A SHIT HOW IT DOES SO
            Platform.runLater(new Runnable()
            {
                public void run()
                {
                    if (success == true) {
                        Group root2 = new Group();

                        AnchorPane ap = new AnchorPane();
                        root2.getChildren().add(ap);

                        Text cardName = new Text();

                        int fontSize = 87;

                        for (int i = 0; i < card.getName().length() - 14; i++) {
                            fontSize -= 1;
                        }

                        if (card.getName().length() > 28) {
                            fontSize -= 14;
                        } else if (card.getName().length() > 25) {
                            fontSize -= 12;
                        } else if (card.getName().length() > 22) {
                            fontSize -= 10;
                        } else if (card.getName().length() > 20) {
                            fontSize -= 8;
                        }

                        cardName.setStyle("-fx-font-size: " + String.valueOf(fontSize));


                        cardName.setText(card.getName());

                        if (card.getCardType().equals("Monster")) {
                            cardName.setId("card-monster-name");
                        } else {
                            cardName.setId("card-spell-name");
                        }


                        cardName.setX(75);
                        cardName.setY(120);

                        try {
                            generateTemplate(card, ap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Image cardArtwork = new Image("file:" + System.getProperty("user.home") + "\\Desktop\\Pictures\\" + card.getName() + ".png", 619, 619, false, true);
                        ImageView artwork = new ImageView(cardArtwork);

                        artwork.setX(97);
                        artwork.setY(217);

                        ap.getChildren().addAll(artwork, cardName);

                        Scene scene = new Scene(root2, 454, 1000);
                        scene.getStylesheets().add("style.css");
                        WritableImage wi = ap.snapshot(null, null);

                        //////////////
                        // SEPARATE THREAD FOR SAVING THE IMAGE, LAG FREE CASH ME OUSSIDE HOWBOW DAT
                        //////////////

                        Task<Void> task = new Task<Void>()
                        {
                            @Override
                            protected Void call() throws Exception
                            {

                                ImageIO.write(SwingFXUtils.fromFXImage(wi, null), "png", new File(cardDirectory+"\\" + card.getName() + ".png"));

                                File file = new File(System.getProperty("user.home") + "\\Desktop\\Pictures\\" + card.getName() + ".png");
                                file.delete();

                                success = false;
                                return null;
                            }
                        };

                        Thread th = new Thread(task);
                        th.setDaemon(true);
                        th.start();

                        //////////////
                        //////////////

                    } else {
                        cardName.setText("Error Fetching Data");
                        cardName.setStyle("-fx-text-fill: red;");
                    }
                }
            });
        }
    }

    @Override public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
        primaryStage.setTitle("Yu-Gi-Oh! Card Builder");
        primaryStage.setScene(new Scene(root, 454, 288));
        primaryStage.show();

        Preferences preferences;

        try {
            FileInputStream fileIn = new FileInputStream("preferences.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            preferences = (Preferences) in.readObject();

            in.close();
            fileIn.close();

            cardDirectory = preferences.cardDirectory;
        }
        catch(Exception e)
        {
            cardDirectory = System.getProperty("user.home") + "\\Desktop";
        }
    }


    public void confirmButtonClicked() throws Exception
    {
        CardThread ct = new CardThread();

        ct.start();
    }

    private void generateTemplate(Card card, AnchorPane ap) throws Exception
    {
        if(card.getCardType().equals("Monster"))
            generateMonsterTemplate(card, ap);

        else if(card.getCardType().equals("Spell"))
                generateSpellTemplate(card, ap);

        else
            generateTrapTemplate(card, ap);

    }

    private void generateSpellTemplate(Card card, AnchorPane ap) throws Exception
    {
        Image baseTemplate = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"Spell"+".png");
        ImageView img = new ImageView(baseTemplate);

        ImageView txt = new ImageView(); //Equip, Normal, Continuous...
        txt.setY(156);

        if(card.getProperty().equals("Normal")) {
            txt.setImage(new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"NormalSpell"+".png"));
            txt.setX(455);
        }
        else
        {
            txt.setX(400);
            txt.setImage(new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+card.getProperty()+".png"));
        }


        ap.getChildren().addAll(img, txt);
        generateDescription(card, ap);
    }

    private void generateTrapTemplate(Card card, AnchorPane ap)
    {
        Image baseTemplate = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"Trap"+".png");
        ImageView img = new ImageView(baseTemplate);

        ImageView txt = new ImageView(); //Equip, Normal, Continuous...
        txt.setY(156);

        if(card.getProperty().equals("Normal")) {
            txt.setImage(new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"NormalTrap"+".png"));
            txt.setX(455);
        }
        else
        {
            txt.setX(410);
            txt.setImage(new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+card.getProperty()+".png"));
        }


        ap.getChildren().addAll(img, txt);
        generateDescription(card, ap);
    }

    private void generateLevels(Card card, AnchorPane ap) //Levels as in the levels of a given monster
    {
        int numberOfLevels = Integer.parseInt(card.getLevel());

        int firstLevelXPosition = 680;

        Image lvl = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"Level"+".png");
        ImageView level = new ImageView(lvl);
        level.setFitWidth(45);
        level.setFitHeight(45);

        level.setY(153);
        level.setX(firstLevelXPosition);

        ap.getChildren().add(level); //Append the first level, the one most to the right

        for(int i=0; i<numberOfLevels - 1; i++) //Append the rest of the levels from right to left
        {
            level = new ImageView(lvl);
            level.setY(153);

            level.setFitWidth(45);
            level.setFitHeight(45);

            level.setX(firstLevelXPosition-=47);
            ap.getChildren().add(level);
        }
    }

    private void generateAttribute(Card card, AnchorPane ap)
    {
        Image image = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+card.getAttribute()+".png");
        ImageView img = new ImageView(image);

        img.setX(662);
        img.setY(55);

        ap.getChildren().add(img);
    }

    private void generateAtkDef(Card card, AnchorPane ap)
    {
        String atkdefwithslash = card.getAtkDef().replaceAll(" / "," ");

        String atkdef[] = atkdefwithslash.split(" "); //atkdef[0] = atk, atkdef[1] = def

        Text atk = new Text(atkdef[0]);
        Text def = new Text(atkdef[1]);

       atk.setId("card-atkdef");
       def.setId("card-atkdef");

        atk.setX(505);
        atk.setY(1105);

        def.setX(670);
        def.setY(1105);

        ap.getChildren().addAll(atk,def);
    }

    private void generateMonsterTemplate(Card card, AnchorPane ap)
    {
       Image baseTemplate;

        if(card.getType().contains("Effect"))
        {
            baseTemplate = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"MonsterEffect"+".png");
        }
        else if(card.getType().contains("Fusion"))
        {
            baseTemplate = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"MonsterFusion"+".png");
        }
        else if(card.getType().contains("Ritual"))
        {
            baseTemplate = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"MonsterRitual"+".png");
        }
        else
        {
            baseTemplate = new Image("file:"+System.getProperty("user.home")+"\\Desktop\\Pictures\\"+"MonsterNormal"+".png");
        }

        ImageView img = new ImageView(baseTemplate);

        ap.getChildren().add(img);

        generateAtkDef(card,ap);
        generateTypes(card,ap);
        generateDescription(card,ap);

        generateLevels(card, ap);
        generateAttribute(card, ap);
    }

    private void generateDescription(Card card, AnchorPane ap) {
        Text cardDescription = new Text(card.getDescription().replaceAll(" ", "  "));
        cardDescription.setX(75);
        cardDescription.setTextAlignment(TextAlignment.JUSTIFY);
        cardDescription.setLineSpacing(-1);
        if (card.getCardType().equals("Monster")) {
            if (card.getType().contains("Effect") || card.getType().contains("Ritual") || card.getType().contains("Fusion")) {
                cardDescription.setId("card-desc-effect");
            }

            cardDescription.setId("card-desc-effect");
            cardDescription.setY(945);
        } else //If it's a magic or a trap card, the description has to be higher because there are no types, [dragon] [spellcaster] etc.
        {
            cardDescription.setId("card-desc-effect");
            cardDescription.setY(918);
        }

        cardDescription.setWrappingWidth(671);


        double numberOfLetters = cardDescription.getText().length();

        int fontSize = 0;

        if (card.getCardType().equals("Monster")) {
            fontSize = 25;
            if (numberOfLetters > 350)
                cardDescription.setLineSpacing(-1.5);
        } else {
            fontSize = 29;
        }

        for (int i = 0; i < numberOfLetters / 100; i++) {
            fontSize -= 1;
        }

        cardDescription.setStyle("-fx-font-size: " + String.valueOf(fontSize));

        ap.getChildren().add(cardDescription);
    }

    private void generateTypes(Card card, AnchorPane ap)
    {
        Text types = new Text("[ "+card.getType()+" ]");

        types.setId("card-types");

        types.setY(918);
        types.setX(75);

        ap.getChildren().addAll(types);
    }

    public void chooseDirectory() throws Exception
    {
        final DirectoryChooser dc = new DirectoryChooser();

        final File selectedDirectory = dc.showDialog(new Stage());

        if(selectedDirectory != null) {
            cardDirectory = selectedDirectory.getAbsolutePath();

            FileOutputStream fileOut = new FileOutputStream("preferences.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            Preferences pf = new Preferences(cardDirectory);

             out.writeObject(pf);
            out.close();

            fileOut.close();
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
    }
}
