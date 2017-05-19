/**
 * Created by mlade on 16-May-17.
 */

public class Main
{
    public static void main(String args[]) throws Exception
    {
        Card card = new Card("Trap Hole");

        //String tcgPath = System.getProperty("user.home") + "\\Desktop\\TCG\\TCGEditor.exe";
        //Process tcg = Runtime.getRuntime().exec(tcgPath);



        TCG t = new TCG();
        t.generateCard(card);
    }
}
