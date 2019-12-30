package hu.idomsoft.szemelyszerviz.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ValidacioHiba {

    @ApiModelProperty("A hibák listáját tartalmazza")
    private List<String> hibaLista;
}
