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
import uniproject.exam.seating.invigilatorAssignment.InvigilatorAssignment;

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
                        // --- NEW: Strip out the Major ID for the PDF ---
                        String displayText = seatInfo;
                        if (!"EMPTY".equals(seatInfo) && seatInfo.contains(" (")) {
                            // This takes "S001 (CS)" and cuts it down to just "S001"
                            displayText = seatInfo.substring(0, seatInfo.indexOf(" ("));
                        }

                        Cell cell = new Cell().add(new Paragraph(displayText).setFontSize(8));

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

    // Method for the Invigilator Assignment list
    public static byte[] generateAssignmentPdf(List<InvigilatorAssignment> assignments) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            document.add(new Paragraph("Invigilator Assignment Master Plan")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            // Create Table with 5 columns: Room, Invigilator, Subject, Date, Time
            Table table = new Table(UnitValue.createPercentArray(new float[]{15, 25, 25, 20, 15})).useAllAvailableWidth();

            // Table Headers
            table.addHeaderCell(new Cell().add(new Paragraph("Room").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Invigilator").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Subject").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Time").setBold()));

            // Populate Table Data
            for (InvigilatorAssignment assign : assignments) {
                table.addCell(new Paragraph(assign.getRoom().getRoomName()).setFontSize(10));
                table.addCell(new Paragraph(assign.getInvigilator().getInvigilatorName() + " (" + assign.getInvigilator().getRank() + ")").setFontSize(10));
                table.addCell(new Paragraph(assign.getExam().getSubject()).setFontSize(10));
                table.addCell(new Paragraph(assign.getExam().getExamDate().toString()).setFontSize(10));
                table.addCell(new Paragraph(assign.getExam().getExamTime().toString()).setFontSize(10));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}