/**
 * Created by mlade on 16-May-17.
 */

import org.sikuli.script.*;

public class Main
{
    public static void main(String args[]) throws Exception
    {
        Card card = new Card("Blue-Eyes_White_Dragon");

        //String tcgPath = System.getProperty("user.home") + "\\Desktop\\TCG\\TCGEditor.exe";
        //Process tcg = Runtime.getRuntime().exec(tcgPath);

        TCG t = new TCG();

        t.setBackground("Spell");
        t.setAttribute("Dark");
        t.setTitle("Swords of Revealing Light");
        t.setArtworkFile("");
        t.setDescription("Flip all monsters your opponent controls face-up. This card remains on the field for 3 of your opponent's turns. While this card is face-up on the field, monsters your opponent controls cannot declare an attack.");
        t.setIcon("None");
        t.setLimitation("Unlimited");
        t.renderCard();
    }
}
