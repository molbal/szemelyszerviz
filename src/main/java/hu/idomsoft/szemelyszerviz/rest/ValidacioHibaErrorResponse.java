package hu.idomsoft.szemelyszerviz.rest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ValidacioHibaErrorResponse {

    @ApiModelProperty("A validáció során feltárt hibák listáját tartalmazza")
    private List<String> hibaLista;
}
