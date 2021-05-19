package com.ag04.pluginplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.DefaultResourceLoader;
import com.ag04.pluginplatform.classloader.PluginClassloader;

@SpringBootApplication
public class PluginPlatformApplication {

	public static void main(String[] args) {
		PluginClassloader classLoader = new PluginClassloader("plugins", Thread.currentThread().getContextClassLoader());
		SpringApplication app = new SpringApplication(PluginPlatformApplication.class);
		app.setResourceLoader(new DefaultResourceLoader(classLoader));
		app.run(args);
	}

}
