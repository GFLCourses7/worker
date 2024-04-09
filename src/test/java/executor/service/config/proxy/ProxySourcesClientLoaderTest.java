//package executor.service.config.proxy;
//
//import executor.service.model.ProxyConfigHolder;
//import executor.service.model.ProxyCredentials;
//import executor.service.model.ProxyNetworkConfig;
//import executor.service.utils.configreader.JsonConfigReader;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.eq;
//import static org.mockito.Mockito.mockStatic;
//
//class ProxySourcesClientLoaderTest {
//    private ProxySourcesClient proxySourcesClient;
//    private ProxyConfigHolder fakeProxyConfigHolder;
//
//    @BeforeEach
//    void setUp() {
//        ProxyNetworkConfig fakeProxyNetworkConfig = new ProxyNetworkConfig("Fake hostname 1", 1010);
//        ProxyCredentials fakeProxyCredentials = new ProxyCredentials("Fake Username 1", "Strong Password");
//
//        fakeProxyConfigHolder = new ProxyConfigHolder(fakeProxyNetworkConfig, fakeProxyCredentials);
//    }
//
//    @Test
//    public void testGetProxy() {
//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//            ProxyConfigHolder proxy = proxySourcesClient.getProxy();
//            assertEquals(fakeProxyConfigHolder, proxy);
//        }
//    }
//
//    @Test
//    public void testGetProxyWithoutProxyHostname() {
//        fakeProxyConfigHolder.getProxyNetworkConfig().setHostname(null);
//
//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//            ProxyConfigHolder proxy = proxySourcesClient.getProxy();
//            assertEquals(fakeProxyConfigHolder, proxy);
//        }
//    }
//
//    @Test
//    public void testGetProxyWithoutProxyPort() {
//        fakeProxyConfigHolder.getProxyNetworkConfig().setPort(null);
//
//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//            ProxyConfigHolder proxy = proxySourcesClient.getProxy();
//            assertEquals(fakeProxyConfigHolder, proxy);
//        }
//    }
//
//    @Test
//    public void testGetProxyWithoutProxyUsername() {
//        fakeProxyConfigHolder.getProxyCredentials().setUsername(null);
//
//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//            ProxyConfigHolder proxy = proxySourcesClient.getProxy();
//            assertEquals(fakeProxyConfigHolder, proxy);
//        }
//    }
//
//    @Test
//    public void testGetProxyWithoutProxyPassword() {
//        fakeProxyConfigHolder.getProxyCredentials().setPassword(null);
//
//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//            ProxyConfigHolder proxy = proxySourcesClient.getProxy();
//            assertEquals(fakeProxyConfigHolder, proxy);
//        }
//    }
//
//    @Test
//    public void testAddProxy() {
//        fakeProxyConfigHolder.getProxyCredentials().setPassword(null);
//
//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//            ProxyConfigHolder proxy1 = proxySourcesClient.getProxy();
//
//            proxySourcesClient.addProxy(fakeProxyConfigHolder);
//            ProxyConfigHolder proxy2 = proxySourcesClient.getProxy();
//
//            assertEquals(fakeProxyConfigHolder, proxy1);
//            assertEquals(fakeProxyConfigHolder, proxy2);
//            assertEquals(proxy1, proxy2);
//        }
//    }
//    @Test
//    public void testAddProxyWhileWaiting() {
//        fakeProxyConfigHolder.getProxyCredentials().setPassword(null);
//
//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//
//            Runnable runnable = () -> {
//                try {
//                    Thread.sleep(5000);
//                    proxySourcesClient.addProxy(fakeProxyConfigHolder);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            };
//
//            runnable.run();
//            ProxyConfigHolder proxy = proxySourcesClient.getProxy();
//
//            assertEquals(fakeProxyConfigHolder, proxy);
//        }
//    }
//
//
//    public void testGetProxyThrowsNoSuchElementException() {
//
//        // Proxy behaviour has been changed, needs reconfiguration
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
//                    .thenReturn(Collections.emptyList());
//
//            proxySourcesClient = new ProxySourcesClientLoader();
//
//            assertThrows(NoSuchElementException.class, () -> proxySourcesClient.getProxy());
//        }
//    }
//
//}