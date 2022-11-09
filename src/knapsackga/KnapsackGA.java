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
    // we need to make rank based selection 
    //m7tagen ne2ra mn file
    //main
    static int populationSize = 10;
    static double pc = 0.7;
    static double pm = 0.1;
    static ArrayList<chromosome> population = new ArrayList<chromosome>();
    static ArrayList<chromosome> pool = new ArrayList<chromosome>();

    public static boolean inArray(chromosome tmp, ArrayList<chromosome> Arr) {
        for (int i = 0; i < Arr.size(); i++)
            if (tmp.binaryName.equals(Arr.get(i).binaryName))
                return true;
        return false;
    }

    public static void intializePopulation(int numItems) {
        if (populationSize > Math.pow(2, numItems)) {
            populationSize = (int) Math.pow(2, numItems);
        }
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
            if (inArray(tmp, population)) {
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
            if (weightOfCh > weightKnapsack || weightOfCh == 0) //rejected
            {
                population.remove(i);
                i--;
            } else //accepted and calculate its fitnessValue
            {
                population.get(i).fitnessValue = calculateValue(population.get(i).binaryName, it);
            }
        }
    }

    public static double randomNumber(int min, int max) {
        return (double) Math.random() * (max - min) + min;
    }

    public static ArrayList<Integer> doSelection() {
        ArrayList<Integer> pre = new ArrayList<>();
        pre.add(0);
        int sum = 0;
        for (int i = 0; i < population.size(); i++) {
            sum += population.get(i).fitnessValue;
            pre.add(sum);
        }
        Random random = new Random();
        int x = random.nextInt(populationSize + 1);
        int y = random.nextInt(populationSize + 1);
        int index1 = -1;
        int index2 = -1;
        for (int i = 0; i < pre.size(); i++) {
            if (!(x >= pre.get(i)) && index1 == -1) {
                index1 = i - 1;
            }
            if (!(y >= pre.get(i)) && index2 == -1) {
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

    public static void doCrossOver(ArrayList<Integer> result) {
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
        } else {
            offSpring1 = population.get(result.get(0));
            offSpring2 = population.get(result.get(1));
        }
        pool.add(offSpring1);
        pool.add(offSpring2);

    }

    public char flipBit(char c) {
        if (c == '1') {
            return '0';
        }
        return '1';
    }

    public void doMutation() {
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
            pool.get(i).binaryName = tmp;
        }
    }

    public static void FullReplacement() {
        Collections.copy(population, pool); // copying the ArrayList pool to the population list

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner input = new Scanner(System.in);
        ArrayList<item> items = new ArrayList<item>();
        item item = new item(0, 0);

        File file = new File("D:\\Faculty\\Semester 7\\Soft Computing\\Assignments\\knapsackGA\\test.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        int noTestCases = Integer.parseInt(br.readLine());
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

                    String[] pair = st.split(" ");
                    int weight = Integer.parseInt(pair[0]);
                    int value = Integer.parseInt(pair[1]);
                    item = new item(weight, value);
                    items.add(item);
                }


            }
        }
        for (int i = 0; i < items.size(); i++) {
            System.out.println("item " + i + " weight:" + items.get(i).weight + " value:" + items.get(i).value);
        }

        //Initialize population
        System.out.println("initialize population");
        intializePopulation(noOfItems);
        printPopulation(population);
        //Evaluate solutions
        System.out.println("Evaluate solutions");
        CalculateFitness(items, knapsackWeight);
        printPopulation(population);
        //Perform selection then crossover (do crossover n times while n=popSize)
        System.out.println("Perform selection then crossover");
        for (int i = 0; i < population.size(); i++) {
            ArrayList<Integer> offsprings;
            offsprings = doSelection();
            doCrossOver(offsprings);
        }
        printPopulation(pool);
        //Perform Mutation
        //Perform Replacement
        //FullReplacement();
    }



}


