package mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.impl;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import mx.gob.imss.cit.pmc.cierreanual.service.FtpClientService;
import mx.gob.imss.cit.pmc.cierreanual.xls.ReporteXLS;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ParamModel;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteOOADModel;


import org.apache.logging.log4j.Level;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReporteOOAD extends ReporteXLS<ReporteOOADModel> {

    @Autowired
    private ResourceLoader resourceLoader;


    @Value("${fileReportes}")
    private String path;

//    @Value("${fileReportesLocal}")
//    private String pathLocal;


    @Autowired
    private FtpClientService ftpService;


    @Override
    public InputStreamResource create(ReporteOOADModel input, String nombre, boolean rfc) throws Exception {
        log.log(Level.INFO,"Iniciando creacion de reporte por ooad rfc " + rfc);
        URL url = null;
        if (rfc) {
            url = ResourceUtils.getURL("classpath:Reporte_Cifras_Cierre_Anual_Casuistica_RFC_IMSS.xlsx");
        } else {
            url = ResourceUtils.getURL("classpath:Reporte_Cifras_Cierre_Anual_Casuistica.xlsx");
        }
        Workbook b = new XSSFWorkbook(url.openStream());
        XSSFSheet sheet = (XSSFSheet) b.getSheetAt(0);

        // Se llena primera parte del documento
        int indexPrimeraSeccion = 14;
        fillData(sheet.getRow(indexPrimeraSeccion), input.getCausisticaPeriodoRevisionTipoRiesgo());

        // Se llena segunda parte del documento
        int indexSegundaSeccion = 18;
        fillData(sheet.getRow(indexSegundaSeccion), input.getCausisticaPeriodoRevisionEstadoRegistro());

        // Se llena tercera parte del documento
        List<Integer> ciclosAnteriores = input.getCicloAnteriores();
        List<Integer> ciclosActual = Collections.singletonList(input.getCicloActual());
        List<Integer> clicosPosteriores = input.getCicloPosteriores();
        int indexTerceraSeccion = 22;
        sheet.getRow(indexTerceraSeccion).getCell(1).setCellValue(input.getCausisticaOtrosAniosPorTipoRiesgo().stream().filter(m -> m.getName().equals("delegacion")).findFirst().get().getValue());
        int inicio = 3;
        int total = 0;
        if (!ciclosAnteriores.isEmpty()) {
            fillCausisticaGlobalAnioRevision(ciclosAnteriores, sheet, "Casos de periodos anteriores", input.getCicloAnterioresCasosValues(), inicio);
            inicio = inicio + ciclosAnteriores.size();
            total = input.getCicloAnterioresCasosValues().stream().mapToInt(m -> Integer.parseInt(m)).sum();
        }
        int totalCicloActual = Integer.parseInt(input.getCausisticaPeriodoRevisionEstadoRegistro().stream().filter(mov -> mov.getName().equals("total")).map(mov -> mov.getValue()).findFirst().orElse("0")) +
                Integer.parseInt(input.getCausisticaPeriodoRevisionOtrosEstadosRegistro().stream().filter(mov -> mov.getName().equals("total")).map(mov -> mov.getValue()).findFirst().orElse("0"));
        input.getCicloActualCasosValue().remove(0);
        input.getCicloActualCasosValue().add(0, totalCicloActual + "");
        fillCausisticaGlobalAnioRevision(ciclosActual, sheet, "A\u00F1o de revisi\u00F3n", input.getCicloActualCasosValue(), inicio);
        inicio = inicio + ciclosActual.size();
        total = total + input.getCicloActualCasosValue().stream().mapToInt(m -> Integer.parseInt(m)).sum();
        if (!clicosPosteriores.isEmpty()) {
            fillCausisticaGlobalAnioRevision(clicosPosteriores, sheet, "Posteriores", input.getCicloPosterioresCasosValue(), inicio);
            inicio = inicio + clicosPosteriores.size();
            total = total + input.getCicloPosterioresCasosValue().stream().mapToInt(m -> Integer.parseInt(m)).sum();
        }

        sheet.addMergedRegion(new CellRangeAddress(20, 21, inicio, inicio));

        XSSFCellStyle cellStyle = (XSSFCellStyle) b.createCellStyle();
        cellStyle.cloneStyleFrom(sheet.getRow(20).getCell(3).getCellStyle());
        XSSFCell cell = sheet.getRow(20).createCell(inicio);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Total");
        sheet.getRow(21).createCell(inicio);
        XSSFCell cellTotal = sheet.getRow(indexTerceraSeccion).createCell(inicio);
        XSSFCellStyle cellStyle3 = sheet.getWorkbook().createCellStyle();

        cellStyle3.cloneStyleFrom(sheet.getRow(indexSegundaSeccion).getCell(7).getCellStyle());
        cellStyle3.setBorderLeft(BorderStyle.NONE);
        cellTotal.setCellStyle(cellStyle3);
        sheet.getRow(indexTerceraSeccion).getCell(inicio).setCellValue(total);

        XSSFCellStyle cellStyle1 = sheet.getRow(indexTerceraSeccion).getCell(inicio).getCellStyle();
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderRight(BorderStyle.THIN);


        sheet.getRow(21).getCell(inicio).getCellStyle().setBorderRight(BorderStyle.THIN);
        sheet.getRow(21).getCell(inicio).getCellStyle().setBorderLeft(BorderStyle.THIN);

        // Se llena cuarta parte de documento
        fillData(sheet.getRow(26), input.getCausisticaPeriodoRevisionOtrosEstadosRegistro());

        // Se llena quinta parte
        fillData(sheet.getRow(31), input.getCausisticaOtrosAniosPorTipoRiesgo());


        // Se llena sexta parte
        Map<String, List<ParamModel>> accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal = input.getAccidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal();
        copyRow(accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size() - 1, 36, sheet.getRow(35), sheet);

        AtomicInteger atomicInteger = new AtomicInteger(0);
        accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.forEach((llave, valor) -> {
            XSSFRow row = sheet.getRow(35 + atomicInteger.get());
            fillData(row, valor);
            Iterator<Cell> cellIterator = row.cellIterator();
            int i = 0;
            int indiceInicial = 1;
            int indiceFinal = 5 + valor.size();
            int valorFinal = 6 + valor.size();
            XSSFCell cell1 = (XSSFCell) cellIterator.next();
            XSSFCellStyle xssfCellStyle = (XSSFCellStyle) b.createCellStyle();
            xssfCellStyle.cloneStyleFrom(sheet.getRow(35).getCell(1).getCellStyle());
            while (cellIterator.hasNext()) {
                if (i == valorFinal) {
                    break;
                }
                if (atomicInteger.get() == 0) {
                    borderCellsTop(i, xssfCellStyle, indiceInicial, indiceFinal);
                } else if (atomicInteger.get() + 1 == accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size()) {
                    borderCellBotton(i, xssfCellStyle, indiceInicial, indiceFinal);
                } else {
                    borderCellMid(i, xssfCellStyle, indiceInicial, indiceFinal);
                }
                cell1.setCellStyle(xssfCellStyle);
                i++;
            }
            atomicInteger.incrementAndGet();
        });


        fillMargin(sheet, 35, 35 + accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size(), 1, 13);
        fixYearFormat(sheet, 35, 2, accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size());
        fixYearFormat(sheet,35 ,3,accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size() );
        XSSFCellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.THIN);

        Iterator<Cell> cellIterator = sheet.getRow(34 + accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size()).cellIterator();
        for (int i = 0; i < indexPrimeraSeccion; i++) {
            if(!cellIterator.hasNext())

                break;
            Cell cell1 = cellIterator.next();
            cell1.setCellStyle(cellStyle2);
        }

        XSSFCellStyle cellStylePrimeraFila = sheet.getWorkbook().createCellStyle();
        cellStylePrimeraFila.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStylePrimeraFila.setAlignment(HorizontalAlignment.CENTER);
        cellStylePrimeraFila.setBorderBottom(BorderStyle.THIN);
        cellStylePrimeraFila.setBorderLeft(BorderStyle.THIN);

        XSSFCellStyle cellStyleUltimaFila = sheet.getWorkbook().createCellStyle();
        cellStyleUltimaFila.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleUltimaFila.setAlignment(HorizontalAlignment.CENTER);
        cellStyleUltimaFila.setBorderBottom(BorderStyle.THIN);
        cellStyleUltimaFila.setBorderRight(BorderStyle.THIN);


        sheet.getRow(34+ accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size()).getCell(1).setCellStyle(cellStylePrimeraFila);
        sheet.getRow(34+ accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size()).getCell(13).setCellStyle(cellStyleUltimaFila);

        Path tempFile = Files.createTempFile(nombre, ".xlsx");
        FileOutputStream fos = new FileOutputStream(tempFile.toFile().getAbsolutePath());
        b.write(fos);
        fos.close();
        b.close();
        log.log(Level.INFO,"Terminando creacion de reporte por ooad rfc " + rfc);
        ftpService.uploadFile(tempFile.toFile().getAbsolutePath(), path + "/" + nombre);
        return new InputStreamResource(new FileInputStream(tempFile.toFile().getAbsolutePath()));

    }


    private void fillCausisticaGlobalAnioRevision(List<Integer> ciclos, XSSFSheet sheet, String name, List<String> valores, int firstCol) {

        XSSFRow headerRow = sheet.getRow(21);
        XSSFRow headerRowNames = sheet.getRow(20);

        XSSFCellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
        cellStyle2.cloneStyleFrom(headerRow.getCell(3).getCellStyle());
        cellStyle2.setBorderTop(BorderStyle.THIN);

        addDatosHorizontales(ciclos.size(), ciclos.stream().map(Object::toString).collect(Collectors.toList()), headerRow, firstCol, cellStyle2);
        addDatosHorizontales(ciclos.size(), new ArrayList<String>(), headerRowNames, firstCol, cellStyle2);
        if ((ciclos.size() - 1) != 0) {
            sheet.addMergedRegion(new CellRangeAddress(20, 20, firstCol, firstCol + ciclos.size() - 1));
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
        XSSFRow dataRow = sheet.getRow(22);
        XSSFCellStyle cellStyleValores = dataRow.getCell(3).getCellStyle();
        addDatosHorizontales(ciclos.size(), valores, dataRow, firstCol, cellStyleValores);
    }


}
