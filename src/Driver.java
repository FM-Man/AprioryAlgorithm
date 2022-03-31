import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Driver {
    public static final ArrayList<Transaction> transactions = new ArrayList<>();

    public static ArrayList<ArrayList<Integer>> candidateSequence = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> eligibleSequence = new ArrayList<>();
    public static int[] count;
    public static int minSupport = 2;

    public static void main(String[] args) throws FileNotFoundException {
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

        print(eligibleSequence);
    }

    private static void join() {
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

    private static int lastElement(ArrayList<Integer> a){
        return a.get(a.size()-1);
    }

    private static boolean hasCommonPrefix(ArrayList<Integer> a, ArrayList<Integer> b){
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

    private static void removeIneligibleCandidates() {
        for(int i= count.length-1;i>=0;i--){
            if(count[i]< minSupport){
                candidateSequence.remove(i);
            }
        }
    }

    private static void countCandidateSequences() {
        count = new int[candidateSequence.size()];
        for (int i = 0; i< candidateSequence.size(); i++) {
            for (Transaction t : transactions) {
                if (t.has(candidateSequence.get(i)))
                    count[i]++;
            }
        }
    }

    private static void addItems() {
        ArrayList<Integer> allItems = new ArrayList<>();
        for (Transaction t:transactions) {
            for (int i:t.items) {
                if(!allItems.contains(i)) allItems.add(i);
            }
        }
        Collections.sort(allItems);
        for (Integer allItem : allItems) {
            ArrayList<Integer> aL = new ArrayList<>();
            aL.add(allItem);
            candidateSequence.add(aL);
        }
    }

    private static void readFile() throws FileNotFoundException {
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

    private static void print(ArrayList<ArrayList<Integer>> a){
        for (ArrayList<Integer> i:a) {
            for (int j:i) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }
}
