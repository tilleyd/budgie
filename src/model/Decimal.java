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

    public Decimal negate() {
        return new Decimal(-value);
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

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        long integer = value / 100;
        long decimal = Math.abs(value) % 100;
        return String.format("%d.%02d", integer, decimal);
    }

    public static Decimal parse(String text) throws NumberFormatException {
        long value;

        int i = text.indexOf('.');
        if (i >= 0) {
            String left = text.substring(0, i);
            String right = text.substring(i + 1);
            long whole = Long.parseLong(left);
            long dec = Long.parseLong(right);
            while (dec > 100) {
                dec /= 10;
            }
            if (whole >= 0) {
                value = whole * 100 + dec;
            } else {
                value = whole * 100 - dec;
            }
        } else {
            value = Long.parseLong(text) * 100;
        }

        return new Decimal(value);
    }
}
