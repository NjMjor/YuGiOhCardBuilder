import org.sikuli.script.Key;
import org.sikuli.script.Screen;

/**
 * Created by mlade on 17-May-17.
 */

enum Icon
{
    NONE,
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

enum Background
{
    NORMAL,
    EFFECT,
    FUSION,
    RITUAL,
    SYNCHRO,
    DARKSYNCHRO,
    MONSTERTOKEN,
    XYZ,
    SPELL,
    TRAP,
    TOKEN
}

public class TCG
{

    Screen screen;

    public TCG()
    {
        screen = new Screen();
    }


    public void setBackground(String background) throws Exception {
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\Background");

        goDown(Background.valueOf(background.toUpperCase()).ordinal());

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
        screen.type(title);
        goToNextField();
        goToNextField();
    }

    public void setArtworkFile(String artPath) throws Exception
    {
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\Three Dots.png");
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\Choose Image.png");
        screen.wait("C:\\Users\\Epl\\Desktop\\Pictures\\Choose Bar.png");
        screen.type("SwordsOfRevealingLight.png");
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\Open.png");
        screen.wait("C:\\Users\\Epl\\Desktop\\Pictures\\pictureMenu.png");
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\OK.png");
        goToNextField();
    }

    public void setDescription(String description) throws Exception
    {
        screen.type(description);
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
        screen.type(limitation);
    }




    public void renderCard() throws Exception
    {
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\Render Card.png");
        screen.wait("C:\\Users\\Epl\\Desktop\\Pictures\\renderCardMenu.png");
        screen.type("Swords of Revealing Light");
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\renderCardSave.png");
        screen.wait("C:\\Users\\Epl\\Desktop\\Pictures\\renderCardSettings.png");
        screen.click("C:\\Users\\Epl\\Desktop\\Pictures\\renderCardOK.png");
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
