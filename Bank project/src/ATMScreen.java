/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/


import java.awt.*;

public class ATMScreen extends Container {

    ATMScreen(){

        super(); //superclass aanroepen
        setLayout(null);

    }

    //elementen toevoegen aan scherm
    public void add(ScreenElement a){

       a.setContainer(this);


    }

    //scherm leegmaken
    public void clear(){

        removeAll();
    }

    public void paint(Graphics g) {   //bank icoon toevoegen
        super.paint(g);
        g.setColor(Color.RED);
        g.fillRoundRect(1200, 600, 35, 35, 10, 10);
        g.fillRect(1230, 630, 5, 5);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("LK", 1203, 620);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.drawString("bank", 1203, 632);
    }
}
