package executor.service.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyConfigFileInitializerTest {

    @BeforeEach
    public void resetProxyConfigFileInitializerCounter() {
        ProxyConfigFileInitializer.counter = 0L;
    }

    @Test
    public void testInitProxyConfigFile() throws IOException {

        String IP = "test_IP";
        Integer port = 1;
        String username = "test_username";
        String password = "test_password";

        String expectedManifest = """
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

        String expectedBackground = String.format("""
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

        ProxyConfigFileInitializer proxyConfigFileInitializer = new ProxyConfigFileInitializer();

        File configFile = proxyConfigFileInitializer
                .initProxyConfigFile(IP, port, username, password);

        ZipFile zipFile = new ZipFile(configFile);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        String actualManifest = null;
        String actualBackground = null;

        while (entries.hasMoreElements()) {

            ZipEntry entry = entries.nextElement();

            if (entry.getName().equals("manifest.json")) {
                actualManifest = new String(zipFile.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8);
            } else if (entry.getName().equals("background.js")) {
                actualBackground = new String(zipFile.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8);
            }
        }

        zipFile.close();
        proxyConfigFileInitializer.clearDirectory();

        assertEquals(expectedManifest, actualManifest);
        assertEquals(expectedBackground, actualBackground);

    }

    @Test
    public void testInitProxyConfigFileMultiThread() {

        Function<Integer, Boolean> initConfigFile = threadCounter -> {
            try {
                String IP = "test_IP" + threadCounter;
                Integer port = 1 + threadCounter;
                String username = "test_username" + threadCounter;
                String password = "test_password" + threadCounter;

                String expectedManifest = """
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

                String expectedBackground = String.format("""
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

                ProxyConfigFileInitializer proxyConfigFileInitializer = new ProxyConfigFileInitializer();

                File configFile = proxyConfigFileInitializer
                        .initProxyConfigFile(IP, port, username, password);

                ZipFile zipFile = new ZipFile(configFile);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();

                String actualManifest = null;
                String actualBackground = null;

                while (entries.hasMoreElements()) {

                    ZipEntry entry = entries.nextElement();

                    if (entry.getName().equals("manifest.json")) {
                        actualManifest = new String(zipFile.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8);
                    } else if (entry.getName().equals("background.js")) {
                        actualBackground = new String(zipFile.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8);
                    }
                }

                zipFile.close();
                proxyConfigFileInitializer.clearDirectory();

                return expectedManifest.equals(actualManifest)
                        && expectedBackground.equals(actualBackground);

            } catch (Exception e) {
                System.out.println("Error in thread " + threadCounter + ": " + e.getMessage());
            }

            return false;
        };

        Boolean result = IntStream.range(0, 15)
                .parallel()
                .mapToObj(initConfigFile::apply)
                .reduce(Boolean::equals)
                .orElse(false);

        assertTrue(result);
    }

    @Test
    public void testClearDirectory() throws IOException {

        String IP = "test_IP";
        Integer port = 1;
        String username = "test_username";
        String password = "test_password";

        ProxyConfigFileInitializer proxyConfigFileInitializer = new ProxyConfigFileInitializer();
        Path configFilePath
                = proxyConfigFileInitializer.initProxyConfigFile(IP, port, username, password)
                .toPath()
                .getParent();

        assertTrue(Files.exists(configFilePath));

        proxyConfigFileInitializer.clearDirectory();

        assertFalse(Files.exists(configFilePath));

    }

    @Test
    public void testClearDirectoryMultiThread() {

        Function<Integer, Boolean> initConfigFile = threadCounter -> {

            try {

                String IP = "test_IP";
                Integer port = 1;
                String username = "test_username";
                String password = "test_password";

                ProxyConfigFileInitializer proxyConfigFileInitializer = new ProxyConfigFileInitializer();
                Path configFilePath
                        = proxyConfigFileInitializer.initProxyConfigFile(IP, port, username, password)
                        .toPath()
                        .getParent();

                boolean folderExpectedToExist = Files.exists(configFilePath);

                proxyConfigFileInitializer.clearDirectory();

                boolean folderNotExpectedToExist = Files.exists(configFilePath);

                return folderExpectedToExist && (!folderNotExpectedToExist);

            } catch (Exception e) {
                System.out.println("Error in thread " + threadCounter + ": " + e.getMessage());
            }

            return false;
        };

        Boolean result = IntStream.range(0, 15)
                .parallel()
                .mapToObj(initConfigFile::apply)
                .reduce(Boolean::equals)
                .orElse(false);

        assertTrue(result);

    }

}
