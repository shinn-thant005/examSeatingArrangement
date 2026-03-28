package uniproject.exam.seating.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import uniproject.exam.seating.seating.SeatingService.SeatingPlanResponse;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfGeneratorUtil {

    // Method for the visual seating grid
    public static byte[] generateSeatingGridPdf(SeatingPlanResponse plan) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Header information
            document.add(new Paragraph("Seating Plan: " + plan.getRoomName())
                    .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Floor: " + plan.getFloor())
                    .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            List<List<String>> layout = plan.getLayout();
            if (layout != null && !layout.isEmpty()) {
                int columns = layout.get(0).size();

                // Create a table where each cell represents a physical seat
                Table table = new Table(UnitValue.createPercentArray(columns)).useAllAvailableWidth();

                for (List<String> row : layout) {
                    for (String seatInfo : row) {
                        Cell cell = new Cell().add(new Paragraph(seatInfo).setFontSize(8));

                        // Highlight empty seats vs occupied seats
                        if ("EMPTY".equals(seatInfo)) {
                            cell.setOpacity(0.5f);
                        } else {
                            cell.setBold();
                        }

                        table.addCell(cell.setTextAlignment(TextAlignment.CENTER));
                    }
                }
                document.add(table);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}