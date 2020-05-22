   /**
     * @author esteban acosta
     */
public class Item implements Comparable<Item>
{

    private int weight;

    private int value;

    private int index;

    public Item(int value, int weight, int index)
    {

        setValue(value);
        setWeight(weight);

    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public double getValueOverWeight()
    {

        return this.value / this.weight;
    }

    @Override
    public int compareTo(Item o)
    {

        if (this.getValueOverWeight() > o.getValueOverWeight())
        {

            return 1;
        }

        else if (this.getValueOverWeight() < o.getValueOverWeight())
        {

            return -1;
        }

        else
        {

            return 0;
        }

    }

    public String toString()
    {

        return "Value:" + value;
    }

}
