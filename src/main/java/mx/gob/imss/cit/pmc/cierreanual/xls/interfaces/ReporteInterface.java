package mx.gob.imss.cit.pmc.cierreanual.xls.interfaces;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;

public interface ReporteInterface<T> {

    InputStreamResource create(T input, String nombre, boolean rfc) throws Exception;

}
