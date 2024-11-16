package xianhhh.Utils;

import xianhhh.Module.Module;

import java.util.ArrayList;
import java.util.HashMap;

public class SortUtils {
    public static ArrayList<String> ModuleNameLengthSort(ArrayList<Module> array) {

        HashMap<String, Integer> hash = new HashMap<String, Integer>();

        ArrayList<String> names = new ArrayList<String>();

        for (Module m : array) {
            int length = m.getName().length();
            String name = m.getName();
            names.add(name);
            hash.put(name, length);
        }
/*
        List<Integer> leh = hash.values().stream().toList();

        ArrayList<Integer> lengthes = new ArrayList<Integer>();

        for (Integer i : leh) {
            lengthes.add(i);
        }

        Collections.sort(lengthes);

        ArrayList<String> names = new ArrayList<String>();
        for (Map.Entry<String, Integer> et : hash.entrySet()) {
            for (Integer l : lengthes) {
                if (l == et.getValue()) {
                    names.add(et.getKey());
                }
            }
        }

        return names;
         */

        names.sort(
                (a,b) -> {
                    return b.length() - a.length();
                }
        );

        return names;
    }

    public static <T> ArrayList<String> LengthSort(ArrayList<T> array){
        ArrayList<String> names = new ArrayList<String>();

        for(T t : array){
            names.add(t.toString());
        }

        names.sort(
                (a,b) -> {
                    return b.length() - a.length();
                }
        );

        return names;
    }


    public static String[] replace(String[] string, String regex , String replacement){
        String[] result = new String[string.length];
        int i = 0;
        for(String s : string){
            s = s.replaceAll(regex, replacement);
            result[i] = s;
            i++;
        }
        return result;
    }
}
