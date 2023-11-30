package mx.gob.imss.cit.pmc.cierreanual.utils;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;

public class FutureUtils {

    public static void wait(CompletableFuture<?> completableFuture, Logger logger, String message) {
        boolean firstTime = Boolean.TRUE;
        if (!completableFuture.isDone()) {
            do {
                if (firstTime) {
                    logger.info(message);
                    firstTime = Boolean.FALSE;
                }
            }
            while (!completableFuture.isDone());
        }
    }

}
