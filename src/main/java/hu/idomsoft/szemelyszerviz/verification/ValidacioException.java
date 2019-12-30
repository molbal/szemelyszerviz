package hu.idomsoft.szemelyszerviz.verification;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidacioException extends RuntimeException {
    private List<String> hibaLista;

    public ValidacioException(List<String> hibaLista) {
        this.hibaLista = hibaLista;
    }
}