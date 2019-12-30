package hu.idomsoft.szemelyszerviz.rest;

import hu.idomsoft.szemelyszerviz.client.OkmanyClient;
import hu.idomsoft.szemelyszerviz.config.HelperConfig;
import hu.idomsoft.szemelyszerviz.kodszotar.AllampolgKonyvtar;
import hu.idomsoft.szemelyszerviz.models.OkmanyDTO;
import hu.idomsoft.szemelyszerviz.models.SzemelyDTO;
import hu.idomsoft.szemelyszerviz.verification.SzemelyEllenorzes;
import hu.idomsoft.szemelyszerviz.verification.ValidacioException;
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

import java.util.*;

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
            @ApiResponse(code = 200, message = "A megadott személy helyes"),
            @ApiResponse(code = 400, message = "A megadott személy helytelen adatokat tartalmaz. Hibalistával tér vissza a webszerviz.", response = ValidacioHibaErrorResponse.class)})
    public SzemelyDTO ellenoriz(@RequestBody SzemelyDTO szemely) {
        log.fine(String.format("Személy ellenőrzése: %s", szemely.toString()));


        List<String> hibaLista = new ArrayList<>();

        if (!szemelyEllenorzes.nevEllenoriz(szemely.getANev())) {
            hibaLista.add(String.format("A megadott anyja neve nem felel meg a név kritériumainak: %s", szemely.getANev()));
        }

        if (!szemelyEllenorzes.nevEllenoriz(szemely.getSzulNev())) {
            hibaLista.add(String.format("A megadott születési név nem felel meg a név kritériumainak: %s", szemely.getSzulNev()));
        }

        if (!szemelyEllenorzes.nevEllenoriz(szemely.getVisNev())) {
            hibaLista.add(String.format("A megadott viselt név nem felel meg a név kritériumainak: %s", szemely.getVisNev()));
        }

        if (!szemely.getNeme().equalsIgnoreCase("F") && !szemely.getNeme().equalsIgnoreCase("N")) {
            hibaLista.add("A nem csak F vagy N lehet");
        }

        if (szemely.getAllampKod().length() != 3) {
            hibaLista.add("Az állampolgárság kód 3 karakteres lehet.");
        }

        if (!allampolgKonyvtar.getAllampolgMap().containsKey(szemely.getAllampKod())) {
            hibaLista.add("Az állampolgárság kód nem szerepel a listában.");
        }
        else {
            String allampolgDekod = allampolgKonyvtar.getAllampolgMap().get(szemely.getAllampKod());
            szemely.setAllampDekod(allampolgDekod);
        }

        Set<String> ervenyesOkmanyok = new HashSet<>();
        for (OkmanyDTO okmany: szemely.getOkmLista()) {
            try {
                OkmanyDTO okmanyEllenorzott = okmanyClient.okmanyEllenoriz(okmany);
                if (okmanyEllenorzott.isErvenyes()) {
                    if (ervenyesOkmanyok.contains(okmanyEllenorzott.getOkmTipus())) {
                        hibaLista.add(String.format("A %s okmánytípusból egyszerre csak egy érvényes lehet.",
                                okmanyEllenorzott.getOkmTipus()));
                    }
                    else {
                        ervenyesOkmanyok.add(okmanyEllenorzott.getOkmTipus());
                    }
                }
            }
            catch (ValidacioException ve) {
                hibaLista.addAll(ve.getHibaLista());
            }
        }

        if (hibaLista.size() > 0) {
            log.info(String.format("Személy ellenőrizve, hibákat tartalmazott: %s", String.join(", ", hibaLista)));
            throw new ValidacioException(hibaLista);
        }
        else {
            log.fine("Személy ellenőrizve, nincs hiba.");
            return szemely;
        }

    }

    /**
     * Visszaadja a betöltött állampolgárságok listáját.
     * @return
     */
    @ApiOperation("Visszaadja a betöltött állampolgárságok listáját.")
    @GetMapping("/debug/okmanytipus")
    public Map<String, String> getAllampolgMap() {
        return allampolgKonyvtar.getAllampolgMap();
    }
}
