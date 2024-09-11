//package com.Email;
//
//import com.Model.CartItem;
//import com.Model.CartList;
//import com.Model.Product;
//import com.Model.PujaAppointment;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class PdfService {
//
//	public byte[] generateInvoicePdf(String userName, CartList cartList) throws IOException, DocumentException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		Document document = new Document();
//		PdfWriter.getInstance(document, baos);
//
//		document.open();
//
//		// Title
//		Paragraph title = new Paragraph("Invoice");
//		document.add(title);
//
//		// User
//		Paragraph user = new Paragraph("User: " + userName);
//		document.add(user);
//
//		// Table
//		PdfPTable table = new PdfPTable(3); // 3 columns
//		table.setWidthPercentage(100);
//
//		// Headers
//		PdfPCell cell1 = new PdfPCell(new Paragraph("Product"));
//		PdfPCell cell2 = new PdfPCell(new Paragraph("Quantity"));
//		PdfPCell cell3 = new PdfPCell(new Paragraph("Price"));
//
//		table.addCell(cell1);
//		table.addCell(cell2);
//		table.addCell(cell3);
//
//		float total = 0;
//
//		// Data
//		for (CartItem item : cartList.getCartItems()) {
//			table.addCell(item.getProduct().getName());
//			table.addCell(String.valueOf(item.getQuantity()));
//			table.addCell(String.valueOf(item.getTotal()));
//			total = total + item.getTotal();
//		}
//
//		table.addCell("");
//		table.addCell("your total");
//		
//		Paragraph space = new Paragraph(" ");
//		document.add(space);
//		
//		Paragraph title2 = new Paragraph("Appointments");
//		document.add(title2);
//		PdfPTable appointmentTable = new PdfPTable(4); // 3 columns
//		PdfPCell Appcell1 = new PdfPCell(new Paragraph("Puja Name"));
//		PdfPCell Appcell2 = new PdfPCell(new Paragraph("Date"));
//		PdfPCell Appcell3 = new PdfPCell(new Paragraph("Confirmed"));
//		PdfPCell Appcell4 = new PdfPCell(new Paragraph("Price"));
//
//		appointmentTable.addCell(Appcell1);
//		appointmentTable.addCell(Appcell2);
//		appointmentTable.addCell(Appcell3);
//		appointmentTable.addCell(Appcell4);
//
//		for (PujaAppointment appointment : cartList.getUserCartListPujas()) {
//			appointmentTable.addCell(appointment.getPuja().getName());
//			total = total+appointment.getPujaFee();
//			appointmentTable.addCell(String.valueOf(appointment.getAppointmentDate()));
//			if (appointment.isConfirmed()) {
//				appointmentTable.addCell(String.valueOf("CONFIRMED"));
//			} else {
//				appointmentTable.addCell(String.valueOf("PENDING"));
//			}
//
//			appointmentTable.addCell(String.valueOf(appointment.getPujaFee()));
//		}
//		table.addCell(String.valueOf(total));
//		document.add(appointmentTable);
//		document.add(table);
//
//		document.close();
//		return baos.toByteArray();
//	}
//}



package com.Email;

import com.Model.CartItem;
import com.Model.CartList;
import com.Model.Product;
import com.Model.PujaAppointment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(String userName, CartList cartList) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Add background color
        Rectangle rect = new Rectangle(document.getPageSize());
        rect.setBackgroundColor(BaseColor.LIGHT_GRAY);
        writer.getDirectContentUnder().rectangle(rect);

        // Add logo
//        Image logo = Image.getInstance("path/to/logo.png");
//        logo.scaleToFit(100, 100);
//        logo.setAlignment(Element.ALIGN_CENTER);
//        document.add(logo);

        // Title
        Paragraph title = new Paragraph("Invoice", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
    
        // Current Date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Paragraph date = new Paragraph("Date: " + currentDate, FontFactory.getFont(FontFactory.HELVETICA, 12));
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);

        // User
        Paragraph user = new Paragraph("User: " + userName, FontFactory.getFont(FontFactory.HELVETICA, 12));
        user.setSpacingBefore(10);
        user.setAlignment(Element.ALIGN_LEFT);
        document.add(user);

        // Product Table
        document.add(new Paragraph("Products", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        PdfPTable productTable = new PdfPTable(3); // 3 columns
        productTable.setWidthPercentage(100);
        productTable.setSpacingBefore(10);
        productTable.setSpacingAfter(10);

        // Headers
        addTableHeader(productTable, new String[]{"Product", "Quantity", "Price"});

        float total = 0;

        // Data
        for (CartItem item : cartList.getCartItems()) {
            addTableCell(productTable, item.getProduct().getName());
            addTableCell(productTable, String.valueOf(item.getQuantity()));
            addTableCell(productTable, String.valueOf(item.getTotal()));
            total += item.getTotal();
        }

        document.add(productTable);

        // Appointments Table
        document.add(new Paragraph("Appointments", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        PdfPTable appointmentTable = new PdfPTable(4); // 4 columns
        appointmentTable.setWidthPercentage(100);
        appointmentTable.setSpacingBefore(10);
        appointmentTable.setSpacingAfter(10);

        // Headers
        addTableHeader(appointmentTable, new String[]{"Puja Name", "Date", "Confirmed", "Price"});

        for (PujaAppointment appointment : cartList.getUserCartListPujas()) {
            addTableCell(appointmentTable, appointment.getPuja().getName());
            addTableCell(appointmentTable, String.valueOf(appointment.getAppointmentDate()));
            addTableCell(appointmentTable, appointment.isConfirmed() ? "CONFIRMED" : "PENDING");
            addTableCell(appointmentTable, String.valueOf(appointment.getPujaFee()));
            total += appointment.getPujaFee();
        }

        document.add(appointmentTable);

        // Total
        Paragraph totalParagraph = new Paragraph("Total: $" + total, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        totalParagraph.setSpacingBefore(10);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);
        
        Paragraph paymentTypeParagraph = new Paragraph("Payment Type: " + "CREDIT/DEBIT", FontFactory.getFont(FontFactory.HELVETICA, 12));
        paymentTypeParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(paymentTypeParagraph);

        // Footer
        document.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph("Contact Us: contact@company.com | Phone: +1 234 567 890\n" +
                "Thank you for shopping with us! Follow us on social media:\n" +
                "Facebook: fb.com/ourcompany | Twitter: @ourcompany | Instagram: @ourcompany",
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    private void addTableCell(PdfPTable table, String content) {
        table.addCell(new PdfPCell(new Paragraph(content, FontFactory.getFont(FontFactory.HELVETICA, 12))));
    }
}
