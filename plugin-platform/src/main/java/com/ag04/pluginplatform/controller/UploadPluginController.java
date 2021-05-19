package com.ag04.pluginplatform.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ag04.pluginplatform.classloader.PluginClassloader;

@Controller
@RequestMapping("/plugins")
public class UploadPluginController {

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        File targetFile = new File("plugins" + File.separator + file.getOriginalFilename());
        OutputStream fileOutputStream = new FileOutputStream(targetFile);

        FileCopyUtils.copy(file.getInputStream(), fileOutputStream);

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl.getParent() instanceof PluginClassloader) {
            ((PluginClassloader)cl.getParent()).init();
        }
        return "ok";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<String> listPlugins() {
        return Arrays.stream(Objects.requireNonNull(new File("plugins").listFiles())).map(File::getName).collect(Collectors.toList());
    }

}
