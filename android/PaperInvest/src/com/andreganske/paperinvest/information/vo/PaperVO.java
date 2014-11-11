package com.andreganske.paperinvest.information.vo;

public class PaperVO {

    public String codigo;

    public String nome;

    public String ibovespa;

    public String data;

    public String abertura;

    public String minimo;

    public String maximo;

    public String medio;

    public String ultimo;

    public String oscilacao;

    public String minino;

    public PaperVO() {

    }

    public PaperVO(String codigo, String nome, String ibovespa, String data, String abertura, String minimo,
            String maximo, String medio, String ultimo, String oscilacao, String minino) {
        this.codigo = codigo;
        this.nome = nome;
        this.ibovespa = ibovespa;
        this.data = data;
        this.abertura = abertura;
        this.minimo = minimo;
        this.maximo = maximo;
        this.medio = medio;
        this.ultimo = ultimo;
        this.oscilacao = oscilacao;
        this.minimo = minimo;
    }

}
