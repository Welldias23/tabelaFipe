package br.com.welldias.tabelaFipe.service;

import java.util.List;

public interface Iconvertedados {
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
