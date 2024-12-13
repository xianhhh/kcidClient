package xianhhh.ModLoader;

import xianhhh.ModLoader.Annotation.ModLoad;
import xianhhh.ModLoader.Annotation.ModMain;
import xianhhh.Utils.ResourcesHelper.ResourcesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModLoader {
    public void load(){
       ArrayList<JarFile> mods = ResourcesUtils.getModJars();
       ResourcesUtils.createDictionary();

       if(mods.isEmpty()){
           return;
       }
       for(JarFile jarFile : mods){
           try {
               // 打开 JAR 文件

               // 遍历 JAR 文件中的所有条目
               Enumeration<JarEntry> entries = jarFile.entries();
               while (entries.hasMoreElements()) {
                   JarEntry entry = entries.nextElement();

                   // 只处理以 ".class" 结尾的条目
                   if (entry.getName().endsWith(".class")) {
                       String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                       Class<?> mainClass = Class.forName(className);
                       ResourcesUtils.isModHasMainAndLoadMDAndInvoke(mainClass, ModMain.class, ModLoad.class);
                   }
               }

               // 关闭 JAR 文件
               jarFile.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
}
