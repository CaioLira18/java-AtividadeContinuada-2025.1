package br.edu.cs.poo.ac.seguro.excecoes;

import java.util.ArrayList;
import java.util.List;

public class ExcecaoValidacaoDados extends Exception {
    private List<String> mensagens;

    public ExcecaoValidacaoDados() {
        this.mensagens = new ArrayList<>();
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public void adicionarMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    public boolean possuiErros() {
        return !mensagens.isEmpty();
    }
}
