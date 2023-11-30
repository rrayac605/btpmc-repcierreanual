package mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.impl;

import lombok.Getter;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class CopyRow implements  Runnable{




    @Getter
    private boolean fail = false;
    private  XSSFSheet sheet;

    private int index;

    private int col1;
    private int col2;
    private static AtomicInteger conteo = new AtomicInteger(0);

    public CopyRow(XSSFSheet sheet, int index, int col1, int col2) {
        this.sheet = sheet;
        this.index = index;
        this.col1 = col1;
        this.col2 = col2;
    }

    private void doit(){
        try {
            sheet.addMergedRegionUnsafe(new CellRangeAddress(index , index,  col1, col2));

            if(conteo.incrementAndGet() % 100 ==0){
                log.log(Level.INFO,"Index {0} : "+ index , conteo.get() );
            }
        }catch (Exception ex ){
            conteo.incrementAndGet();
            log.error("error : ",ex.getMessage());
            log.info("Index " + index);
            log.info("sheet is null : " + (sheet == null));
            log.info("col1 : " + col1);
            log.info("col2 : " + col2);
            fail = true;
        }
    }
    @Override
    public void run() {
        doit();
    }
}
