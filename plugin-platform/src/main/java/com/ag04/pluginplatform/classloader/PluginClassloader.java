package com.ag04.pluginplatform.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class PluginClassloader extends ClassLoader {

    private final String pluginsFolder;
    private List<JarFile> jars;

    public PluginClassloader(String pluginsFolder, ClassLoader parent) {
        super(parent);
        this.pluginsFolder = pluginsFolder;

        init();
    }

    public void init() {
        File[] jarFiles = new File(pluginsFolder).listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null) {
            jars = Collections.emptyList();
            return;
        }

        this.jars = Arrays.stream(jarFiles).map(jarFile -> {
            try {
                return new JarFile(jarFile);
            } catch (IOException e) {
                // we've just listed them, they're here
                return null;
            }
        }).collect(Collectors.toList());
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        String className = name.replace('.', '/').concat(".class");
        Optional<URL> optionalURL = getClassUrl(className);

        return optionalURL.map(classUrl -> {
            byte[] bytes = getBytes(classUrl);
            Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
            resolveClass(clazz);
            return clazz;
        }).orElseThrow(ClassNotFoundException::new);
    }

    @Override
    protected URL findResource(final String name) {
        return getClassUrl(name).orElse(null);
    }

    @Override
    protected Enumeration<URL> findResources(final String name) {
        Optional<URL> classUrlOptional = getClassUrl(name);
        return classUrlOptional.map(url -> Collections.enumeration(Collections.singletonList(url))).orElse(null);
    }

    private byte[] getBytes(final URL classUrl) {
        try {
            return classUrl.openStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private Optional<URL> getClassUrl(String className) {
        for (JarFile jar : jars) {
            ZipEntry entry = jar.getEntry(className);
            if (entry == null) {
                continue;
            }

            return of(createUrl(entry.getName(), jar));
        }

        return empty();
    }

    private URL createUrl(final String className, final JarFile jarFile) {
        try {
            return new URL("jar", null, "file:" + jarFile.getName() + "!/" + className);
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        }
    }
}
