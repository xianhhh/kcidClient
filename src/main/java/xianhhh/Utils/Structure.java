package xianhhh.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Structure<T> {
    private T s;
    private final HashMap<Integer,T> save = new HashMap<Integer,T>();
   public int getMaxKey(){
       List<Integer> list = save.keySet().stream().toList();

       list.sort((a,b) -> b - a);

       return list.get(list.size());
   }

   public T getT(){
       return s;
   }

   public void add(T value,int index){
       save.put(index,value);
   }
    public void add(T value){
       for(Iterator<Integer> i = save.keySet().iterator(); save.keySet().iterator().hasNext();){
           int next = i.next();
           save.putIfAbsent(next, value);
       }
    }

   public List<T> elements(){
       return save.values().stream().toList();
   }

   public int size(){
        return save.size();
   }

}
