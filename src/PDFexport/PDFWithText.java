package PDFexport;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PDFWithText {
    public static void main(String[] args) {
        PDDocument doc = null;
        PDPage page = null;

        try {
            doc = new PDDocument();
            page = new PDPage();

            doc.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;

            PDPageContentStream content = new PDPageContentStream(doc, page);

            content.beginText();
            content.setFont( font, 20 );
            content.setNonStrokingColor(Color.black);
            content.moveTextPositionByAmount( 100, 700 );
            String txt="hoang van nam";

            content.drawString("Hello It's me");
            content.endText();
            content.close();

            doc.save("pdf_with_text.pdf");
            doc.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void PdfToImage(String PDFFILE){
        try{
            PDDocument document = PDDocument.load(new File(PDFFILE));
            PDPage pd;

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {
                pd = document.getPage(page);
                pd.setCropBox(new PDRectangle(100, 100,100,100));
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(bim, "TEST.png", 300);


            }
            document.close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getStackTrace());
        }
    }
}
