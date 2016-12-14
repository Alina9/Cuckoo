package Main;

import java.util.*;

/**
 * Created by Алина on 29.11.2016.
 */
public class Cuckoo<K,V> //implements Map
        {
            Random random = new Random(1);
            int size =  0;
            int h1 = 0;
            int h2 = 0;
            int n = 0;
            Bucket<K,V>[] A;

            public Cuckoo() {
                n = 100000000;
                A = new Bucket[n];
                while (h1 == h2) {
            h1 = ((random.nextInt()) * 100 + 99);
            h2 = ((random.nextInt()) * 100 + 99);
        }
    }
            public Cuckoo(Bucket[]A,int h1,int h2) {
                this.A = A;
                this.h1 = h1;
                this.h2 = h2;
            }

    public int hashF1(K key) {return Math.abs(h1 * key.hashCode() ) % (A.length-2);
    }
    public  int hashF2(K key){
        return  Math.abs(h2 * key.hashCode() ) % (A.length-2);
    }


    public double occupancy(double size){
        double s = size / A.length*2;
        return s;
    }

    public Bucket<K, V>[] grow(Bucket<K, V>[] A) {
        Bucket<K,V>[] Aold = A;
        n = Aold.length *2 + 1;
        A = (Bucket<K,V>[]) new Bucket[n];
        for (int i = 0; i < Aold.length; i++) {
            if (Aold[i] != null) {
                A[i] = Aold[i];
               /* A.put(Aold[i].getKey(), Aold[i].getValue());*/
            }
        }
        return A;
    }


   // @Override
    public int size() {
        return size;
    }


   // @Override
    public boolean isEmpty() {
        if (size == 0){
            return true;
        }
        else return false;
    }

   // @Override
    public boolean containsKey(Object key) {
        return (keySet().contains(key));
    }


   // @Override
    public boolean containsValue(Object value) {
        return (values().contains(value));
    }

    //@Override
    public V get(K key) {
        if (A[hashF1(key)] != null) {
            if (key.equals(A[hashF1(key)].getKey())) {
                return A[hashF1(key)].getValue();
            }
        }
        else
            if (A[hashF2(key)] != null) {
            if (key.equals(A[hashF1(key)].getKey())) {
                return A[hashF2(key)].getValue();
            }
        }
        if (A[hashF1(key)] == null && A[hashF2(key)] == null)
            return null;

        return  null;
    }

public  V put (K key, V value) {
    int hash1 = hashF1(key);
    int hash2 = hashF2(key);
    Bucket temp = new Bucket(key, value);
    Bucket t = new Bucket(key, value);
    int a = 0;
    boolean b = true;
    if (occupancy(size) > 0.6) {
        double f = occupancy(size);
        grow(A);
    }
    while  (b) {
        if (A[hash1] != null && A[hash2] != null) {
            K k1 = A[hash1].getKey();
            K k2 = A[hash2].getKey();
            V v1 = A[hash1].getValue();
            V v2 = A[hash2].getValue();
            if(a % 2 == 0){
                A[hash1] = temp;
                temp = new Bucket(k1,v1);
                hash2 = hashF2(k1);
                a ++;
                if (temp == t) b = false;

            }
            else {
                A[hash2] = temp;
                temp = new Bucket(k2,v2);
                hash1 = hashF2(k2);
                a ++;
                if (temp == t) b= false;
            }
        } else {
            if (A[hash1] == null) {
                A[hash1] = temp;
                b = false;
            }
            else {
                A[hash2] = temp;
                b = false;
            }
            size++;
        }
    }
    if (a !=0 && (A[hash1] == t || A[hash2] == t)){
        Cuckoo B = new Cuckoo<K, V>();
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                K kA = A[i].getKey();
                V vA = A[i].getValue();
                B.put(kA, vA);
            }
        }
        B.put(key, value);
    }
    return value;
}

    public V remove(K key) {
        Bucket<K, V> b1 = A[hashF1(key)];
        if (b1 != null) {
            if (key.equals(b1.getKey())) {
                V value = A[hashF1(key)].getValue();
                A[hashF1((K) key)] = null;
                size -= 1;
                return value;
            } else return null;
        }else return null;
    }




    public void putAll(Cuckoo m) {
            Set<Bucket> entrySet = m.entrySet();
            for (Object bucket : entrySet) {
                this.put(((Map.Entry<K, V>)bucket).getKey(), (((Map.Entry<K, V>)bucket).getValue()));
            }

        }

    //@Override
    public void clear() {
       A = new Bucket [10];
        while (h1 != h2){
            h1 =((random.nextInt())*100+99);
            h2 =((random.nextInt())*100+99);
            size = 0;
        }
    }

    //@Override
    public Set<K> keySet() {
        Set<K> kList = new TreeSet<>();
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                kList.add(A[i].getKey());
            }
        }
        return kList;
    }

   // @Override
    public Collection values() {
        Set<V> kList = new TreeSet<>();
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                kList.add(A[i].getValue());
            }
        }
        return kList;
    }

   // @Override
    public Set<Bucket> entrySet() {
        Set<Bucket> kList = new TreeSet<>();
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                kList.add(A[i]);
            }
        }
        return kList;
    }

    public class Bucket<K , V> implements Map.Entry<K,V> {

        private K key;
        private V value;

        public Bucket(K key,V value) {
            this.key = key;
            this.value = value;

        }
        @Override
        public K getKey() {
            if (this != null)
            return key;
            else return null;
        }
        @Override
        public V getValue() {
            if (this != null)
                return value;
            else return null;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
        public boolean Empty() {
            if (this == null){
                return true;
            }
            else return false;
        }
    }

}

