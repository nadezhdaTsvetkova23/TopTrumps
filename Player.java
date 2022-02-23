package a11942924;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class Player implements Comparable<Player>, RemovableCards{
    private String name ;
    private Queue<VehicleCard> deck = new ArrayDeque();

    public Player (final String name) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("the name of the player can't be null or empty");
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        int score = 0;
        for(VehicleCard card: deck) {
            score += card.totalBonus();
        }
        return score;
    }

    public void addCards(final Collection<VehicleCard> cards) {
        if(cards == null)
            throw new IllegalArgumentException("cards arg can't be null");
        for(VehicleCard c: cards){
            if(c == null)
                throw new IllegalArgumentException("each card is not null");
            deck.add(c);
        }
    }

    public void addCard(final VehicleCard card) {
        if(card == null)
            throw new IllegalArgumentException("the card can't be null");
        deck.add(card);
    }

    public void clearDeck() {
        deck.clear();
    }

    public List<VehicleCard> getDeck() {
        return new ArrayList<VehicleCard>(deck);
    }

    protected VehicleCard peekNextCard() {
        return deck.peek();
    }

    public VehicleCard playNextCard() {
        return deck.poll();
    }

    public int compareTo(final Player other) {
        //null check?
        if(other == null)
            throw new IllegalArgumentException("the other player can't be null");
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
            /* int result = (int) (id ^ (id >>> 32));
               result = 31 * result + name.hashCode();
               return result;*/
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if(obj instanceof Player){
            Player that = (Player)obj;
            if(that.name.equalsIgnoreCase(this.name))
                //if(that.name.toLowerCase().equals(this.name.toLowerCase()))
                return true;
        }
        return false;
        /* auto generate but cmp name case
insensitive */}
    @Override
    public String toString() {
        String result = name + "(" + getScore() + "):";
        for(VehicleCard v: deck){
            result += "\n" + v.toString();
        }
        return result;
        /* format : name ( score ), one card per line , e.g
.:
Maria (73214) :
- Porsche 911(73054) -> { Beschleunigung = <val > , Zylinder = <val > , ...}
- Renault Clio (160) -> {...}
*/}
    public boolean challengePlayer(Player p) {
        if(p == null || p.equals(this))
            throw new IllegalArgumentException("illegal player");

        int deck_size = this.getDeck().size();
        Collection<VehicleCard> p1 = new ArrayList<>();
        Collection<VehicleCard> p2 = new ArrayList<>();

        while(true){
            if(this.getDeck().isEmpty() || p.getDeck().isEmpty()){
                this.addCards(p1);
                p.addCards(p2);
                return false;
            }

            int winner = this.peekNextCard().compareTo(p.peekNextCard());
            p1.add(this.playNextCard());
            p2.add(p.playNextCard());
            if(winner > 0){
                this.addCards(p2);
                this.addCards(p1);
                break;
            }
            else if(winner < 0){
                p.addCards(p1);
                p.addCards(p2);
                break;
            }
        }
        return this.getDeck().size() > deck_size;
    }

    public static Comparator<Player> compareByScore() {
        return new Comparator<Player>() {
            @Override
            public int compare(Player a, Player b) {

                return Integer.compare(a.getScore(), b.getScore());
            }
        };
    }

    public static Comparator<Player> compareByDeckSize() {
        return new Comparator<Player>() {
            @Override
            public int compare(Player a, Player b) {

                return Integer.compare(a.getDeck().size(), b.getDeck().size());
            }
        };
    }

    public static SortedSet<VehicleCard> removeBelowAvg(Collection<VehicleCard> cards){
        if(cards == null || cards.isEmpty())
            throw new IllegalArgumentException("cards can't be null or empty");

        int avg = 0;

        for(VehicleCard v: cards){
            avg+=v.totalBonus();
        }
        avg = avg/ cards.size();
        SortedSet<VehicleCard> result = new TreeSet<VehicleCard>();
       // List<VehicleCard> found = new ArrayList<>();
        List<VehicleCard> notfound = new ArrayList<>(cards);

        for(VehicleCard v: cards){
            if(v.totalBonus() < avg) {
                result.add(v);
                notfound.remove(v);
            }
        }
        cards.clear();
        cards.addAll(notfound);

        return result;
    }

    @Override
    public boolean removeCards(VehicleCard v) {
        return this.getDeck().remove(v);
    }
}

