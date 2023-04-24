package model;

public class Category {
    final private int id;
    final private String name;
    final private CategoryType type;
    final private CategoryGroup group;

    public Category(int id, String name, CategoryType type, CategoryGroup group) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public CategoryGroup getGroup() {
        return group;
    }

    public boolean incomeAllowed() {
        return type == CategoryType.INCOME || type == CategoryType.INCOME_OR_EXPENSE;
    }

    public boolean expenseAllowed() {
        return type == CategoryType.EXPENSE || type == CategoryType.INCOME_OR_EXPENSE;
    }

    @Override
    public String toString() {
        return name;
    }
}
