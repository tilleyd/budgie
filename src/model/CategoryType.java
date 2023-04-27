package model;

public enum CategoryType {
    INCOME(1),
    EXPENSE(2),
    INCOME_OR_EXPENSE(3);

    final private int id;

    CategoryType(int id) {
        this.id = id;
    }

    public static CategoryType fromString(String s) {
        if (s.equals("INCOME")) {
            return CategoryType.INCOME;
        }
        if (s.equals("EXPENSE")) {
            return CategoryType.EXPENSE;
        }
        return CategoryType.INCOME_OR_EXPENSE;
    }

    public int getId() {
        return id;
    }

    public String toInternalString() {
        return switch (this) {
            case INCOME -> "INCOME";
            case EXPENSE -> "EXPENSE";
            case INCOME_OR_EXPENSE -> "INCOME_OR_EXPENSE";
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case INCOME -> "Income";
            case EXPENSE -> "Expense";
            case INCOME_OR_EXPENSE -> "Income/Expense";
        };
    }
}
