package br.com.welldias.tabelaFipe.principal;

import br.com.welldias.tabelaFipe.model.Dados;
import br.com.welldias.tabelaFipe.model.Veiculo;
import br.com.welldias.tabelaFipe.model.modelos;
import br.com.welldias.tabelaFipe.service.ConumoApi;
import br.com.welldias.tabelaFipe.service.Covertedados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/api/v1/";
    private ConumoApi conumoApi = new ConumoApi();
    private Covertedados coversor = new Covertedados();


    public void exibeMenu() {
        var menu = """
            *** OPCOES ***
            Carro
            Moto
            Caminhao
            
            Digite uma das opcoes para consultar:
            """;
        System.out.println(menu);

        var opcao = leitura.nextLine();

        String endereco = "";

        if (opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else if (opcao.toLowerCase().contains("cam")) {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = conumoApi.obterdados(endereco);
        System.out.println(json);
        var marcas = coversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite a marca desejada:");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = conumoApi.obterdados(endereco);

        var modelosLista = coversor.obterDados(json, modelos.class);
        System.out.println("\nModelos dessa marca:");

        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trexo do nome do veiculo a ser buscado:");

        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados:");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigte por favor o codigo do modelo para buscar a avaliacao:");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = conumoApi.obterdados(endereco);

        List<Dados> anos = coversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = conumoApi.obterdados(enderecoAnos);
            Veiculo veiculo = coversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados por an com avaliacoes:");
        veiculos.forEach(System.out::println);

    }
}
