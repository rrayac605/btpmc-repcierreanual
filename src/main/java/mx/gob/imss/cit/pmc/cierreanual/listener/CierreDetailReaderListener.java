package mx.gob.imss.cit.pmc.cierreanual.listener;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class CierreDetailReaderListener implements ItemReadListener<Object> {

    Logger logger = LoggerFactory.getLogger(CierreDetailReaderListener.class);

    @Override
    public void beforeRead() { }

    @Override
    public void afterRead(Object item) {
        logger.info("");
    }

    @Override
    public void onReadError(Exception ex) { }

}
