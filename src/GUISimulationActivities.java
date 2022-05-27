import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class GUISimulationActivities implements ActionListener { //has activity
    private final JTextArea INFO_SCREEN;
    private final Inventory INFO;
    private JFrame frame;
    private JLabel daysPassed;
    private JPanel actionsPanel;
    private JPanel continuePanel;
    private JPanel savePanel;
    private JPanel foodBuyPanel;
    private JPanel foodUsePanel;
    private JPanel foodGivePanel;
    private JTextField choiceField;
    private int napCount;

    public GUISimulationActivities(Inventory info) {
        INFO_SCREEN = new JTextArea(20, 40);
        choiceField = new JTextField();
        this.INFO = info;
        setupGUI();
        napCount = 0;
        loadInfoScreen();
    }

    public void setupGUI() {
        JFrame frame = new JFrame("Homeless Simulation");
        this.frame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel daysPassed = new JLabel("Days Passed: 0");
        daysPassed.setFont(new Font("Courier", Font.BOLD, 20));
        daysPassed.setForeground(Color.black);
        JPanel logoWelcome = new JPanel();
        logoWelcome.add(daysPassed);
        this.daysPassed = daysPassed;

        JPanel inventoryPanel = new JPanel();
        INFO_SCREEN.setText("inventory loading...");
        INFO_SCREEN.setFont(new Font("Courier", Font.PLAIN, 15));
        INFO_SCREEN.setWrapStyleWord(true);
        INFO_SCREEN.setLineWrap(true);
        inventoryPanel.add(INFO_SCREEN);

        JPanel actionsPanel = new JPanel();
        JButton begButton = new JButton("Beg");
        JButton scavengeButton = new JButton("Scavenge");
        JButton showerButton = new JButton("Shower");
        JButton shopButton = new JButton("Shop");
        JButton napButton = new JButton("Nap");
        JButton eatButton = new JButton("Eat");
        JButton feedCatButton = new JButton("Feed Cat");
        JButton saveButton = new JButton("Save");
        begButton.addActionListener(this);
        scavengeButton.addActionListener(this);
        showerButton.addActionListener(this);
        shopButton.addActionListener(this);
        napButton.addActionListener(this);
        eatButton.addActionListener(this);
        feedCatButton.addActionListener(this);
        saveButton.addActionListener(this);
        actionsPanel.add(begButton);
        actionsPanel.add(scavengeButton);
        actionsPanel.add(showerButton);
        actionsPanel.add(shopButton);
        actionsPanel.add(napButton);
        actionsPanel.add(eatButton);
        actionsPanel.add(feedCatButton);
        actionsPanel.add(saveButton);
        this.actionsPanel = actionsPanel;
        frame.add(logoWelcome, BorderLayout.NORTH);
        frame.add(inventoryPanel,BorderLayout.CENTER);
        frame.add(actionsPanel, BorderLayout.SOUTH);

        foodBuyPanel = new JPanel();
        foodUsePanel = new JPanel();
        foodGivePanel = new JPanel();
        frame.pack();
        frame.setVisible(true);
    }

    public void continueOption() {
        JPanel continuePanel = new JPanel();
        JButton continueButton = new JButton("Continue");
        continuePanel.add(continueButton);
        continueButton.addActionListener(this);
        frame.add(continuePanel, BorderLayout.SOUTH);
        continuePanel.setVisible(true);
        this.continuePanel = continuePanel;
    }

    private void loadInfoScreen() {
        daysPassed.setText("Days Passed: " + INFO.getDaysPassed());
        actionsPanel.setVisible(true);
        DecimalFormat dF = new DecimalFormat("#.##");
        dF.setRoundingMode(RoundingMode.DOWN);
        double money = Double.parseDouble(INFO.getMoney() + "");
        String status = "Money: $" + money + "\n";
        status += "Appeal: " + INFO.getAppeal() + "\n";
        status += "Energy: " + INFO.getEnergy() + "\n";
        if (INFO.getCatEnergy() == -1) {
            status +=  "Cat Energy: X";
        } else {
            status += "Cat Energy: " + INFO.getCatEnergy();
        }
        status += "\nActions Left: " + INFO.getActionCount();
        status += "\n\nWhat do you want to do? Choose an action.";
        INFO_SCREEN.setText(status);
    }

    public void save() {
        actionsPanel.setVisible(false);
        String ending = "";
        if (napCount == 3) { ending +=  "You napped the whole day and find yourself sleeping, never waking up again."; }
            if ((INFO.getActionCount() == 0 && INFO.getEnergy() < 0)) {
            ending += "It's the end of the day, but you don't have any energy left...\nCongrats on surviving for " + INFO.getDaysPassed() + " days!\n";
        }
        ending += "Do you want to save your data?";
        INFO_SCREEN.setText(ending);
        JPanel savePanel = new JPanel();
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        savePanel.add(yesButton);
        savePanel.add(noButton);
        yesButton.addActionListener(this);
        noButton.addActionListener(this);
        frame.add(savePanel, BorderLayout.SOUTH);
        this.savePanel = savePanel;
        savePanel.setVisible(true);
    }

    public void transition() {
        continuePanel.setVisible(false);
        INFO_SCREEN.setText("");
        if (INFO.getActionCount() == 0 && INFO.getEnergy() < 0) {
            save();
        } else if (INFO.getActionCount() == 0 && INFO.getEnergy() >= 0) {
            if (napCount == 3) { save(); }
            else {
                INFO.addDaysPassed();
                INFO.setActionCount(3);
                INFO.changeEnergy(-1);
                INFO.changeCatEnergy(-1);
                if (INFO.getCatEnergy() == 0) {
                    INFO.changeCatEnergy(-1);
                    String badMews = "It's the end of the day, but your cat doesn't have any energy left...\nBut you must go on.";
                    INFO_SCREEN.setText(badMews);
                    continueOption();
                } else {
                    INFO_SCREEN.setText("You finished 1 whole day. Congrats!");
                    continueOption();
                }
            }
        } else {
            loadInfoScreen();
        }
    }

    public void beg() {
        INFO.decreaseActionCount();
        INFO.changeEnergy(-2);
        actionsPanel.setVisible(false);
        int earningsWhole = (int) (Math.random() * 50) + 1;
        for (int i = 3; i < INFO.getAppeal(); i++) {
            int bonus = (int) (Math.random() * 10) + 1;
            earningsWhole += bonus;
        }
        DecimalFormat dF = new DecimalFormat("#.##");
        dF.setRoundingMode(RoundingMode.DOWN);
        String earningsString = dF.format(earningsWhole / 100.0);
        double earnings = Double.parseDouble(earningsString);
        INFO.changeMoney(earnings);
        String activity = "";
        if ((int) (Math.random() * 2) == 0) { activity = "shaking your cup"; }
        else { activity = "holding up a cardboard paper"; }
        INFO_SCREEN.setText("You wait for a few hours " + activity + "...\nYou earned $" + earningsString + "!");
        INFO.changeAppeal(-1);
        continueOption();
    }

    public void scavenge() {
        INFO.setEnergy(30);
        INFO.setActionCount(3);
        INFO.changeAppeal(-3);
        transition();
    }

    public void shower() {
        INFO.decreaseActionCount();
        INFO.changeEnergy(-3);
        actionsPanel.setVisible(false);
        int appealGain = (int) (Math.random() * 2) + 1;
        INFO.changeAppeal(appealGain);
        String screenText = "You take a shower in the park's public restroom...\nYou gained ";
        if (appealGain == 1) { screenText += "an appeal point."; }
        else { screenText += appealGain + " appeal points!"; }
        INFO_SCREEN.setText(screenText);
        continueOption();
    }

    public void shop() {
        actionsPanel.setVisible(false);
        String screenText = "Shop:\n# - name - energy refuel - price\n";
        for (int i = 0; i != INFO.getFOOD_SHOP().length; i++) {
            Food food = INFO.getFOOD_SHOP()[i];
            screenText += (i + 1) + " - " + food.getName() + " - " + food.getEnergy() + " - $" + food.getPrice() + "\n";
        }
        screenText += "\nWhich do you want to buy? You have $" + INFO.getMoney();
        INFO_SCREEN.setText(screenText);
        JLabel choiceBox = new JLabel("Choice #: ");
        JPanel foodBuyPanel = new JPanel();
        JTextField choiceField = new JTextField(3);
        this.choiceField = choiceField;
        JButton buyButton = new JButton("Buy");
        JButton stopButton = new JButton("Stop");
        foodBuyPanel.add(choiceBox);
        foodBuyPanel.add(choiceField);
        foodBuyPanel.add(buyButton);
        foodBuyPanel.add(stopButton);
        buyButton.addActionListener(this);
        stopButton.addActionListener(this);
        frame.add(foodBuyPanel, BorderLayout.SOUTH);
        foodBuyPanel.setVisible(true);
        this.foodBuyPanel = foodBuyPanel;
    }

    public void continuePurchase() {
        foodBuyPanel.setVisible(false);
        String foodChoiceString = choiceField.getText();
        int foodChoice = Integer.parseInt(foodChoiceString);
        if (foodChoice < 0 || foodChoice > INFO.getFOOD_SHOP().length) {
            INFO_SCREEN.setText("That's not an option.");
            continueOption();
        } else {
            Food food = INFO.getFOOD_SHOP()[foodChoice - 1];
            String screenText = "";
            if (INFO.getMoney() < food.getPrice()) {
                screenText += "Purchase Denied:\nYou are short $" + (food.getPrice() - INFO.getMoney());
            } else {
                screenText += "Purchase Successful:\nYou spent $" + food.getPrice();
                INFO.addFood(food);
                INFO.changeMoney(-food.getPrice());
            }
            INFO_SCREEN.setText(screenText + " for a " + food.getName().toLowerCase() + "." );
            continueOption();
        }
    }

    public void nap() {
        INFO.decreaseActionCount();
        actionsPanel.setVisible(false);
        int energyGain = (int) (Math.random() * 3);
        INFO.changeAppeal(energyGain);
        String screenText = "You take a nap...\nYou gained ";
        if (energyGain == 1) { screenText += "an energy point."; }
        else { screenText += energyGain + " energy points!"; }
        int moneyStolenWhole = (int) (Math.random() * (INFO.getMoney() + 1));
        for (int i = 3; i < INFO.getAppeal(); i++) {
            int bonus = (int) (Math.random() * 10) + 1;
            moneyStolenWhole += bonus;
        }
        DecimalFormat dF = new DecimalFormat("#.##");
        dF.setRoundingMode(RoundingMode.DOWN);
        String moneyStolenString = dF.format(moneyStolenWhole / 100.0);
        double moneyStolen = Double.parseDouble(moneyStolenString);
        screenText += "\nBut while you were sleeping, you were robbed $" + moneyStolen + "!";
        INFO.changeMoney(-moneyStolen);
        INFO_SCREEN.setText(screenText);
        napCount++;
        continueOption();
    }

    public void eat() {
        actionsPanel.setVisible(false);
        String screenText = "Inventory:\n# - name - energy refuel\n";
        for (int i = 0; i != INFO.getFoods().size(); i++) {
            Food food = INFO.getFoods().get(i);
            screenText += (i + 1) + " - " + food.getName() + " - " + food.getEnergy() + " - $" + food.getPrice() + "\n";
        }
        screenText += "\nWhich do you want to eat? You have " + INFO.getEnergy() + " energy.";
        INFO_SCREEN.setText(screenText);
        JLabel choiceBox = new JLabel("Choice #: ");
        JTextField choiceField = new JTextField(3);
        this.choiceField = choiceField;
        JButton useButton = new JButton("Use");
        JButton stopButton = new JButton("Stop");
        JPanel foodUsePanel = new JPanel();
        foodUsePanel.add(choiceBox);
        foodUsePanel.add(choiceField);
        foodUsePanel.add(useButton);
        foodUsePanel.add(stopButton);
        useButton.addActionListener(this);
        stopButton.addActionListener(this);
        frame.add(foodUsePanel, BorderLayout.SOUTH);
        foodUsePanel.setVisible(true);
        this.foodUsePanel = foodUsePanel;
    }

    public void use() {
        foodUsePanel.setVisible(false);
        String foodChoiceString = choiceField.getText();
        int foodChoice = Integer.parseInt(foodChoiceString);
        if (foodChoice < 0 || foodChoice > INFO.getFoods().size()) {
            INFO_SCREEN.setText("That's not an option.");
            continueOption();
        } else {
            Food food = INFO.getFoods().get(foodChoice - 1);
            INFO.changeEnergy(food.getEnergy());
            INFO.removeFood(foodChoice - 1);
            INFO_SCREEN.setText("You used a " + food.getName().toLowerCase() + " and gained " + food.getEnergy() + " energy!");
            continueOption();
        }
    }

    public void feedCat() {
        actionsPanel.setVisible(false);
        String screenText = "Inventory:\n# - name - energy refuel\n";
        for (int i = 0; i != INFO.getFoods().size(); i++) {
            Food food = INFO.getFoods().get(i);
            screenText += (i + 1) + " - " + food.getName() + " - " + food.getEnergy() + " - $" + food.getPrice() + "\n";
        }
        screenText += "\nWhich do you want to feed to your cat? The cat has " + INFO.getCatEnergy() + " energy.";
        INFO_SCREEN.setText(screenText);
        JLabel choiceBox = new JLabel("Choice #: ");
        JTextField choiceField = new JTextField(3);
        this.choiceField = choiceField;
        JButton feedButton = new JButton("Feed");
        JButton stopButton = new JButton("Stop");
        JPanel foodGivePanel = new JPanel();
        foodGivePanel.add(choiceBox);
        foodGivePanel.add(choiceField);
        foodGivePanel.add(feedButton);
        foodGivePanel.add(stopButton);
        feedButton.addActionListener(this);
        stopButton.addActionListener(this);
        frame.add(foodGivePanel, BorderLayout.SOUTH);
        foodGivePanel.setVisible(true);
        this.foodGivePanel = foodGivePanel;
    }

    public void feed() {
        foodGivePanel.setVisible(false);
        String foodChoiceString = choiceField.getText();
        int foodChoice = Integer.parseInt(foodChoiceString);
        if (foodChoice < 0 || foodChoice > INFO.getFoods().size()) {
            INFO_SCREEN.setText("That's not an option.");
            continueOption();
        } else {
            Food food = INFO.getFoods().get(foodChoice - 1);
            INFO.changeCatEnergy(food.getEnergy());
            INFO.removeFood(foodChoice - 1);
            String screenText = "You used a " + food.getName().toLowerCase() + " to feed the cat. The cat gained " + food.getEnergy() + " energy!";
            INFO_SCREEN.setText(screenText);
            continueOption();
        }
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) (e.getSource());
        String text = button.getText();
        switch (text) {
            case "Beg" -> beg();
            case "Scavenge" -> scavenge();
            case "Shower" -> shower();
            case "Shop" -> shop();
            case "Nap" -> nap();
            case "Eat" -> eat();
            case "Use" -> use();
            case "Feed Cat" -> {
                if (INFO.getCatEnergy() == -1) {
                    INFO_SCREEN.setText("You're too late...");
                    continueOption();
                } else { feedCat(); }
            }
            case "Feed" -> feed();
            case "Save" -> save();
            case "Continue" -> {
                transition();
            }
            case "Buy" -> { continuePurchase(); }
            case "Stop" -> {
                foodBuyPanel.setVisible(false);
                foodUsePanel.setVisible(false);
                foodGivePanel.setVisible(false);
                loadInfoScreen();
            }
            case "Yes" -> {
                INFO.save();
                savePanel.setVisible(false);
                INFO_SCREEN.setText("Progress saved;\nCome again!");
            }
            case "No" -> {
                savePanel.setVisible(false);
                INFO_SCREEN.setText("Progress not saved.\nCome again!");
            }
        }
    }
}