package hu.idomsoft.szemelyszerviz.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ruzsinak
 */
@Data
public class OkmanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Okány típusa")
    private String okmTipus;

    @ApiModelProperty("Okmány száma")
    private String okmanySzam;

    @ApiModelProperty("Okmánykép")
    private byte[] okmanyKep;

    @ApiModelProperty("Lejárat dátuma")
    private Date lejarDat;

    @ApiModelProperty("Érvényes?")
    private boolean ervenyes;

}
