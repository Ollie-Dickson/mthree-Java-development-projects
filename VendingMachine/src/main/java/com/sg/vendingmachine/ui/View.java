package com.sg.vendingmachine.ui;

import com.sg.vendingmachine.dto.Coins;
import com.sg.vendingmachine.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class View {
    private UserIO io;

    @Autowired
    public View(UserIO io){
        this.io = io;
    }

    public List<String> displayMenu(List<Item> stockedItems, BigDecimal cash){
        /*                    (EXAMPLE DISPLAY)
        |-----------VENDING MACHINE-----------|
        |       Money inserted: £00.00        |
        |                                     |
        |          Available Items:           |
        |     1.     Malteasers     £0.73     |
        |     2.      Coca-cola     £1.20     |
        |     3.   Walkers Crisps   £0.85     |
        |-------------------------------------|
        |     To insert money, enter '0'      |
        |                                     |
        |   To purchase an item, enter the    |
        |  associated number as shown above   |
        |                                     |
        |     To quit and receive change,     |
        |             enter '-1'              |
        |-------------------------------------|
        */
        String cashString = cash.toPlainString();
        io.print("|-----------VENDING MACHINE-----------|");
        if(cashString.length() == 4) {
            io.print("|       Money inserted: £"+ cashString +"         |");
        } else if(cashString.length() == 6){
            io.print("|       Money inserted: £"+ cashString +"       |");
        } else {
            io.print("|       Money inserted: £"+ cashString +"        |");
        }
        io.print("|                                     |");
        io.print("|         Available Items:            |");
        int itemCount = 0;
        List<String> itemsIndex = new ArrayList<>();
        if(stockedItems.size() == 0){
            io.print("| Sorry this machine is out of stock  |");
        }
        for(Item item : stockedItems){
            itemCount++;
            itemsIndex.add(item.getName());
            int nameLength = item.getName().length();
            String output = "|     "+ itemCount +".";
            //Algorithm to format spacing around Item names
            int space;
            if(nameLength % 2 == 0){
                space = (20 - nameLength) / 2;
            } else {
                space = (20 - nameLength - 1) / 2;
                output += " ";// extra leading space for odd numbers
            }
            for(int i=0; i<space; i++){
                output += " ";
            }
            output += item.getName();
            for(int i=0; i<space; i++){
                output += " ";
            }
            output += "£"+ item.getCost() +"     |";
            io.print(output);
        }
        io.print("|-------------------------------------|\n" +
                "|     To insert money, enter '0'      |\n" +
                "|                                     |\n" +
                "|   To purchase an item, enter the    |\n" +
                "|  associated number as shown above   |\n" +
                "|                                     |\n" +
                "|     To quit and receive change,     |\n" +
                "|             enter '-1'              |\n" +
                "|-------------------------------------|");
        return itemsIndex;
    }

    public int getMenuSelection(int itemCount){
        return io.readInt(":", -1, itemCount);
    }

    public void displayErrorMessage(String errorMessage) {
        io.print("           === ERROR ===");
        io.print(errorMessage);
    }

    public void displayPurchaseSuccess(Item purchasedItem, BigDecimal cash) {
        io.print(purchasedItem.getName() + " successfully purchased.");
        if(purchasedItem.getInventory() == 0){
            io.print(purchasedItem.getName() + " is no longer in stock.");
        }
        io.print("Cash remaining: "+ cash);
    }

    public BigDecimal getMoneyAmount() {
        float input = io.readFloat("Please enter the amount of money you would like to insert (£):",
                0, 100);
        BigDecimal moneyAdded = new BigDecimal(Float.toString(input));
        return moneyAdded.setScale(2, RoundingMode.HALF_UP);
    }

    public void displayReturnedChange(List<Coins> change) {
        io.print("Thank you for using this vending machine.");
        if(change.size() > 0){
            io.print("Here is your change:");
            String changeString = "";
            for (Coins coin : change){
                changeString += coin.name() + ", ";
            }
            changeString = changeString.substring(0, changeString.length()-2);
            io.print(changeString);
        } else {
            io.print("No change given.");
        }
        io.print(" ");
        io.print("Goodbye.");
    }
}
