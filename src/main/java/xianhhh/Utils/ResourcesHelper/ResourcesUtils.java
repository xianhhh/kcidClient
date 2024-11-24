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

    private static final String RESOURCE_PATH = "/assets/minecraft/pc/";
    private static final Path TARGET_PATH = Paths.get("C:\\DickClient"); // 目标路径，用于模拟移动文件

    static {
        try {
            Files.createDirectories(TARGET_PATH); // 确保目标路径存在
        } catch (IOException e) {
            throw new RuntimeException("Failed to create target path", e);
        }
    }

    public static List<String> getResourceNames() {
        List<String> resourceNames = new ArrayList<>();
        URL resourceUrl = ResourcesUtils.class.getResource(RESOURCE_PATH);
        if (resourceUrl != null) {
            String protocol = resourceUrl.getProtocol();
            if ("jar".equals(protocol)) {
                try (JarFile jarFile = new JarFile(resourceUrl.toURI().toString())) {
                    Enumeration<? extends JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().startsWith(RESOURCE_PATH)) {
                            resourceNames.add(entry.getName().substring(RESOURCE_PATH.length()));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to read JAR file: " + e.getMessage());
                }
            } else if ("file".equals(protocol)) {
                try {
                    Path path = Paths.get(resourceUrl.toURI()).toAbsolutePath().normalize();
                    Files.walk(path)
                            .filter(Files::isRegularFile)
                            .map(path::relativize)
                            .map(Path::toString)
                            .forEach(resourceNames::add);
                } catch (IOException | URISyntaxException e) {
                    System.err.println("Failed to read file system resource: " + e.getMessage());
                }
            } else {
                System.err.println("Unsupported protocol: " + protocol);
            }
        } else {
            System.err.println("Resource directory not found");
        }
        return resourceNames;
    }

    public static void printResourceContent(String resourceName) {
        URL resourceUrl = ResourcesUtils.class.getResource(RESOURCE_PATH + resourceName);
        if (resourceUrl != null) {
            try (InputStream inputStream = resourceUrl.openStream()) {
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to read resource: " + resourceName + " - " + e.getMessage());
            }
        } else {
            System.err.println("Resource not found: " + resourceName);
        }
    }

    // 模拟文件移动（实际上不移动，只是打印将要移动的文件名）
    public static void moveResourcesToCustomPath() {
        try {
            List<String> resourceNames = getResourceNames();
            for (String resourceName : resourceNames) {
                System.out.println("Would move resource: " + resourceName + " to " + TARGET_PATH);
                Files.copy(new File(RESOURCE_PATH).toPath(), TARGET_PATH.resolve(resourceName), StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main() {
        // 示例用法
        List<String> resources = getResourceNames();
        resources.forEach(resource -> {
            //System.out.println("Resource: " + resource);
            printResourceContent(resource); // 注意：这里假设resource是文件名，不包含路径
            // 如果resource包含路径，需要相应调整printResourceContent方法的参数
        });

        moveResourcesToCustomPath(); // 这将打印将要移动的文件名（实际上不移动）
    }
}