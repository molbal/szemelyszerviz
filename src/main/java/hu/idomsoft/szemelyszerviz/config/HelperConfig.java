package hu.idomsoft.szemelyszerviz.config;

import hu.idomsoft.szemelyszerviz.kodszotar.AllampolgKonyvtar;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = AllampolgKonyvtar.class)
@Data
public class HelperConfig {

    @Value("${kodszotar.filenev:kodszotar21_allampolg.json}")
    private String kodszotarFilenev;

    @Value("${okmanyszerviz.wsroot:http://127.0.0.1:8001/}")
    private String okmanySzervizRoot;
}
