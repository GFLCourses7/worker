package executor.service.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProxyConfigFileInitializer {

    private static final String fileSeparator = FileSystems.getDefault().getSeparator();

    protected static final String PROXY_FOLDER = "proxy" + fileSeparator +"temp";

    protected static Long counter = 0L;

    protected final Long id;

    public ProxyConfigFileInitializer() {
        this.id = progressCounter();
    }

    private static synchronized Long progressCounter() {
        if (counter == Long.MAX_VALUE)
            counter = 0L;
        return counter++;
    }

    public File initProxyConfigFile(String IP, Integer port, String username, String password) throws IOException {

        // Create necessary directories
        Files.createDirectories(Paths.get(getPath()));

        String manifest = getManifest();
        File manifestFile = createFile(manifest, "manifest.json");

        String background = getBackground(IP, Integer.toString(port), username, password);
        File backgroundFile = createFile(background, "background.js");

        return createZipArchive("proxy.zip", manifestFile, backgroundFile);
    }

    public void clearDirectory() {
        File directory = new File(getPath());
        if (!deleteDirectory(directory)) {
            throw new IllegalStateException(String.format("Couldn't clear directory %s", directory.getAbsolutePath()));
        }
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private String getManifest() {
        return """
                {
                    "version": "1.0.0",
                    "manifest_version": 2,
                    "name": "Chrome Proxy",
                    "permissions": [
                        "proxy",
                        "tabs",
                        "unlimitedStorage",
                        "storage",
                        "<all_urls>",
                        "webRequest",
                        "webRequestBlocking"
                    ],
                    "background": {
                        "scripts": ["background.js"]
                    },
                    "minimum_chrome_version":"22.0.0"
                }
                """;
    }

    private String getBackground(String IP, String port, String username, String password) {
        return String.format("""
                var config = {
                    mode: "fixed_servers",
                    rules: {
                        singleProxy: {
                            scheme: "http",
                            host: "%s",
                            port: parseInt(%s)
                        },
                        bypassList: ["localhost"]
                    }
                };
                chrome.proxy.settings.set({value: config, scope: "regular"}, function() {});
                function callbackFn(details) {
                    return {
                        authCredentials: {
                            username: "%s",
                            password: "%s"
                        }
                    };
                }
                chrome.webRequest.onAuthRequired.addListener(
                    callbackFn,
                    {urls: ["<all_urls>"]},
                    ['blocking']
                );
                """, IP, port, username, password);
    }

    private String getPath() {
        return System.getProperty("user.dir")
                + fileSeparator + PROXY_FOLDER
                + fileSeparator + id
                + fileSeparator;
    }

    private String getPath(String filename) {
        return getPath() + filename;
    }

    private File createFile(String contents, String filename) throws IOException {

        String filepath = getPath(filename);

        FileWriter fileWriter = new FileWriter(filepath);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(contents);
        printWriter.close();

        return new File(filepath);
    }

    private File createZipArchive(String filename, File... files) throws IOException {

        String path = getPath(filename);

        final FileOutputStream fos = new FileOutputStream(path);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (File srcFile : files) {
            FileInputStream fis = new FileInputStream(srcFile);
            ZipEntry zipEntry = new ZipEntry(srcFile.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();

        return new File(path);
    }
}
