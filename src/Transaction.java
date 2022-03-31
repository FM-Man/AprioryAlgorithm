import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Transaction {
    public final int[] items;

    public Transaction(int[] items) {
        this.items = items;
        Arrays.sort(this.items);
    }

    public boolean has(ArrayList<Integer> itemsToCheck){
        Collections.sort(itemsToCheck);

        if(itemsToCheck.get(0) < items[0] || itemsToCheck.get(itemsToCheck.size()-1) > items[items.length-1]){
            return false;
        }

        int i=0,j=0;
        boolean flag = true;
        while(true){
            if(j==itemsToCheck.size()){
                break;
            }
            if(i == items.length){
                flag = false;
                break;
            }


            if(items[i]<itemsToCheck.get(j)) {
                i++;
            } else if(items[i]==itemsToCheck.get(j)) {
                i++;
                j++;
            } else {
                flag = false;
                break;
            }
        }

        return flag;
    }

    public void print(){
        for (int i:items) {
            System.out.print(i);
        }
        System.out.println();
    }
}
