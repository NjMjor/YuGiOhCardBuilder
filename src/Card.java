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

public class Card {
    String yugiWiki = "http://yugioh.wikia.com/wiki/";
    Document doc; //html document of the card from wikipedia yugioh.wikia.com



    //Common fields among all card types
    private String name;
    private String pictureURL;
    private String cardType;
    private String attribute;
    private String description;
    private String limit;

    //Fields for Monster cards
    private String type;
    private String level;
    private String atkDef;


    //Fields for Spell or Trap cards
    private String property;

    public Card(String name) throws Exception
    {
        setName(name);


        doc = Jsoup.connect(yugiWiki + name).get();


        setPictureURL();
        setName(name);
        setCardType();
        setLimit();
        setDescription();

        if(cardType.equals("Monster"))
        {
            setAttribute();
            setType();
            setLevel();
            setAtkDef();
        }
        else if(cardType.equals("Spell") || cardType.equals("Trap"))
        {
            setProperty();
        }
    }

    private void setProperty()
    {
        Element element = doc.select("tr[class=cardtablerow] th a[href=/wiki/Property]").get(0).parent().parent().child(1);

        this.property = element.text();
    }

    private void setName(String name) {
        this.name = name.replaceAll("_", " ");
    }

    private void setCardType() {
        //Gets the html element with specified attributes
        Element element = doc.select("tr[class=cardtablerow] th a[href=/wiki/Card_type] ").get(0).parent().parent().child(1);

        this.cardType = element.text();
    }

    private void setAttribute() {
        //Gets the html element with specified attributes
        Element element = doc.select("tr[class=cardtablerow] th a[href=/wiki/Attribute] ").get(0).parent().parent().child(1);

        this.attribute = element.text();
    }

    private void setType() {
        Element element = doc.select("tr[class=cardtablerow] th a[href=/wiki/Type] ").get(0).parent().parent().child(1);

        this.type = element.text();
    }

    private void setLevel() {
        Element element = doc.select("tr[class=cardtablerow] th a[href=/wiki/Level] ").get(0).parent().parent().child(1);

        this.level = element.text();
    }

    private void setAtkDef()
    {
        Element element = doc.select("tr[class=cardtablerow] th a[href=/wiki/Atk] ").get(0).parent().parent().child(1);

        this.atkDef = element.text();
    }

    private void setLimit()
    {
        Elements elements = doc.select("td[class=cardtablerowdata] a[href=/wiki/Limited], " +
                "td[class=cardtablerowdata] a[href=/wiki/Forbidden], " +
                "td[class=cardtablerowdata] a[href=/wiki/Unlimited]");

        if(elements.size() == 3)
        {
            this.limit = elements.get(1).text(); // 0 is for OCG, 1 is for TCG Advanced, 2 is for TCG Traditional
        }
        else
        {
            this.limit = elements.get(0).text();
        }
    }



    private void setDescription()
    {
        //Gets the html element with specified attributes
        Element element = doc.select("td[class=navbox-list]").get(0);

        this.description = element.text();
    }

    private void setPictureURL() throws Exception
    {
        URL picURL = null;
        BufferedImage img = null;
        Document galleryDoc = null;


        String picHTMLAlts[] =
                {
                        "File:" + name.replaceAll(" |-","") + "-OW.png",
                        "File:" + name.replaceAll(" |-","") + "-TF04-JP-VG.png",
                        "File:" + name.replaceAll(" |-","") + "-TF04-JP-VG.jpg",
                        "File:" + name.replaceAll(" |-","") + "-JP-Anime-DM-NC.png",
                        "File:" + name.replaceAll(" |-","") + "-TF06-JP-VG.png",
                };


        String picElement = null;


        boolean loop = true;
        for (int i =0; i<picHTMLAlts.length && loop; ++i)
        {
            String picHTMLAlt = picHTMLAlts[i];

            try {
                galleryDoc = Jsoup.connect(yugiWiki + picHTMLAlt).get();
            }catch(Exception e){}

            if(galleryDoc != null)
            {
                picElement = picHTMLAlt;
                loop = false;
            }
        }



        Element pic = galleryDoc.select("img[alt="+picElement).first();


        picURL = new URL(pic.attr("src"));


        img = ImageIO.read(picURL);


        this.pictureURL = "Pictures\\" + name + ".png";

        File pictureOutput = new File(pictureURL);


        ImageIO.write(img,"png",pictureOutput);
    }


    public String getName() {
        return name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getCardType() {
        return cardType;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getType() {
        return type;
    }

    public String getLevel() {
        return level;
    }

    public String getAtkDef() {
        return atkDef;
    }

    public String getLimit() {
        return limit;
    }

    public String getDescription() {
        return description;
    }

    public String getProperty() { return property; }
}
