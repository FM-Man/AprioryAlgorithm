import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        Apriori apriori = new Apriori(2);
        apriori.countBestSequence();

    }
}
