package ua.bolt.twitterbot.domain;

/**
 * Created by ackiybolt on 20.12.14.
 */
public class Rate {

    public final RateType type;
    public final Double value;

    public Rate(Double value, RateType direction) {
        this.value = value;
        this.type = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rate rate = (Rate) o;

        if (type != rate.type) return false;
        if (!value.equals(rate.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type, value);
    }
}
