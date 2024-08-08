package com.hackathon.fiap.mscliente.infrastructure.util;

public class StringUtils {
    public static String formataDados(String dado){
        return dado.replaceAll("[^0-9]+", "");
    }
}
