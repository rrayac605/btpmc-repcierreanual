package mx.gob.imss.cit.pmc.cierreanual.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class DelegacionOOADDTO {

    private Integer claveDelegacion;
    private List<Integer> claveSubdelegaciones;
    private String descripcion;

}
