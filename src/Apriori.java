import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Apriori {
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> candidateSequence = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> eligibleSequence = new ArrayList<>();
    private int[] countOfCandidateSequence;
    private final int minSupport;

    public Apriori(int minSupport){
        this.minSupport = minSupport;
    }

    public void countBestSequence() throws FileNotFoundException {
        readFile();
        addItems();

        while (true){
            countCandidateSequences();
            removeIneligibleCandidates();

            if(candidateSequence.size() > 0){
                eligibleSequence = new ArrayList<>(candidateSequence);
                candidateSequence = new ArrayList<>();
            } else break;
            if(eligibleSequence.size() <= 1) break;

            join();
        }


        Object[] options = {"See Confidence",
                "Exit"};
        int n = JOptionPane.showOptionDialog(new Frame(),//parent container of JOptionPane
                "Most Frequent Itemset:\n"+print(eligibleSequence),
                "Apriori Algorithm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,//do not use a custom Icon
                options,//the titles of buttons
                options[0]);//default button title

        if(n==0) confidence();
    }




    private void join() {
        for(int i = 0; i< eligibleSequence.size(); i++){
            for (int j = i+1; j< eligibleSequence.size(); j++){
                if(hasCommonPrefix(eligibleSequence.get(i), eligibleSequence.get(j))){
                    ArrayList<Integer> newOne = new ArrayList<>(eligibleSequence.get(i));
                    newOne.add( lastElement(eligibleSequence.get(j)) );
                    boolean flag= true;
                    for (int x=newOne.size()-1;x>=0;x--){
                        ArrayList<Integer> checking = new ArrayList<>(newOne);
                        checking.remove(x);
                        if(!eligibleSequence.contains(checking)){
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        candidateSequence.add(newOne);
                    }
                }
                else break;
            }
        }
    }

    private int lastElement(ArrayList<Integer> a){
        return a.get(a.size()-1);
    }

    private boolean hasCommonPrefix(ArrayList<Integer> a, ArrayList<Integer> b){
        if(a.size() <= 1) return true;
        else {
            boolean flag = true;
            for (int i=0;i<a.size()-1;i++){
                if(!a.get(i).equals(b.get(i))){
                    flag = false;
                    break;
                }
            }
            return flag;
        }
    }




    private void removeIneligibleCandidates() {
        for(int i = countOfCandidateSequence.length-1; i>=0; i--){
            if(countOfCandidateSequence[i]< minSupport){
                candidateSequence.remove(i);
            }
        }
    }

    private void countCandidateSequences() {
        countOfCandidateSequence = new int[candidateSequence.size()];
        for (int i = 0; i< candidateSequence.size(); i++) {
            for (Transaction t : transactions) {
                if (t.has(candidateSequence.get(i)))
                    countOfCandidateSequence[i]++;
            }
        }
    }

    private void addItems() {
        ArrayList<Integer> allItems = new ArrayList<>();
        for (Transaction t:transactions) {
            for (int i:t.items()) {
                if(!allItems.contains(i)) allItems.add(i);
            }
        }
        Collections.sort(allItems);
        for (Integer anItem : allItems) {
            ArrayList<Integer> aL = new ArrayList<>();
            aL.add(anItem);
            candidateSequence.add(aL);
        }
    }




    private void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("transactions.txt"));

        while (scanner.hasNextLine()){
            String string = scanner.nextLine();
            String[] splitString = string.split(",",0);
            int[] arguments = new int[splitString.length];
            for(int i=0; i< splitString.length;i++){
                arguments[i] = Integer.parseInt(splitString[i]);
            }
            transactions.add(new Transaction(arguments));
        }
    }

    private String print(ArrayList<ArrayList<Integer>> a){
        StringBuilder returnString = new StringBuilder();
        for (ArrayList<Integer> i:a) {
            for (int j:i) {
                returnString.append(j).append(" ");
            }
            returnString.append("\n");
        }
        return returnString.toString();
    }





    private void confidence(){
        String output = "";
        for(ArrayList<Integer> eligibleSeq: eligibleSequence){
            output+=powerSet(eligibleSeq, -1, new ArrayList<>());
        }
        JOptionPane.showMessageDialog(null, output);
    }

    private String powerSet(ArrayList<Integer> sequence, int index, ArrayList<Integer> prefix) {
        int n = sequence.size();

        if (index == n) return "";
        String output = "";

        ArrayList<Integer> complimentSet = new ArrayList<>(sequence);
        if(prefix.size() != 0 && prefix.size() != sequence.size()) {
            complimentSet.removeAll(prefix);
            output+= "confidence of " + prefix + " => " + complimentSet+" is "+countConfidence(prefix,sequence)+" %\n";
        }

        for (int i = index + 1; i < n; i++) {
            prefix.add(sequence.get(i));
            output+=powerSet(sequence, i, prefix);

            prefix.remove(prefix.size()-1);
        }

        return output;
    }

    private double countConfidence(ArrayList<Integer> given, ArrayList<Integer> expected){
        int givenCount = 0;
        int expectedCount = 0;

        for (Transaction t:transactions){
            if(t.has(given)) givenCount++;
            if(t.has(expected)) expectedCount++;
        }
        return (double) ((int)(((double) expectedCount)/((double) givenCount)*10000))/100;
    }
}
