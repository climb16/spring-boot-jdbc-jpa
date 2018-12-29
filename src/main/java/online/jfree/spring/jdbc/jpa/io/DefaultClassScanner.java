package online.jfree.spring.jdbc.jpa.io;

import online.jfree.spring.jdbc.jpa.exceptions.JdbcJpaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Guo Lixiao
 * @description
 * @date 2018/12/28 14:56
 * @sign 1.0
 */
public class DefaultClassScanner implements ClassScanner {

    private Logger logger = LoggerFactory.getLogger(DefaultClassScanner.class);
    private static final String DIR_SEPARATOR = System.getProperty("file.separator");
    private static final String PACKAGE_SEPARATOR = ".";

    private Set<Class<?>> classSet = new HashSet<>();

    @Override
    public Set<Class<?>> scanClass(String[] basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            logger.warn("scanner base packages is empty");
            return this.classSet;
        }
        for (String basePackage : basePackages) {
            if (isNotEmptyStr(basePackage)) {
                scanClass(basePackage);
            }
        }
        return this.classSet;
    }

    private void scanClass(String basePackages) {
        try {
            // 从包名获取 URL 类型的资源
            Enumeration<URL> urls = list(basePackages.replace(PACKAGE_SEPARATOR, DIR_SEPARATOR));
            // 遍历 URL 资源
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    // 获取协议名（分为 file 与 jar）
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        // 若在 class 目录中，则执行添加类操作
                        String packagePath = url.getPath().replaceAll("%20", " ").replace("%5c", DIR_SEPARATOR);
                        addClass(packagePath, basePackages);
                    } else if (protocol.equals("jar")) {
                        // 若在 jar 包中，则解析 jar 包中的 entry
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            // 判断该 entry 是否为 class
                            if (jarEntryName.endsWith(".class")) {
                                // 获取类名
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(PACKAGE_SEPARATOR)).replaceAll(DIR_SEPARATOR, PACKAGE_SEPARATOR);
                                // 执行添加类操作
                                doAddClass(className);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addClass(String packagePath, String packageName) {
        try {
            // 获取包名路径下的 class 文件或目录
            File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
            // 遍历文件或目录
            for (File file : files) {
                String fileName = file.getName();
                // 判断是否为文件或目录
                if (file.isFile()) {
                    // 获取类名
                    String className = fileName.substring(0, fileName.lastIndexOf(PACKAGE_SEPARATOR));
                    if (!isNotEmptyStr(packageName)) {
                        className = packageName + PACKAGE_SEPARATOR + className;
                    }
                    // 执行添加类操作
                    doAddClass(className);
                } else {
                    // 获取子包
                    String subPackagePath = fileName;
                    if (isNotEmptyStr(packagePath)) {
                        subPackagePath = packagePath + DIR_SEPARATOR + subPackagePath;
                    }
                    // 子包名
                    String subPackageName = fileName;
                    if (isNotEmptyStr(packageName)) {
                        subPackageName = packageName + PACKAGE_SEPARATOR + subPackageName;
                    }
                    // 递归调用
                    addClass(subPackagePath, subPackageName);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doAddClass(String className) {
        try {
            //反射类，不进行实例化
            Class<?> cls = Class.forName(className, false, getClassLoader());
            this.classSet.add(cls);
        } catch (ClassNotFoundException e) {
            throw new JdbcJpaException(e);
        }
    }

    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private Enumeration<URL> list(String path) throws IOException {
        return getClassLoader().getResources(path);
    }


    private boolean isNotEmptyStr(String str) {
        if (str == null || str.trim().length() < 1) return false;
        return true;
    }
}
