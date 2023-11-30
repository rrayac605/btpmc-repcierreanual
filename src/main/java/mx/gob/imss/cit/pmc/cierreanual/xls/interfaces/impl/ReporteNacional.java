package mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.impl;

import lombok.extern.log4j.Log4j2;
import mx.gob.imss.cit.pmc.cierreanual.service.FtpClientService;
import mx.gob.imss.cit.pmc.cierreanual.xls.ReporteXLS;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ParamModel;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteOOADModel;
import org.apache.logging.log4j.Level;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReporteNacional extends ReporteXLS<List<ReporteOOADModel>> {

    @Value("${fileReportes}")
    private String path;

    @Autowired
    private FtpClientService ftpService;

    @Override
    public InputStreamResource create(List<ReporteOOADModel> input, String nombre, boolean rfc) throws Exception {
        log.log(Level.INFO,"Iniciando creacion de reporte nacional rfc " + rfc);
        URL url = null;
        if(!rfc){
            url = ResourceUtils.getURL("classpath:Reporte_Cifras_Cierre_Anual_Casuistica_Nacional.xlsx");
        }else{
            url = ResourceUtils.getURL("classpath:Reporte_Cifras_Cierre_Anual_Casuistica_Nacional_RFC_IMSS.xlsx");
        }
        XSSFWorkbook bFinal = new XSSFWorkbook(url.openStream());
        SXSSFWorkbook workbook = new SXSSFWorkbook(bFinal,100);

        XSSFSheet sheet = workbook.getXSSFWorkbook().getSheetAt(0);
        XSSFWorkbook b = workbook.getXSSFWorkbook();

        int sizeDelegaciones = input.size() - 1;
        
        copyRow(sizeDelegaciones, 14, sheet.getRow(13), sheet);
        copyRow(sizeDelegaciones, 19 + sizeDelegaciones, sheet.getRow(18 + sizeDelegaciones),sheet);
        copyRow(sizeDelegaciones, 24 + (sizeDelegaciones*2), sheet.getRow(23 + (sizeDelegaciones*2)),sheet);
        copyRow(sizeDelegaciones, 30 + (sizeDelegaciones*3), sheet.getRow(29 + (sizeDelegaciones*3)),sheet);
        copyRow(sizeDelegaciones, 37 + (sizeDelegaciones*4), sheet.getRow(36 + (sizeDelegaciones*4)),sheet);

        int totalDeRP = input.stream().mapToInt(model -> model.getAccidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal().size()).sum();
        int inicioMerge = CellReference.convertColStringToIndex("D");
        int inicioSegundoMerge = CellReference.convertColStringToIndex("F");
        copyRowHigh(totalDeRP - 1 ,43 + (sizeDelegaciones*5),sheet.getRow(42 + (sizeDelegaciones*5)),sheet,
                Arrays.asList(inicioMerge, inicioMerge+1),
                Arrays.asList(inicioSegundoMerge, inicioSegundoMerge+4)
                );


        List<Integer> ciclosAnterioresG = input.stream().map(in -> in.getCicloAnteriores()).flatMap(m -> m.stream()).collect(Collectors.toSet()).stream().collect(Collectors.toList());
        ciclosAnterioresG.sort((i1,i2)-> i1.compareTo(i2));
        List<Integer> ciclosPosterioesG = input.stream().map(in -> in.getCicloPosteriores()).flatMap(m -> m.stream()).collect(Collectors.toSet()).stream().collect(Collectors.toList());
        ciclosPosterioesG.sort((i1,i2)-> i1.compareTo(i2));
        XSSFRow headerRow = sheet.getRow(20 + (input.size()*2));
        XSSFRow headerRowNames = sheet.getRow(19 +(input.size()*2));
        XSSFCellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
        cellStyle2.cloneStyleFrom(headerRow.getCell(3).getCellStyle());
        cellStyle2.setBorderTop(BorderStyle.THIN);
        int inicio = 3;
        int total = 0;
        if(!ciclosAnterioresG.isEmpty()){
            addDatosHorizontales(ciclosAnterioresG.size(),ciclosAnterioresG.stream().map(Object::toString).collect(Collectors.toList()), headerRow, inicio,cellStyle2);
            addDatosHorizontales( ciclosAnterioresG.size(),new ArrayList<String>(), headerRowNames, inicio, cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(19+(input.size()*2),19+ (input.size()*2), inicio, inicio +ciclosAnterioresG.size()-1));
            inicio = inicio + ciclosAnterioresG.size();
        }

        List<Integer> cicloActualG = Collections.singletonList(input.get(0).getCicloActual());
        addDatosHorizontales(cicloActualG.size(),cicloActualG.stream().map(m->m+"").collect(Collectors.toList()), headerRow, inicio ,cellStyle2);
        addDatosHorizontales( cicloActualG.size(),new ArrayList<String>(), headerRowNames, inicio, cellStyle2);
        inicio = inicio +  1;

        if(!ciclosPosterioesG.isEmpty()){
            addDatosHorizontales(ciclosPosterioesG.size(),ciclosPosterioesG.stream().map(Object::toString).collect(Collectors.toList()), headerRow, inicio ,cellStyle2);
            addDatosHorizontales( ciclosPosterioesG.size(),new ArrayList<String>(), headerRowNames, inicio , cellStyle2);

            if(ciclosPosterioesG.size() > 1) {
            	sheet.addMergedRegion(new CellRangeAddress(19+(input.size()*2),19+ (input.size()*2), inicio, inicio +ciclosPosterioesG.size()-1));
            }
            inicio = inicio + ciclosPosterioesG.size();
        }
        
        sheet.addMergedRegion(new CellRangeAddress(19 + (input.size()*2),20  +(input.size()*2), inicio , inicio));
        XSSFCellStyle cellStyle = (XSSFCellStyle) b.createCellStyle();
        
        cellStyle.cloneStyleFrom(sheet.getRow(19 + (input.size()*2)).getCell(3).getCellStyle());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
		
        XSSFCell cell = sheet.getRow(19 + (input.size()*2)).createCell(inicio);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Total");
        sheet.getRow(20 + (input.size() * 2)).createCell(inicio);

        sheet.getRow(cell.getRowIndex()).getCell(inicio).getCellStyle().setBorderRight(BorderStyle.THIN);
        sheet.getRow(cell.getRowIndex()).getCell(inicio).getCellStyle().setBorderLeft(BorderStyle.THIN);
		
        int acumulador = 0;
        for (int index = 0; index < input.size(); index++) {
            ReporteOOADModel in = input.get(index);
            fillData(sheet.getRow(13+ index), in.getCausisticaPeriodoRevisionTipoRiesgo());
            // Se llena segunda parte del documento
            fillData(sheet.getRow(17 +index + input.size()),in.getCausisticaPeriodoRevisionEstadoRegistro());

            // Se llena tercera parte del documento
            List<Integer> ciclosAnteriores = in.getCicloAnteriores();
            List<Integer> ciclosActual = Collections.singletonList(in.getCicloActual());
            List<Integer> clicosPosteriores = in.getCicloPosteriores();
            inicio = 3;
            total = 0;

            sheet.getRow(21 +index +(input.size()*2)).getCell(1).setCellValue(in.getCausisticaOtrosAniosPorTipoRiesgo().stream().filter(m->m.getName().equals("delegacion")).findFirst().get().getValue());
            if(!ciclosAnteriores.isEmpty()){
                List<String> cicloAnterioresCasosValues = in.getCicloAnterioresCasosValues();

                fillZero(cicloAnterioresCasosValues, ciclosAnterioresG.size());

                fillCausisticaGlobalAnioRevision(ciclosAnterioresG,sheet,"Casos de periodos anteriores", cicloAnterioresCasosValues,inicio,index +(input.size()*2),headerRow, headerRowNames);
                inicio = inicio + ciclosAnterioresG.size();
                total = cicloAnterioresCasosValues.stream().mapToInt(m -> Integer.parseInt(m)).sum();
            }
            int totalCicloActual = Integer.parseInt(in.getCausisticaPeriodoRevisionEstadoRegistro().stream().filter(mov -> mov.getName().equals("total")).map(mov -> mov.getValue()).findFirst().orElse("0")) +
                    Integer.parseInt(in.getCausisticaPeriodoRevisionOtrosEstadosRegistro().stream().filter(mov -> mov.getName().equals("total")).map(mov -> mov.getValue()).findFirst().orElse("0"));
            in.getCicloActualCasosValue().remove(0);
            in.getCicloActualCasosValue().add(0,totalCicloActual+"");
            fillCausisticaGlobalAnioRevision(ciclosActual,sheet,"A\u00F1o de revisi\u00F3n",in.getCicloActualCasosValue(), inicio,index +(input.size()*2),headerRow, headerRowNames);
            inicio = inicio +  ciclosActual.size();
            total = total + in.getCicloActualCasosValue().stream().mapToInt(Integer::parseInt).sum();

            if(!ciclosPosterioesG.isEmpty()){

                List<String> cicloPosterioresCasosValue = in.getCicloPosterioresCasosValue();
                fillZero(cicloPosterioresCasosValue, ciclosPosterioesG.size());
                fillCausisticaGlobalAnioRevision(ciclosPosterioesG,sheet,"Posteriores", cicloPosterioresCasosValue,inicio, index +(input.size()*2),headerRow, headerRowNames);
                inicio = inicio + clicosPosteriores.size();
                total = total + cicloPosterioresCasosValue.stream().mapToInt(m -> Integer.parseInt(m)).sum();
            }
            
            inicio = ciclosAnterioresG.size() + ciclosPosterioesG.size() + 4;
            XSSFCell cellTotal =  sheet.getRow(21 + index + (input.size() * 2)).createCell(inicio);

            cellTotal.setCellType(CellType.NUMERIC);
            cellTotal.setCellValue(total);
            
            XSSFCellStyle cellStyle1 = (XSSFCellStyle) b.createCellStyle();
            cellStyle1.setBorderLeft(BorderStyle.THIN);
            cellStyle1.setBorderRight(BorderStyle.THIN);
            
            cellTotal.setCellStyle(cellStyle1);

            sheet.getRow(cellTotal.getRowIndex()).getCell(inicio).getCellStyle().cloneStyleFrom(cell.getCellStyle());

            
            sheet.getRow(cellTotal.getRowIndex()).getCell(inicio).getCellStyle().setBorderRight(BorderStyle.THIN);
            sheet.getRow(cellTotal.getRowIndex()).getCell(inicio).getCellStyle().setBorderLeft(BorderStyle.THIN);
            
            // Se llena cuarta parte de documento
            fillData(sheet.getRow(26+ index +(input.size()*3)),in.getCausisticaPeriodoRevisionOtrosEstadosRegistro());

            // Se llena quinta parte
            fillData(sheet.getRow(32 + index +(input.size()*4)),in.getCausisticaOtrosAniosPorTipoRiesgo());

            // Se llena sexta parte
            Map<String, List<ParamModel>> accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal = in.getAccidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal();


            AtomicInteger atomicInteger = new AtomicInteger(0);
            log.log(Level.INFO,"Inicio ciclo final");

            XSSFCellStyle xssfCellStyle = (XSSFCellStyle) b.createCellStyle();
            xssfCellStyle.cloneStyleFrom(sheet.getRow(37).getCell(1).getCellStyle());
            for (Map.Entry<String, List<ParamModel>> entry : accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.entrySet()) {
                String llave = entry.getKey();
                List<ParamModel> valor = entry.getValue();

                int rownum = 37 +  acumulador + (input.size() * 5) + atomicInteger.get();

                log.log(Level.DEBUG, "Row num " + rownum);
                XSSFRow row = sheet.getRow(rownum);

                fillData(row, valor);
                Iterator<Cell> cellIterator = row.cellIterator();
                int i = 0;
                int indiceInicial = 1;
                int indiceFinal = 5 + valor.size();
                int valorFinal = 6 + valor.size();
                XSSFCell cell2 = (XSSFCell) cellIterator.next();
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
                    cell2.setCellStyle(xssfCellStyle);
                    i++;
                }
                atomicInteger.incrementAndGet();
            }
            acumulador = acumulador + accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal.size();

        }

        sheet = getSheetMod(input, sheet, ciclosAnterioresG, cicloActualG, ciclosPosterioesG, totalDeRP, inicio);
        
        XSSFFormulaEvaluator.evaluateAllFormulaCells(b);
        Path tempFile = Files.createTempFile(nombre, ".xlsx");
        FileOutputStream fos = new FileOutputStream(tempFile.toFile().getAbsolutePath());
        b.write(fos);
        fos.close();
        b.close();
        log.log(Level.INFO,"Terminando creacion de reporte nacional rfc " + rfc);
        ftpService.uploadFile(tempFile.toFile().getAbsolutePath(),path +"/"+nombre);
        return new InputStreamResource(new FileInputStream(tempFile.toFile().getAbsolutePath()));

    }


    private XSSFSheet getSheetMod(List<ReporteOOADModel> input, XSSFSheet sheet, List<Integer> ciclosAnterioresG,
    		List<Integer> cicloActualG, List<Integer> ciclosPosterioesG, int totalDeRP, int inicio){

        // Se agregan margenes
        int recorrido = input.size();
        fillMargin(sheet,13,(13+ recorrido),1,14);
        fixYearFormat(sheet,13,2,recorrido);
        addSumatoria(sheet.getRow(13+ recorrido), 14 , (13+ recorrido));

        fillMargin(sheet, (17 + recorrido),(17+ (recorrido*2)),1,7);
        fixYearFormat(sheet,(17 + recorrido),2,recorrido);
        recorrido = recorrido * 2;
        addSumatoria(sheet.getRow(17+ (recorrido)), (18 + (recorrido/2)) , (17 + recorrido));

        fillMargin(sheet, (21 +recorrido),(21 + recorrido + input.size() + 1) ,1,(3 + cicloActualG.size()+ ciclosAnterioresG.size() +ciclosPosterioesG.size()));
        XSSFCell cellTotal1 = sheet.getRow((21 + recorrido + input.size())).getCell(3);
//        cellTotal1.setCellType(CellType.FORMULA);
        XSSFCellStyle cellStyle1 = sheet.getWorkbook().createCellStyle();
        
        cellStyle1.cloneStyleFrom(cellTotal1.getCellStyle());

        cellTotal1.setCellFormula("D1+D2");
        for(int i = 4; i < (4 + cicloActualG.size()+ ciclosAnterioresG.size() +ciclosPosterioesG.size()); i++){
            XSSFCell cellTotal = sheet.getRow((21 + recorrido + input.size())).createCell(i);

            XSSFCellStyle cellStyleTemp = sheet.getWorkbook().createCellStyle();
            cellStyleTemp.cloneStyleFrom(cellTotal1.getCellStyle());
            cellTotal.setCellStyle(cellStyleTemp);
            cellTotal.setCellFormula("D1+D2");
        }
        
        fillMargin(sheet,(21 + recorrido + input.size()) ,(21 + recorrido + input.size() +1),1,(3 + cicloActualG.size()+ ciclosAnterioresG.size() +ciclosPosterioesG.size()));      
        recorrido = recorrido + input.size();
        addSumatoria(sheet.getRow(21+ recorrido), (22 + recorrido- input.size()) , (21+ recorrido));
        
        fillMargin(sheet,(26 + recorrido) ,(26 + recorrido + input.size()) ,1,13);
        fixYearFormat(sheet,(26 + recorrido) ,2,input.size());
        recorrido = recorrido + input.size();
        addSumatoria(sheet.getRow(26+ recorrido), (27 + recorrido- input.size()) , (26+ recorrido));
        
        fillMargin(sheet,(32 + recorrido) ,(32 + recorrido + input.size()) ,1,14);
        recorrido = recorrido + input.size();
        addSumatoria(sheet.getRow(32+ recorrido), (33 + recorrido- input.size()), (32+ recorrido));
        
        fillMargin(sheet,(37 + recorrido),(37 + recorrido + totalDeRP) ,1,13);
        fixYearFormat(sheet,(37 + recorrido) ,2,totalDeRP);
        fixYearFormat(sheet,(37 +recorrido), 3,totalDeRP);
        addSumatoria(sheet.getRow(37+ recorrido +totalDeRP), (38 + recorrido), (37+recorrido + totalDeRP));
        recorrido = recorrido + input.size();
        
        return sheet;
    }


    private void fillZero(List<String> valores, int size){
        while(valores.size() < size){
            valores.add("0");
        }
    }

    private void addSumatoria(XSSFRow row, int indexInicial, int indexFinal) {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()){
            XSSFCell cell = (XSSFCell) cellIterator.next();
            if(cell.getCellType().equals(CellType.FORMULA)){
                String letter = CellReference.convertNumToColString(cell.getColumnIndex());
                cell.setCellFormula("SUM("+letter+ indexInicial +":"+letter+ indexFinal +")");
                cell.setCellType(CellType.FORMULA);
            }
        }
    }
}


