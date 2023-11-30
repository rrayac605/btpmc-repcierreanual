package mx.gob.imss.cit.pmc.cierreanual.xls;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.ReporteInterface;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.impl.CopyRow;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ParamModel;

import org.apache.logging.log4j.Level;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.boot.actuate.integration.IntegrationGraphEndpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
public abstract class ReporteXLS<T> implements ReporteInterface<T> {


    protected void addColumn(int numberColumn, XSSFRow row, int index, XSSFCellStyle style) {
        for (int i = 0; i < numberColumn; i++) {
            XSSFCellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
            cellStyle.cloneStyleFrom(style);
            XSSFCell cell = row.createCell(index + i);
            cell.setCellStyle(cellStyle);
        }
    }

    protected void fillData(XSSFRow row, List<String> valores, int index) {
        AtomicInteger i = new AtomicInteger(0);
        row.cellIterator().forEachRemaining(cell -> {
            if (i.get() >= index - 1 && valores.size() > (i.get() - index + 1)) {
                try {
                    int valor = Integer.parseInt(valores.get(i.get() - index + 1));
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(valor);
                } catch (Exception ex) {

                    cell.setCellValue(valores.get(i.get() - index + 1));
                    cell.setCellType(CellType.STRING);
                }
            }
            i.incrementAndGet();
        });
    }


    protected void fillData(XSSFRow row, List<ParamModel> parametros) {
        parametros.stream().forEach(param -> {
            row.cellIterator().forEachRemaining(cell -> {
                if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue().equals(param.getName())) {

                    switch (param.getType()) {
                        case STRING:
                            cell.setCellValue(param.getValue());
                            try {
                                Integer valor = Integer.parseInt(param.getValue());
                                cell.setCellType(CellType.NUMERIC);
                                cell.setCellValue(valor);

                            } catch (Exception ex) {
                                cell.setCellType(CellType.STRING);
                                cell.setCellValue(param.getValue());
                            }
                            break;
                        case NUMERIC:
                            cell.setCellValue(Integer.valueOf(param.getValue()));
                            cell.setCellType(CellType.NUMERIC);
                            break;
                        case FORMULA:
                            break;
                        default:
                            break;
                    }


                }
            });
        });
    }

    protected void copyRowHigh(int numberCopys, int startPosition, XSSFRow row, XSSFSheet sheet, List<Integer>... merge) throws Exception {
        if (numberCopys == 0) {
            return;
        }
        SXSSFSheet sheet1 = new SXSSFSheet(new SXSSFWorkbook(sheet.getWorkbook()), sheet);
        sheet.shiftRows(startPosition, sheet.getLastRowNum(), numberCopys, true, false);
        List<List<Integer>> lists = Arrays.asList(merge);
        List<String> valores = new ArrayList<>();
        valores.add("");
        valores.add("delegacion");
        valores.add("anio");
        valores.add("registroPatronal");
        valores.add("");
        valores.add("razonSocial");
        valores.add("");
        valores.add("");
        valores.add("");
        valores.add("");
        valores.add("casos");
        valores.add("diasSubsidiados");
        valores.add("incapacidadesPermanentes");
        valores.add("defunciones");
        for (int i = startPosition; i < startPosition + numberCopys; i++) {
            XSSFRow row1 = sheet.createRow(i);
            row1.setRowStyle(row.getRowStyle());
            for (int j = 0; j < valores.size(); j++) {
                String a = valores.get(j);
                XSSFCell cell1 = row1.createCell(j);
                cell1.setCellValue(a);
            }
            if ((i - startPosition) % 100 == 0) {
                log.info("Rows copiados " + (i - startPosition));
            }
        }
//        List<CopyRow> mergeRow = new ArrayList<>();
//        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = startPosition; i < startPosition + numberCopys; i++) {
            XSSFRow row1 = sheet.getRow(i);
            for (List<Integer> region : lists) {
//                CopyRow copyRow = new CopyRow(sheet,i,region.get(0),region.get(1));
//                mergeRow.add(copyRow);
//                executorService.execute(copyRow);
                sheet.addMergedRegionUnsafe(new CellRangeAddress(i , i,  region.get(0), region.get(1)));
            }
        }
//        executorService.shutdown();
//        executorService.awaitTermination(1,TimeUnit.HOURS);
//        mergeRow.stream().filter(m->m.isFail()).forEach(mergeR->{
//            mergeR.run();
//        });
    }

    protected void copyRow(int numberCopys, int startPosition, XSSFRow row, XSSFSheet sheet) {
        if (numberCopys == 0) {
            return;
        }
        CellCopyPolicy build = new CellCopyPolicy.Builder()
                .cellFormula(false)
                .cellStyle(true)
                .cellValue(true)
                .rowHeight(true)
                .build();
        sheet.shiftRows(startPosition, sheet.getLastRowNum(), numberCopys, true, false);
        for (int i = startPosition; i < startPosition + numberCopys; i++) {
            sheet.copyRows(Arrays.asList(row), i, build);
        }
    }

    protected void borderCellOneRow(int i, XSSFCellStyle xssfCellStyle, int indiceInicial, int indiceFinal) {
        if (i == indiceInicial) {
            xssfCellStyle.setBorderBottom(BorderStyle.THIN);
            xssfCellStyle.setBorderTop(BorderStyle.THIN);
            xssfCellStyle.setBorderLeft(BorderStyle.THIN);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        } else if (i == indiceFinal) {
            xssfCellStyle.setBorderBottom(BorderStyle.THIN);
            xssfCellStyle.setBorderTop(BorderStyle.THIN);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.THIN);
        } else {
            xssfCellStyle.setBorderBottom(BorderStyle.THIN);
            xssfCellStyle.setBorderTop(BorderStyle.THIN);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        }
    }

    protected void borderCellBotton(int i, XSSFCellStyle xssfCellStyle, int indiceInicial, int indiceFinal) {
        if (i == indiceInicial) {
            xssfCellStyle.setBorderBottom(BorderStyle.THIN);
            xssfCellStyle.setBorderTop(BorderStyle.NONE);
            xssfCellStyle.setBorderLeft(BorderStyle.THIN);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        } else if (i == indiceFinal) {
            xssfCellStyle.setBorderBottom(BorderStyle.THIN);
            xssfCellStyle.setBorderTop(BorderStyle.NONE);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.THIN);
        } else {
            xssfCellStyle.setBorderBottom(BorderStyle.THIN);
            xssfCellStyle.setBorderTop(BorderStyle.NONE);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        }
    }


    protected void borderCellsTop(int index, XSSFCellStyle xssfCellStyle, int indiceInicial, int indiceFinal) {
        if (index == indiceInicial) {
            xssfCellStyle.setBorderBottom(BorderStyle.NONE);
            xssfCellStyle.setBorderTop(BorderStyle.THIN);
            xssfCellStyle.setBorderLeft(BorderStyle.THIN);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        } else if (index == indiceFinal) {
            xssfCellStyle.setBorderBottom(BorderStyle.NONE);
            xssfCellStyle.setBorderTop(BorderStyle.THIN);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.THIN);
        } else {
            xssfCellStyle.setBorderBottom(BorderStyle.NONE);
            xssfCellStyle.setBorderTop(BorderStyle.THIN);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        }
    }

    protected void borderCellMid(int i, XSSFCellStyle xssfCellStyle, int indiceInicial, int indiceFinal) {
        if (i == indiceInicial) {
            xssfCellStyle.setBorderBottom(BorderStyle.NONE);
            xssfCellStyle.setBorderTop(BorderStyle.NONE);
            xssfCellStyle.setBorderLeft(BorderStyle.THIN);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        } else if (i == indiceFinal) {
            xssfCellStyle.setBorderBottom(BorderStyle.NONE);
            xssfCellStyle.setBorderTop(BorderStyle.NONE);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.THIN);
        } else {
            xssfCellStyle.setBorderBottom(BorderStyle.NONE);
            xssfCellStyle.setBorderTop(BorderStyle.NONE);
            xssfCellStyle.setBorderLeft(BorderStyle.NONE);
            xssfCellStyle.setBorderRight(BorderStyle.NONE);
        }
    }


    protected void borderCellGeneric(int i, XSSFCell cell, int indiceInicial, int indiceFinal, XSSFCellStyle
            first, XSSFCellStyle mid, XSSFCellStyle last) {
        XSSFDataFormat dataFormat = cell.getRow().getSheet().getWorkbook().createDataFormat();
        if (i == indiceInicial) {
            first.setDataFormat(dataFormat.getFormat("#,##0"));
            first.setVerticalAlignment(VerticalAlignment.CENTER);
            first.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(first);
        } else if (i == indiceFinal) {
            last.setDataFormat(dataFormat.getFormat("#,##0"));
            last.setVerticalAlignment(VerticalAlignment.CENTER);
            last.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(last);
        } else {
            mid.setDataFormat(dataFormat.getFormat("#,##0"));
            mid.setVerticalAlignment(VerticalAlignment.CENTER);
            mid.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(mid);
        }
    }


    protected void addDatosHorizontales(int columnNumber, List<String> valores, XSSFRow row,
                                        int index, XSSFCellStyle style) {
        addColumn(columnNumber, row, index, style);
        if (!valores.isEmpty()) {
            fillData(row, valores, index);
        }
    }

    protected void fillMargin(XSSFSheet sheet, int rowInicial, int rowFinal, int colInicial, int colFinal) {

        XSSFCellStyle cellStyleOneRowLast = sheet.getWorkbook().createCellStyle();
        cellStyleOneRowLast.setBorderLeft(BorderStyle.NONE);
        cellStyleOneRowLast.setBorderRight(BorderStyle.THIN);
        cellStyleOneRowLast.setBorderBottom(BorderStyle.THIN);
        cellStyleOneRowLast.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle cellStyleOneRowFirst = sheet.getWorkbook().createCellStyle();
        cellStyleOneRowFirst.setBorderLeft(BorderStyle.THIN);
        cellStyleOneRowFirst.setBorderRight(BorderStyle.NONE);
        cellStyleOneRowFirst.setBorderBottom(BorderStyle.THIN);
        cellStyleOneRowFirst.setBorderTop(BorderStyle.THIN);


        XSSFCellStyle cellStyleOneRowMid = sheet.getWorkbook().createCellStyle();
        cellStyleOneRowMid.setBorderLeft(BorderStyle.NONE);
        cellStyleOneRowMid.setBorderRight(BorderStyle.NONE);
        cellStyleOneRowMid.setBorderBottom(BorderStyle.THIN);
        cellStyleOneRowMid.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle cellStyleBottonLast = sheet.getWorkbook().createCellStyle();
        cellStyleBottonLast.setBorderLeft(BorderStyle.NONE);
        cellStyleBottonLast.setBorderRight(BorderStyle.THIN);
        cellStyleBottonLast.setBorderBottom(BorderStyle.THIN);
        cellStyleBottonLast.setBorderTop(BorderStyle.NONE);

        XSSFCellStyle cellStyleBottonFisrt = sheet.getWorkbook().createCellStyle();
        cellStyleBottonFisrt.setBorderLeft(BorderStyle.THIN);
        cellStyleBottonFisrt.setBorderRight(BorderStyle.NONE);
        cellStyleBottonFisrt.setBorderBottom(BorderStyle.THIN);
        cellStyleBottonFisrt.setBorderTop(BorderStyle.NONE);


        XSSFCellStyle cellStyleBottonMid = sheet.getWorkbook().createCellStyle();
        cellStyleBottonMid.setBorderLeft(BorderStyle.NONE);
        cellStyleBottonMid.setBorderRight(BorderStyle.NONE);
        cellStyleBottonMid.setBorderBottom(BorderStyle.THIN);
        cellStyleBottonMid.setBorderTop(BorderStyle.NONE);

        XSSFCellStyle cellStyleTopLast = sheet.getWorkbook().createCellStyle();
        cellStyleTopLast.setBorderLeft(BorderStyle.NONE);
        cellStyleTopLast.setBorderRight(BorderStyle.THIN);
        cellStyleTopLast.setBorderBottom(BorderStyle.NONE);
        cellStyleTopLast.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle cellStyleTopFirst = sheet.getWorkbook().createCellStyle();
        cellStyleTopFirst.setBorderLeft(BorderStyle.THIN);
        cellStyleTopFirst.setBorderRight(BorderStyle.NONE);
        cellStyleTopFirst.setBorderBottom(BorderStyle.NONE);
        cellStyleTopFirst.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle cellStyleTopMid = sheet.getWorkbook().createCellStyle();
        cellStyleTopMid.setBorderLeft(BorderStyle.NONE);
        cellStyleTopMid.setBorderRight(BorderStyle.NONE);
        cellStyleTopMid.setBorderBottom(BorderStyle.NONE);
        cellStyleTopMid.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle cellStyleMidMid = sheet.getWorkbook().createCellStyle();
        cellStyleMidMid.setBorderLeft(BorderStyle.NONE);
        cellStyleMidMid.setBorderRight(BorderStyle.NONE);
        cellStyleMidMid.setBorderBottom(BorderStyle.NONE);
        cellStyleMidMid.setBorderTop(BorderStyle.NONE);

        XSSFCellStyle cellStyleMidFirst = sheet.getWorkbook().createCellStyle();
        cellStyleMidFirst.setBorderLeft(BorderStyle.THIN);
        cellStyleMidFirst.setBorderRight(BorderStyle.NONE);
        cellStyleMidFirst.setBorderBottom(BorderStyle.NONE);
        cellStyleMidFirst.setBorderTop(BorderStyle.NONE);


        XSSFCellStyle cellStyleMidLast = sheet.getWorkbook().createCellStyle();
        cellStyleMidLast.setBorderLeft(BorderStyle.NONE);
        cellStyleMidLast.setBorderRight(BorderStyle.THIN);
        cellStyleMidLast.setBorderBottom(BorderStyle.NONE);
        cellStyleMidLast.setBorderTop(BorderStyle.NONE);


        for (int i = rowInicial; i < rowFinal; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = colInicial; j <= colFinal; j++) {
                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                if (rowFinal - rowInicial == 1) {
                    borderCellGeneric(j, cell, colInicial, colFinal, cellStyleOneRowFirst, cellStyleOneRowMid, cellStyleOneRowLast);
                    continue;
                }
                if (i == rowInicial) {
                    borderCellGeneric(j, cell, colInicial, colFinal, cellStyleTopFirst, cellStyleTopMid, cellStyleTopLast);
                } else if (i > rowInicial && i < rowFinal) {
                    borderCellGeneric(j, cell, colInicial, colFinal, cellStyleMidFirst, cellStyleMidMid, cellStyleMidLast);
                } else {
                    borderCellGeneric(j, cell, colInicial, colFinal, cellStyleBottonFisrt, cellStyleBottonMid, cellStyleBottonLast);
                }
            }
        }
    }

    protected void fillCausisticaGlobalAnioRevision(List<Integer> ciclos, XSSFSheet sheet, String
            name, List<String> valores, int firstCol, int index, XSSFRow headerRow, XSSFRow headerRowNames) {

        XSSFCellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
        cellStyle2.cloneStyleFrom(headerRow.getCell(3).getCellStyle());
        cellStyle2.setBorderTop(BorderStyle.THIN);

        if ((ciclos.size() - 1) != 0) {

            int i = 0;
            Iterator<Cell> cellIterator = headerRow.cellIterator();
            while (cellIterator.hasNext()) {
                if (i == firstCol + ciclos.size()) {
                    break;
                }
                XSSFCell cellI = (XSSFCell) cellIterator.next();
                XSSFCellStyle cellStyleHeader = sheet.getWorkbook().createCellStyle();
                cellStyleHeader.cloneStyleFrom(headerRow.getCell(3).getCellStyle());
                if (i == firstCol - 1) {
                    cellI.setCellStyle(cellStyleHeader);
                    cellStyleHeader.setBorderRight(BorderStyle.NONE);
                } else if (i >= firstCol && i < firstCol + ciclos.size()) {
                    cellI.setCellStyle(cellStyleHeader);
                    cellStyleHeader.setBorderLeft(BorderStyle.NONE);
                    cellStyleHeader.setBorderRight(BorderStyle.NONE);
                }
                i++;
            }
        } else {
            headerRow.getCell(firstCol).getCellStyle().setBorderLeft(BorderStyle.THIN);
        }
        XSSFCell cell = headerRowNames.getCell(firstCol);
        XSSFCellStyle cellStyle = cell.getCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cell.setCellValue(name);
        XSSFRow dataRow = sheet.getRow(21 + index);
        XSSFCellStyle cellStyleValores = dataRow.getCell(3).getCellStyle();
        addDatosHorizontales(ciclos.size(), valores, dataRow, firstCol, cellStyleValores);
    }

    protected void fixYearFormat(XSSFSheet sheet, int row, int col, int numrows) {

        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        for (int i = 0; i < numrows; i++) {
            XSSFRow row1 = sheet.getRow(row + i);
            row1.getCell(col).setCellStyle(cellStyle);
        }


    }
}
