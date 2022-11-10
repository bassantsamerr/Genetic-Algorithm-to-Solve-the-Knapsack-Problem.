package knapsackga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

public class KnapsackGA {

    //b3ml crossover kam mara

    static int populationSize =100 ;
    static double pc = 0.7;
    static double pm = 0.1;
    static chromosome max;
    static ArrayList<chromosome> population = new ArrayList<chromosome>();
    static ArrayList<chromosome> pool = new ArrayList<chromosome>();

    public static boolean inArray(chromosome tmp, ArrayList<chromosome> Arr) {
        for (int i = 0; i < Arr.size(); i++)
            if (tmp.binaryName.equals(Arr.get(i).binaryName))
                return true;
        return false;
    }

    public static void intializePopulation(int numItems,ArrayList<item> it,int weightKnapsack) {
        for (int i = 0; i < populationSize; i++) // 10 is the number of chromosomes generated
        {
            chromosome tmp = new chromosome("");
            for (int j = 0; j < numItems; j++) {
                float x = (float) Math.random();
                if (x <= 0.5) // 1
                {
                    tmp.binaryName += '1';
                } else //0
                {
                    tmp.binaryName += '0';
                }
            }
            if (calculateWeight(tmp.binaryName,it)>weightKnapsack|| calculateWeight(tmp.binaryName,it)== 0) {
                i--;
            } else {
                population.add(tmp);
            }
        }
    }

    public static void printPopulation(ArrayList<chromosome> p) {
        for (int i = 0; i < p.size(); i++) {
            System.out.println(p.get(i).toString());
        }
    }

    public static int calculateWeight(String s, ArrayList<item> it) {
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                res += it.get(i).weight;
            }
        }
        return res;
    }

    public static int calculateValue(String s, ArrayList<item> it) {
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                res += it.get(i).value;
            }
        }
        return res;
    }

    public static void CalculateFitness(ArrayList<item> it, int weightKnapsack) {
        for (int i = 0; i < population.size(); i++) {
            int weightOfCh = calculateWeight(population.get(i).binaryName, it);
            //accepted and calculate its fitnessValue
            population.get(i).fitnessValue = calculateValue(population.get(i).binaryName, it);

        }
    }

    public static double randomNumber(int min, int max) {
        return (double) Math.random() * (max - min) + min;
    }

    public static ArrayList<Integer> doSelection() {
        ArrayList<Integer> pre = new ArrayList<>();
        pre.add(0);

        Random random = new Random();
        int x = random.nextInt(populationSize + 1);
        int y = random.nextInt(populationSize + 1);
        int index1 = -1;
        int index2 = -1;
        for (int i = 0; i < population.size(); i++) {
            if (!(x >= population.get(i).percentage) && index1 == -1) {
                index1 = i - 1;
            }
            if (!(y >=  population.get(i).percentage) && index2 == -1) {
                index2 = i - 1;
            }
        }
        index1 = Math.max(0, index1);
        index2 = Math.max(0, index2);
        ArrayList<Integer> result = new ArrayList<>();
        result.add(index1);
        result.add(index2);
        return result;
    }

    public static void doCrossOver(ArrayList<Integer> result,ArrayList<item> it,int weightKnapsack) {
        ArrayList<chromosome> offSprings = new ArrayList<>();
        chromosome offSpring1 = new chromosome("");
        chromosome offSpring2 = new chromosome("");
        Random rn = new Random();
        int n = population.get(0).binaryName.length() - 1 - 1 + 1;
        int test = rn.nextInt() % n;
        int crossoverPoint = Math.abs(1 + test);
        double r2 = randomNumber(0, 1);
        if (r2 <= pc) {
            for (int i = 0; i < population.get(0).binaryName.length(); i++) {
                if (i < crossoverPoint) {
                    offSpring1.binaryName += population.get(result.get(0)).binaryName.charAt(i);
                    offSpring2.binaryName += population.get(result.get(1)).binaryName.charAt(i);
                } else {
                    offSpring1.binaryName += population.get(result.get(1)).binaryName.charAt(i);
                    offSpring2.binaryName += population.get(result.get(0)).binaryName.charAt(i);
                }

            }
            if(calculateWeight(offSpring1.binaryName,it)>weightKnapsack||calculateWeight(offSpring1.binaryName,it)==0){
                offSpring1 = population.get(result.get(0));
            }
            if(calculateWeight(offSpring2.binaryName,it)>weightKnapsack||calculateWeight(offSpring1.binaryName,it)==0){
                offSpring2 = population.get(result.get(1));
            }

        }
        else {
            offSpring1 = population.get(result.get(0));
            offSpring2 = population.get(result.get(1));
        }
        pool.add(offSpring1);
        pool.add(offSpring2);

    }

    public static char flipBit(char c) {
        if (c == '1') {
            return '0';
        }
        return '1';
    }

    public static void doMutation(ArrayList<item> it,int weightKnapsack) {
        for (int i = 0; i < pool.size(); i++) {
            String tmp = "";
            for (int j = 0; j < pool.get(i).binaryName.length(); j++) {
                double x = randomNumber(0, 1);
                if (x <= pm) //flipbit
                {
                    tmp += flipBit(pool.get(i).binaryName.charAt(j));

                } else {
                    tmp += pool.get(i).binaryName.charAt(j);
                }
            }
            if(calculateWeight(tmp,it)<=weightKnapsack&&calculateWeight(tmp,it)!=0){
                pool.get(i).binaryName = tmp;
            }
//            pool.get(i).binaryName = tmp;
        }
    }

    public static void FullReplacement() {
        Collections.copy(population, pool); // copying the ArrayList pool to the population list
        // population = pool;
    }
    public static void sortBasedOnFitness()
    {

        for(int i=0;i<population.size();i++)
        {
            for(int j=0;j<population.size();j++)
            {
                if (population.get(i).fitnessValue<population.get(j).fitnessValue)
                {
                    chromosome tmp=population.get(i);
                    population.set(i,population.get(j));
                    population.set(j,tmp);
                }
            }
        }
        int sumOfRanks=(population.size()*(population.size()+1))/2;
        for(int i=0;i<population.size();i++)
        {
            population.get(i).rank=i+1;
            population.get(i).percentage=(int) ((double)(i+1)/sumOfRanks*100);
        }
    }
    public static chromosome returnMax(ArrayList<chromosome>finalPopulation){
        max=new chromosome("000");

        for(int i=0;i<finalPopulation.size();i++){
            if(finalPopulation.get(i).fitnessValue>max.fitnessValue){
                max=finalPopulation.get(i);
            }
        }
        return max;
    }


    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        int t=1;
        File file = new File("Bassant Samer - knapsack_input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        int noTestCases = Integer.parseInt(br.readLine());
        while (noTestCases > 0) {
            max=new chromosome("000");
            population=new ArrayList<chromosome>();
            pool=new ArrayList<chromosome>();
            ArrayList<item> items = new ArrayList<item>();
            item item = new item(0, 0);
            int noOfGenerations = 50;
            int knapsackWeight = -1;
            int noOfItems = 0;

            //reading from file
            while ((st = br.readLine()) != null) {
                //Ignore empty lines
                if (st.isEmpty()) {
                    continue;
                }
                //parse knapsackWeight and noOfItems
                else {
                    if (knapsackWeight == -1) {
                        knapsackWeight = Integer.parseInt(st);

                    } else if (noOfItems == 0) {
                        noOfItems = Integer.parseInt(st);

                    }
                    //parse weight and value of each item
                    else {
                        for (int i = 0; i < noOfItems; i++) {

                            String[] pair = st.split(" ");
                            int weight = Integer.parseInt(pair[0]);
                            int value = Integer.parseInt(pair[1]);
                            item = new item(weight, value);
                            items.add(item);
                            if(i!=noOfItems-1)
                            {st=br.readLine();}
                        }
                        break;
                    }
                }
            }
            //Initialize population
            intializePopulation(noOfItems, items, knapsackWeight);

            while(noOfGenerations>0) {
                //Evaluate solutions
                CalculateFitness(items, knapsackWeight);
                sortBasedOnFitness();

                //Perform selection then crossover (do crossover n/2 times while n=popSize)
                for (int i = 0; i < populationSize/2 ; i++) {
                    ArrayList<Integer> offsprings;
                    offsprings = doSelection();
                    doCrossOver(offsprings, items, knapsackWeight);
                }

                //Perform Mutation
                doMutation(items,knapsackWeight);

                //Perform Replacement
                FullReplacement();

                CalculateFitness(items, knapsackWeight);
                pool=new ArrayList<chromosome>();
                noOfGenerations--;
            }

            System.out.println("TestCase "+t+":");
            chromosome print= returnMax(population);
            System.out.println("the Fitness value is :"+print.fitnessValue);
            int ones =0;
            for(int i=0;i<print.binaryName.length();i++)
            {
                if (print.binaryName.charAt(i)=='1')
                {
                    ones++;
                }
            }
            System.out.println("the number of selected items is :"+ones);
            System.out.println("the selected items : ");
            for(int i=0;i<print.binaryName.length();i++)
            {
                if (print.binaryName.charAt(i)=='1')
                {
                    System.out.println("the weight : "+items.get(i).weight+"  the value : "+items.get(i).value);
                }
            }
            System.out.println("=========================");

            t++;
            noTestCases--;

        }

    }

}


