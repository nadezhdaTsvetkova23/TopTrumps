package a11942924;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class VehicleCard implements Comparable<VehicleCard>, Pollutor {

    @Override
    public double calculatePollution() {
        double acc = 0.;
        double displ = 0.;
        for(Map.Entry<Category, Double> entry: getCategories().entrySet()){
            if(entry.getKey() == Category.ACCELERATION)
                acc = entry.getValue().doubleValue();
            if(entry.getKey() == Category.DISPLACEMENT_CCM)
                displ = entry.getValue().doubleValue();
        }

        return (1/acc)*displ;
    }

    public enum Category {
        ECONOMY_MPG("Miles/Galon"), CYLINDERS_CNT("Zylinder"),
        DISPLACEMENT_CCM("Hubraum[cc]"), POWER_HP("Leistung[hp]"),
        WEIGHT_LBS("Gewicht[lbs]"){
            @Override
            public boolean isInverted() {
                return true;
            }
        }, ACCELERATION("Beschleunigung"){
            @Override
            public boolean isInverted() {
                return true;
            }
        },
        YEAR("Baujahr[19xx]");

        private final String categoryName;

        private Category(final String categoryName) {
            if(categoryName == null || categoryName.isEmpty())
                throw new IllegalArgumentException("The name of category can't be null or empty");
            this.categoryName = categoryName;
        }

        public boolean isInverted() {
            return false;
        }
        public int bonus(final Double value) {
            //int result = this.isInverted() ? -(value.intValue()) : value.intValue();
            //return result;
            if(this.isInverted()) {
                int ret = value.intValue();
                return -ret;
            }
            else
                return value.intValue();
        }
        @Override
        public String toString () {
            return categoryName;
        }
    }

    private String name;
    private Map<Category, Double> categories;

    public VehicleCard(final String name, final Map<Category, Double> categories) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("the name can't be null or empty");
        if(categories == null)
            throw new IllegalArgumentException("the map with categories can't be null");
        if(categories.size() != 7)
            throw new IllegalArgumentException("the map doesn't contain all possible categories");
        for(Map.Entry<Category, Double> entry: categories.entrySet()){
            if(entry.getValue() == null || entry.getValue() < 0)
                throw new IllegalArgumentException("the map can't have null values or less than 0 values");
            if(entry.getKey() == null)
                throw new IllegalArgumentException("the key also can't be null");
        }
        this.name = name;
        this.categories = new LinkedHashMap<Category, Double>(categories);
    }

    // getters for _immutable_ class, no setters (!)
    public String getName() {
        return this.name;
    }

    public Map<Category, Double> getCategories() {
        return new LinkedHashMap<Category, Double>(categories);
    }

    public static Map<Category, Double> newMap(double economy, double cylinders, double displacement,
                                               double power, double weight, double acceleration,
                                               double year) {
        Map<Category, Double> resultMap = new LinkedHashMap<Category, Double>();
        resultMap.put(Category.ECONOMY_MPG, economy);
        resultMap.put(Category.CYLINDERS_CNT, cylinders);
        resultMap.put(Category.DISPLACEMENT_CCM, displacement);
        resultMap.put(Category.POWER_HP, power);
        resultMap.put(Category.WEIGHT_LBS, weight);
        resultMap.put(Category.ACCELERATION, acceleration);
        resultMap.put(Category.YEAR, year);

        return resultMap;
    }

    @Override
    public int compareTo(final VehicleCard other) {
        return this.totalBonus() - other.totalBonus();
//            if(this.totalBonus() > other.totalBonus())
//                return 1;
//            else if(this.totalBonus() < other.totalBonus())
//                return -1;
//            else
//                return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if(o instanceof VehicleCard){
            VehicleCard that = (VehicleCard) o;
            if(that.name.equals(this.name) && that.categories.equals(this.categories))
                return true;
        }
        return false;
        //return Objects.equals(name, that.name) && Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, totalBonus());
    }

    public int totalBonus () {
        int totalBonus = 0;
        for(Map.Entry<Category, Double> entry: this.categories.entrySet()){
            totalBonus+= entry.getKey().bonus(entry.getValue());
        }
        return totalBonus;
    }

    @Override
    public String toString() {
        return "- " + name +
                "(" + totalBonus() + ") -> " +
                categories;
    }
}

