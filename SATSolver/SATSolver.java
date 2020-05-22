import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SATSolver
{

    /**
     * Helper function for DPLL
     * @param formulaSoFar
     * @param truthAssignmentSoFar
     * @return
     */
    public List<Object> UnitPropagation(Formula formulaSoFar, Clause truthAssignmentSoFar)
    {
        int count = 1;

        // Create copies so that there won't be any weird errors with the original formula & truth assignment passed to this method
        Formula formula = copyFormula(formulaSoFar);

        Clause truthAssignment = copyClause(truthAssignmentSoFar);

        while (formula.containsUnitClause() && formula.containsEmptyClause() == false)
        {
            // System.out.println("TIME : " + count + " " + formula);

            Clause unitClause = new Clause();

            // loop through the formula to check to see which clause has a unit clause
            for (Clause eachClause : formula.getClauses())
            {

                // if that clause is a unit clause (has one literal)
                if (eachClause.isUnitClause())
                {

                    // store that clause
                    unitClause = eachClause;

                    // break out the loop
                    break;
                }
            }

            // Let x_j be the variable in some unit clause
            Literal x_j = unitClause.getLiteral(0);

            // if x_j appears positively in the unit clause
            if (x_j.isPositiveLiteral())
            {

                // loop through each literal in truthAssignmentSoFar
                for (Literal eachLiteral : truthAssignment.getLiterals())
                {

                    // if x_j and the literal in truthAssignmentSoFar are the same
                    if (x_j.getVarNum() == eachLiteral.getVarNum())
                    {

                        // add x_j = TRUE to truthAssignmentSoFar
                        eachLiteral.setTrue();

                        break;
                    }
                }

                // Positive version of x_j
                Literal positiveX_J = x_j;

                // Negated version of x_j ( ~x_j)
                Literal negatedX_J = new Literal(x_j.getVarNum(), false);

                Iterator<Clause> clauses = formula.getClauses().iterator();

                // Remove every clause that contains x_j from formula
                while (clauses.hasNext())
                {

                    Clause nextClause = clauses.next();

                    if (nextClause.containsLiteral(positiveX_J))
                    {

                        clauses.remove();

                    }
                }

                // Remove ~x_j from every clause in formula containing ~x_j
                for (Clause eachClause : formula.getClauses())
                {

                    Iterator<Literal> literals = eachClause.getLiterals().iterator();

                    while (literals.hasNext())
                    {

                        Literal nextLiteral = literals.next();

                        if (nextLiteral.equals(negatedX_J))
                        {

                            literals.remove();
                        }
                    }
                }

            }

            else
            {

                // loop through each literal in the truth assignment
                for (Literal eachLiteral : truthAssignment.getLiterals())
                {

                    // if x_j and the literal in truthAssigmentSoFar are the same
                    if (x_j.getVarNum() == eachLiteral.getVarNum())
                    {

                        // add x_j = FALSE to truthAssignmentSoFar
                        eachLiteral.setFalse();

                        break;

                    }
                }

                // Positive version of x_j
                Literal positiveX_J = new Literal(x_j.getVarNum(), true);

                // Negated version of x_j ( ~x_j)
                Literal negatedX_J = x_j;

                Iterator<Clause> clauses = formula.getClauses().iterator();

                // Remove every clause that contains ~x_j from formula
                while (clauses.hasNext())
                {
                    Clause nextClause = clauses.next();

                    if (nextClause.containsLiteral(negatedX_J))
                    {

                        clauses.remove();

                    }
                }

                // Remove x_j from every clause in formula containing x_j
                for (Clause nextClause : formula.getClauses())
                {

                    Iterator<Literal> literals = nextClause.getLiterals().iterator();

                    while (literals.hasNext())
                    {

                        Literal nextLiteral = literals.next();

                        if (nextLiteral.equals(positiveX_J))
                        {

                            literals.remove();
                        }
                    }
                }

            }

            count++;
        }
        return Arrays.asList(formula, truthAssignment);
    }

    /***
     * DPLL algorithm
     * @param formulaSoFar
     * @param truthAssignmentSoFar
     * @return
     */
    public Clause DPLL(Formula formulaSoFar, Clause truthAssignmentSoFar)
    {

        // Call unit propagation
        List<Object> UnitPropagationResults = UnitPropagation(formulaSoFar, truthAssignmentSoFar);

        // Set newFormula to the formula returned by UnitPropagation
        Formula newFormula = (Formula) UnitPropagationResults.get(0);

        // Set newAssignment to the truth assignment returned by UnitPropagation
        Clause newAssignment = (Clause) UnitPropagationResults.get(1);

        if (newFormula.isEmpty())
        {

            return newAssignment;

        }

        if (newFormula.containsEmptyClause())
        {

            // should return an unassigned truth assignment (the equivalent of returning null)
            return newAssignment;

        }

        // Let xi be the first variable that is not assigned in newAssigment
        Literal x_i = null;

        for (Literal eachLiteral : newAssignment.getLiterals())
        {
            if (eachLiteral.isAssigned() == false)
            {
                x_i = new Literal(eachLiteral.getVarNum(), eachLiteral.isPositiveLiteral());
                break;
            }
        }

        // Create a new clause
        Clause newClause = new Clause();

        // Add x_i to the new clause
        newClause.addLiteral(x_i);

        // Add clause (x_i) to newFormula
        newFormula.addClause(newClause);

        // Add xi = TRUE to new assignment
        for (Literal eachLiteral : newAssignment.getLiterals())
        {
            if (x_i.getVarNum() == eachLiteral.getVarNum())
            {
                eachLiteral.setTrue();
            }
        }

        // Set result to what this method returns
        Clause result = DPLL(newFormula, newAssignment);

        boolean satisfiable = false;

        // If all of the literals in the truth assignment have been assigned
        if (isAllAreAssignedInClause(result))
        {
            // then assign the values to the formula
            Formula assignedFormula = getAssignedFormula(newFormula, result);

            // if the formula is satisfied
            if (assignedFormula.isSatisfied())
            {
                // set the boolean to true
                satisfiable = true;
            }
            // Otherwise
            else
            {
                // set it to false
                satisfiable = false;
            }
        }

        // If the not all of the literals in the truth assignment has been assigned, set satisfiability to false
        else
        {

            satisfiable = false;

        }

        if (satisfiable)
        {

            return result;

        }

        else
        {

            // replace ( x_i ) with ( ~x_i ) in new formula
            for (Clause eachClause : newFormula.getClauses())
            {

                if (eachClause.isUnitClause())
                {
                    eachClause.getLiteral(0).becomeOppositeKindOfLiteral();
                }
            }

            // Replace the literal x_i = true to x_i = false in new assignment
            for (Literal eachLiteral : newAssignment.getLiterals())
            {
                if (eachLiteral.getVarNum() == x_i.getVarNum())
                {

                    eachLiteral.setFalse();

                }
            }

            return DPLL(newFormula, newAssignment);

        }
    }

    /***
     * GSAT algorithm
     * @param formula
     * @param originalTruthAssignment
     * @return
     */
    public Clause GSAT(Formula formula, Clause originalTruthAssignment)
    {

        Clause truthAssignmentSoFar = copyClause(originalTruthAssignment);

        Clause bestTruthAssignmentSoFar = new Clause();
        // Keeps track of how many tries
        int numOfTries = 0;

        // Max # of tries;
        int maxTries = 20;

        // Keeps track of how many flips
        int numOfFlips = 0;

        // Max # of flips
        int maxFlips = 20;

        // Continue looping until we either have found the truth assignment that works
        // for the given formula or when we have exceeded the maximum number of tries
        while (numOfTries < maxTries)
        {

            // Get a random truth assignment
            truthAssignmentSoFar = getRandomTruthAssignment(truthAssignmentSoFar);

            // Start by assigning the literal values to the formula based on the given truth assignment
            formula = getAssignedFormula(formula, truthAssignmentSoFar);

            // Find the number of unsatisfied clauses in the formula
            int minUnsatisfiedClauses = howManyUnsatisfiedClauses(formula, truthAssignmentSoFar);

            // Store the truth assignment with the least amount of unsatisfied clauses
            bestTruthAssignmentSoFar = truthAssignmentSoFar;

            // If the formula is satisfied then return the random truth assignment
            if (formula.isSatisfied())
            {

                return bestTruthAssignmentSoFar;
            }

            while (numOfFlips < maxFlips)
            {

                // Loop through the truthAsssignment
                for (int whichLiteral = 0; whichLiteral < truthAssignmentSoFar.getNumOfLiterals(); whichLiteral++)
                {

                    // Copy the original truth assignment
                    Clause thisRandTruthAssignment = copyClause(truthAssignmentSoFar);

                    // Store the literal we are going to flip
                    boolean literalValForThisLiteral = thisRandTruthAssignment.getLiteral(whichLiteral).getAssignedLiteralValue();

                    // Take the literal in that position and flip its assignment (so true -> false & false -> true)
                    thisRandTruthAssignment.getLiteral(whichLiteral).setLiteralValue(!literalValForThisLiteral);

                    // Assign a value to each literal in the formula based on the literals assignment in the truth assignment
                    formula = getAssignedFormula(formula, thisRandTruthAssignment);

                    // Count how many unsatisfied clauses there are in the assigned formula
                    int countUnsatisfiedClauses = howManyUnsatisfiedClauses(formula, thisRandTruthAssignment);

                    // if the # of unsatisfied clauses is less than the current minimum
                    if (countUnsatisfiedClauses < minUnsatisfiedClauses)
                    {

                        // Set the bestTruthAssignmentSoFar to that truth assignment
                        bestTruthAssignmentSoFar = thisRandTruthAssignment;

                        // Set the minimum number of satisfied of clauses to the current # of unsatisfied clauses
                        minUnsatisfiedClauses = countUnsatisfiedClauses;

                        // If the truth assignment satisfies this formula (no unsatisfied clauses)
                        if (formula.isSatisfied())
                        {

                            return bestTruthAssignmentSoFar;
                        }

                    }

                }

                numOfFlips++;

                // Make sure that we set the new assignment to be the best truth assignment for
                // the next iteration
                truthAssignmentSoFar = bestTruthAssignmentSoFar;

            }

            numOfFlips = 0;

            numOfTries++;

        }

        return bestTruthAssignmentSoFar;
    }

    /***
     * The simple randomized 7/8 -approximation algorithm for MAX3SAT.
     * @return
     */

    public Clause RandomApproximation(Clause truthAssignment)
    {

        return getRandomTruthAssignment(truthAssignment);

    }

    /**
     * Creates one random sat instance.
     * @return a sat instance
     */
    public Formula createRandom3SATInstance()
    {

        Random rand = new Random();

        // Create an array list of clauses;
        ArrayList<Clause> clauses = new ArrayList<Clause>();

        // Randomize the # of clauses this sat instance is going to have
        int randNumOfClauses = rand.nextInt(20) + 20;

        // Create a random number of clauses
        // Now loop through as many times as there are clauses
        for (int nextClause = 0; nextClause < randNumOfClauses; nextClause++)
        {

            // Create an array list of literals (this will have only three literals)
            ArrayList<Literal> literals = new ArrayList<Literal>();

            // Make sure we only create three literals per clause (3SAT)
            for (int nextLiteral = 0; nextLiteral < 3; nextLiteral++)
            {

                // Add the new literal with its new random variable number and boolean variable
                literals.add(new Literal(rand.nextInt(15) + 1, rand.nextBoolean()));
            }

            // Continue assigning random variable numbers until all three literals in the clause
            // don't have the same variable number
            while (literals.get(0).getVarNum() == literals.get(1).getVarNum()
            || literals.get(0).getVarNum() == literals.get(2).getVarNum()
            || literals.get(1).getVarNum() == literals.get(2).getVarNum()
            || literals.get(0).getVarNum() == literals.get(1).getVarNum() && literals.get(1).getVarNum() == literals.get(2).getVarNum())
            {

                // loop thru each literal in the clause
                for (Literal eachLiteral : literals)
                {

                    // set a random variable for each literal
                    eachLiteral.setVarNum(rand.nextInt(15) + 1);
                }
            }

            // Add the three new random literals into this clause
            clauses.add(new Clause(literals));
        }

        // Add all the clauses that have been created into this SAT instance
        return new Formula(clauses);

    }

    public static void main(String[] args)
    {

        SATSolver satSolver = new SATSolver();

        System.out.println("REPORT FOR SAT ALGORITHMS:");
        System.out.println();

        ArrayList<Formula> SATinstances = new ArrayList<Formula>();

        ArrayList<Long> DPLLrunTimes = new ArrayList<Long>();

        ArrayList<Long> GSATrunTimes = new ArrayList<Long>();

        ArrayList<Long> RandApproxRunTimes = new ArrayList<Long>();

        // create 100 3SAT instances
        for (int numOfSats = 0; numOfSats < 100; numOfSats++)
        {

            SATinstances.add(satSolver.createRandom3SATInstance());

        }

        int numSatisfiedForGSAT = 0, numSatisfiedForDPLL = 0, numSatisfiedForRandApprox = 0;

        ArrayList<Integer> numSatisfiedInUnsatisfiedForGSAT = new ArrayList<Integer>();
        ArrayList<Integer> numSatisfiedInUnsatisfiedForDPLL = new ArrayList<Integer>();
        ArrayList<Integer> numSatisfiedInUnsatisfiedForRandApprox = new ArrayList<Integer>();

        int which3SAT = 1;

        // Call GSAT, randomApproximation, and DPLL on each 3SAT instance
        for (Formula each3SAT : SATinstances)
        {
            System.out.println("Number of Clauses For This 3SAT Instance: " + each3SAT.getNumClauses());
            System.out.println("3SAT instance #" + which3SAT + ": " + each3SAT);
            System.out.println();

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // DPLL
            System.out.println("DPLL:");

            // get the start time
            long startDPLL = System.currentTimeMillis();

            Clause DPLLSolution = satSolver.DPLL(each3SAT, each3SAT.getTruthAssignment());

            // get the finish time
            long finishDPLL = System.currentTimeMillis();

            // Let's make sure that DPLL returned a fully assigned truth assignment ( meaning that each literal in the truth assignment has an assigned value)
            if (satSolver.isAllAreAssignedInClause(DPLLSolution))
            {
                each3SAT = satSolver.getAssignedFormula(each3SAT, DPLLSolution);
                System.out.println("Is this 3SAT instance satisfied when passed to DPLL? " + (each3SAT.isSatisfied() ? "SATISFIED" : "UNSASTIFIED"));

                if (each3SAT.isSatisfied())
                {

                    numSatisfiedForDPLL++;
                }
                else
                {

                    int numUnsatisfiedClauses = satSolver.howManyUnsatisfiedClauses(each3SAT, DPLLSolution);

                    int maxNumSatisfied = each3SAT.getNumClauses() - numUnsatisfiedClauses;

                    numSatisfiedInUnsatisfiedForDPLL.add(maxNumSatisfied);

                }

            }

            else
            {
                System.out.println("Is this 3SAT instance satisfied when passed to DPLL? UNSASTIFIED");

            }

            System.out.println("Truth Assignment: " + each3SAT.getTruthAssignment());
            System.out.println("Truth Assignment Assigned Values For DPLL: " + DPLLSolution.assignedValueToString());

            long elapsedTimeDPLL = finishDPLL - startDPLL;

            System.out.println("Elapsed Time For DPLL: " + elapsedTimeDPLL + (elapsedTimeDPLL != 1 ? " milliseconds \n" : " millisecond \n"));

            // Add this runtime to the list of DPLL run times
            DPLLrunTimes.add(elapsedTimeDPLL);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // GSAT
            System.out.println("GSAT:");

            // get the start time
            long startGSAT = System.currentTimeMillis();

            Clause GSATSolution = satSolver.GSAT(each3SAT, each3SAT.getTruthAssignment());

            // get the finish time
            long finishGSAT = System.currentTimeMillis();

            each3SAT = satSolver.getAssignedFormula(each3SAT, GSATSolution);

            System.out.println("Is this 3SAT instance satisfied when passed to GSAT? " + (each3SAT.isSatisfied() ? "SATISFIED" : "UNSATISFIED"));
            System.out.println("Truth Assignment Literals: " + each3SAT.getTruthAssignment());
            System.out.println("Truth Assignment Assigned Values For GSAT: " + GSATSolution.assignedValueToString());

            if (each3SAT.isSatisfied())
            {
                numSatisfiedForGSAT++;
            }
            else
            {

                int numUnsatisfiedClauses = satSolver.howManyUnsatisfiedClauses(each3SAT, GSATSolution);

                int maxNumSatisfied = each3SAT.getNumClauses() - numUnsatisfiedClauses;

                numSatisfiedInUnsatisfiedForGSAT.add(maxNumSatisfied);

            }

            long elapsedTimeGSAT = finishGSAT - startGSAT;

            System.out.println("Elapsed Time For GSAT: " + elapsedTimeGSAT + (elapsedTimeGSAT != 1 ? " milliseconds \n" : " millisecond \n"));

            // Add this runtime to the list of GSAT run times
            GSATrunTimes.add(elapsedTimeGSAT);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // Random Approximation
            System.out.println("Random Approximation:");

            long startRandApprox = System.currentTimeMillis();

            Clause RandApproxSolution = satSolver.RandomApproximation(each3SAT.getTruthAssignment());

            long finishRandApprox = System.currentTimeMillis();

            each3SAT = satSolver.getAssignedFormula(each3SAT, RandApproxSolution);

            System.out.println("Is this 3SAT instance satisfied when passed to Random Approximation? " + (each3SAT.isSatisfied() ? "SATISFIED" : "UNSATISFIED"));
            System.out.println("Truth Assignment Literals: " + each3SAT.getTruthAssignment());
            System.out.println("Truth Assignment Assigned Values For Random Approx.: " + RandApproxSolution.assignedValueToString());

            long elapsedTimeRandApprox = finishRandApprox - startRandApprox;

            System.out.println("Elapsed Time For Random Approximation: " + elapsedTimeRandApprox + (elapsedTimeRandApprox != 1 ? " milliseconds \n" : " millisecond \n"));

            System.out.println("-------------------------------------------------------------------------");

            if (each3SAT.isSatisfied())
            {
                numSatisfiedForRandApprox++;
            }
            else
            {
                int numUnsatisfiedClauses = satSolver.howManyUnsatisfiedClauses(each3SAT, RandApproxSolution);

                int maxNumSatisfied = each3SAT.getNumClauses() - numUnsatisfiedClauses;

                numSatisfiedInUnsatisfiedForRandApprox.add(maxNumSatisfied);

            }

            // Add this runtime to the list of Rand Approx run times
            RandApproxRunTimes.add(elapsedTimeRandApprox);

            which3SAT++;
        }

        System.out.println("GENERAL STATUS REPORT: ");
        System.out.println("The # of satisfied DPLL instances: " + numSatisfiedForDPLL);
        System.out.println("The # of satisifed GSAT instances: " + numSatisfiedForGSAT);
        System.out.println("The # of satisfied Random Approximation instances: " + numSatisfiedForRandApprox);
        System.out.println();
        System.out.println("The # of unsatisfied DPLL instances: " + (SATinstances.size() - numSatisfiedForDPLL));
        System.out.println("The # of unsatisfied GSAT instances: " + (SATinstances.size() - numSatisfiedForGSAT));
        System.out.println("The # of unsatisfied Random Approximation instances: " + (SATinstances.size() - numSatisfiedForRandApprox));
        System.out.println();

        satSolver.RunTimeStatusReport(SATinstances, DPLLrunTimes, GSATrunTimes, RandApproxRunTimes);
        satSolver.UnsatisfiedStatusReport(SATinstances, numSatisfiedInUnsatisfiedForGSAT, numSatisfiedInUnsatisfiedForDPLL, numSatisfiedInUnsatisfiedForRandApprox);
    }

    public void RunTimeStatusReport(ArrayList<Formula> SATinstances, ArrayList<Long> DPLLrunTimes, ArrayList<Long> GSATrunTimes, ArrayList<Long> RandApproxRunTimes)
    {

        long maxGSATRunTime = 0, maxDPLLRunTime = 0, maxRandApproxRunTime = 0;
        long minGSATRunTime = 0, minDPLLRunTime = 0, minRandApproxRunTime = 0;
        double avgGSATRunTime = 0, avgDPLLRunTime = 0, avgRandApproxRunTime = 0;
        double medianGSATRunTime = 0, medianDPLLRunTime = 0, medianRandApproxRunTime = 0;

        Collections.sort(DPLLrunTimes);
        Collections.sort(GSATrunTimes);
        Collections.sort(RandApproxRunTimes);

        for (int i = 0; i < SATinstances.size(); i++)
        {

            avgDPLLRunTime += DPLLrunTimes.get(i);
            avgGSATRunTime += GSATrunTimes.get(i);
            avgRandApproxRunTime += RandApproxRunTimes.get(i);

        }
        // Since we've only take the sum of the runtimes we need to take the sum and divide it by the number of instances
        avgDPLLRunTime /= SATinstances.size();

        avgGSATRunTime /= SATinstances.size();

        avgRandApproxRunTime /= SATinstances.size();

        // Use collections to find the max and min run time of the list of running times for GSAT, DPLL, and Rand Approx
        maxDPLLRunTime = Collections.max(DPLLrunTimes);
        minDPLLRunTime = Collections.min(DPLLrunTimes);

        maxGSATRunTime = Collections.max(GSATrunTimes);
        minGSATRunTime = Collections.min(GSATrunTimes);

        maxRandApproxRunTime = Collections.max(RandApproxRunTimes);
        minRandApproxRunTime = Collections.min(RandApproxRunTimes);

        // To find the median run time, we need to first determine if there is an even # of SAT instances or an odd # of SAT instances
        // if there is an even # of instances, we need to take the average of the two middle values.
        // If there is an odd # of instances, we need to take the middle value
        if (SATinstances.size() % 2 == 0)
        {

            int middleNum = SATinstances.size() / 2;
            medianDPLLRunTime = (long) ((DPLLrunTimes.get(middleNum) + DPLLrunTimes.get(middleNum - 1)) / 2.0);
            medianGSATRunTime = (long) ((GSATrunTimes.get(middleNum) + GSATrunTimes.get(middleNum - 1)) / 2.0);
            medianRandApproxRunTime = (long) ((RandApproxRunTimes.get(middleNum) + RandApproxRunTimes.get(middleNum - 1)) / 2.0);

        }

        else
        {
            int middleNum = SATinstances.size() / 2;
            medianDPLLRunTime = DPLLrunTimes.get(middleNum);
            medianGSATRunTime = GSATrunTimes.get(middleNum);
            medianRandApproxRunTime = RandApproxRunTimes.get(middleNum);
        }

        System.out.println("DPLL Runtime:");
        System.out.println("Max RunTime For DPLL: " + maxDPLLRunTime + (maxDPLLRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Min RunTime For DPLL: " + minDPLLRunTime + (minDPLLRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Avg RunTime For DPLL: " + avgDPLLRunTime + (avgDPLLRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Median RunTime For DPLL: " + medianDPLLRunTime + (medianDPLLRunTime != 1 ? " milliseconds \n" : " millisecond \n"));

        System.out.println("GSAT Runtime:");
        System.out.println("Max RunTime For GSAT: " + maxGSATRunTime + (maxGSATRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Min RunTime For GSAT: " + minGSATRunTime + (minGSATRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Avg RunTime For GSAT: " + avgGSATRunTime + (avgGSATRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Median RunTime For GSAT: " + medianGSATRunTime + (medianGSATRunTime != 1 ? " milliseconds \n" : " millisecond \n"));

        System.out.println("Random Approximation Runtime:");
        System.out.println("Max RunTime For Random Approximation: " + maxRandApproxRunTime + (maxRandApproxRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Min RunTime For Random Approximation: " + minRandApproxRunTime + (minRandApproxRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Avg RunTime For Random Approximation: " + avgRandApproxRunTime + (avgRandApproxRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Median RunTime For Random Approximation: " + medianRandApproxRunTime + (medianRandApproxRunTime != 1 ? " milliseconds" : " millisecond\n"));

    }

    public void UnsatisfiedStatusReport(ArrayList<Formula> SATinstances, ArrayList<Integer> numSatisfiedInUnsatisfiedForGSAT, ArrayList<Integer> numSatisfiedInUnsatisfiedForDPLL, ArrayList<Integer> numSatisfiedInUnsatisfiedForRandApprox)
    {

        System.out.println("\nUNSATISFIED TRUTH ASSIGNMENTS STATUS REPORT: ");

        double avgNumOfSatisfiedInUnSatisfiedGSAT = 0, avgNumOfSatisfiedInUnSatisfiedDPLL = 0, avgNumOfSatisfiedInUnSatisfiedRandApprox = 0;
        int maxNumOfSatisfiedInUnSatisfiedGSAT = 0, maxNumOfSatisfiedInUnSatisfiedDPLL = 0, maxNumOfSatisfiedInUnSatisfiedRandApprox = 0;
        int minNumOfSatisfiedInUnSatisfiedGSAT = 0, minNumOfSatisfiedInUnSatisfiedDPLL = 0, minNumOfSatisfiedInUnSatisfiedRandApprox = 0;
        double medianNumOfSatisfiedInUnSatisfiedGSAT = 0, medianNumOfSatisfiedInUnSatisfiedDPLL = 0, medianNumOfSatisfiedInUnSatisfiedRandApprox = 0;

        Collections.sort(numSatisfiedInUnsatisfiedForDPLL);
        Collections.sort(numSatisfiedInUnsatisfiedForGSAT);
        Collections.sort(numSatisfiedInUnsatisfiedForRandApprox);

        // if DPLL returns at least one unsatisfying truth assignment
        if (numSatisfiedInUnsatisfiedForDPLL.size() > 0)
        {
            // Find an unsatisfied formula that gives the most number of satisfied clauses
            maxNumOfSatisfiedInUnSatisfiedDPLL = Collections.max(numSatisfiedInUnsatisfiedForDPLL);

            // Find an unsatisfied formula that gives the least number of satisfied clauses
            minNumOfSatisfiedInUnSatisfiedDPLL = Collections.min(numSatisfiedInUnsatisfiedForDPLL);

            for (int i = 0; i < numSatisfiedInUnsatisfiedForDPLL.size(); i++)
            {

                avgNumOfSatisfiedInUnSatisfiedDPLL += numSatisfiedInUnsatisfiedForDPLL.get(i);

            }
            // Find the average number of satisfied clauses in all of the unsatisfied formulas
            avgNumOfSatisfiedInUnSatisfiedDPLL /= numSatisfiedInUnsatisfiedForDPLL.size();

            // Find the median number of satisfied clauses in all of the unsatisfied formulas
            if (numSatisfiedInUnsatisfiedForDPLL.size() % 2 == 0)
            {

                int middleNum = numSatisfiedInUnsatisfiedForDPLL.size() / 2;
                medianNumOfSatisfiedInUnSatisfiedDPLL = ((numSatisfiedInUnsatisfiedForDPLL.get(middleNum) + numSatisfiedInUnsatisfiedForDPLL.get(middleNum - 1)) / 2.0);

            }

            else
            {
                int middleNum = numSatisfiedInUnsatisfiedForDPLL.size() / 2;
                medianNumOfSatisfiedInUnSatisfiedDPLL = numSatisfiedInUnsatisfiedForDPLL.get(middleNum);

            }
        }

        // if GSAT returns at least one unsatisfying truth assignment
        if (numSatisfiedInUnsatisfiedForGSAT.size() > 0)
        {

            // Find an unsatisfied formula that gives the most number of satisfied clauses
            maxNumOfSatisfiedInUnSatisfiedGSAT = Collections.max(numSatisfiedInUnsatisfiedForGSAT);

            // Find an unsatisfied formula that gives the least number of satisfied clauses
            minNumOfSatisfiedInUnSatisfiedGSAT = Collections.min(numSatisfiedInUnsatisfiedForGSAT);

            for (int i = 0; i < numSatisfiedInUnsatisfiedForGSAT.size(); i++)
            {

                avgNumOfSatisfiedInUnSatisfiedGSAT += numSatisfiedInUnsatisfiedForGSAT.get(i);

            }

            // Find the average number of satisfied clauses in all of the unsatisfied formulas
            avgNumOfSatisfiedInUnSatisfiedGSAT /= numSatisfiedInUnsatisfiedForGSAT.size();

            // Find the median number of satisfied clauses in all of the unsatisfied formulas
            if (numSatisfiedInUnsatisfiedForGSAT.size() % 2 == 0)
            {

                int middleNum = numSatisfiedInUnsatisfiedForGSAT.size() / 2;
                medianNumOfSatisfiedInUnSatisfiedGSAT = ((numSatisfiedInUnsatisfiedForGSAT.get(middleNum) + numSatisfiedInUnsatisfiedForGSAT.get(middleNum - 1)) / 2.0);

            }

            else
            {
                int middleNum = numSatisfiedInUnsatisfiedForGSAT.size() / 2;
                medianNumOfSatisfiedInUnSatisfiedGSAT = numSatisfiedInUnsatisfiedForGSAT.get(middleNum);

            }

        }

        // if Rand Approx returns at least one unsatisfying truth assignment
        if (numSatisfiedInUnsatisfiedForRandApprox.size() > 0)
        {

            // Find an unsatisfied formula that gives the most number of satisfied clauses
            maxNumOfSatisfiedInUnSatisfiedRandApprox = Collections.max(numSatisfiedInUnsatisfiedForRandApprox);

            // Find an unsatisfied formula that gives the least number of satisfied clauses
            minNumOfSatisfiedInUnSatisfiedRandApprox = Collections.min(numSatisfiedInUnsatisfiedForRandApprox);

            for (int i = 0; i < numSatisfiedInUnsatisfiedForRandApprox.size(); i++)
            {

                avgNumOfSatisfiedInUnSatisfiedRandApprox += numSatisfiedInUnsatisfiedForRandApprox.get(i);

            }

            // Find the average number of satisfied clauses in all of the unsatisfied formulas
            avgNumOfSatisfiedInUnSatisfiedRandApprox /= numSatisfiedInUnsatisfiedForRandApprox.size();

            // Find the median number of satisfied clauses in all of the unsatisfied formulas
            if (numSatisfiedInUnsatisfiedForRandApprox.size() % 2 == 0)
            {

                int middleNum = numSatisfiedInUnsatisfiedForRandApprox.size() / 2;
                medianNumOfSatisfiedInUnSatisfiedRandApprox = ((numSatisfiedInUnsatisfiedForRandApprox.get(middleNum) + numSatisfiedInUnsatisfiedForRandApprox.get(middleNum - 1)) / 2.0);

            }

            else
            {
                int middleNum = numSatisfiedInUnsatisfiedForRandApprox.size() / 2;
                medianNumOfSatisfiedInUnSatisfiedRandApprox = numSatisfiedInUnsatisfiedForRandApprox.get(middleNum);

            }
        }

        // if (numSatisfiedInUnsatisfiedForDPLL.size() > 0)
        // {
        // System.out.println("DPLL:");
        // System.out.println("Max # of Satisfied Clauses In An Unsatisfied Formula For DPLL: " + maxNumOfSatisfiedInUnSatisfiedDPLL);
        // System.out.println("Min # of Satisfied Clauses In An Unsatisfied Formula For DPLL: " + minNumOfSatisfiedInUnSatisfiedDPLL);
        // System.out.println("Avg of Satisfied Clauses In All Unsatisfied Formulas For DPLL: " + avgNumOfSatisfiedInUnSatisfiedDPLL);
        // System.out.println("Median of Satisfied Clauses In All Unsatisfied Formulas For DPLL: " + medianNumOfSatisfiedInUnSatisfiedDPLL + "\n");
        //
        // }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (numSatisfiedInUnsatisfiedForGSAT.size() > 0)
        {
            System.out.println("GSAT:");
            System.out.println("Max # of Satisfied Clauses In An Unsatisfied Formula For GSAT: " + maxNumOfSatisfiedInUnSatisfiedGSAT);
            System.out.println("Min # of Satisfied Clauses In An Unsatisfied Formula For GSAT: " + minNumOfSatisfiedInUnSatisfiedGSAT);
            System.out.println("Avg of Satisfied Clauses In All Unsatisfied Formulas For GSAT: " + avgNumOfSatisfiedInUnSatisfiedGSAT);
            System.out.println("Median of Satisfied Clauses In All Unsatisfied Formulas For GSAT: " + medianNumOfSatisfiedInUnSatisfiedGSAT + "\n");

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (numSatisfiedInUnsatisfiedForRandApprox.size() > 0)
        {
            System.out.println("Random Approximation:");
            System.out.println("Max # of Satisfied Clauses In An Unsatisfied Formula For RandApprox: " + maxNumOfSatisfiedInUnSatisfiedRandApprox);
            System.out.println("Min # of Satisfied Clauses In An Unsatisfied Formula For RandApprox: " + minNumOfSatisfiedInUnSatisfiedRandApprox);
            System.out.println("Avg # of Satisfied Clauses In All Unsatisfied Formulas For RandApprox: " + avgNumOfSatisfiedInUnSatisfiedRandApprox);
            System.out.println("Median # of Satisfied Clauses In All Unsatisfied Formulas For RandApprox: " + medianNumOfSatisfiedInUnSatisfiedRandApprox + "\n");

        }

    }

    /***
     * Helper function for GSAT & Randomized Approximation that returns a random truth assignment
     * @param truthAssignmentSoFar
     * @return
     */
    public Clause getRandomTruthAssignment(Clause truthAssignmentSoFar)
    {

        Random rand = new Random();

        // loop through each literal in the truth assignment
        for (Literal eachLiteralInTruthAssign : truthAssignmentSoFar.getLiterals())
        {

            // and randomly assign a literal value to each literal
            eachLiteralInTruthAssign.setLiteralValue(rand.nextBoolean());
        }

        return truthAssignmentSoFar;

    }

    /***
     * @param givenClause
     * @return
     */
    public boolean isAllAreAssignedInClause(Clause givenClause)
    {
        for (Literal l : givenClause.getLiterals())
        {
            if (l.isAssigned() == false)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Helper function for GSAT that counts how many unsatisfied clauses there are
     * in a Formula
     * @param someFormula
     * @param someTruthAssignment
     * @return
     */
    public int howManyUnsatisfiedClauses(Formula someFormula, Clause someTruthAssignment)
    {
        int countUnsatisfiedClauses = 0;

        for (Clause eachClauseInFormula : someFormula.getClauses())
        {

            if (eachClauseInFormula.isSatisfied() == false)
            {
                countUnsatisfiedClauses++;
            }
        }

        return countUnsatisfiedClauses;

    }

    /***
     * Helper function for GSAT, DPLL & RandomApproximation that assigns a literal value for each literal in the
     * Formula based on the assigned literal values in the truth assignment
     * @param toBeAssigned
     * @param truthAssignment
     * @return
     */
    public Formula getAssignedFormula(Formula toBeAssigned, Clause truthAssignment)
    {

        for (Clause eachC : toBeAssigned.getClauses())
        {
            for (Literal eachL : eachC.getLiterals())
            {
                for (int i = 0; i < truthAssignment.getNumOfLiterals(); i++)
                {
                    if (truthAssignment.getLiteral(i).getVarNum() == eachL.getVarNum())
                    {
                        eachL.setLiteralValue(truthAssignment.getLiteral(i).getAssignedLiteralValue());
                    }
                }
            }
        }

        return toBeAssigned;

    }

    /***
     * Helper function for GSAT & DPLL that helps copy the given clause into a new clause
     * @param original
     * @return
     */
    public Clause copyClause(Clause original)
    {

        ArrayList<Literal> literalsInClause = new ArrayList<Literal>();

        for (Literal l : original.getLiterals())
        {

            if (l.isAssigned())
            {
                literalsInClause.add(new Literal(l.getVarNum(), l.isPositiveLiteral(), l.getAssignedLiteralValue()));

            }

            else
            {
                literalsInClause.add(new Literal(l.getVarNum(), l.isPositiveLiteral()));
            }
        }

        return new Clause(literalsInClause);

    }

    /**
     * Helper function for DPLL that helps copy the given Formula into a new
     * Formula
     * @param original
     * @return
     */
    public Formula copyFormula(Formula original)
    {

        ArrayList<Clause> copyClauses = new ArrayList<Clause>();

        for (Clause eachClause : original.getClauses())
        {

            ArrayList<Literal> literalsInEachClause = new ArrayList<Literal>();

            for (Literal eachLiteral : eachClause.getLiterals())
            {

                if (eachLiteral.isAssigned())
                {
                    literalsInEachClause.add(new Literal(eachLiteral.getVarNum(), eachLiteral.isPositiveLiteral(), eachLiteral.getAssignedLiteralValue()));
                }
                else
                {
                    literalsInEachClause.add(new Literal(eachLiteral.getVarNum(), eachLiteral.isPositiveLiteral()));
                }

            }

            copyClauses.add(new Clause(literalsInEachClause));
        }

        return new Formula(copyClauses);
    }
}