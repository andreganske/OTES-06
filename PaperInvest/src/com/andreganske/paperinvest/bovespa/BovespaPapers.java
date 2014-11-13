package com.andreganske.paperinvest.bovespa;

public enum BovespaPapers {

    ALLL3("ALLL3", "America"),
    ABEV3("ABEV3", "Ambev"),
    BBAS3("BBAS3", "Teste"),
    BBDC3("BBDC3", "Teste"),
    BBDC4("BBDC4", "Teste"),
    BBSE3("BBSE3", "Teste"),
    BRAP4("BRAP4", "Teste"),
    BRFS3("BRFS3", "Teste"),
    BRKM5("BRKM5", "Teste"),
    BRML3("BRML3", "Teste"),
    BRPR3("BRPR3", "Teste"),
    BVMF3("BVMF3", "Teste"),
    CCRO3("CCRO3", "Teste"),
    CESP6("CESP6", "Teste"),
    CIEL3("CIEL3", "Teste"),
    CMIG4("CMIG4", "Teste"),
    CPFE3("CPFE3", "Teste"),
    CPLE6("CPLE6", "Teste"),
    CRUZ3("CRUZ3", "Teste"),
    CSAN3("CSAN3", "Teste"),
    CSNA3("CSNA3", "Teste"),
    CTIP3("CTIP3", "Teste"),
    CYRE3("CYRE3", "Teste"),
    DTEX3("DTEX3", "Teste"),
    ECOR3("ECOR3", "Teste"),
    ELET3("ELET3", "Teste"),
    ELET6("ELET6", "Teste"),
    ELPL4("ELPL4", "Teste"),
    EMBR3("EMBR3", "Teste"),
    ENBR3("ENBR3", "Teste"),
    ESTC3("ESTC3", "Teste"),
    EVEN3("EVEN3", "Teste"),
    FIBR3("FIBR3", "Teste"),
    GFSA3("GFSA3", "Teste"),
    GGBR4("GGBR4", "Teste"),
    GOAU4("GOAU4", "Teste"),
    GOLL4("GOLL4", "Teste"),
    HGTX3("HGTX3", "Teste"),
    HYPE3("HYPE3", "Teste"),
    ITSA4("ITSA4", "Teste"),
    ITUB4("ITUB4", "Teste"),
    JBSS3("JBSS3", "Teste"),
    KLBN1("KLBN1", "Teste"),
    KROT3("KROT3", "Teste"),
    LAME4("LAME4", "Teste"),
    LIGT3("LIGT3", "Teste"),
    LREN3("LREN3", "Teste"),
    MRFG3("MRFG3", "Teste"),
    MRVE3("MRVE3", "Teste"),
    NATU3("NATU3", "Teste"),
    OIBR4("OIBR4", "Teste"),
    PCAR4("PCAR4", "Teste"),
    PDGR3("PDGR3", "Teste"),
    PETR3("PETR3", "Teste"),
    PETR4("PETR4", "Teste"),
    POMO4("POMO4", "Teste"),
    QUAL3("QUAL3", "Teste"),
    RENT3("RENT3", "Teste"),
    RLOG3("RLOG3", "Teste"),
    RSID3("RSID3", "Teste"),
    SANB11("SANB11", "Teste"),
    SBSP3("SBSP3", "Teste"),
    SUZB5("SUZB5", "Teste"),
    TBLE3("TBLE3", "Teste"),
    TIMP3("TIMP3", "Teste"),
    UGPA3("UGPA3", "Teste"),
    USIM5("USIM5", "Teste"),
    VALE3("VALE3", "Teste"),
    VALE5("VALE5", "Teste"),
    VIVT4("VIVT4", "Teste");

    public static final String[] INDICES = new String[]{"ALLL3", "ABEV3", "BBAS3", "BBDC3", "BBDC4", "BBSE3", "BRAP4",
            "BRFS3", "BRKM5", "BRML3", "BRPR3", "BVMF3", "CCRO3", "CESP6", "CIEL3", "CMIG4", "CPFE3", "CPLE6", "CRUZ3",
            "CSAN3", "CSNA3", "CTIP3", "CYRE3", "DTEX3", "ECOR3", "ELET3", "ELET6", "ELPL4", "EMBR3", "ENBR3", "ESTC3",
            "EVEN3", "FIBR3", "GFSA3", "GGBR4", "GOAU4", "GOLL4", "HGTX3", "HYPE3", "ITSA4", "ITUB4", "JBSS3", "KLBN1",
            "KROT3", "LAME4", "LIGT3", "LREN3", "MRFG3", "MRVE3", "NATU3", "OIBR4", "PCAR4", "PDGR3", "PETR3", "PETR4",
            "POMO4", "QUAL3", "RENT3", "RLOG3", "RSID3", "SANB11", "SBSP3", "SUZB5", "TBLE3", "TIMP3", "UGPA3",
            "USIM5", "VALE3", "VALE5", "VIVT4"};

    private String code;

    private String name;

    private BovespaPapers(String code, String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
