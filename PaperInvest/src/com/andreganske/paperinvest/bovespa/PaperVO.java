package com.andreganske.paperinvest.bovespa;

public class PaperVO {

    private String codigo;

    private String nome;

    private String ibovespa;

    private String data;

    private String abertura;

    private String minimo;

    private String maximo;

    private String medio;

    private String ultimo;

    private String oscilacao;

    public PaperVO() {
    	super();
    }
    
    public PaperVO(String codigo) {
    	super();
    	this.codigo = codigo;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIbovespa() {
		return ibovespa;
	}

	public void setIbovespa(String ibovespa) {
		this.ibovespa = ibovespa;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getAbertura() {
		return abertura;
	}

	public void setAbertura(String abertura) {
		this.abertura = abertura;
	}

	public String getMinimo() {
		return minimo;
	}

	public void setMinimo(String minimo) {
		this.minimo = minimo;
	}

	public String getMaximo() {
		return maximo;
	}

	public void setMaximo(String maximo) {
		this.maximo = maximo;
	}

	public String getMedio() {
		return medio;
	}

	public void setMedio(String medio) {
		this.medio = medio;
	}

	public String getUltimo() {
		return ultimo;
	}

	public void setUltimo(String ultimo) {
		this.ultimo = ultimo;
	}

	public String getOscilacao() {
		return oscilacao;
	}

	public void setOscilacao(String oscilacao) {
		this.oscilacao = oscilacao;
	}

}
