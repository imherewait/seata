package org.apache.seata.spring.boot.autoconfigure;

import org.apache.seata.config.ConfigurationFactory;
import org.apache.seata.mockserver.MockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AutoConfigurationBaseTest {
    public static final int MOCK_SERVER_PORT = 8099;

    @BeforeAll
    public static void before() {
        ConfigurationFactory.reload();
        MockServer.start(MOCK_SERVER_PORT);
    }

    @AfterAll
    public static void after() {
        MockServer.close();
    }
}
