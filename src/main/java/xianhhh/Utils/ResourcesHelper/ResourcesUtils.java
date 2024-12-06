package xianhhh.Utils.ResourcesHelper;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourcesUtils {
    private static final File main = new File("C:\\DickClient")
            ,mod = new File("C:\\DickClient\\Mods");

    private static void createDictionary() {
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
            if(f.getName().split("\\.")[length].equalsIgnoreCase("jar")){
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
}