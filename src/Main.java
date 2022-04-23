import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        while (true) {
            Object[] options = {
                    "New Transaction",
                    "Most Frequent Itemset",
                    "Exit"
            };
            int n = JOptionPane.showOptionDialog(null,
                    "Welcome!",
                    "Apriori Algorithm",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if(n==2) break;
            else if(n==1){
                int minimumSupport = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Minimum Support:"));
                new Apriori(minimumSupport).countBestSequence();
            }
            else if(n==0){
                String newTrans = JOptionPane.showInputDialog(null, "New Transactions (Items Separated by comma):");
                BufferedWriter bf = new BufferedWriter(new FileWriter("transactions.csv",true));
                bf.append("\n").append(newTrans);
                bf.close();
            }
        }
    }
}
