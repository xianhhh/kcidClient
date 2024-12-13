package xianhhh.ModLoader;

import xianhhh.Client.Client;
import xianhhh.Event.EventBus.Annotation.EventTarget;
import xianhhh.Event.EventHandleT;
import xianhhh.Event.Events.GameStartEvent;
import xianhhh.ModLoader.Annotation.ModLoad;
import xianhhh.ModLoader.Annotation.ModMain;
import xianhhh.Utils.ResourcesHelper.ResourcesUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModLoader {

    public static void registerSelf() {
        Client.eventHandle.supe(new ModLoader(), EventHandleT.Mode.REGISTER);
    }

    @EventTarget
    public void load(GameStartEvent ev) {
        ResourcesUtils.createDictionary();
        ArrayList<File> mods = ResourcesUtils.getMod();

        if (mods.isEmpty()) {
            return;
        }
        for (File jarFile : mods) {
            Class<?> mainClass = loadMainClassFromJar(jarFile);
            ResourcesUtils.isModHasMainAndLoadMDAndInvoke(mainClass, ModMain.class, ModLoad.class);
        }
    }

    public Class<?> loadMainClassFromJar(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            URL[] urls = {jarFile.toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls);

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace("/", ".").substring(0, entry.getName().length() - 6);
                    Class<?> loadedClass = classLoader.loadClass(className);
                    return loadedClass;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
