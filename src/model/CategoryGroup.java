package model;

public enum CategoryGroup {
    DAY_TO_DAY,
    RECURRING,
    EXCEPTION,
    INVESTMENT,
    DEBT,
    INTEREST;

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

    @Override
    public String toString() {
        return switch (this) {
            case DAY_TO_DAY -> "DAY_TO_DAY";
            case RECURRING -> "RECURRING";
            case EXCEPTION -> "EXCEPTION";
            case INVESTMENT -> "INVESTMENT";
            case DEBT -> "DEBT";
            case INTEREST -> "INTEREST";
        };
    }

    public String toFriendlyString() {
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
