package knapsackga;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * @author reem , beso
 */
public class KnapsackGA { 
   
    static int populationSize = 10;
    ArrayList<chromosome> population = new ArrayList<chromosome>();
    public boolean inArray(chromosome tmp ,   ArrayList<chromosome> Arr ){
        for (int i = 0; i < Arr.size(); i++) 
            if(tmp.binaryName.equals(Arr.get(i).binaryName))
              return true;
        return false;
    }
    public void intializePopulation(int numItems) {
        if(populationSize>Math.pow(2,numItems)){
        populationSize=(int) Math.pow(2,numItems);
        }
        for (int i = 0; i < populationSize; i++) // 10 is the number of chromosomes generated
        {   chromosome tmp= new chromosome("");
            for (int j = 0; j < numItems; j++) {
                float x = (float) Math.random();
                char c;
                if (x <= 0.5) // 1
                {c = '1';}
                else //0
                {c = '0';}
                tmp.binaryName += c;
            }
            System.out.println(inArray(tmp,population));
            if (inArray(tmp,population)) {
                i--;
            } else {
                population.add(tmp);
            }
        }
    }
    
   public void printPopulation() {
        for (int i = 0; i < population.size(); i++) {
          System.out.println(population.get(i).toString());
        }
    }

    public int calculateWeight(String s,ArrayList<item> it )
    {
        int res=0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='1'){
            res+=it.get(i).weight;
            }
        }
        return res;
    }
    
    public int calculateValue(String s,ArrayList<item> it )
    {
        int res=0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='1'){
            res+=it.get(i).value;
            }
        }
        return res;
    }
    
    public void CalculateFitness(ArrayList<item> it, int weight)
    {
        for (int i = 0; i < population.size(); i++)
        {
          int weightOfCh =calculateWeight(population.get(i).binaryName,it);
          if(weightOfCh>weight || weightOfCh==0) //rejected
           {
               population.remove(i);
               i--;
           }
          else //accepted and calculate its fitnessValue
          {
          population.get(i).fitnessValue=calculateValue(population.get(i).binaryName,it);
          }
        }
    }
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<item> Test = new ArrayList<item>();
        item obj = new item(3,1);
        Test.add(obj);
        obj = new item(1,6);
        Test.add(obj);
        obj = new item(2,4);
        Test.add(obj);
        obj = new item(6,9);
       // Test.add(obj);
        obj = new item(4,3);
      //  Test.add(obj);
       
      int t = 1;
        while (t > 0) {
            //int n = input.nextInt();
            KnapsackGA object = new KnapsackGA();
            object.intializePopulation(Test.size());
              object.printPopulation();
            object.CalculateFitness(Test,8);
            System.out.println("reko");
            object.printPopulation(); //after rejection

            t--;
        }
        
//         KnapsackGA object = new KnapsackGA() ;
//         object.intializePopulation(8);
//         object.printPopulation();
//         System.out.println("reem");
//         object.intializePopulation(6);
//         object.printPopulation();
//        
//         ArrayList<item> items = new ArrayList<item>();
//         int tc;
//         tc=input.nextInt();
//         while(tc>0){
//             int size=10;
//             int num=3;
//             
//             tc--;
//         }

    }

}
