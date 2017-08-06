package utils.evaluation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * For writing excel output of evaluation
 */
public class ExcelWriter {

        private WritableCellFormat timesBoldUnderline;
        private WritableCellFormat times;
        private String inputFile;
        private WritableWorkbook workbook;
        private WritableSheet excelSheet;
        private WritableSheet findingsSheet;
    private ArrayList<WritableSheet> sheets;

    public ExcelWriter(String path, String label){
            sheets = new ArrayList<>();
            setOutputFile(path);
            File file = new File(inputFile);
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));


            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
            } catch (IOException e) {
                e.printStackTrace();
            }
            workbook.createSheet(label, 0);
            workbook.createSheet("Findings",1);
            excelSheet = workbook.getSheet(0);
            sheets.add(excelSheet);
            findingsSheet = workbook.getSheet(1);
            sheets.add(findingsSheet);
            try {
                createLabel(label);
            } catch (WriteException e) {
                e.printStackTrace();
            }

        }

        public WritableSheet getSheet(){
            return excelSheet;
        }

        public void setOutputFile(String inputFile) {
            this.inputFile = System.getProperty("user.dir")+inputFile;
        }

        public void write() throws IOException, WriteException {
            workbook.write();
            workbook.close();
        }

        public void createLabel(String label)
                throws WriteException {
            // Lets create a times font
            WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
            // Define the cell format
            times = new WritableCellFormat(times10pt);
            // Lets automatically wrap the cells
            times.setWrap(true);

            // create create a bold font with unterlines
            WritableFont times10ptBoldUnderline = new WritableFont(
                    WritableFont.TIMES, 10, WritableFont.BOLD, false,
                    UnderlineStyle.SINGLE);
            timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
            // Lets automatically wrap the cells
            timesBoldUnderline.setWrap(true);

            CellView cv = new CellView();
            cv.setFormat(times);
            cv.setFormat(timesBoldUnderline);
            cv.setAutosize(true);

            // Write a few headers
            addCaption(excelSheet, 0, 0, label);


        }
/*
        private void createContent(WritableSheet sheet) throws WriteException,
                RowsExceededException {
            // Write a few number
            for (int i = 1; i < 10; i++) {
                // First column
                addNumber(sheet, 0, i, i + 10);
                // Second column
                addNumber(sheet, 1, i, i * i);
            }
            // Lets calculate the sum of it
            StringBuffer buf = new StringBuffer();
            buf.append("SUM(A2:A10)");
            Formula f = new Formula(0, 10, buf.toString());
            sheet.addCell(f);
            buf = new StringBuffer();
            buf.append("SUM(B2:B10)");
            f = new Formula(1, 10, buf.toString());
            sheet.addCell(f);

            // now a bit of text
            for (int i = 12; i < 20; i++) {
                // First column
                addLabel(sheet, 0, i, "Boring text " + i);
                // Second column
                addLabel(sheet, 1, i, "Another text");
            }
        }
*/
        private void addCaption(WritableSheet sheet, int column, int row, String s)
                throws RowsExceededException, WriteException {
            Label label;
            label = new Label(column, row, s, timesBoldUnderline);
            sheet.addCell(label);
        }

    public void addNumber(int column, int row,
                          Integer integer,WritableSheet sheet)  {
        Number number;
        number = new Number(column, row, integer, times);
        try {
            sheet.addCell(number);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

        public void addNumber(int column, int row,
                               Integer integer)  {
            Number number;
            number = new Number(column, row, integer, times);
            try {
                excelSheet.addCell(number);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }

        public void addLabel(int column, int row, String s)  {
            Label label;
            label = new Label(column, row, s, times);
            try {
                excelSheet.addCell(label);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }

    public void addLabel(int column, int row, String s,WritableSheet sheet)  {
        Label label;
        label = new Label(column, row, s, times);
        try {
            sheet.addCell(label);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public void addBoldLabel(int column, int row, String s)  {
        Label label;
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        WritableCellFormat timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        label = new Label(column, row, s, timesBoldUnderline);
        try {
            excelSheet.addCell(label);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public void addBoldLabel(int column, int row, String s,WritableSheet sheet)  {
        Label label;
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        WritableCellFormat timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        label = new Label(column, row, s, timesBoldUnderline);
        try {
            sheet.addCell(label);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public WritableSheet getSheet(int i) {
        if(sheets.size()>i){
            return sheets.get(i);
        }else{
            return null;
        }
    }


    public int addSheet() {
        int sheetIndex = sheets.size();
        workbook.createSheet(String.valueOf(sheets.size()+2),sheetIndex);
        sheets.add(workbook.getSheet(sheetIndex));
        return sheetIndex;
    }
}
