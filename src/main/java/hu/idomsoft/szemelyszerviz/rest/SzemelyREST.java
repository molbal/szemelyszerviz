package hu.idomsoft.szemelyszerviz.rest;

import hu.idomsoft.szemelyszerviz.client.OkmanyClient;
import hu.idomsoft.szemelyszerviz.config.HelperConfig;
import hu.idomsoft.szemelyszerviz.kodszotar.AllampolgKonyvtar;
import hu.idomsoft.szemelyszerviz.models.SzemelyDTO;
import hu.idomsoft.szemelyszerviz.verification.SzemelyEllenorzes;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Log
public class SzemelyREST {

    private final HelperConfig helper;
    private final SzemelyEllenorzes szemelyEllenorzes;
    private final OkmanyClient okmanyClient;
    private final AllampolgKonyvtar allampolgKonyvtar;

    public SzemelyREST(HelperConfig helper, SzemelyEllenorzes szemelyEllenorzes, OkmanyClient okmanyClient, AllampolgKonyvtar allampolgKonyvtar) {
        this.helper = helper;
        this.szemelyEllenorzes = szemelyEllenorzes;
        this.okmanyClient = okmanyClient;
        this.allampolgKonyvtar = allampolgKonyvtar;
    }


    /**
     * Ellenőrzi a megadott Okmányt, hibamentesség esetén kitölti az érvényesség mezőt, vagy hibák esetén visszaadja a hibalistát.
     * @param szemely Ellenőrzenő okmány
     * @return
     */
    @SneakyThrows
    @ApiOperation("Ellenőrzi a megadott Személyt, hibamentesség esetén kitölti az állampolgárság, vagy hibák esetén visszaadja a hibalistát.")
    @PostMapping("/okmany/ellenoriz")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A megadott okmány helyes"),
            @ApiResponse(code = 400, message = "A megadott okmány helytelen adatokat tartalmaz. Hibalistával tér vissza a webszerviz.", response = ValidacioHibaErrorResponse.class)})
    public SzemelyDTO ellenoriz(@RequestBody SzemelyDTO szemely) {
        log.fine(String.format("Okmány ellenőrzése: %s", szemely.toString()));
        return szemely;
    }

    /**
     * Visszaadja a betöltött állampolgárságok listáját.
     * @return
     */
    @ApiOperation("Visszaadja a betöltött okmánytípusok listáját.")
    @GetMapping("/debug/okmanytipus")
    public Map<String, String> getAllampolgMap() {
        return allampolgKonyvtar.getAllampolgMap();
    }
}
