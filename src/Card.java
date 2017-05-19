import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mlade on 16-May-17.
 */

public class Card
{
    Document doc; //html document of the card from wikipedia yugioh.wikia.com

    private String name;
    private String limit;
    private String cardType;
    private String types[];
    private String description;
    private String pictureURL;

    public Card(String name, String pictureURL)
    {

    }

    public Card(String name)
    {
        this.name = name;

        try
        {
            doc = Jsoup.connect("http://yugioh.wikia.com/wiki/"+name).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        setCardType();
        setLimit();
        setDescription();
        setPictureURL();

        /*
        if(cardType.equals("Monster"))
        {
            //TODO: Monster Card needs more fields
            MonsterCard.create(name, limit, cardType, description, doc);
        }
        else
        {
            SpellTrapCard.create(name, limit, cardType, description, doc);
        }
        */
    }

    private void setPictureURL()
    {
        URL picURL = null;
        BufferedImage img = null;
        Document galleryDoc = null;

        String picHTMLAlt = "File:" + name.replaceAll("_|-","") + "-OW.png";
        try
        {
             galleryDoc = Jsoup.connect("http://yugioh.wikia.com/wiki/"+picHTMLAlt).get();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        Element pic = galleryDoc.select("img[alt="+picHTMLAlt).first();

        try
        {
            picURL = new URL(pic.attr("src"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            img = ImageIO.read(picURL);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.pictureURL = System.getProperty("user.home") + "\\Desktop\\Yu-Gi-Oh! artwork\\" + name + ".png";

        File pictureOutput = new File(pictureURL);

        try
        {
            ImageIO.write(img,"png",pictureOutput);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void setCardType()
    {
        //Gets the html element with specified attributes
        Element element = doc.select("td[class=\"cardtablerowdata\"] a[href][title~=Card]").get(0);

        this.cardType = element.text();
    }

    private void setLimit()
    {
        //Gets the html element with specified attributes
        Element element = doc.select("td[class=\"cardtablerowdata\"][style] a[href][title]").get(0);

        this.limit = element.text();
    }

    private void setDescription()
    {
        //Gets the html element with specified attributes
        Element element = doc.select("td[class=navbox-list]").get(0);

        this.description = element.text();
    }
}
