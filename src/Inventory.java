import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Food> foods;
    private double money;
    private int appeal;
    private int energy;
    private int catEnergy;
    private int daysPassed;
    private int actionCount;

    public Inventory() {
        foods = new ArrayList<Food>();
        money = 0.0;
        appeal = 3;
        energy = 5;
        catEnergy = 5;
        daysPassed = 0;
        actionCount = 3;
    }

    public ArrayList<Food> getFoods() { return foods; }
    public double getMoney() { return money; }
    public int getAppeal() { return appeal; }
    public int getEnergy() { return energy; }
    public int getCatEnergy() { return catEnergy; }
    public int getDaysPassed() { return daysPassed; }
    public int getActionCount() { return actionCount; }

    public void setFoods(ArrayList<Food> foodList) { foods = foodList; }
    public void setMoney(double amt) { money = amt; }
    public void setAppeal(int amt) { appeal = amt; }
    public void setEnergy(int amt) { energy = amt; }
    public void setCatEnergy(int amt) { catEnergy = amt; }
    public void addDaysPassed() { daysPassed++; }
    public void setActionCount(int amt) { actionCount = amt; }

    public void addFood(Food food) { foods.add(food); }
    public void removeFood(int index) { foods.remove(index); }

    public String toString() {
        String i = "Day: " + (daysPassed - 1);
        String ii = "Money: $" + money;
        String iii = "Appeal: " + appeal;
        String iv = "Energy: ";
        for (int k = 0; k != energy; k++) {
            System.out.println();
        }
        return i;
    }

    public void save() {
        try {
            File f = new File("src/inventory.data");
            f.createNewFile();
            FileWriter fileWriter = new FileWriter("src/inventory.data");
            for (Food food: getFoods()) {
                fileWriter.write(food.getName() + "; " + food.getPrice() + food.getEnergy() + "\n");
            }
            fileWriter.write("\n" + getMoney() + "; ");
            fileWriter.write(getAppeal() + "; ");
            fileWriter.write(getEnergy() + "; ");
            fileWriter.write(getCatEnergy() + "; ");
            fileWriter.write(getDaysPassed() + "; ");
            fileWriter.write(getActionCount());
            fileWriter.close();
        }
        catch (IOException e) {
            System.out.println("Unable to create file");
            e.printStackTrace();
        }
    }
}