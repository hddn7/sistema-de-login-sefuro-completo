package br.com.loginseguro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class TemaConfig {

    @Value("${app.tema}")
    private String tema;

    @ModelAttribute("tema")
    public String temaAtual() {
        return tema;
    }
}
