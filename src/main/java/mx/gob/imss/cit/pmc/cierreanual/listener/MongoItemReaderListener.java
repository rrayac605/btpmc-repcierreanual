package mx.gob.imss.cit.pmc.cierreanual.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class MongoItemReaderListener implements ItemReadListener<Object> {

    Logger logger = LoggerFactory.getLogger(MongoItemReaderListener.class);

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(Object item) {
        logger.info(item.toString());
    }

    @Override
    public void onReadError(Exception ex) {

    }

}
