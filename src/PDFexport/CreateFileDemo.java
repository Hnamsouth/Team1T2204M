//package PDFexport;
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.Date;
//import java.text.DateFormat;
//import java.text.Format;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.FormatStyle;
//import java.util.Calendar;
//
//public class CreateFileDemo {
//    public static void main(String[] args) throws IOException, ParseException {
//        Format fm = new SimpleDateFormat("HH:mm a");
//        LocalTime t= LocalTime.now();
//        LocalDate d= LocalDate.now();
//        LocalDateTime dt= LocalDateTime.now();
////        System.out.println(t.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
////        System.out.println(dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
//        System.out.println(dt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + dt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
////        System.out.println(fm.format(t.toString()));
////        try {
////            File f= new File("F:\\Github\\Team1\\Team1T2204M\\src\\PDFexport\\demo.txt");
////            if(f.createNewFile()){
////                System.out.println("file created : "+f.getName());
////            }else{
////                System.out.println("File already exists.");
////            }
////        }catch (IOException e) {
////            e.printStackTrace();
////        }
//
//
//    }
//}
