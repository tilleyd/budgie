package model;

public enum CategoryGroup {
    DAY_TO_DAY(1),
    RECURRING(2),
    EXCEPTION(3),
    INVESTMENT(4),
    DEBT(5),
    INTEREST(6);

    final private int id;

    CategoryGroup(int id) {
        this.id = id;
    }

    public static CategoryGroup fromString(String s) {
        if (s.equals("DAY_TO_DAY")) {
            return CategoryGroup.DAY_TO_DAY;
        }
        if (s.equals("RECURRING")) {
            return CategoryGroup.RECURRING;
        }
        if (s.equals("INVESTMENT")) {
            return CategoryGroup.INVESTMENT;
        }
        if (s.equals("DEBT")) {
            return CategoryGroup.DEBT;
        }
        if (s.equals("INTEREST")) {
            return CategoryGroup.INTEREST;
        }
        return CategoryGroup.EXCEPTION;
    }

    public int getId() {
        return id;
    }

    public String toInternalString() {
        return switch (this) {
            case DAY_TO_DAY -> "DAY_TO_DAY";
            case RECURRING -> "RECURRING";
            case EXCEPTION -> "EXCEPTION";
            case INVESTMENT -> "INVESTMENT";
            case DEBT -> "DEBT";
            case INTEREST -> "INTEREST";
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case DAY_TO_DAY -> "Day-to-day";
            case RECURRING -> "Recurring";
            case EXCEPTION -> "Exception";
            case INVESTMENT -> "Investment";
            case DEBT -> "Debt";
            case INTEREST -> "Interest";
        };
    }
}
