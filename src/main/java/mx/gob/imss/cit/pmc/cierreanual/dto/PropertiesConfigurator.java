package mx.gob.imss.cit.pmc.cierreanual.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring")
public class PropertiesConfigurator {

    @Getter
    @Setter
    private DataSourceDTO dataSource;

}
