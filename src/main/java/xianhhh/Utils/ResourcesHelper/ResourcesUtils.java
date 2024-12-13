package xianhhh.Utils.ResourcesHelper;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class ResourcesUtils {
    private static final File main = new File("C:\\DickClient")
            ,mod = new File("C:\\DickClient\\Mods");

    public static void createDictionary() {
        if (!main.exists())
            main.mkdir();
        if (!mod.exists()) {
            mod.mkdir();
        }
    }

    public static ArrayList<File> getMod() {
        ArrayList<File> list = new ArrayList<File>();
        File[] files = mod.listFiles();
        for(File f : files){
            int length = f.getName().split("\\.").length;
            if(f.getName().split("\\.")[length-1].equalsIgnoreCase("jar")){
                list.add(f);
            }
        }
        return list;
    }

    public static ArrayList<JarFile> getModJars(){
        try {
            ArrayList<JarFile> list = new ArrayList<JarFile>();
            for (File f : getMod()) {
                list.add(new JarFile(f));
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void isModHasMainAndLoadMDAndInvoke(Class<?> clazz, Class<? extends Annotation> mainAn, Class<? extends Annotation> modloadAn){
        try {
            ArrayList<Method> mts = new ArrayList<Method>();
            if (clazz.isAnnotationPresent(mainAn)) {
                for (Method mt : clazz.getDeclaredMethods()) {
                    mt.setAccessible(true);
                    if (mt.isAnnotationPresent(modloadAn)) {
                        mts.add(mt);
                    }
                }
                for (Method mt : mts) {
                    mt.invoke(clazz, (Object) null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}