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
    Bucket<K,V>[] A;

    public Cuckoo() {
        A = new Bucket[10];
        while (h1 == h2) {
            h1 = ((random.nextInt()) * 100 + 99);
            h2 = ((random.nextInt()) * 100 + 99);
        }
    }
    public int hashF1(K key) {return (h1 * key.hashCode() ) % (A.length-1);
    }
    public  int hashF2(K key){
        return  (h2 * key.hashCode()) % (A.length-2);
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
        int a = 2;
        int hash1key = hashF1(key);
        int hash2key = hashF2(key);
        V myV = this.get(key);//элемент с тем же хешкодом
        Bucket temp1 = new Bucket(key, value);
        Bucket temp2;
        K k1 = null;
        K k2 = null;
        V v1 = null;
        V v2 = null;

        if (occupancy(size) > 0.6) {
            grow(A);
        }
        if(myV != null) {
            k1 = A[hash1key].getKey();// ключ по 1 коду
            k2 = A[hash2key].getKey();// ключ по 2 коду
            v1 = A[hash1key].getValue();// значение по 1 коду
            v2 = A[hash2key].getValue();// значение по 2 коду
        }
            if (myV == value || k1 == key || k2 == key) {
                return null;
            } else {
                if (A[hash1key] == null) {
                    A[hash1key] = temp1;
                    size++;
                    return value;
                } else {
                    if (A[hash2key] == null) {
                        A[hash2key] = temp1;
                        size++;
                    } else {
                        while (v1 != myV && v1 != null && v2 != myV && v2 != null) {
                            A[hash1key] = temp1;
                            temp1 = new Bucket<>(k1, v1);
                            temp2 = new Bucket<>(k2, v1);
                            hash1key = hashF1(k1);
                            hash2key = hashF2(k1);
                            if (a % 2 == 0) {
                                v2 = A[hash2key].getValue();
                                k2 = A[hash2key].getKey();

                                if (A[hash1key] == null) {
                                    A[hash1key] = temp1;
                                    size++;
                                    a +=1;
                                    return value;
                                } else {
                                    if (A[hash2key] == null) {
                                        A[hash2key] = temp1;
                                        size++;
                                        a +=1;
                                        return  value;
                                    }
                                }
                            } else {
                                v1 = A[hash1key].getValue();
                                k1 = A[hash1key].getKey();
                                if (A[hash1key] == null) {
                                    A[hash1key] = temp2;
                                    size++;
                                    a +=1;
                                    return value;

                                } else {
                                    if (A[hash2key] == null) {
                                        A[hash2key] = temp2;
                                        size++;
                                        a +=1;
                                        return value;

                                    }
                                }
                            }
                        }
                    }
                    if (v1 == myV || v2 == myV) {
                        Cuckoo B = new Cuckoo<K, V>();
                        for (int i = 0; i < A.length; i++) {
                            if (A[i] != null) {
                                K kA = A[i].getKey();
                                V vA = A[i].getValue();
                                B.put(kA, vA);
                            }
                        }
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

