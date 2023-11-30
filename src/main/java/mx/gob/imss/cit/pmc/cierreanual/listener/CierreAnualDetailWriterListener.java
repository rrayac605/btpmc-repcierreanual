package mx.gob.imss.cit.pmc.cierreanual.listener;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

public class CierreAnualDetailWriterListener implements ItemWriteListener<Object> {

    Logger logger = LoggerFactory.getLogger(CierreAnualDetailWriterListener.class);

    @Override
    public void beforeWrite(List<? extends Object> items) {
        logger.info("Se inicia el borrado de registros");
    }

    @Override
    public void afterWrite(List<? extends Object> items) {
        logger.info(MessageFormat.format("Se borraron {0} registros", items.size()));
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Object> items) {
        logger.info("Sucedio un error al borrar uno de los registros");
    }

}
