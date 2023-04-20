package model;

public class Decimal {
    private long value;

    public Decimal(long decimalValue) {
        value = decimalValue;
    }

    public void add(Decimal other) {
        value += other.value;
    }

    public void sub(Decimal other) {
        value -= other.value;
    }

    public Decimal abs() {
        return new Decimal(Math.abs(value));
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Decimal o) {
            return o.value == value;
        }
        return false;
    }

    public boolean isPositive() {
        return value >= 0;
    }

    public boolean isNegative() {
        return value < 0;
    }

    public boolean isZero() {
        return value == 0;
    }

    public float toFloat() {
        long integer = value / 100;
        long decimal = integer % 100;
        return integer + (decimal / 100.0f);
    }

    @Override
    public String toString() {
        long integer = value / 100;
        long decimal = Math.abs(value) % 100;
        return String.format("%d.%02d", integer, decimal);
    }
}
