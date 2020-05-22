import java.util.ArrayList;

   /**
     * @author esteban acosta
     */
   
public class Clause
{
 

    private ArrayList<Literal> clause = new ArrayList<Literal>();

    /**
     * Default Constructor
     */
    public Clause()
    {

    }

    /**
     * Second constructor that automatically adds a list of literals to the clause
     * @param newClause
     */
    public Clause(ArrayList<Literal> bunchOfLiterals)
    {

        clause.addAll(bunchOfLiterals);
    }

    /**
     * Adds a literal to the this clause
     * @param newLiteral
     */
    public void addLiteral(Literal newLiteral)
    {

        clause.add(newLiteral);

    }

    /**
     * Removes a literal from the this clause
     * @param oldLiteral
     */
    public void removeLiteral(Literal oldLiteral)
    {

        clause.remove(oldLiteral);

    }

    /**
     * Retrieves that specific literal from this clause
     * @param thisLiteral
     * @return literal at that position
     */
    public Literal getLiteral(int literalPosition)
    {

        return clause.get(literalPosition);

    }

    /**
     * Returns true if the literal is in the clause, false if it doesn't
     * @param thisLiteral
     * @return
     */
    public boolean containsLiteral(Literal thisLiteral)
    {

        for (Literal eachLiteral : clause)
        {

            if (eachLiteral.equals(thisLiteral))
            {
                return true;
            }
        }

        return false;

    }

    /**
     * Retrieves the number of literals in this clause
     * @return the number of literals in this clause
     */
    public int getNumOfLiterals()
    {

        return clause.size();
    }

    /**
     * Gets the literals that are in this clause
     * @return the clause
     */
    public ArrayList<Literal> getLiterals()
    {

        return clause;

    }

    /**
     * Checks to see if this clause has no literals
     * @return true if clause has no literal; false otherwise
     */
    public boolean isEmpty()
    {

        if (clause.size() == 0)
        {
            return true;
        }

        return false;
    }

    /**
     * If this clause has one literal, it is a unit clause
     * @return true if this has one literal , false if it doesn't have exactly one literal
     */
    public boolean isUnitClause()
    {

        return getNumOfLiterals() == 1 ? true : false;
    }

    /**
     * If there is at least one literal that is true, the clause is satisfied
     * If the clause is satisfied, the method will return true,
     * otherwise it will return false
     * @return true if clause is satisfied, otherwise it will return false
     */
    public boolean isSatisfied()
    {

        for (Literal eachLiteral : clause)
        {
            // If there is at least one unassigned literal in the clause
            // This clause isn't satisfied
            if (eachLiteral.isAssigned() == false)
            {
                return false;
            }

            // If the evaluated literal value is true, then the clause is satisfied
            if (eachLiteral.getEvaluatedLiteralValue())
            {
                return true;
            }
        }
        return false;
    }

    /***
     * String representation of the clause Should display a literal followed by a or
     * (disjunction) Ex: X1 V X2
     */
    public String toString()
    {

        String fullClause = "( ";

        int numOfLiterals = clause.size();

        for (int eachLiteral = 0; eachLiteral < numOfLiterals; eachLiteral++)
        {

            if (eachLiteral != numOfLiterals - 1)
            {

                fullClause += clause.get(eachLiteral).toString() + " v ";
            }

            else
            {
                fullClause += clause.get(eachLiteral).toString();
            }

        }

        fullClause += " )";

        return fullClause;
    }

    /**
     * String representation of the clause Should display a literal followed by a or
     * (disjunction) But instead of displaying the literal name, it should display
     * the value that each literal in this clause has been assigned to Ex: true V
     * false
     * @return
     */
    public String assignedValueToString()
    {

        String fullClause = "( ";

        int numOfLiterals = clause.size();

        for (int eachLiteral = 0; eachLiteral < numOfLiterals; eachLiteral++)
        {

            if (eachLiteral != numOfLiterals - 1)
            {

                if (clause.get(eachLiteral).isAssigned())
                {
                    fullClause += clause.get(eachLiteral).getAssignedLiteralValue() + " v ";
                }
                else
                {
                    fullClause += "null v ";
                }

            }

            else
            {
                if (clause.get(eachLiteral).isAssigned())
                {
                    fullClause += clause.get(eachLiteral).getAssignedLiteralValue();
                }
                else
                {
                    fullClause += "null";
                }

            }

        }

        fullClause += " )";

        return fullClause;

    }

    /**
     * String representation of the clause. Should display a literal followed by a
     * or (disjunction) But instead of displaying the literal name, it should
     * display the value that each literal in this clause has been evaluated to.
     * What evaluated to means is that if a positive literal is assigned to true, it
     * will be evaluated to true. But if a negative literal is assigned true, it
     * will be evaluated to false. Original Clausal Form : (~X2 V ~X3) Assigned Form
     * : (false v false) Evaluated Form: (true v true)
     */
    public String evaluatedValueToString()
    {
        String fullClause = "( ";

        int numOfLiterals = clause.size();

        for (int eachLiteral = 0; eachLiteral < numOfLiterals; eachLiteral++)
        {

            if (eachLiteral != numOfLiterals - 1)
            {

                fullClause += clause.get(eachLiteral).getEvaluatedLiteralValue() + " v ";
            }

            else
            {
                fullClause += clause.get(eachLiteral).getEvaluatedLiteralValue();
            }

        }

        fullClause += " )";

        return fullClause;
    }
}
