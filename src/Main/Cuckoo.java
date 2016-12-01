package Main;

import java.util.*;

/**
 * Created by Алина on 29.11.2016.
 */
public class Cuckoo<K,V> implements Map {
    final Random random = new Random();
    int size =  0;
    int h1 = 0;
    int h2 = 0;
    Bucket<K,V>[] A;
    Bucket<K,V>[] B;

    public Cuckoo() {
        A = new Bucket[10];
        B = new Bucket[0];
        while (h1 != h2) {
            h1 = ((random.nextInt()) * 100 + 99);
            h2 = ((random.nextInt()) * 100 + 99);
        }
    }
    public int hashF1(K key) {
        return (h1 * key.hashCode()) % A.length;
    }
    public  int hashF2(K key){
        return  (h2 * key.hashCode()) % A.length;
    }


    public int occupancy(int size){
        return (size/ A.length*2);
    }

    public void grow(Bucket<K, V>[] A) {
        Bucket<K,V>[] Aold = A;
        A = (Bucket<K,V>[]) new Bucket[size * 2 + 1];
        for (int i = 0; i < Aold.length; i++) {
            if (Aold[i] != null) {
                A[i] = Aold[i];
               /* A.put(Aold[i].getKey(), Aold[i].getValue());*/
            }
        }
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() {
        if (size == 0){
            return true;
        }
        else return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return (keySet().contains(key));
    }


    @Override
    public boolean containsValue(Object value) {
        return (values().contains(value));
    }

    public V get(K key) {
        if (A[hashF1(key)] != null) {
            if (key.equals(A[hashF1(key)].getKey())) {
                return A[hashF1(key)].getValue();
            } else return null;
        }else  return  null;
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>,
     * if the implementation supports <tt>null</tt> values.)
     * @throws UnsupportedOperationException if the <tt>put</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     */

    public V put( K key, V value) {
        int p1 = hashF1(key);
        int p2 = hashF2(key);
        V myV = this.get(key);
        Bucket temp = new Bucket(key,value);
        if (occupancy(size) < 0.8) {
            grow(A);
        }
        if(myV != value){
            if (A[p1] == null){
                A[p1] = temp;
                size ++;
            } else {
                if (A[p2] == null) {
                    A[p2] = temp;
                    size ++;
                }
                }
                if ((A[p1] != null) && (A[p2] != null)){
                    V vp1 = A[p1].getValue();
                    K kp1 = A[p1].getKey();
                    while (vp1 == myV || vp1 == null){
                        A[p1] = temp;
                        temp = new Bucket(kp1,vp1);
                        p1 = hashF1(kp1);
                        p2 = hashF2(kp1);
                        vp1 = A[hashF1(kp1)].getValue();
                        kp1 = A[hashF1(kp1)].getKey();
                    }
                }
        }





        return null;

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

    @Override
    public void clear() {
       A = new Bucket [10];
        while (h1 != h2){
            h1 =((random.nextInt())*100+99);
            h2 =((random.nextInt())*100+99);
            size = 0;
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> kList = new TreeSet<>();
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                kList.add(A[i].getKey());
            }
        }
        return kList;
    }

    @Override
    public Collection values() {
        Set<V> kList = new TreeSet<>();
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                kList.add(A[i].getValue());
            }
        }
        return kList;
    }

    @Override
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
            return key;
        }
        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }

}

