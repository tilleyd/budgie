package model;

public class Currency {
    final private int id;
    final private String code;
    final private String symbol;

    public Currency(int id, String code, String symbol) {
        this.id = id;
        this.code = code;
        this.symbol = symbol;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }
}
