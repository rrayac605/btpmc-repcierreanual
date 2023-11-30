package mx.gob.imss.cit.pmc.cierreanual.dto;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MCT_MOVIMIENTO")
@Getter
public class MovimientoDTO {

    private static final long serialVersionUID = 1L;

    /**
     * Id mongo
     */

    @Id
    private String objectIdCambio;



    private PatronDTO patronDTO;



}
