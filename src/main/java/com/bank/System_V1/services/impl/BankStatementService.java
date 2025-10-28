package com.bank.System_V1.services.impl;

import com.bank.System_V1.dto.EmailDetails;
import com.bank.System_V1.entity.Transaction;
import com.bank.System_V1.entity.User;
import com.bank.System_V1.repository.transactionRepository;
import com.bank.System_V1.repository.userRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import java.util.List;


/**
 * retrieve list of transactions within a date range given an accountNumber
 * generate a pdf file of transactions
 * send the file via email
 * */

@Service
@AllArgsConstructor
@Slf4j
public class BankStatementService {

    @Autowired
    private transactionRepository TransRepo;

    @Autowired
    private userRepository userRepo;

    @Autowired
    private EmailService emailService;

    private static final String FILE = "D:\\Spring projects\\BankFiles\\MyTransactions.pdf";

    public List<Transaction> generateStatement(String accountNumber,String startDate,String endDate) throws FileNotFoundException, DocumentException {
        LocalDate startTime = LocalDate.parse(startDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endTime = LocalDate.parse(endDate,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Transaction> transactionList = TransRepo.findByAccountNumberAndDateBetween(accountNumber,startTime,endTime);
        User user = userRepo.findByAccountNumber(accountNumber);
        String userName = user.getFirstName() + " " + user.getLastName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize, 50f, 50f, 50f, 50f);
        log.info("Setting document size...");

        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();

        BaseColor primaryColor = new BaseColor(22, 82, 101);
        BaseColor accentColor = new BaseColor(230, 240, 245);
        BaseColor textColor = new BaseColor(40, 40, 40);
        BaseColor white = BaseColor.WHITE;


        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);

        PdfPCell titleCell = new PdfPCell(new Phrase("Sab3 Islamic Bank",
                new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, white)));
        titleCell.setBackgroundColor(primaryColor);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setPadding(20f);
        titleCell.setBorder(0);
        headerTable.addCell(titleCell);

        PdfPCell subtitleCell = new PdfPCell(new Phrase("12, 5th SAM, Cairo | +20 101 985 5004",
                new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, white)));
        subtitleCell.setBackgroundColor(primaryColor);
        subtitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        subtitleCell.setPaddingBottom(10f);
        subtitleCell.setBorder(0);
        headerTable.addCell(subtitleCell);

        document.add(headerTable);
        document.add(Chunk.NEWLINE);


        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10f);
        infoTable.setSpacingAfter(15f);
        infoTable.setWidths(new float[]{1, 1});

        Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, primaryColor);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, textColor);

        PdfPCell title = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT",
                new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, primaryColor)));
        title.setHorizontalAlignment(Element.ALIGN_RIGHT);
        title.setBorder(0);

        PdfPCell startDateCell = new PdfPCell(new Phrase("Start Date: " + startDate, valueFont));
        PdfPCell endDateCell = new PdfPCell(new Phrase("End Date: " + endDate, valueFont));
        PdfPCell nameCell = new PdfPCell(new Phrase("Customer Name: " + userName, valueFont));
        PdfPCell addressCell = new PdfPCell(new Phrase("Customer Address: " + user.getAddress(), valueFont));

        for (PdfPCell cell : new PdfPCell[]{startDateCell, title, endDateCell, new PdfPCell(), nameCell, addressCell}) {
            cell.setBorder(0);
            cell.setPaddingBottom(8f);
        }

        infoTable.addCell(startDateCell);
        infoTable.addCell(title);
        infoTable.addCell(endDateCell);
        infoTable.addCell(new PdfPCell());
        infoTable.addCell(nameCell);
        infoTable.addCell(addressCell);

        document.add(infoTable);


        LineSeparator separator = new LineSeparator();
        separator.setLineColor(primaryColor);
        separator.setLineWidth(1f);
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE);


        PdfPTable transactionsTable = new PdfPTable(4);
        transactionsTable.setWidthPercentage(100);
        transactionsTable.setWidths(new float[]{2, 4, 2, 2});

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, white);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, textColor);

        String[] headers = {"DATE", "TRANSACTION TYPE", "AMOUNT", "STATUS"};
        for (String h : headers) {
            PdfPCell header = new PdfPCell(new Phrase(h, headerFont));
            header.setBackgroundColor(primaryColor);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(10f);
            header.setBorder(0);
            transactionsTable.addCell(header);
        }

        boolean alternate = false;
        for (Transaction transaction : transactionList) {
            BaseColor rowColor = alternate ? accentColor : white;
            alternate = !alternate;

            PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getDate().toString(), cellFont));
            PdfPCell typeCell = new PdfPCell(new Phrase(transaction.getTransactionType(), cellFont));
            PdfPCell amountCell = new PdfPCell(new Phrase(transaction.getAmount() + " EGP", cellFont));
            PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus(), cellFont));

            for (PdfPCell c : new PdfPCell[]{dateCell, typeCell, amountCell, statusCell}) {
                c.setBackgroundColor(rowColor);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                c.setPadding(8f);
                c.setBorder(0);
            }

            transactionsTable.addCell(dateCell);
            transactionsTable.addCell(typeCell);
            transactionsTable.addCell(amountCell);
            transactionsTable.addCell(statusCell);
        }

        document.add(transactionsTable);
        document.add(Chunk.NEWLINE);


        LineSeparator footerLine = new LineSeparator();
        footerLine.setLineColor(accentColor);
        footerLine.setLineWidth(1f);
        document.add(new Chunk(footerLine));
        document.add(Chunk.NEWLINE);

        Paragraph footer = new Paragraph("Generated by Sab3 Islamic Bank System",
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, primaryColor));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();



        emailService.sendEmailWithAttachment(EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Bank Statement")
                .message("Kindly find your requested account statement attached!")
                .attachment(FILE)
                .build());

        return transactionList;
    }


}
