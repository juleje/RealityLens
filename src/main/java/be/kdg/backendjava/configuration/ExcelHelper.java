package be.kdg.backendjava.configuration;

import be.kdg.backendjava.domain.CheckpointExcelReturn;
import be.kdg.backendjava.dtos.CheckpointExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERS = { "name", "description", "image", "latitude", "longitude" };
    static String SHEET = "Blad1";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static CheckpointExcelReturn excelToCheckpoints(InputStream is) {
        CheckpointExcelReturn returnValue = new CheckpointExcelReturn();
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<CheckpointExcel> checkpointExcels = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                CheckpointExcel checkpointExcel = new CheckpointExcel();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            checkpointExcel.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            checkpointExcel.setDescription(currentCell.getStringCellValue());
                            break;
                        case 2:
                            if (currentCell.getStringCellValue().length() > 225) {
                                returnValue.setErrorMessage("De korte beschrijving mag niet langer zijn dan 225 karakters");
                                return returnValue;
                            }
                            checkpointExcel.setShortDescription(currentCell.getStringCellValue());
                            break;
                        case 3:
                            checkpointExcel.setImage(currentCell.getStringCellValue());
                            break;
                        case 4:
                            checkpointExcel.setLatitude(currentCell.getNumericCellValue());
                            break;
                        case 5:
                            checkpointExcel.setLongitude(currentCell.getNumericCellValue());
                        default:
                            break;
                    }
                    cellIdx++;
                }
                checkpointExcels.add(checkpointExcel);
            }
            workbook.close();
            returnValue.setCheckpoints(checkpointExcels);
            return returnValue;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
