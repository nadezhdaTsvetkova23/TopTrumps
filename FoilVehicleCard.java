package a11942924;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.abs;

public class FoilVehicleCard extends VehicleCard {
    private Set<Category> specials;

    public FoilVehicleCard(final String name, final Map<Category, Double> categories,
                           final Set<Category> specials) {
        super(name, categories);
        if(specials == null || specials.isEmpty())
            throw new IllegalArgumentException("the set of categories can't be null or empty");
        //?!
        for(VehicleCard.Category sp: specials)
            if(sp == null)
                throw new IllegalArgumentException("special categories can't be null");
        if(specials.size() > 3)
            throw new IllegalArgumentException("the set of categories can't have more than 3 items");

        this.specials = new LinkedHashSet<Category>(specials);
    }

    public Set<Category> getSpecials() {
        return new LinkedHashSet<Category>(specials);
    }

    @Override
    public int totalBonus () {
        int resultTotalBonus = super.totalBonus();
        for(Category c: specials){
            for(Map.Entry<Category, Double> entry: this.getCategories().entrySet()){
                if(entry.getKey() == c){
                    resultTotalBonus+= abs(c.bonus(entry.getValue()));
                    break;
                }
            }
        }
        return resultTotalBonus;
    }

    public String toString () {
        boolean first = false;
        String result = "- " + getName() +
                "(" + totalBonus() + ") -> {";
        for(Map.Entry<Category, Double> entry: getCategories().entrySet()){
            if(first) {
                result+= ", ";
                if (specials.contains(entry.getKey()))
                    result += "*" + entry.getKey() + "*=" + entry.getValue();
                else
                    result += entry;
            }
            else {
                first = true;
                if (specials.contains(entry.getKey()))
                    result += "*" + entry.getKey() + "*=" + entry.getValue();
                else
                    result += entry;
            }
        }
        return result+= "}";
    }

    @Override
    public double calculatePollution() {
       return super.calculatePollution()*getCategories().size();
    }

}

