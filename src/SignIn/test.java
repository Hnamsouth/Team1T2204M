package SignIn;

import java.sql.Time;
import java.time.LocalTime;

public class test {
    public static void main(String[] args) {
        System.out.println(LocalTime.now().isBefore(Time.valueOf("20:15:00").toLocalTime().plusMinutes(15)));
        String st="asdasdsa106sadasdsa";
        String sr2=st.replaceAll("[a-z]","");
        System.out.println(sr2);
    }
}
