package mx.gob.imss.cit.pmc.cierreanual.dto;

import lombok.Getter;
import lombok.Setter;

public class DataSourceDTO {

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String driverClassName;

}

