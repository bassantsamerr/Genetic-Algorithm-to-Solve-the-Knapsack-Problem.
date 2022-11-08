package knapsackga;
/**
 *
 * @author reem
 */
public class chromosome {
    
    public String binaryName;
    public int fitnessValue;
    
    public chromosome(String name, int fitnessValue) {
        this.binaryName = name;
        this.fitnessValue = fitnessValue;
    }

    public chromosome(String name) {
        this.binaryName = name;
        this.fitnessValue=0;
    }

    @Override
    public String toString() {
        return "chromosome{" + "binaryName=" + binaryName + ", fitnessValue=" + fitnessValue + '}';
    }
    
    
}
