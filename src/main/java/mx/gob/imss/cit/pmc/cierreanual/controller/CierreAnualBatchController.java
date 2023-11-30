package mx.gob.imss.cit.pmc.cierreanual.controller;

import org.springframework.http.ResponseEntity;

public interface CierreAnualBatchController {

    ResponseEntity<?> deleteOldRegisters();

    ResponseEntity<?> execute();

    void restart();

}
