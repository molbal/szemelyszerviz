package hu.idomsoft.szemelyszerviz.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.idomsoft.szemelyszerviz.config.HelperConfig;
import hu.idomsoft.szemelyszerviz.models.OkmanyDTO;
import hu.idomsoft.szemelyszerviz.models.ValidacioHiba;
import hu.idomsoft.szemelyszerviz.verification.ValidacioException;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
@Log
public class OkmanyClient {

    final HelperConfig config;

    public OkmanyClient(HelperConfig config) {
        this.config = config;
    }

    /**
     * Ellenőrzi az okmányt és kiegészíti az ércényesség mezőt
     *
     * @param okmany Ellenőrizendő okmány
     * @return Okmány, ellenőrizve, kiegészítve az érvényesség mezővel.
     *
     * @throws ValidacioException Validáció hiba esetén a hívás kivételt dob, mely a hibalitát tartalmazza
     * @throws RuntimeException Egyéb WS hiba esetén runtimeexceptiont dob
     */
    @SneakyThrows
    public OkmanyDTO okmanyEllenoriz(OkmanyDTO okmany) {

        // Http kliens példányosítás
        CloseableHttpClient client = HttpClients.createDefault();
        final String uri = config.getOkmanySzervizRoot() + "/okmany/ellenoriz";
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        // Okmány -> json string
        ObjectMapper mapper = new ObjectMapper();
        String okmanyJson = mapper.writeValueAsString(okmany);
        httpPost.setEntity(new StringEntity(okmanyJson));

        // WS hívás végrehajtása
        CloseableHttpResponse response = client.execute(httpPost);

        // WS hívás lezárása
        String responseContent = EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        client.close();

        // Válasz kiértékelése
        switch (statusCode) {
            case 200:
                return mapper.readValue(responseContent, OkmanyDTO.class);

            //case 400:
            case 404:
                ValidacioHiba hibaLista = mapper.readValue(responseContent, ValidacioHiba.class);
                throw new ValidacioException(hibaLista.getHibaLista());

            default:
                log.warning(String.format("Az okmányszerviz a %s útvonalon a következő hibával tért vissza: (HTTP kód: %d)%s",
                        uri,
                        statusCode,
                        responseContent));

                throw new RuntimeException(String.format("Az okmányszervizzel való kommunikáció során hiba lépett fel: %d hibakód, válasz szövege: %s",
                        statusCode,
                        responseContent));
        }
    }
}
