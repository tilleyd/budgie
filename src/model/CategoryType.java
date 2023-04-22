package model;

public enum CategoryType {
    INCOME,
    EXPENSE,
    INCOME_OR_EXPENSE;

    public static CategoryType fromString(String s) {
        if (s.equals("INCOME")) {
            return CategoryType.INCOME;
        }
        if (s.equals("EXPENSE")) {
            return CategoryType.EXPENSE;
        }
        return CategoryType.INCOME_OR_EXPENSE;
    }

    @Override
    public String toString() {
        return switch (this) {
            case INCOME -> "INCOME";
            case EXPENSE -> "EXPENSE";
            case INCOME_OR_EXPENSE -> "INCOME_OR_EXPENSE";
        };
    }

    public String toFriendlyString() {
        return switch (this) {
            case INCOME -> "Income";
            case EXPENSE -> "Expense";
            case INCOME_OR_EXPENSE -> "Income/Expense";
        };
    }
}
