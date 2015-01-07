package ua.bolt.twitterbot.domain;

/**
 * Created by ackiybolt on 20.12.14.
 */
public class Market {

    public final MarketType type;
    public final RatePair eur;
    public final RatePair usd;
    public final RatePair rub;


    public Market(MarketType type, RatePair eur, RatePair usd, RatePair rub) {
        this.type = type;
        this.eur = eur;
        this.usd = usd;
        this.rub = rub;
    }

    public boolean isFull() {
        return type != null && eur != null && usd != null && rub != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Market market = (Market) o;

        if (!eur.equals(market.eur)) return false;
        if (!rub.equals(market.rub)) return false;
        if (type != market.type) return false;
        if (!usd.equals(market.usd)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + eur.hashCode();
        result = 31 * result + usd.hashCode();
        result = 31 * result + rub.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "%s\n%s\n%s\n%s", type, eur, usd, rub);
    }
}
