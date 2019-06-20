/********************************\
 Author: Tim de Jong
 Studentnumber: 0968586
 Date: 17-06-2019
 Class: TI1A
 Subject: Poject  3/4
 \*******************************/



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JavaGetRequest {

    private static HttpURLConnection con;
    static StringBuilder content;
    static String contents;

    public static String httpRequest(String a) throws MalformedURLException,
            ProtocolException, IOException {

        // Inloggen
        // 1 => ingelogd
        // 0=> foute pin
        // 2 => blocked
        // 3 => pas bestaat niet
        // String url = "https://projectbank.azurewebsites.net/api/Authentication/[password]/[Nuid]";

        // Get Client saldo
        // api/ClientSaldo/[Iban]
        // -1 => badrequest
        // 0 => notfound
        // [saldo] => OK

        //URL: api/Withdraw/[iban]/[destination iban]/[amount]
        /*
         * -1 => onvoldoende saldo
         *  0 => succesful
         *  1 => notfound
         *
         * destination iban> ATM
         */

        //https://debankproject34.azurewebsites.net/api
        //https://projectbank.azurewebsites.net/api/
        //https://project34bank.azurewebsites.net
        //url/api/withdraw/iban/destiban/amount/pin
        String url = "https://project34bank.azurewebsites.net/api/" + a ; // + controller + parameters

        try {  //open connectie naar API voor opvragen van gegevens en bedragen afschrijven

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");



            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println(content.toString());
            contents = content.toString();

        } finally {

            con.disconnect();
        }
        return contents;
    }
}