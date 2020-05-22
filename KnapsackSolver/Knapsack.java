import java.util.ArrayList;

public class Knapsack
{

    private ArrayList<Item> items;

    private ArrayList<Integer> weights;

    private ArrayList<Integer> values;

    private int targetWeight;

    public Knapsack(ArrayList<Item> items, int targetWeight)
    {

        setItems(items);

        setTargetWeight(targetWeight);

        setValues(items);

        setWeights(items);

    }

    public ArrayList<Item> getItems()
    {
        return this.items;
    }

    public void setItems(ArrayList<Item> items)
    {
        this.items = items;
    }

    public int getTargetWeight()
    {
        return targetWeight;
    }

    public void setTargetWeight(int targetWeight)
    {
        this.targetWeight = targetWeight;
    }

    public void replaceWeights(ArrayList<Integer> weights)
    {
        this.weights = weights;
    }

    public void setWeights(ArrayList<Item> items)
    {

        ArrayList<Integer> w = new ArrayList<Integer>();

        for (Item eachItem : items)
        {

            w.add(eachItem.getWeight());
        }

        this.weights = w;
    }

    public ArrayList<Integer> getWeights()
    {

        return this.weights;
    }

    public void setValues(ArrayList<Item> items)
    {

        ArrayList<Integer> v = new ArrayList<Integer>();

        for (Item eachItem : items)
        {

            v.add(eachItem.getValue());
        }

        this.values = v;

    }

    public ArrayList<Integer> getValues()
    {

        return this.values;
    }

    public String weightsToString()
    {

        String w = "";

        w += "Weights: {";

        int numWeights = 0;

        for (int eachW : weights)
        {

            if (numWeights == 0)
            {
                w += eachW;
            }
            else
            {
                w += " , " + eachW;
            }

            numWeights++;
        }

        w += "} \n\n";

        return w;
    }

    public String valuesToString()
    {

        String v = "";

        v += "Values: {";

        int numValues = 0;

        for (int eachV : values)
        {

            if (numValues == 0)
            {
                v += eachV;
            }
            else
            {
                v += " , " + eachV;
            }

            numValues++;
        }

        v += "} \n\n";

        return v;

    }

    public String toString()
    {

        String knapsack = valuesToString() + weightsToString() + "Target Weight: " + targetWeight + "\n";

        return knapsack;
    }
}
