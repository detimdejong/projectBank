import java.time.*;


public class DatumTijd {

   static  LocalDate date;
   static  LocalTime time;
   static String dateAdj;
   static String timeAdj;

   public DatumTijd(){

    }

    static String getDate(){  //funtie om datum te krijgen

        date = LocalDate.now();
        dateAdj = date.toString(); //omzetten naar string

        System.out.println(dateAdj);

        return dateAdj;
    }

    static String getTime(){ //functie om tijd te krijgen



        time = LocalTime.now();
        timeAdj = time.toString(); //omzetten naar string
        System.out.println(timeAdj);

        return timeAdj.substring(0, 8);
    }

}
