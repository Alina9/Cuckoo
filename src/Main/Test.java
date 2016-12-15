package Main;

import java.util.Random;

/**
 * Created by Алина on 05.12.2016.
 */
public class Test {
    public static void main(String[] args) {
        Random rand = new Random(1);
        int expected[] = new int[10000];
        Cuckoo cuckoo = new Cuckoo<Integer,Double>();
        for (int i = 0; i < 10000; i++){
            expected[i] = rand.nextInt();
            cuckoo.put(i,expected[i]);
        }
        for (int i = 0; i < 10000; i++){
            assert((int)cuckoo.get(i) == expected[i]);
            System.out.println(cuckoo.get(i));
        }
        System.out.println(cuckoo.get(0));
        System.out.println(cuckoo.size());
    }
}