//package PDFexport;
//
//import java.io.*;
//
//// importing itext library packages
//import com.itextpdf.io.image.*;
//import com.itextpdf.kernel.pdf.*;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Image;
//
//public class InsertImagePDF {
//    public static void main(String[] args)
//            throws IOException
//    {
//
//        String currentPath="F:/Github/Team1/Team1T2204M/src/PDFexport";
//        String pdfPath=currentPath+"/InsertImage.pdf";
//
//        // Creating path for the new pdf file
//        PdfWriter writer = new PdfWriter(pdfPath);
//
//        // Creating PdfWriter object to write pdf file to
//        // the path
//        PdfDocument pdfDoc = new PdfDocument(writer);
//
//        // Creating a PdfDocument object
//        Document doc = new Document(pdfDoc);
//
//        // Creating a Document object
//        ImageData imageData = ImageDataFactory.create( currentPath + "/bgticket.png");
//        // Creating imagedata from image on disk(from given
//        // path) using ImageData object
//        Image img = new Image(imageData);
//
//        // Creating Image object from the imagedata
//        doc.add(img);
//
//        // Adding Image to the empty document
//        doc.close();
//
//        // Closing the document
//        System.out.println(
//                "Image added successfully and PDF file created!");
//    }
//}
//
