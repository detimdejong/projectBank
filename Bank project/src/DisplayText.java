/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/

import java.awt.*;



public class DisplayText extends ScreenElement implements OutputDevice {

    private Label label;

    DisplayText(String naam, Point pos){

        super(naam, pos); //superclass aanroepen

        label = new Label();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 30));
        label.setBounds(pos.x, pos.y, 1000, 35);
    }

    @Override
   public void setContainer(Container a) {

        a.add(label);

    }

    @Override
    public void giveOutput(String iets) {

        label.setText(iets);

    }
}
