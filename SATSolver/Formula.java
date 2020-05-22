
import java.util.ArrayList;
import java.util.Collections;

public class Formula
{

    private ArrayList<Clause> formula = new ArrayList<Clause>();

    /**
     * Default constructor of a Formula
     */
    public Formula()
    {

    }

    /**
     * Second constructor passes a list of clauses The Formula adds all the clauses
     * that are passed to it
     * @param clauses
     */
    public Formula(ArrayList<Clause> clauses)
    {

        formula.addAll(clauses);
    }

    /**
     * Adds a new clause to this Formula
     * @param newClause
     */
    public void addClause(Clause newClause)
    {

        formula.add(newClause);

    }

    /**
     * Removes a clause from this Formula
     * @param oldClause
     */
    public void removeClause(Clause oldClause)
    {

        formula.remove(oldClause);

    }

    /**
     * @return this Formula
     */

    public ArrayList<Clause> getClauses()
    {

        return this.formula;

    }

    /**
     * Retrieves a specific clause from this Formula
     * @param thisClause
     * @return
     */
    public Clause getClause(int thisClause)
    {

        return formula.get(thisClause);
    }

    /**
     * Gets the number of clauses in the Formula
     * @return
     */
    public int getNumClauses()
    {

        return formula.size();

    }

    public Clause getTruthAssignment()
    {

        // Create a new clause that will have all the distinct literals in the Formula
        Clause allLiteralsInFormula = new Clause();

        // This will store the literal unique identifiers
        ArrayList<Integer> varNums = new ArrayList<Integer>();

        // Loop through each clause in the Formula
        for (Clause eachClause : this.formula)
        {
            // Loop through each literal in that clause
            for (Literal eachLiteral : eachClause.getLiterals())
            {
                // if the literal unique identifier isn't already in varNums
                if (varNums.contains(eachLiteral.getVarNum()) == false)
                {
                    // add that literal unique identifier in varNums
                    varNums.add(eachLiteral.getVarNum());
                }
            }
        }

        // Sort the variable numbers in ascending order
        Collections.sort(varNums);

        // Loop through each value in varNums
        for (int varNum : varNums)
        {
            // Add a new literal with the unique literal identifier to the clause
            allLiteralsInFormula.addLiteral(new Literal(varNum, true));
        }

        return allLiteralsInFormula;
    }

    /**
     * Checks to see there are no clauses in this Formula
     * @return
     */
    public boolean isEmpty()
    {

        if (formula.size() == 0)
        {
            return true;
        }

        return false;
    }

    /**
     * Checks to see if there is at least one clause that is empty (it has no
     * literals)
     * @return true if there is at least one clause that has no literal false if
     *         there are no empty clauses
     */
    public boolean containsEmptyClause()
    {

        for (Clause eachClause : formula)
        {

            if (eachClause.isEmpty())
            {

                return true;

            }
        }

        return false;
    }

    /***
     * Checks to see if this Formula has a unit clause (a unit clause is a clause
     * that has exactly one literal)
     * @return true if it has a unit clause, false if it doesn't
     */
    public boolean containsUnitClause()
    {

        for (Clause eachClause : formula)
        {

            if (eachClause.getNumOfLiterals() == 1)
            {

                return true;

            }
        }

        return false;
    }

    /**
     * Checks to see if the Formula is satisfied by checking that every clause in
     * the Formula is satisfied
     * @return true if all the clauses are satisfied, false if there is at least one
     *         clause that's not satisfied
     */
    public boolean isSatisfied()
    {

        for (Clause eachClause : formula)
        {
            if (!eachClause.isSatisfied())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * String representation of a Formula This is known as CNF form This should
     * display a clause followed by an AND symbol (conjunction) Ex: (X1 v X2 ) ^ (X3
     * v X4)
     */
    public String toString()
    {

        String fullFormula = "";

        int numOfClauses = formula.size();

        for (int eachClause = 0; eachClause < numOfClauses; eachClause++)
        {

            if (eachClause != numOfClauses - 1)
            {

                fullFormula += formula.get(eachClause).toString() + " ^ ";
            }
            else
            {
                fullFormula += formula.get(eachClause);
            }

        }

        return fullFormula;
    }

    /**
     * String representation of a Formula This is known as CNF form This should
     * display a clause followed by an AND symbol (conjunction) Instead of
     * displaying the variable name, it should display the value that each literal
     * has been assigned to Ex: (true v false) ^ (false v false)
     */
    public String assignedValueToString()
    {

        String fullFormula = "";

        int numOfClauses = formula.size();

        for (int eachClause = 0; eachClause < numOfClauses; eachClause++)
        {

            if (eachClause != numOfClauses - 1)
            {

                fullFormula += formula.get(eachClause).assignedValueToString() + " ^ ";
            }
            else
            {
                fullFormula += formula.get(eachClause).assignedValueToString();
            }

        }

        return fullFormula;

    }

    /**
     * String representation of a Formula (This is known as CNF form) This should
     * display a clause followed by an AND symbol (conjunction) Instead of
     * displaying the variable name, it should display the value that each literal
     * is evaluated to What evaluated to means is that if a positive literal is
     * assigned to true, it will be evaluated to true. But if a negative literal is
     * assigned true, it will be evaluated to false Original Clausal Form : (X1 v
     * X2) ^ (~X2 V ~X3) Assigned Form : (true v false ) ^ (false v false) Evaluated
     * Form: (true v false) ^ (true v true)
     */
    public String evaluatedValueToString()
    {

        String fullFormula = "";

        int numOfClauses = formula.size();

        for (int eachClause = 0; eachClause < numOfClauses; eachClause++)
        {

            if (eachClause != numOfClauses - 1)
            {

                fullFormula += formula.get(eachClause).evaluatedValueToString() + " ^ ";
            }
            else
            {
                fullFormula += formula.get(eachClause).evaluatedValueToString();
            }

        }

        return fullFormula;

    }

}
