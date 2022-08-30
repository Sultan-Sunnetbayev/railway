package tm.salam.hazarLogistika.railway.services;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelReaderServiceImpl implements ExcelReaderService {

    @Override
    public List<TreeMap<Integer, List<Object>>> read(String filename) throws IOException {

        Workbook workbook = loadWorkbook(filename);
        var sheetIterator = workbook.sheetIterator();
        List<TreeMap<Integer,List<Object>>>data = new ArrayList<>();

        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            TreeMap<Integer, List<Object>> helper=processSheet(sheet);
            data.add(helper);

        }

        return data;
    }

    private Workbook loadWorkbook(String filename) throws IOException {
        var extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        var file = new FileInputStream(new File(filename));
        switch (extension) {
            case "xls":
                // old format
                return new HSSFWorkbook(file);
            case "xlsx":
                // new format
                return new XSSFWorkbook(file);
            default:
                File excelFile=new File(filename);

                if(excelFile.exists()){

                    excelFile.delete();
                }
                throw new RuntimeException("Unknown Excel file extension: " + extension);
        }
    }

    private TreeMap<Integer, List<Object>> processSheet(Sheet sheet) {

//        System.out.println("Sheet: " + sheet.getSheetName());

        var data = new TreeMap<Integer, List<Object>>();
        var iterator = sheet.rowIterator();
        for (var rowIndex = 0; iterator.hasNext(); rowIndex++) {
            var row = iterator.next();
            processRow(data, rowIndex, row);
        }

        return data;
    }

    private void processRow(TreeMap<Integer, List<Object>> data, int rowIndex, Row row) {
        data.put(rowIndex, new ArrayList<>());
        for (var cell : row) {
            processCell(cell, data.get(rowIndex));
        }
    }

    private void processCell(Cell cell, List<Object> dataRow) {

        switch (cell.getCellType()) {
            case STRING:
                dataRow.add(cell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    dataRow.add(cell.getLocalDateTimeCellValue());
                } else {
                    dataRow.add(NumberToTextConverter.toText(cell.getNumericCellValue()));
                }
                break;
            case BOOLEAN:
                dataRow.add(cell.getBooleanCellValue());
                break;
            case FORMULA:
                switch (cell.getCachedFormulaResultType()){
                    case BOOLEAN:
                        dataRow.add(cell.getBooleanCellValue());
                        break;
                    case NUMERIC:
                        dataRow.add(cell.getNumericCellValue());
                        break;
                    case STRING:
                        dataRow.add(cell.getStringCellValue());
                        break;
                    default:
                        dataRow.add(" ");
                }
                break;
            default:
                dataRow.add(" ");
        }
    }

}