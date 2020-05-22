
   /**
     * @author esteban acosta
     */
public class Literal
{

    private Boolean assignedLiteralValue;

    private int varNum;

    private boolean positiveLiteral;

    private boolean assignedSoFar = false;

    /**
     * Default constructor passes a unique identifier to the literal and a boolean
     * that determines if it's a positive or negated literal
     * @param num
     */
    public Literal(int varNum, boolean positiveLiteral)
    {

        this.positiveLiteral = positiveLiteral;

        setVarNum(varNum);

    }

    /**
     * Second constructor passes a unique identifier to the literal a boolean
     * that determines if it's a positive or negated literal, and an assigned literal value
     * @param num
     */
    public Literal(int varNum, boolean positiveLiteral, boolean assignedLiteralValue)
    {

        this.positiveLiteral = positiveLiteral;

        setLiteralValue(assignedLiteralValue);

        setVarNum(varNum);

        // Make sure that since this literal is being automatically assigned a value, you set it to true
        this.assignedSoFar = true;

    }

    /**
     * Give the literal a unique identifier
     * @param num
     */
    public void setVarNum(int num)
    {

        this.varNum = num;

    }

    /**
     * Get this literal's unique identifier
     * @return
     */
    public int getVarNum()
    {

        return this.varNum;

    }

    /**
     * Assigns a value to this literal
     * @param literalValue
     */
    public void setLiteralValue(boolean assignedLiteralValue)
    {

        this.assignedSoFar = true;

        this.assignedLiteralValue = assignedLiteralValue;

    }

    /**
     * Changes this literal to false
     */
    public void setFalse()
    {

        this.assignedSoFar = true;

        this.assignedLiteralValue = false;

    }

    /**
     * Changes this literal to true
     */
    public void setTrue()
    {

        this.assignedSoFar = true;

        this.assignedLiteralValue = true;

    }

    /***
     * Changes this literal to a negated literal
     */
    public void becomeNegatedLiteral()
    {

        this.positiveLiteral = false;
    }

    /**
     * Changes this literal to a positive literal
     */
    public void becomePositiveLiteral()
    {
        this.positiveLiteral = true;
    }

    public void becomeOppositeKindOfLiteral()
    {
        this.positiveLiteral = !this.positiveLiteral;
    }

    /**
     * Retrieves the literal value of this literal
     * @return
     */
    public boolean getAssignedLiteralValue()
    {

        return this.assignedLiteralValue;

    }

    /**
     * Evaluates the assigned value of the literal
     * @return
     */
    public boolean getEvaluatedLiteralValue()
    {

        return isPositiveLiteral() ? getAssignedLiteralValue() : !getAssignedLiteralValue();
    }

    /**
     * @return true if the literal value is true, false if the literal is false
     */
    public boolean isTrue()
    {
        return assignedLiteralValue == true;
    }

    /**
     * @return true if the literal value is false, false if the literal is true
     */
    public boolean isFalse()
    {
        return assignedLiteralValue == false;
    }

    /***
     * Determines if this literal has been assigned
     * @return
     */
    public boolean isAssigned()
    {

        if (assignedLiteralValue == null)
        {

            return this.assignedSoFar = false;
        }

        return this.assignedSoFar = true;
    }

    /**
     * Determines if this literal is a positive literal or a negated literal
     * @return
     */
    public boolean isPositiveLiteral()
    {

        return this.positiveLiteral;

    }

    @Override
    public int hashCode()
    {
        return varNum + (isPositiveLiteral() ? 1 : 0);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }

        if (o == this)
        {
            return true;
        }

        if (!getClass().equals(o.getClass()))
        {
            return false;
        }

        Literal l = (Literal) o;

        return this.varNum == l.getVarNum() && this.isPositiveLiteral() == l.isPositiveLiteral();
    }

    /**
     * String representation of the literal It should have the variable name
     * followed by a unique identifier Ex: X1
     */
    public String toString()
    {
        return (isPositiveLiteral() ? "X" : "~X") + varNum;
    }

}
