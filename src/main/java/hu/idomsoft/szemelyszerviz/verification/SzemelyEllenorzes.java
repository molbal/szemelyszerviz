package hu.idomsoft.szemelyszerviz.verification;

import hu.idomsoft.szemelyszerviz.config.HelperConfig;
import hu.idomsoft.szemelyszerviz.kodszotar.AllampolgKonyvtar;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

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
     * Ellenőrzi a megadott szöveget, mint név
     * @param nev Ellenőrzendő név
     * @return
     */
    public boolean nevEllenoriz(@NotNull String nev) {
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
        // TODO: Aposztrof karakterek regex-be kiszervezése
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
        str = str.replaceAll("'","").replaceAll("\"","");
        return !str.equals("") && str.matches(config.getNevKarakter());
    }

}
