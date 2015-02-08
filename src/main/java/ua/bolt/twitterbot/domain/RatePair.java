package ua.bolt.twitterbot.domain;

/**
 * Created by ackiybolt on 24.12.14.
 */
public class RatePair {

    public final Currency currency;
    public final Rate buy;
    public final Rate sell;

    public RatePair(Currency currency, Rate buy, Rate sell) {
        this.currency = currency;
        this.buy = buy;
        this.sell = sell;
    }

    public Growing getGrowing (RatePair that) {
        Growing result = null;

        if (that != null) {
            double thisMiddle = (this.buy.value + this.sell.value) / 2.0;
            double thatMiddle = (that.buy.value + that.sell.value) / 2.0;

            if (thisMiddle > thatMiddle)
                result = Growing.UP;
            else if (thisMiddle < thatMiddle)
                result = Growing.DOWN;
            else
                result = Growing.SAME;
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatePair that = (RatePair) o;

        if (!buy.equals(that.buy)) return false;
        if (currency != that.currency) return false;
        if (!sell.equals(that.sell)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + buy.hashCode();
        result = 31 * result + sell.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s / %s", currency, buy, sell);
    }
}
