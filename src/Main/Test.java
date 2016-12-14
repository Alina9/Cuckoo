package Main;

import java.util.Random;

/**
 * Created by Алина on 05.12.2016.
 */
public class Test {
    public static void main(String[] args) {
        Random rand = new Random(1);
        Cuckoo cuckoo = new Cuckoo<Integer,Double>();
        for (int i = 0; i < 1000; i++){
            cuckoo.put(i,rand.nextInt());
        }
        rand = new Random(1);
        for (int i = 0; i < 10000; i++){
            System.out.println(cuckoo.get(i));
        }
        System.out.println(cuckoo.get(0));
        System.out.println(cuckoo.size());
    }
}