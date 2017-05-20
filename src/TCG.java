import org.sikuli.basics.Settings;
import org.sikuli.script.Key;
import org.sikuli.script.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by mlade on 17-May-17.
 */

enum Icon
{
    NORMAL,
    CONTINOUS,
    COUNTER,
    EQUIP,
    FIELD,
    QUICKPLAY,
    RITUAL
}

enum Attribute
{
    DARK,
    DIVINE,
    EARTH,
    FIRE,
    LAUGH,
    LIGHT,
    SPELL,
    TRAP,
    WATER,
    WIND
}

public class TCG
{

    private Screen screen;
    private String picturesPath = System.getProperty("user.home")+"\\Desktop\\Pictures\\";

    public TCG()
    {
        Settings.MoveMouseDelay = 0f; // 0 = Instant mouse movement

        screen = new Screen();
    }

    public void generateCard(Card card) throws Exception
    {
        if(card.getCardType().equals("Monster"))
        {
            generateCardMonster(card);
        }
        else if(card.getCardType().equals("Spell") || card.getCardType().equals("Trap"))
        {
            generateCardSpellorTrap(card);
        }

        renderCard(card.getName());

        screen.click(picturesPath+"Reset Fields");

        File file = new File(card.getPictureURL());
        file.delete();
    }

    private void generateCardMonster(Card card) throws Exception
    {
        setBackgroundMonster(card.getType().toUpperCase());

        setAttribute(card.getAttribute().toUpperCase());

        setTitle(card.getName());

        setArtworkFile(card.getName());

        setDescription(card.getDescription());

        setLevel(card.getLevel());

        setSubTypes(card.getType());

        setAtkDef(card.getAtkDef());


        //SKIPS 'Edition', 'Card Set', 'Card Number'
        goToNextField();
        goToNextField();
        goToNextField();

        setLimitation(card.getLimit());
    }

    private void generateCardSpellorTrap(Card card) throws Exception
    {
        setBackgroundSpellorTrap(card.getCardType().toUpperCase());

        goToNextField(); //skips Attribute in TCG

        setTitle(card.getName());

        setArtworkFile(card.getName());

        setDescription(card.getDescription());

        setIcon(card.getProperty());

        setLimitation(card.getLimit());
    }

    private void setBackgroundSpellorTrap(String background) throws Exception
    {
        screen.click(picturesPath+"Background");

        if (background.equals("SPELL"))
        {
            goDown(8);
        }
        else if (background.equals("TRAP"))
        {
            goDown(9);
        }

        pressEnter();
        goToNextField();
    }

    private void setAtkDef(String atkDef) throws Exception
    {
        atkDef = atkDef.replaceAll("/","");

        String types[] = atkDef.split("  ");

        screen.paste(types[0]);

        goToNextField();

        screen.paste(types[1]);

        goToNextField();
    }

    private void setSubTypes(String type) throws Exception
    {
        type = type.replaceAll("/","");

        String types[] = type.split("  ");

        for(String s : types)
        {
            screen.click(picturesPath+"Add SubType");
            screen.paste(s);
        }

        goToNextField();
    }

    public void setLevel(String level) throws Exception
    {
        screen.paste(level);
    }
    public void setBackgroundMonster(String background) throws Exception {
        screen.click(picturesPath+"Background");

        background = background.replaceAll("/","");

        String types[] = background.split("  ");

        boolean ready = false;

        for(int i=0; i<types.length && ready==false; i++)
        {
            if (types[i].equals("FUSION"))
            {
                goDown(2);
                ready = true;
            }
            else if (types[i].equals("RITUAL"))
            {
                goDown(3);
                ready = true;
            }
            else if (types[i].equals("EFFECT"))
            {
                goDown(1);
                ready = true;
            }
        }

        pressEnter();
        goToNextField();
    }



    public void setAttribute(String attribute) throws Exception
    {
       goDown(Attribute.valueOf(attribute.toUpperCase()).ordinal());
       goToNextField();
    }

    public void setTitle(String title) throws Exception
    {
        screen.paste(title);
        goToNextField();
        goToNextField();
    }

    public void setArtworkFile(String artPath) throws Exception
    {
        screen.click(picturesPath+"Three Dots");

        screen.click(picturesPath+"Choose Image");

        screen.wait(picturesPath+"Choose Bar");

        screen.paste(artPath+".png");

        screen.click(picturesPath+"Open");

        screen.wait(picturesPath+"pictureMenu");

        screen.click(picturesPath+"OK");

        goToNextField();
    }

    public void setDescription(String description) throws Exception
    {
        screen.paste(description);
        goToNextField();
    }

    public void setIcon(String icon) throws Exception
    {
        goDown(Icon.valueOf(icon.toUpperCase()).ordinal());


        //SKIPS 'Edition', 'Card Set', 'Card Number'
        goToNextField();
        goToNextField();
        goToNextField();
        goToNextField();
    }

    public void setLimitation(String limitation) throws Exception
    {
        screen.paste(limitation);
    }




    public void renderCard(String name) throws Exception
    {
        screen.click(picturesPath+"Render Card");
        screen.wait(picturesPath+"renderCardMenu");
        screen.click(picturesPath+"Card Path");
        screen.paste(System.getProperty("user.home")+"\\Desktop\\Yu-Gi-Oh! Cards");
        pressEnter();
        screen.click(picturesPath+"File Name");
        screen.paste(name);
        screen.click(picturesPath+"renderCardSave");
        screen.wait(picturesPath+"renderCardSettings");
        screen.click(picturesPath+"renderCardOK");
    }





    //Navigation methods, go Up, go Down, confirm selection, go to the next field

    public void goUp(int up) throws Exception
    {
        for(int i=0; i<up; i++)
        {
            screen.keyDown(Key.UP);
            screen.keyUp(Key.UP);
        }
    }

    public void goDown(int down) throws Exception
    {
        for(int i=0; i<down; i++)
        {
            screen.keyDown(Key.DOWN);
            screen.keyUp(Key.DOWN);
        }
    }

    public void pressEnter() throws Exception
    {
        screen.keyDown(Key.ENTER);
        screen.keyUp(Key.ENTER);
    }

    public void goToNextField() throws Exception
    {
        screen.keyDown(Key.TAB);
        screen.keyUp(Key.TAB);
    }
}
