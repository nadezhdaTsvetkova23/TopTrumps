package a11942924;

import java.util.*;

import static a11942924.Player.removeBelowAvg;
import static java.util.Arrays.asList;

public class MainTest {

    private static Map<VehicleCard.Category, VehicleCard> getMaxPerCategory(Collection<VehicleCard> cards){
        if(cards == null || cards.isEmpty())
            throw new IllegalArgumentException("cards can't be null or empty");

        Map<VehicleCard.Category, VehicleCard> result = new HashMap<>();

        for(VehicleCard v: cards){
            for(Map.Entry<VehicleCard.Category, Double> entry: v.getCategories().entrySet()){
                if(!result.containsKey(entry.getKey())){
                    result.put(entry.getKey(), v);
                }
                else
                    if(entry.getValue() > result.get(entry.getKey()).getCategories().get(entry.getKey()))
                        result.put(entry.getKey(), v);
            }
        }

        return result;

    }

    private static Collection<VehicleCard> removeBelow(Collection<VehicleCard> cards, Double val){
        if(cards == null || cards.isEmpty())
            throw new IllegalArgumentException("cards can't be null or empty");
        if(val == null || val <= 0.0)
            throw new IllegalArgumentException("val can't be null or 0");

        List<VehicleCard> notfound = new ArrayList<>(cards);
        List<VehicleCard> found = new ArrayList<>();
        for(VehicleCard v: cards){
            for(Map.Entry<VehicleCard.Category, Double> entry: v.getCategories().entrySet()){
                if(entry.getValue() < val){
                    found.add(v);
                    notfound.remove(v);
                    break;
                }
            }
        }
        cards.clear();
//        cards.addAll(notfound);
//        for(VehicleCard v: notfound){
//            cards.add(v);
//        }
        cards.addAll(notfound);

        found.sort(new Comparator<VehicleCard>() {
            @Override
            public int compare(VehicleCard a, VehicleCard b) {
                return b.getName().compareTo(a.getName());
            }
        });
        return found;
    }

    private static Map<VehicleCard.Category, VehicleCard> getMaxVehiclePerCategory(Player p){
        if(p == null || p.getDeck().isEmpty())
            throw new IllegalArgumentException("illegal player");

        Map<VehicleCard.Category, VehicleCard> result = new HashMap<>();

        //for(VehicleCard.Category c: VehicleCard.Category.values())
          //  result.put(c, null);

        //Double helper = 0.0;
        for(VehicleCard v: p.getDeck()){
            for(Map.Entry<VehicleCard.Category, Double> entry: v.getCategories().entrySet()){
                if(!result.containsKey(entry.getKey()))
                    result.put(entry.getKey(), v);
                else
                    if(entry.getValue() > result.get(entry.getKey()).getCategories().get(entry.getKey()))
                        result.put(entry.getKey(), v);
            }
        }
        return result;
    }

    private static Collection<Player> sortCollection(Collection<Player> col){
        List<Player> result = new ArrayList<>(col);

        result.sort(new Comparator<Player>(){
            @Override
            public int compare(Player a, Player b){
                return b.getName().compareTo(a.getName());//absteigend
            }
        });
        return result;
    }

    private static TreeMap<Integer, HashSet<String>> categorize1(List<Player> me){
        TreeMap<Integer, HashSet<String>> result = new TreeMap<>();

        HashSet<String> players_names = new HashSet<>();
        for(Player p: me){
            result.put(p.getDeck().size(), null);
        }
        for(Player p: me){
            for(Map.Entry<Integer, HashSet<String>> entry: result.entrySet()){
                if(p.getDeck().size() == entry.getKey()){
                    entry.getValue().add(p.getName());
                }
            }
        }
        return result;
    }

    private static TreeMap<Integer, List<VehicleCard>> categorize2(List<VehicleCard> l){
        TreeMap<Integer, List<VehicleCard>> result = new TreeMap<>();
        List<VehicleCard> myList = new ArrayList<>();

        for(VehicleCard v: l){
            result.put(v.totalBonus()/10, null);
        }
        for(VehicleCard v: l){
            for(Map.Entry<Integer, List<VehicleCard>> entry: result.entrySet()){
                if(v.totalBonus() >= entry.getKey() || v.totalBonus() <= (entry.getKey()+1)){
                    entry.getValue().add(v);
                }
            }
        }
        return result;

    }

    public static TreeMap<Integer,List<VehicleCard>> categorize(List<VehicleCard> l){
        TreeMap<Integer, List<VehicleCard>> result = new TreeMap<>();
        for(VehicleCard card: l) {
            result.get(card.totalBonus()/10).add(card);
        }
        return result;
    }

    public static void main(String[] args) {

        final VehicleCard ente = new VehicleCard("Ente", VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49));
        final FoilVehicleCard amphicar = new FoilVehicleCard("Amphicar 770", VehicleCard.newMap(21.0, 4., 1147., 38., 2314, 14., 61.), Set.of(VehicleCard.Category.DISPLACEMENT_CCM, VehicleCard.Category.WEIGHT_LBS));

        System.out.println(ente);
        System.out.println(amphicar);
        /**
         * Expected Output:
         - Ente(-798) -> {Gewicht[lbs]=1234.0, Hubraum[cc]=375.0, Leistung[hp]=9.0, Zylinder=4.0, Beschleunigung=21.0, Miles/Galon=20.4, Baujahr[19xx]=49.0}
         - Amphicar 770(2404) -> {*Gewicht[lbs]*=2314.0, *Hubraum[cc]*=1147.0, Leistung[hp]=38.0, Zylinder=4.0, Beschleunigung=14.0, Miles/Galon=21.0, Baujahr[19xx]=61.0}
         */

        Player otto = new Player("Otto");
        Player anna = new Player("Anna");

        anna.addCards(asList(ente, ente, amphicar));
        otto.addCards(asList(ente, ente, ente));

        System.out.println(otto.challengePlayer(anna));
        System.out.println(anna);
        System.out.println(otto);

        /**
         * Expected Output (order of Anna's cards may vary):
         false
         Anna(-1586):
         - Ente(-798) -> {Gewicht[lbs]=1234.0, Hubraum[cc]=375.0, Leistung[hp]=9.0, Zylinder=4.0, Beschleunigung=21.0, Miles/Galon=20.4, Baujahr[19xx]=49.0}
         - Ente(-798) -> {Gewicht[lbs]=1234.0, Hubraum[cc]=375.0, Leistung[hp]=9.0, Zylinder=4.0, Beschleunigung=21.0, Miles/Galon=20.4, Baujahr[19xx]=49.0}
         - Ente(-798) -> {Gewicht[lbs]=1234.0, Hubraum[cc]=375.0, Leistung[hp]=9.0, Zylinder=4.0, Beschleunigung=21.0, Miles/Galon=20.4, Baujahr[19xx]=49.0}
         - Ente(-798) -> {Gewicht[lbs]=1234.0, Hubraum[cc]=375.0, Leistung[hp]=9.0, Zylinder=4.0, Beschleunigung=21.0, Miles/Galon=20.4, Baujahr[19xx]=49.0}
         - Ente(-798) -> {Gewicht[lbs]=1234.0, Hubraum[cc]=375.0, Leistung[hp]=9.0, Zylinder=4.0, Beschleunigung=21.0, Miles/Galon=20.4, Baujahr[19xx]=49.0}
         - Amphicar 770(2404) -> {*Gewicht[lbs]*=2314.0, *Hubraum[cc]*=1147.0, Leistung[hp]=38.0, Zylinder=4.0, Beschleunigung=14.0, Miles/Galon=21.0, Baujahr[19xx]=61.0}
         Otto(0):
         */

        //my custom tests:
        try{
            //Constructor VehicleCard with empty name
            final VehicleCard n1 = new VehicleCard("", VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49));
        }
        catch (IllegalArgumentException e){
            System.out.println("Something went wrong1");
        }
        try {
            //Constructor VehicleCard with null name
            final VehicleCard n2 = new VehicleCard(null, VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49));
        }
        catch(IllegalArgumentException e){
            System.out.println("Something went wrong.2");
        }
        catch (Exception e){
            System.out.println("Something went wrong.2.1");
        }
        try{
            //Constructor VehicleCard with null categories
            final VehicleCard n2 = new VehicleCard("smth", null);
        }
        catch(IllegalArgumentException e){
            System.out.println("Something went wrong.3");
        }
        try {
            //Constructor VehicleCard - categories does not contain all category values
            Map<VehicleCard.Category, Double> m = new HashMap<VehicleCard.Category, Double>(1, 4);
            final VehicleCard n2 = new VehicleCard("smth", m);
        }
        catch (IllegalArgumentException e){
            System.out.println("Something went wrong.4");
        }
        //Constructor VehicleCard - categories contains more than 7 category values
        //and more tests for cstr needed

        try{
            //getName() test
            final VehicleCard n1 = new VehicleCard("n", VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49));
            if(n1.getName()=="n")
                System.out.println(true);
        }
        catch (Exception e){
            System.out.println("Something went wrong.");
        }
        try{
            //getCategories() test
            final VehicleCard n1 = new VehicleCard("n", VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49));
            if(n1.getCategories().equals(VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49)))
                System.out.println(true);
        }
        catch (Exception e){
            System.out.println("Something went wrong.");
        }
        //need to test the method newMap(...) in class VehicleCard

        final FoilVehicleCard nn = new FoilVehicleCard("N2", VehicleCard.newMap(21.0, 4., 1147., 38., 2314, 14., 61.), Set.of(VehicleCard.Category.ACCELERATION, VehicleCard.Category.ECONOMY_MPG));

        System.out.println("---------------");

        try {
            VehicleCard brum = new VehicleCard(null, VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49));
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 1");
        }

        try {
            VehicleCard wroom = new VehicleCard("", VehicleCard.newMap(20.4, 4., 375., 9., 1234., 21., 49));
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 2");
        }

        try {
            VehicleCard tut = new VehicleCard("Auto", VehicleCard.newMap(20.4, 4., 375., -9., 1234., 21., 49));
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 3");
        }

        try {
            Map<VehicleCard.Category, Double> help= new HashMap<VehicleCard.Category, Double>();
            help.put(VehicleCard.Category.ACCELERATION, 2.);
            help.put(VehicleCard.Category.POWER_HP, 2.);
            help.put(VehicleCard.Category.WEIGHT_LBS, 2.);
            help.put(VehicleCard.Category.DISPLACEMENT_CCM, 2.);

            VehicleCard car = new VehicleCard("Auto", help);
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 4");
        }

        try {
            Map<VehicleCard.Category, Double> help= new HashMap<VehicleCard.Category, Double>();
            help.put(VehicleCard.Category.ACCELERATION, 2.);
            help.put(VehicleCard.Category.POWER_HP, 2.);
            help.put(VehicleCard.Category.WEIGHT_LBS, 2.);
            help.put(VehicleCard.Category.DISPLACEMENT_CCM, 2.);
            help.put(VehicleCard.Category.CYLINDERS_CNT, 2.);
            help.put(VehicleCard.Category.ECONOMY_MPG, 2.);
            help.put(VehicleCard.Category.YEAR, null);


            VehicleCard car = new VehicleCard("Auto", help);
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 5");
        }

        try {
            VehicleCard tut = new VehicleCard("Auto", null);
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 6");
        }

        try {
            FoilVehicleCard hehehe = new FoilVehicleCard("Amphicar 770", VehicleCard.newMap(21.0, 4., 1147., 38., 2314, 14., 61.), Set.of(VehicleCard.Category.DISPLACEMENT_CCM, VehicleCard.Category.WEIGHT_LBS, VehicleCard.Category.ACCELERATION, VehicleCard.Category.ECONOMY_MPG));

        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 7");
        }

        try {
            FoilVehicleCard hehehe = new FoilVehicleCard("Amphicar 770", VehicleCard.newMap(21.0, 4., 1147., 38., 2314, 14., 61.), null);

        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 8");
        }
        try {
            Player spieler = new Player("");
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 9");
        }

        try {
            Player spieler = new Player(null);
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 10");
        }
        try {
            Player spieler = new Player("Joe");
            spieler.challengePlayer(spieler);
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 11");
        }
        try {
            Player spieler = new Player("Joe");
            spieler.challengePlayer(null);
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 12");
        }


        //Bonus - steht nicht in der Angabe!
        try {
            Set<VehicleCard.Category> n = new HashSet<VehicleCard.Category>();
            n.add(VehicleCard.Category.DISPLACEMENT_CCM);
            n.add(null);
            FoilVehicleCard hehehe = new FoilVehicleCard("Amphicar 770", VehicleCard.newMap(21.0, 4., 1147., 38., 2314, 14., 61.), n);
        }
        catch(IllegalArgumentException i) {
            System.out.println("Error 13");
        }

        final Map<VehicleCard.Category, Double> cardMap1 = VehicleCard.newMap(15.0, 4., 250., 8., 900., 20., 15);
        final Map<VehicleCard.Category, Double> cardMap2 = VehicleCard.newMap(16.5, 6., 300., 10., 900., 30., 5);
        final Map<VehicleCard.Category, Double> cardMap3 = VehicleCard.newMap(18.0, 2., 250., 9., 900., 25., 10);
        final Map<VehicleCard.Category, Double> cardMap4 = VehicleCard.newMap(13.5, 5., 300., 11., 900., 35., 3);
        final Map<VehicleCard.Category, Double> cardMap5 = VehicleCard.newMap(11.0, 7., 250., 7., 900., 23., 12);
        final Map<VehicleCard.Category, Double> cardMap6 = VehicleCard.newMap(14.5, 10., 300., 10., 900., 31., 6);

        VehicleCard card1 = new VehicleCard("Lada", cardMap1);
        VehicleCard card2 = new VehicleCard("Mazda", cardMap2);
        VehicleCard card3 = new VehicleCard("Opel", cardMap3);
        VehicleCard card4 = new VehicleCard("BMW", cardMap4);
        VehicleCard card5 = new VehicleCard("Opel", cardMap5);
        VehicleCard card6 = new VehicleCard("Toyota", cardMap6);

        Collection<VehicleCard> myCol = new ArrayList<>();
        myCol.add(card1);
        myCol.add(card2);
        myCol.add(card3);
        myCol.add(card4);
        myCol.add(card5);
        myCol.add(card6);
        myCol.add(card1);
        myCol.add(card2);
        myCol.add(card1);
        myCol.add(card2);

        Double d = 4.1;
        Collection<VehicleCard> result = removeBelow(myCol, d);

        for(VehicleCard v: result)
            System.out.println(v);

        for(VehicleCard v: myCol)
            System.out.println(v);

        Player st = new Player("Stertz");
        Player wanek = new Player("Wanek");
        st.addCard(card1);
        st.addCards(myCol);
        wanek.addCard(card2);
        wanek.addCard(card3);

        Collection<VehicleCard> ret = removeBelow(st.getDeck(), 5.0);
        for(VehicleCard v: ret)
            System.out.println(v);

        // for(VehicleCard v: st.getDeck())
        //   System.out.println(v);

        for(ListIterator<VehicleCard> i = st.getDeck().listIterator(); i.hasNext();){
            System.out.println(i.next());
        }
//        for(Map.Entry<VehicleCard.Category, VehicleCard> entry: getMaxVehiclePerCategory(st).entrySet()){
//            System.out.println(entry);
//        }

        HonoraryPlayer p1 = new HonoraryPlayer("A");
        HonoraryPlayer p2 = new HonoraryPlayer("B");
        HonoraryPlayer p3 = new HonoraryPlayer("C");

        System.out.println(p1);
        System.out.println(p1);
        System.out.println(p1);

        System.out.println(p2.getCount());
        System.out.println(p3.getCount());

        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(3, "val");
        map.put(2, "val");
        map.put(1, "val");
        map.put(5, "val");
        map.put(4, "val");

        Integer highestKey = map.lastKey();
        Integer lowestKey = map.firstKey();
        Set<Integer> keysLessThan3 = map.headMap(3).keySet();
        Set<Integer> keysGreaterThanEqTo3 = map.tailMap(3).keySet();


        System.out.println(removeBelowAvg(result));
        System.out.println(getMaxPerCategory(result));


    }
}
