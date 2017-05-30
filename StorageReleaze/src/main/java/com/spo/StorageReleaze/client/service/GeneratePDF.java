package com.spo.StorageReleaze.client.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spo.StorageReleaze.client.controller.MainController;
import com.spo.StorageReleaze.client.main.Main;
import com.spo.StorageReleaze.service.model.StorageItem;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static com.itextpdf.text.Font.NORMAL;

/**
 * Created by Antoha12018 on 30.04.2017.
 * Class generator of pdf documents.
 */
public class GeneratePDF {


    private static Font TIME_ROMAN_BIG = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private static Font TIME_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);

    public GeneratePDF() throws IOException, DocumentException {
    }

    /**
     * @param way
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void createPDF(String way) throws IOException, DocumentException, SQLException, ClassNotFoundException {


        File file = new File(way);
        if (file.exists())
            file.delete();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(way));
        document.open();
        addMetaDataToPDF(document);
        addTitlePageToPDF(document);
        createTableOnPDF(document);
        document.close();
    }

    /**
     * @param document
     */
    private static void addMetaDataToPDF(Document document) {
        document.addTitle("Table in PDF");
        document.addSubject("PDF doc");
        document.addAuthor("Antoha Karachun");
        document.addCreator("antoha12018");
    }

    /**
     * @param document
     * @throws DocumentException
     */
    private static void addTitlePageToPDF(Document document) throws DocumentException {

        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("FreeSans.ttf", "cp1251", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font font = new Font(baseFont, 20, Font.BOLD);
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(" "));
        paragraph.add(new Phrase("***СКЛАД***", font));

        Font dateFont = new Font(baseFont, 13, Font.BOLD);
        paragraph.add(new Paragraph(" "));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        paragraph.add(new Phrase("Дата создания файла " + simpleDateFormat.format(new Date()), dateFont));
        document.add(paragraph);

    }

    /**
     * @param document
     * @throws DocumentException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void createTableOnPDF(Document document)
            throws DocumentException, ClassNotFoundException, SQLException, IOException {

        BaseFont baseFont = BaseFont.createFont("FreeSans.ttf", "cp1251", BaseFont.EMBEDDED);
        Font headerFont = new Font(baseFont, Font.DEFAULTSIZE, Font.BOLD);

        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(" "));
        paragraph.add(new Paragraph(" "));
        document.add(paragraph);
        PdfPTable table;
        table = new PdfPTable(4);
        table.setWidthPercentage(100);
        int width[] = {40, 30, 45, 45};
        table.setWidths(width);
        PdfPCell c1 = new PdfPCell(new Phrase("Название", headerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Количество", headerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Дата поступления", headerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Категория", headerFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        Font font = new Font(baseFont, Font.DEFAULTSIZE, Font.NORMAL);
        table.setHeaderRows(1);
        LinkedList<StorageItem> item;
        synchronized (MainController.getStorageList()) {
            item = MainController.getStorageList();
        }


        int size = item.size();
        for (int i = 0; i < size; i++) {

            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            StorageItem itm = item.get(i);

            table.addCell(new Phrase(itm.getName(), font));
            table.addCell(new Phrase(Integer.toString(itm.getCount()), font));
            table.addCell(new Phrase(itm.getDate(), font));
            table.addCell(new Phrase(itm.getCategory(), font));


        }

        document.add(table);
    }
}
