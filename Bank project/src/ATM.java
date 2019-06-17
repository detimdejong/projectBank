/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/



import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.lang.InterruptedException;

import static java.lang.Thread.sleep;


public class ATM {


    private boolean pinlength = false;
    private boolean gekozen = false;
    private String reknum = "";
    private String input = "";
    private String input2 = "";
    private String bedrag = "";
    private int bedragint = 0;
    private int kiesBiljet;
    private String pin = "";

    private int keuze = 0;
    private int vijftig = 0;
    private int twintig = 0;
    private int tien = 0;

    private String cardnumber;

    private ATMScreen as;
    private CardReader cardreader;
    private Keypad keypad;


    private ScreenButton cor;
    private ScreenButton enter;

    private ScreenButton moneyButton1;
    private ScreenButton moneyButton2;
    private ScreenButton moneyButton3;
    private ScreenButton moneyButton4;
    private ScreenButton moneyButton5;


    private ScreenButton menuButton1;
    private ScreenButton menuButton2;
    private ScreenButton menuButton3;
    private ScreenButton menuButton4;


    private ScreenButton yesButton;
    private ScreenButton noButton;

    private ScreenButton snelpinnen;


    private DisplayText text;
    private DisplayText text2;
    private DisplayText xjes;
    private String x = "";
    private String starredIban = "";

    private JavaGetRequest httpGet;
    private String response;




    private ArrayList<InputDevice> knoppen = new ArrayList<InputDevice>();


    ATM() throws InterruptedException {



        as = new ATMScreen();
        Frame f = new Frame("My ATM");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setUndecorated(false);
        f.setBackground(Color.ORANGE);
        f.addWindowListener(new MyWindowAdapter(f));
        f.add(as);
        f.setVisible(true);

        httpGet = new JavaGetRequest();

        text = new DisplayText("tekst1", new Point(350, 150));
        text2 = new DisplayText("tekst2", new Point(350, 180));
        xjes = new DisplayText("xjes", new Point(420, 270));

        cardreader = new CardReader("Cardreader");
        keypad = new Keypad("Keypad");

        moneyButton1 = new ScreenButton("10", new Point(350, 200));
        moneyButton2 = new ScreenButton("20", new Point(350, 230));
        moneyButton3 = new ScreenButton("50", new Point(350, 260));
        moneyButton4 = new ScreenButton("100", new Point(350, 290));
        moneyButton5 = new ScreenButton("Anders...", new Point(350, 320 ));

        cor = new ScreenButton("cor", new Point(350, 330));
        enter = new ScreenButton("enter", new Point(400, 330));

        menuButton1 = new ScreenButton("Toon saldo", new Point(350, 210));
        menuButton2 = new ScreenButton("Geld opnemen", new Point(350, 240));
        snelpinnen = new ScreenButton("Snel 50 Roebel opnemen", new Point(350, 270));
        menuButton3 = new ScreenButton("Afbreken", new Point(1000, 600));
        menuButton4 = new ScreenButton("Terug", new Point(900, 600));
        yesButton = new ScreenButton("Yes", new Point(350, 320));
        noButton = new ScreenButton(" No", new Point(410, 320));

        knoppen.add(cor);           //knoppen toevoegen aan arraylist
        knoppen.add(enter);

        knoppen.add(moneyButton1);
        knoppen.add(moneyButton2);
        knoppen.add(moneyButton3);
        knoppen.add(moneyButton4);

        knoppen.add(menuButton1);
        knoppen.add(menuButton2);
        knoppen.add(menuButton3);
        knoppen.add(menuButton4);
        knoppen.add(moneyButton5);

        knoppen.add(snelpinnen);

        knoppen.add(yesButton);
        knoppen.add(noButton);



        SerialOut.open();   //seriele connectie openen naar gelddispenser
        sleep(6000);  // even wachten om verbinding tot stand te brengen

        while (true) {
            doTransaction();
        }
    }

    private void doTransaction() throws InterruptedException {


        text.giveOutput("Welkom");
        text2.giveOutput("scan uw pas");
        as.add(text);
        as.add(text2);

        reknum = ""; //variabelen legen voor hergebruik
        input = "";
        input2 = "";
        bedrag = "";
        bedragint = 0;


        do {  //input opvragen van cardreader


            cardnumber = cardreader.getInput();


        } while (cardnumber == null);




        as.clear();
        text.giveOutput("Voer uw pin in");
        as.add(text);

        as.add(menuButton3);  //knop voor afbreken

        x = "";
        xjes.giveOutput(x);
        as.add(xjes);

        System.out.println("Enter pin");
        pin = "";
        input = "";
        input2 = "";


        while (!pinlength) {   //keypad uitlezen

            Thread.yield();

            for (InputDevice inputDevice : knoppen) {

                input = inputDevice.getInput();


                if (input != null) {

                    goodbye();
                    return;
                }
            }
            input2 = keypad.getInput();
            if (input2 != null) {
                pin += input2;
                x += "X";
                //System.out.println(input2);

                xjes.giveOutput(x);

                if (pin.length() == 4) {

                    pinlength = true;
                }
            }

        }



        pinlength = false;


        try {
            sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //kijk in database of pin en kaartnummer kloppen
        // 1 => ingelogd
        // 0=> foute pin
        // 2 => blocked
        // 3 => pas bestaat niet
        try {
            response = httpGet.httpRequest("Authentication/" + pin + "/" + cardnumber);
            response = response.replaceAll("\n", "");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(response);

        if (response.equals("1")) {
            System.out.println("gelukt");
            menu();
        } else if(response.equals("0")){
            System.out.println("pin incorrect");
            text.giveOutput("Pin incorrect, probeer het opnieuw");
            as.add(text);
            Thread.sleep(3000);
            goodbye();
            return;
        }else if(response.equals("2")) {
            System.out.println("pas geblokkeerd");
            text.giveOutput("pas geblokkeerd, neem contact op met de bank");
            as.add(text);
            Thread.sleep(3000);
            goodbye();
            return;
        }else if(response.equals("3")) {
            System.out.println("onbekende pas");
            text.giveOutput("Onbekende pas, neem contact op met de bank");
            as.add(text);
            Thread.sleep(3000);
            goodbye();
            return;
        }

    }

    private void saldoScherm() throws InterruptedException {

        as.clear(); //scherm leeg maken
        //saldo opvragen
        try {
            response = httpGet.httpRequest("ClientSaldo/" + cardnumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        text.giveOutput("Saldo: " + response); //tekst weergeven op scherm
        as.add(text);
        input = ""; //input leegmaken voor hergebruik
        as.add(menuButton4);
        as.add(menuButton3);

        while (!gekozen) {

            for (InputDevice inputDevice : knoppen) {  //loopen langs alle knoppen om te checken of er een wordt ingedrukt

                input = inputDevice.getInput();

                if (input != null) {

                    if (input == "Terug") {

                        gekozen = true; //breaken uit while loop
                    } else if (input == "Afbreken") {
                        goodbye();
                        return;
                    }
                }
            }
        }
        gekozen = false;

        menu();


    }

    private void menu() throws InterruptedException {
        as.clear();
        text.giveOutput("Menu");
        as.add(text);
        as.add(menuButton1);  //knoppen toevoegen aan scherm
        as.add(menuButton2);
        as.add(menuButton3);
        as.add(snelpinnen);

        while (!gekozen) {

            for (InputDevice inputDevice : knoppen) {  //loopen langs alle knoppen om te checken of er een wordt ingedrukt

                input = inputDevice.getInput();

                if (input != null) {

                    if (input == "Afbreken") {

                        goodbye();
                        return;
                    }
                    if (input == "Snel 50 Roebel opnemen") {

                        snel50();
                        return;
                    }

                    if (input == "Geld opnemen") {

                        geldOpnemen();

                    }

                    if (input == "Toon saldo") {

                        saldoScherm();
                    }

                    gekozen = true;  //uit while loop breaken
                }
            }
        }

        gekozen = false; //gekozen weer op false zetten voor volgende ronde


        as.clear();
    }



    private void geldOpnemen() throws InterruptedException {
        System.out.println("geld opnemen");
        as.clear(); //scherm leeg maken

        text.giveOutput("Snel kiezen"); //tekst weergeven op scherm
        as.add(text);

        as.add(moneyButton1);  //knoppen met bedragen toevoegen
        as.add(moneyButton2);
        as.add(moneyButton3);
        as.add(moneyButton4);
        as.add(moneyButton5);
        as.add(menuButton3);
        as.add(menuButton4);

        input = ""; //input leegmaken voor hergebruik

        while (!gekozen) {

            for (InputDevice inputDevice : knoppen) {  //loopen langs alle knoppen om te checken of er een wordt ingedrukt

                input = inputDevice.getInput();

                if (input != null) {

                    if (input == "Terug") {
                        menu();
                    } else if (input == "Afbreken") {
                        goodbye();
                        return;
                    }
                    else if (input != "Anders...") {
                        bedrag = input;  //als er een in ingedrukt, opslaan
                    } else {

                        bedragIntoetsen();
                    }

                    gekozen = true; //breaken uit while loop

                }
            }


        }
        gekozen = false; //gekozen weer op; false zetten voor volgende ronde
        input = ""; //input leegmaken voor hergebruik
        System.out.println("Choosen amount: " + bedrag);  //gekozen bedrag printen op terminal

        bedragint = Integer.parseInt(bedrag);//gekozen bedrag omzetten van string naar int
        System.out.println(bedragint);

        if (bedragint % 10 != 0) {  //als bedrag niet afgerond is op tientallen, geef melding en keer terug naar menu

            System.out.println("ongeldig bedrag");
            text.giveOutput("Ongeldig bedrag, rond aub af naar tientallen");
            as.clear();
            as.add(text);

            Thread.sleep(2500);


            menu();

        }

        //kijken of er genoeg saldo is en mogelijk gelijk afschrijven
        try {
            response = httpGet.httpRequest("Withdraw/" + cardnumber + "/ATM/" + bedragint);
            response = response.replaceAll("\n", "");

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.equals("-1")) {

            as.clear();
            text.giveOutput("Onvoldoende saldo");
            as.add(text);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();

            menu();

        } else  if (response.equals("1")) {

            as.clear();
            text.giveOutput("Er is iets mis gegaan");
            as.add(text);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();

            goodbye();
            return;
        }

        biljettenKiezen(); //biljetten kiezen

        as.clear();

        text.giveOutput("Wilt u een bon?");  //tekst weergeven op scherm
        as.add(text);

        as.add(yesButton);  //knoppen toevoegen
        as.add(noButton);

        //laatste 3 cijfers van Iban opslaan voor op de bon
        starredIban = "***********";
        starredIban += cardnumber.charAt(11);
        starredIban += cardnumber.charAt(12);
        starredIban += cardnumber.charAt(13);
        System.out.println(starredIban);

        //optie geven voor bon
        while (!gekozen) {

            for (InputDevice inputDevice : knoppen) {  //loopen langs alle knoppen om te checken of er een wordt ingedrukt

                input = inputDevice.getInput();

                if (input != null) {

                    if (input == "Yes") {  //als de gebruiken 'yes' heeft gekozen, gegevens naar arduino sturen voor bon

                        SerialOut.sendToArduino("" + bedragint + ">" + starredIban);
                        Thread.sleep(3500);
                    }
                    gekozen = true;  //uit while loop breaken

                }

            }

        }

        gekozen = false; //gekozen weer op false zetten voor volgende ronde

        as.clear(); //scherm leegmaken

        text.giveOutput("€" + bedrag + " uitgeven"); //gekozen bedrag op scherm tonen
        text2.giveOutput("Vergeet niet uw pas");
        as.add(text);
        as.add(text2);

        text = new DisplayText("en geld", new Point(350, 150)); // nieuwe positie toewijzen aan 'text'
        text.giveOutput("en geld");
        as.add(text); // 'text' opnieuw toevoegen aan scherm

        kiesBiljet = Integer.parseInt(bedrag);  //gekozen biljet omzetten naar een int

        biljettenBerekenen(kiesBiljet, bedragint);  //berekenen welke biljetten er uitgegevn moeten worden


        as.clear();
        System.out.println("geld uitgeven");

        //geld codes en aantallen doorgeven aan arduino voor uitgifte
        if (kiesBiljet == 10) {
            SerialOut.sendToArduino("$" + keuze);

            Thread.sleep(2000 * keuze); //na elke doorgestuurde regel even wachten, zodat de arduino tijd heeft voor uitgifte

        }
        if (kiesBiljet == 20) {
            SerialOut.sendToArduino("%" + keuze);
            Thread.sleep(2000 * keuze);

        }
        if (kiesBiljet == 50) {
            SerialOut.sendToArduino("&" + keuze);
            Thread.sleep(2000 * keuze);

        }
        if (vijftig > 0) {
            SerialOut.sendToArduino("&" + vijftig);
            Thread.sleep(2000 * vijftig);
        }
        if (twintig > 0) {


            SerialOut.sendToArduino("%" + twintig);
            Thread.sleep(2000 * twintig);
        }
        if (tien > 0) {
            SerialOut.sendToArduino("$" + tien);
            Thread.sleep(2000 * tien);

        }
        try {
            sleep(2500); //wacht 2,5 seconden
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        goodbye();
        return;


    }
    
    //functie voor intoetsen van bedrag

    private void bedragIntoetsen() throws InterruptedException {
        as.clear(); //scherm leeg maken
        text.giveOutput("Toets bedrag in");
        as.add(text);  //toevoegen aan scherm

        x = "";
        xjes.giveOutput(x);
        as.add(xjes);
        as.add(menuButton3);
        as.add(menuButton4);
        as.add(cor);
        as.add(enter);

        input2 = "";
        input = "";


        //keypad en knoppen op het scherm uitlezen
        while (!pinlength) {

            Thread.yield();


            input2 = keypad.getInput();

            System.out.println(input2);

            if (input2 != null) {

                x += input2;
                xjes.giveOutput(x);
            }


            for (InputDevice inputDevice : knoppen) {
                input = inputDevice.getInput();

                if (input != null) {
                    if (input == "Terug") {

                        as.clear();
                        geldOpnemen();
                    } else if (input == "Afbreken") {
                        goodbye();
                        return;
                    }
                    else if (input == "cor") {

                        x = x.substring(0, x.length() - 1);
                        xjes.giveOutput(x);
                    } else if (input == "enter") {

                        bedrag = x;
                        pinlength = true;

                    }


                }

            }
        }



        pinlength = false;



    }

    
    //totziens scherm
    private void goodbye() {

        as.clear(); //scherm leeg maken

        text = new DisplayText("goodbye", new Point(350, 150)); // nieuwe positie toewijzen aan 'text'
        text.giveOutput("Tot ziens");
        as.add(text);  //toevoegen aan scherm

        try {
            sleep(1000); //wacht 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        as.clear(); //scherm leegmaken
        return;


    }


    //functie voor het kiezen van biljet
    private void biljettenKiezen() throws InterruptedException {
        as.clear();

        text.giveOutput("Kies de biljetten waarvan u de meeste wilt");
        as.add(text);
        as.add(moneyButton1);
        
        if (bedragint >= 20) {
            as.add(moneyButton2);
        } 
        if (bedragint >= 50) {
            as.add(moneyButton3);
        }
        as.add(menuButton3);
        as.add(menuButton4);

        input = ""; //input leegmaken voor hergebruik

        while (!gekozen) {

            for (InputDevice inputDevice : knoppen) {  //loopen langs alle knoppen om te checken of er een wordt ingedrukt

                input = inputDevice.getInput();

                if (input != null) {

                    if (input == "Terug") {
                        geldOpnemen();
                    } else if (input == "Afbreken") {
                        goodbye();
                        return;
                    }
                    else {
                        bedrag = input;  //als er een in ingedrukt, opslaan
                    }

                    gekozen = true; //breaken uit while loop

                }
            }


        }
        gekozen = false; //gekozen weer op; false zetten voor volgende ronde
        input = ""; //input leegmaken voor hergebruik
        System.out.println(bedrag);


    }

    private void biljettenBerekenen(int biljet, int bedrag) {

        keuze = 0;  //gekozen biljet
        vijftig = 0;      //biljetten van 50
        twintig = 0;      //biljetten van 20
        tien = 0;      //biljetten van 10

        while (bedrag >= biljet) {  //biljet waardes steeds van gepinde bedrag afhalen en aantal biljetten tellen
            keuze++;      //x is aantal gekozen biljetten
            bedrag -= biljet;
        }
        while (bedrag >= 50) {

            vijftig++;   //biljetten van 50
            bedrag -= 50;

        }
        while (bedrag >= 20) {

            twintig++;   //biljetten van 20
            bedrag -= 20;

        }
        while (bedrag >= 10) {

            tien++;   //biljetten van 10
            bedrag -= 10;

        }

        System.out.println("gekozen: " + keuze);
        System.out.println("10: " + tien);
        System.out.println("20: " + twintig);
        System.out.println("50: " + vijftig);
        

    }
    
    //functie voor snel pinnen van 50 roebel
    private void snel50() {
        bedragint = 50;

        as.clear(); //scherm leegmaken

        //kijken of er genoeg saldo is en mogelijk gelijk afschrijven
        try {
            response = httpGet.httpRequest("Withdraw/" + cardnumber + "/ATM/" + bedragint);
            response = response.replaceAll("\n", "");

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.equals("-1")) {

            as.clear();
            text.giveOutput("Onvoldoende saldo");
            as.add(text);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();
            return;
        } else  if (response.equals("1")) {

            as.clear();
            text.giveOutput("Er is iets mis gegaan");
            as.add(text);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();
            return;
        }


        text.giveOutput("€" + bedragint + " uitgeven"); //gekozen bedrag op scherm tonen
        text2.giveOutput("Vergeet niet uw pas");
        as.add(text);
        as.add(text2);

        text = new DisplayText("en geld", new Point(350, 150)); // nieuwe positie toewijzen aan 'text'
        text.giveOutput("en geld");
        as.add(text); // 'text' opnieuw toevoegen aan scherm

        //biljet van 50 roebel uitgeven
        SerialOut.sendToArduino("&" + 1);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        as.clear();

        goodbye();
    }


}
