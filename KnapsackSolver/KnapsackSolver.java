import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
   /**
     * @author esteban acosta
     */
public class KnapsackSolver
{
    /**
     * Helper function for O(nW) algorithm. Constructs a solution from the solution table output by O(nW)
     * @param knapsack
     * @param solved
     * @return
     */
    public ArrayList<Item> ConstructMaxKnapsackSolutionForOne(Knapsack knapsack, int[][] solved)
    {

        ArrayList<Item> solution = new ArrayList<Item>();

        ArrayList<Item> items = knapsack.getItems();

        ArrayList<Integer> weights = knapsack.getWeights();

        int numOfItems = items.size();

        int targetWeight = knapsack.getTargetWeight();

        while (numOfItems > 0 && targetWeight > 0)
        {

            if (solved[numOfItems][targetWeight] != solved[numOfItems - 1][targetWeight])
            {

                solution.add(items.get(numOfItems - 1));

                targetWeight -= weights.get(numOfItems - 1);

            }

            numOfItems--;
        }

        return solution;

    }

    /**
     * The O(nW) dynamic programming algorithm (305 Algorithm)
     */

    public ArrayList<Item> FirstDynamicProgramming(Knapsack knapsack)
    {

        ArrayList<Item> items = knapsack.getItems();

        ArrayList<Integer> weights = knapsack.getWeights();

        ArrayList<Integer> values = knapsack.getValues();

        int numOfItems = items.size();

        int targetWeight = knapsack.getTargetWeight();

        int solution[][] = new int[numOfItems + 1][targetWeight + 1];

        for (int itemNum = 0; itemNum <= numOfItems; itemNum++)
        {
            for (int weight = 0; weight <= targetWeight; weight++)
            {

                if (itemNum == 0 || weight == 0)
                {

                    solution[itemNum][weight] = 0;

                }

                else if (weights.get(itemNum - 1) <= weight)
                {

                    int a = solution[itemNum - 1][weight - weights.get(itemNum - 1)] + values.get(itemNum - 1);
                    int b = solution[itemNum - 1][weight];

                    solution[itemNum][weight] = Math.max(a, b);

                }

                else
                {

                    solution[itemNum][weight] = solution[itemNum - 1][weight];

                }
            }
        }

        return ConstructMaxKnapsackSolutionForOne(knapsack, solution);
    }

    /**
     * Helper function for O(n2 * v(a_max)) dynamic programming that constructs
     * a solution when passed the two generated tables from the The O(n2 *
     * v(a_max)) dynamic programming algorithm
     * @param Knapsack
     * @param MinCost
     * @param Take
     * @return
     */
    public ArrayList<Item> ConstructMaxKnapsackSolutionForTwo(Knapsack knapsack, double[][] MinCost, boolean[][] Take)
    {

        ArrayList<Item> items = knapsack.getItems();

        ArrayList<Integer> values = knapsack.getValues();

        int numOfItems = MinCost.length - 1;

        int targetWeight = knapsack.getTargetWeight();

        int optimalValue = MinCost[0].length - 1;

        while (optimalValue > 0 && MinCost[numOfItems][optimalValue] > targetWeight)
        {
            optimalValue--;
        }

        ArrayList<Item> solution = new ArrayList<Item>();

        int i = numOfItems;

        int t = optimalValue;

        while (i > 0 && t > 0)
        {

            if (Take[i][t] == true)
            {

                solution.add(items.get(i - 1));

                t -= values.get(i - 1);

            }

            i--;
        }

        return solution;

    }

    /**
     * The O(n2 * v(a_max)) dynamic programming algorithm
     */
    public ArrayList<Item> SecondDynamicProgramming(Knapsack knapsack)
    {

        ArrayList<Item> items = knapsack.getItems();

        ArrayList<Integer> weights = knapsack.getWeights();

        ArrayList<Integer> values = knapsack.getValues();

        int numOfItems = items.size();

        int maxVal = 0;

        for (Item eachItem : items)
        {
            if (eachItem.getValue() > maxVal)
            {

                maxVal = eachItem.getValue();
            }
        }

        double[][] MinCost = new double[numOfItems + 1][(numOfItems * maxVal) + 1];

        boolean[][] Take = new boolean[numOfItems + 1][(numOfItems * maxVal) + 1];

        for (int i = 1; i < MinCost.length; i++)
        {
            MinCost[i][0] = 0;
        }

        // when t <= v(1), target t can be achieved by taking object 1
        for (int t = 1; t <= values.get(0); t++)
        {
            MinCost[1][t] = weights.get(0);
            Take[1][t] = true;
        }

        // when t > v(1), target cannot be reached with only object 1 available
        for (int t = values.get(0) + 1; t < MinCost[0].length; t++)
        {
            MinCost[1][t] = Double.POSITIVE_INFINITY;
            Take[1][t] = false;
        }

        for (int i = 2; i < MinCost.length; i++)
        {
            for (int t = 1; t < MinCost[0].length; t++)
            {

                int nextT = Math.max(0, t - values.get(i - 1));

                if (MinCost[i - 1][t] <= weights.get(i - 1) + MinCost[i - 1][nextT])
                {

                    MinCost[i][t] = MinCost[i - 1][t];
                    Take[i][t] = false;

                }

                else
                {
                    MinCost[i][t] = weights.get(i - 1) + MinCost[i - 1][nextT];
                    Take[i][t] = true;

                }

            }
        }

        return ConstructMaxKnapsackSolutionForTwo(knapsack, MinCost, Take);

    }

    /**
     * The greedy 2-approximation algorithm
     * @return
     */
    public ArrayList<Item> Greedy2Approximation(Knapsack knapsack)
    {

        // Get the list of items
        ArrayList<Item> items = knapsack.getItems();

        // Initialize G (solution) to be the empty set
        ArrayList<Item> solution = new ArrayList<Item>();

        // Initialize A (list of values over weights) to the set of values over
        // the set of weights
        // (costs)
        ArrayList<Item> copyItems = new ArrayList<Item>();

        // Copy all the items from the list of items
        for (int i = 0; i < items.size(); i++)
        {

            copyItems.add(new Item(items.get(i).getValue(),
            items.get(i).getWeight(), i));

        }

        Collections.sort(copyItems);

        // Initialize the amount of remaining weight (L) to the target weight
        // (B)
        int remainingWeight = knapsack.getTargetWeight();

        int whichItem = 0;

        // for each element in valuesOverWeights (each a in A) and while
        // remaining weight is greater
        // than zero (L > 0)
        while (whichItem < copyItems.size() && remainingWeight > 0)
        {

            // if w(a) (c(a)) <= remaining weight (L)
            if (copyItems.get(whichItem).getWeight() <= remainingWeight)
            {

                // add a to G
                solution.add(copyItems.get(whichItem));

                // decrease remaining weight(L) by w(a)
                remainingWeight -= copyItems.get(whichItem).getWeight();

            }

            whichItem++;
        }

        // let a_max be the object with maximum value
        int valOfa_max = 0;

        Item a_max = null;

        for (Item eachVW : copyItems)
        {

            if (eachVW.getValue() > valOfa_max)
            {

                valOfa_max = eachVW.getValue();

                a_max = eachVW;

            }

        }

        // Let Value(G) be the total value of all the values in G
        int valueOfG = sumOfValues(solution);

        if (valOfa_max > valueOfG)
        {

            solution.clear();

            solution.add(a_max);

            return solution;
        }

        else
        {

            return solution;

        }

    }

    /***
     * The FPTAS based on scaling with the optimal dynamic programming algorithm
     * @param eachKnapsack
     */

    public ArrayList<Item> FPTAS(Knapsack knapsack)
    {

        // Make a copy of the values of the original list of values
        ArrayList<Integer> values = new ArrayList<Integer>(knapsack.getValues());

        ArrayList<Integer> weights = new ArrayList<Integer>(knapsack.getWeights());

        ArrayList<Double> copyValues = new ArrayList<Double>();

        // copy the values from the original list into the new list
        for (int i = 0; i < values.size(); i++)
        {

            copyValues.add((double) values.get(i));
        }

        // have a variable that stores the size of the list of the items
        int numItems = knapsack.getItems().size();

        // set an epsilon value
        double epsilon = .5;

        // compute the scale value
        double scaleVal = numItems * (1 / epsilon);

        // Get the max value
        double maxVal = Collections.max(copyValues);

        // Through each value in the list
        for (int i = 0; i < copyValues.size(); i++)
        {
            // Take each value and divide it by the max value
            copyValues.set(i, copyValues.get(i) / maxVal);

            // Then multiply each each by the scale value
            copyValues.set(i, copyValues.get(i) * scaleVal);

        }

        // Remove the values in the original list
        values.clear();

        // Truncate each value in the list
        for (int i = 0; i < copyValues.size(); i++)
        {

            DecimalFormat df = new DecimalFormat("#");

            String truncatedValue = df.format(copyValues.get(i));

            values.add(Integer.parseInt(truncatedValue));

        }

        ArrayList<Item> modifiedItems = new ArrayList<Item>();

        for (int i = 0; i < numItems; i++)
        {
            modifiedItems.add(new Item(values.get(i), weights.get(i), i));
        }

        // Create a copy knapsack object that stores the a list of items with the new values and the target weight
        Knapsack copyKnapsack = new Knapsack(modifiedItems, knapsack.getTargetWeight());

        return SecondDynamicProgramming(copyKnapsack);

    }

    /**
     * Generates a random knapsack instance
     * @return
     */
    public Knapsack getRandomKnapsackInstance()
    {

        Random rand = new Random();

        int howManyItems = rand.nextInt(15) + 15;

        // Create a list of items
        ArrayList<Item> items = new ArrayList<Item>();

        // Generate a random value and random weight
        for (int i = 0; i < howManyItems; i++)
        {

            int randVal = rand.nextInt(50) + 50;

            int randW = rand.nextInt(50) + 50;

            // Create an item object that has an index, value or weight and add
            // that new item to the list
            // of items
            items.add(new Item(randVal, randW, i));

        }

        // Get a random target weight value
        int targetWeight = rand.nextInt(500) + 100;

        // Return a Knapsack object that contains a list of each random item
        // and a target weight
        return new Knapsack(items, targetWeight);

    }

    public static void main(String[] args)
    {

        KnapsackSolver ks = new KnapsackSolver();

        ArrayList<Knapsack> allKnapsacks = new ArrayList<Knapsack>();

        for (int numOfInstances = 0; numOfInstances < 100; numOfInstances++)
        {

            allKnapsacks.add(ks.getRandomKnapsackInstance());

        }

        int whichKP = 1;

        ArrayList<Long> FirstDynamicRunTimes = new ArrayList<Long>();

        ArrayList<Long> SecondDynamicRunTimes = new ArrayList<Long>();

        ArrayList<Long> Greedy2RunTimes = new ArrayList<Long>();

        ArrayList<Long> FPTASRunTimes = new ArrayList<Long>();

        for (Knapsack eachKnapsack : allKnapsacks)
        {
            System.out.println("Knapsack Instance #" + whichKP + ":");
            System.out.println(eachKnapsack);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("Greedy-2-Approximation:");

            long startGreedy2 = System.currentTimeMillis();

            ArrayList<Item> greedy2Solution = ks.Greedy2Approximation(eachKnapsack);

            long finishGreedy2 = System.currentTimeMillis();

            System.out.print("Solution Values For Greedy2Approximation: ");

            // Print the solution values
            ks.printSolutionValues(greedy2Solution);

            System.out.println("\nTotal Solution Values For Greedy2Approximation: " + ks.sumOfValues(greedy2Solution));
            System.out.println("Total Weight Of Solution Values For Greedy2Approximation: " + ks.sumOfWeights(greedy2Solution));

            long elapsedTimeGreedy2 = finishGreedy2 - startGreedy2;

            System.out.println("Elapsed Time For Greedy2Approximation: " + elapsedTimeGreedy2 + (elapsedTimeGreedy2 != 1 ? " milliseconds \n" : " millisecond \n"));

            // Add this runtime to the list of Greedy2 run times
            Greedy2RunTimes.add(elapsedTimeGreedy2);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("The O(nW) dynamic programming algorithm (aka First Dynamic Progamming Algorithm):");

            long startFirstDynamic = System.currentTimeMillis();

            ArrayList<Item> firstDynamicSolution = ks.FirstDynamicProgramming(eachKnapsack);

            long finishFirstDynamic = System.currentTimeMillis();

            System.out.print("Solution Values For O(nW) Dynamic Programming Algorithm: ");

            // Print the solution values
            ks.printSolutionValues(firstDynamicSolution);

            System.out.println("\nTotal Solution Values For O(nW) Dynamic Programming Algorithm: " + ks.sumOfValues(firstDynamicSolution));
            System.out.println("Total Weight Of Solution Values For O(nW) Dynamic Programming Algorithm: " + ks.sumOfWeights(firstDynamicSolution));

            long elapsedFirstDynamic = finishFirstDynamic - startFirstDynamic;

            System.out.println("Elapsed Time For O(nW): " + elapsedFirstDynamic + (elapsedFirstDynamic != 1 ? " milliseconds \n" : " millisecond \n"));

            // Add this runtime to the list of O(nW) run times
            FirstDynamicRunTimes.add(elapsedFirstDynamic);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("The O(n^2 * v(a_max)) dynamic programming algorithm (aka the Second Dynamic Programming Algorithm):");

            System.out.print("Solution Values For O(n^2 * v(a_max)) dynamic programming algorithm: ");

            long startSecondDynamic = System.currentTimeMillis();

            ArrayList<Item> secondDynamicSolution = ks.SecondDynamicProgramming(eachKnapsack);

            long finishSecondDynamic = System.currentTimeMillis();

            // Print the solution values
            ks.printSolutionValues(secondDynamicSolution);

            System.out.println("\nTotal Solution Values For O(n^2 * v(a_max)) dynamic programming algorithm: " + ks.sumOfValues(secondDynamicSolution));
            System.out.println("Total Weight Of Solution Values For O(n^2 * v(a_max)) dynamic programming algorithm: " + ks.sumOfWeights(secondDynamicSolution));

            long elapsedSecondDynamic = finishSecondDynamic - startSecondDynamic;

            System.out.println("Elapsed Time For  O(n^2 * v(a_max)): " + elapsedSecondDynamic + (elapsedSecondDynamic != 1 ? " milliseconds \n" : " millisecond \n"));

            // Add this runtime to the list of O(n^2 * v(a_max)) run times
            SecondDynamicRunTimes.add(elapsedSecondDynamic);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("FPTAS:");
            System.out.print("Solution Values For FPTAS: ");

            long startFPTAS = System.currentTimeMillis();

            ArrayList<Item> FPTASSolution = ks.FPTAS(eachKnapsack);

            long finishFPTAS = System.currentTimeMillis();

            // Print the solution values
            ks.printSolutionValues(FPTASSolution);

            System.out.println("\nTotal Solution Values For FTPAS: " + ks.sumOfValues(FPTASSolution));
            System.out.println("Total Weight Of Solution Values For FTPAS: " + ks.sumOfWeights(FPTASSolution));

            long elapsedFPTAS = finishFPTAS - startFPTAS;

            System.out.println("Elapsed Time For  FPTAS: " + elapsedFPTAS + (elapsedFPTAS != 1 ? " milliseconds \n" : " millisecond \n"));

            // Add this runtime to the list of O(n^2 * v(a_max)) run times
            FPTASRunTimes.add(elapsedFPTAS);

            ks.RunQualityStatusReport(firstDynamicSolution, greedy2Solution, FPTASSolution);

            System.out.println("---------------------------------------------------------------------------------------------------------");

            whichKP++;
        }

        ks.RuntimeStatusReport(allKnapsacks, FirstDynamicRunTimes, SecondDynamicRunTimes, Greedy2RunTimes, FPTASRunTimes);

    }

    /**
     * Prints a status report about the quality of the solutions
     * @param firstDynamicSolution
     * @param greedy2Solution
     * @param fPTASSolution
     */
    public void RunQualityStatusReport(ArrayList<Item> firstDynamicSolution, ArrayList<Item> greedy2Solution, ArrayList<Item> fPTASSolution)
    {

        ArrayList<Integer> greedyValues = new ArrayList<Integer>();

        ArrayList<Integer> optimalValues = new ArrayList<Integer>();

        ArrayList<Integer> FPTASValues = new ArrayList<Integer>();

        // Get the values from the solution lists and put them into their respective lists
        for (Item i : firstDynamicSolution)
        {
            optimalValues.add(i.getValue());
        }

        for (Item i : greedy2Solution)
        {
            greedyValues.add(i.getValue());
        }

        for (Item i : fPTASSolution)
        {
            FPTASValues.add(i.getValue());
        }

        double optimalMax = Collections.max(optimalValues), optimalMin = Collections.min(optimalValues);

        double FPTASMax = Collections.max(FPTASValues), FPTASMin = Collections.min(FPTASValues);

        double GreedyMax = Collections.max(greedyValues), GreedyMin = Collections.min(greedyValues);

        double FPTASAverage = sumOfValues(fPTASSolution) / fPTASSolution.size(), optimalAverage = sumOfValues(firstDynamicSolution) / firstDynamicSolution.size(), GreedyAverage = sumOfValues(greedy2Solution) / greedy2Solution.size();

        double GreedyMedian = 0, optimalMedian = 0, FPTASMedian = 0;

        // Get the median number for the optimal value
        if (optimalValues.size() % 2 == 0)
        {
            int middleNum = optimalValues.size() / 2;
            optimalMedian = (optimalValues.get(middleNum) + optimalValues.get(middleNum - 1) / 2.0);

        }

        else
        {
            int middleNum = optimalValues.size() / 2;
            optimalMedian = optimalValues.get(middleNum);
        }

        // Get the median number for FPTAS
        if (FPTASValues.size() % 2 == 0)
        {
            int middleNum = FPTASValues.size() / 2;
            FPTASMedian = (FPTASValues.get(middleNum) + FPTASValues.get(middleNum - 1) / 2.0);

        }

        else
        {
            int middleNum = FPTASValues.size() / 2;
            FPTASMedian = FPTASValues.get(middleNum);
        }

        // Get the median number for Greedy2
        if (greedyValues.size() % 2 == 0)
        {
            int middleNum = greedyValues.size() / 2;
            GreedyMedian = (greedyValues.get(middleNum) + greedyValues.get(middleNum - 1) / 2.0);

        }

        else
        {
            int middleNum = greedyValues.size() / 2;
            GreedyMedian = greedyValues.get(middleNum);
        }

        System.out.println();
        System.out.println("Greedy-2-Approximation:");
        System.out.println("Max: " + GreedyMax / optimalMax);
        System.out.println("Min: " + GreedyMin / optimalMin);
        System.out.println("Average: " + GreedyAverage / optimalAverage);
        System.out.println("Median: " + GreedyMedian / optimalMedian);

        System.out.println();
        System.out.println("FPTAS:");
        System.out.println("Max: " + FPTASMax / optimalMax);
        System.out.println("Min: " + FPTASMin / optimalMin);
        System.out.println("Average: " + FPTASAverage / optimalAverage);
        System.out.println("Median: " + FPTASMedian / optimalMedian);
    }

    /***
     * Prints a status report about the runtime
     * @param allKnapsacks
     * @param FirstDynamicRunTimes
     * @param SecondDynamicRunTimes
     * @param Greedy2RunTimes
     * @param FPTASRunTimes
     */
    public void RuntimeStatusReport(ArrayList<Knapsack> allKnapsacks, ArrayList<Long> FirstDynamicRunTimes, ArrayList<Long> SecondDynamicRunTimes, ArrayList<Long> Greedy2RunTimes, ArrayList<Long> FPTASRunTimes)
    {
        System.out.println("RUN TIME STATUS REPORT:");
        long maxFirstDynamicRunTime = 0, maxSecondDynamicRunTime = 0, maxGreedy2RunTime = 0, maxFPTASRunTime = 0;
        long minFirstDynamicRunTime = 0, minSecondDynamicRunTime = 0, minGreedy2RunTime = 0, minFPTASRunTime = 0;
        double avgFirstDynamicRunTime = 0, avgSecondDynamicRunTime = 0, avgGreedy2RunTime = 0, avgFPTASRunTime = 0;
        double medianFirstDynamicRunTime = 0, medianSecondDynamicRunTime = 0, medianGreedy2RunTime = 0, medianFPTASRunTime = 0;

        Collections.sort(FirstDynamicRunTimes);
        Collections.sort(SecondDynamicRunTimes);
        Collections.sort(Greedy2RunTimes);
        Collections.sort(FPTASRunTimes);

        for (int i = 0; i < allKnapsacks.size(); i++)
        {

            avgFirstDynamicRunTime += FirstDynamicRunTimes.get(i);

            avgSecondDynamicRunTime += SecondDynamicRunTimes.get(i);

            avgGreedy2RunTime += Greedy2RunTimes.get(i);

            avgFPTASRunTime += FPTASRunTimes.get(i);
        }

        // Since we've only take the sum of the runtimes we need to take the sum and divide it by the number of instances
        avgFirstDynamicRunTime /= allKnapsacks.size();

        avgSecondDynamicRunTime /= allKnapsacks.size();

        avgGreedy2RunTime /= allKnapsacks.size();

        avgFPTASRunTime /= allKnapsacks.size();

        // Use collections to find the max and min run time of the list of running times for GSAT, DPLL, and Rand Approx
        maxGreedy2RunTime = Collections.max(Greedy2RunTimes);
        minGreedy2RunTime = Collections.min(Greedy2RunTimes);

        maxFirstDynamicRunTime = Collections.max(FirstDynamicRunTimes);
        minFirstDynamicRunTime = Collections.min(FirstDynamicRunTimes);

        maxSecondDynamicRunTime = Collections.max(SecondDynamicRunTimes);
        minSecondDynamicRunTime = Collections.min(SecondDynamicRunTimes);

        maxFPTASRunTime = Collections.max(FPTASRunTimes);
        minFPTASRunTime = Collections.min(FPTASRunTimes);

        // To find the median run time, we need to first determine if there is an even # of Knapsack instances or an odd # of Knapsack instances
        // if there is an even # of instances, we need to take the average of the two middle values.
        // If there is an odd # of instances, we need to take the middle value

        if (allKnapsacks.size() % 2 == 0)
        {
            int middleNum = allKnapsacks.size() / 2;
            medianFirstDynamicRunTime = (long) ((FirstDynamicRunTimes.get(middleNum) + FirstDynamicRunTimes.get(middleNum - 1)) / 2.0);
            medianSecondDynamicRunTime = (long) ((SecondDynamicRunTimes.get(middleNum) + SecondDynamicRunTimes.get(middleNum - 1)) / 2.0);
            medianGreedy2RunTime = (long) ((Greedy2RunTimes.get(middleNum) + Greedy2RunTimes.get(middleNum - 1)) / 2.0);
            medianFPTASRunTime = (long) ((FPTASRunTimes.get(middleNum) + FPTASRunTimes.get(middleNum - 1)) / 2.0);

        }

        else
        {
            int middleNum = allKnapsacks.size() / 2;
            medianFirstDynamicRunTime = FirstDynamicRunTimes.get(middleNum);
            medianSecondDynamicRunTime = SecondDynamicRunTimes.get(middleNum);
            medianGreedy2RunTime = Greedy2RunTimes.get(middleNum);
            medianFPTASRunTime = FPTASRunTimes.get(middleNum);
        }

        System.out.println("O(nW) aka First Dynammic Programming Algorithm");
        System.out.println("Max RunTime For O(nW): " + maxFirstDynamicRunTime + (maxFirstDynamicRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Min RunTime For O(nW): " + minFirstDynamicRunTime + (minFirstDynamicRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Avg RunTime For O(nW): " + avgFirstDynamicRunTime + (avgFirstDynamicRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Median RunTime For O(nW): " + medianFirstDynamicRunTime + (medianFirstDynamicRunTime != 1 ? " milliseconds \n" : " millisecond \n"));

        System.out.println("O(n^2 * v(a_max)) aka Second Dynamic Programming Algorithm");
        System.out.println("Max RunTime For O(n^2 * v(a_max)): " + maxSecondDynamicRunTime + (maxSecondDynamicRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Min RunTime For O(n^2 * v(a_max)): " + minSecondDynamicRunTime + (minSecondDynamicRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Avg RunTime For O(n^2 * v(a_max)): " + avgSecondDynamicRunTime + (avgSecondDynamicRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Median RunTime For O(n^2 * v(a_max)): " + medianSecondDynamicRunTime + (medianSecondDynamicRunTime != 1 ? " milliseconds \n" : " millisecond \n"));

        System.out.println("Greedy2Approximation");
        System.out.println("Max RunTime For Greedy2Approximation: " + maxGreedy2RunTime + (maxGreedy2RunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Min RunTime For Greedy2Approximation: " + minGreedy2RunTime + (minGreedy2RunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Avg RunTime For Greedy2Approximation: " + avgGreedy2RunTime + (avgGreedy2RunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Median RunTime For Greedy2Approximation: " + medianGreedy2RunTime + (medianGreedy2RunTime != 1 ? " milliseconds \n" : " millisecond \n"));

        System.out.println("FPTAS:");
        System.out.println("Max RunTime For FPTAS: " + maxFPTASRunTime + (maxFPTASRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Min RunTime For FPTAS: " + minFPTASRunTime + (minFPTASRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Avg RunTime For FPTAS: " + avgFPTASRunTime + (avgFPTASRunTime != 1 ? " milliseconds" : " millisecond"));
        System.out.println("Median RunTime For FPTAS: " + medianFPTASRunTime + (medianFPTASRunTime != 1 ? " milliseconds" : " millisecond"));

    }

    /***
     * Sum of the values of the solution items
     * @param items
     * @return
     */
    public int sumOfValues(ArrayList<Item> items)
    {

        int sumOfValues = 0;
        for (Item eachItem : items)
        {

            sumOfValues += eachItem.getValue();
        }

        return sumOfValues;
    }

    /***
     * Sum of the weights of the solution items
     * @param items
     * @return
     */
    public int sumOfWeights(ArrayList<Item> items)
    {

        int sumOfWeights = 0;

        for (Item eachItem : items)
        {

            sumOfWeights += eachItem.getWeight();
        }

        return sumOfWeights;
    }

    public void printSolutionValues(ArrayList<Item> solution)
    {

        System.out.print("{ ");
        int count = 0;
        for (Item eachVal : solution)
        {

            if (count == solution.size() - 1)
            {
                System.out.print(eachVal.getValue());
            }
            else
            {

                System.out.print(eachVal.getValue() + " , ");
            }

            count++;
        }
        System.out.print(" }");

    }

    /**
     * This method is used to print the MinCost table from the n^2 * v(a_max)) dynamic programming algorithm
     * @param solutionTable
     */
    public void printMinCostTable(double[][] solutionTable)
    {

        for (int row = 0; row < solutionTable.length; row++)
        {
            for (int col = 0; col < solutionTable[row].length; col++)
            {
                System.out.print(solutionTable[row][col] + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * This method is used to print the bottom up table from the o(nW) dynamic programming algorithm
     * @param solutionTable
     */
    public void printBottomUpTable(int[][] solutionTable)
    {

        for (int row = 0; row < solutionTable.length; row++)
        {
            for (int col = 0; col < solutionTable[row].length; col++)
            {
                System.out.print(solutionTable[row][col] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints the Take table from the n^2 * v(a_max)) dynamic programming algorithm
     */
    public void printTakeTable(boolean[][] takeTable)
    {
        for (int row = 0; row < takeTable.length; row++)
        {
            for (int col = 0; col < takeTable[row].length; col++)
            {
                if (takeTable[row][col] == true)
                {
                    System.out.print("Y ");
                }
                else
                {
                    System.out.print("N ");
                }

            }
            System.out.println();
        }
    }

}
