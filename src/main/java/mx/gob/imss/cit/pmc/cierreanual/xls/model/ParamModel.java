package mx.gob.imss.cit.pmc.cierreanual.xls.model;


import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.CellType;

@Getter
@Setter
public class ParamModel {

    private String name;
    private String value;
    private CellType type;

}
