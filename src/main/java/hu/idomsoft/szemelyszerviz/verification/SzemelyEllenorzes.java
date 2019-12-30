package hu.idomsoft.szemelyszerviz.verification;

import hu.idomsoft.szemelyszerviz.config.HelperConfig;
import hu.idomsoft.szemelyszerviz.kodszotar.AllampolgKonyvtar;
import hu.idomsoft.szemelyszerviz.models.SzemelyDTO;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Log
public class SzemelyEllenorzes {

    private final AllampolgKonyvtar allampolgKonyvtar;
    private final HelperConfig config;

    public SzemelyEllenorzes(HelperConfig config, AllampolgKonyvtar allampolgKonyvtar) {
        this.config = config;
        this.allampolgKonyvtar = allampolgKonyvtar;
    }

    /**
     * Ellenőrzi a személy okmányt és visszaadja a hibalistát
     * @param szemely Személy, amit meg kell találni
     * @return
     */
    public List<String> ellenoriz(SzemelyDTO szemely) {

        List<String> hibaLista = new ArrayList<>();

        if (!nevEllenoriz(szemely.getANev())) {
            hibaLista.add(String.format("A megadott anyja neve nem felel meg a név kritériumainak: %s", szemely.getANev()));
        }

        if (!nevEllenoriz(szemely.getSzulNev())) {
            hibaLista.add(String.format("A megadott születési név nem felel meg a név kritériumainak: %s", szemely.getSzulNev()));
        }

        if (!nevEllenoriz(szemely.getVisNev())) {
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
            final String allampolgDekod = allampolgKonyvtar.getAllampolgMap().get(szemely.getAllampKod());
        }



        return hibaLista;
    }


    /**
     * Ellenőrzi a megadott szöveget, mint név
     * @param nev Ellenőrzendő név
     * @return
     */
    private boolean nevEllenoriz(@NotNull String nev) {
        log.fine(String.format("Név ellenőrzése: %s", nev));

        // Név max hossz ellenőrzés
        if (nev.length() > config.getNevMaxHossz()) {
            log.info(String.format("Név nem felel meg: %d kevesebb, mint a hossz: %d",
                    config.getNevMaxHossz(),
                    nev.length()));
            return false;
        }

        // Név min hossz ellenőrzés
        if (nev.length() == 0){
            log.info("A név üres string.");
            return false;
        }

        // Megadott karakterek számának ellenőrzése
        if (!abcEllenorzes(nev)) {
            log.info("A string eltérő karakteretet tartalmaz.");
            return false;
        }

        // Dr. nélkül névrészek számának ellenőrzése
        String nevDrNelkul = nev.replaceAll("Dr\\.?", "").trim();
        if (nevDrNelkul.split("\\s+").length < 2) {
            log.info("Nincs elég szórész.");
            return false;
        }

        // Ha minden ellenőrzés sikeres volt, akkor helyes
        log.info("Név ellenőrzés sikerült.");
        return true;
    }

    /**
     * Ellenőrzi, hogy a névben csak a megadott karakterek szerepeljenej
     * @param str Szöveg, amit ellenőrizni kell
     * @return igaz, ha csak a megadott karaktereket tartalmazza
     */
    private boolean abcEllenorzes(String str) {
        return !str.equals("") && str.matches(config.getNevKarakter());
    }

}
