package org.apache.seata.spring.boot.autoconfigure;


import org.apache.seata.spring.annotation.GlobalTransactionScanner;
import org.apache.seata.spring.boot.autoconfigure.properties.SeataProperties;
import org.apache.seata.spring.boot.autoconfigure.properties.SpringCloudAlibabaConfiguration;
import org.apache.seata.spring.boot.autoconfigure.provider.SpringApplicationContextProvider;
import org.apache.seata.tm.api.FailureHandler;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.apache.seata.common.Constants.BEAN_NAME_SPRING_APPLICATION_CONTEXT_PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SeataAutoConfigurationTest.TestConfig.class, SeataAutoConfiguration.class})
@Import({SeataProperties.class,SpringCloudAlibabaConfiguration.class})
public class SeataAutoConfigurationTest extends AutoConfigurationBaseTest{

    @Autowired
    private GlobalTransactionScanner scanner;

    @Autowired
    private FailureHandler failureHandler;

    @Test
    void testSeataAutoConfiguration() {
        assertNotNull(failureHandler);
        assertNotNull(scanner);
        assertEquals(scanner.getTxServiceGroup(), "default_tx_group");
    }

    @Configuration
    static class TestConfig {

        @Bean(BEAN_NAME_SPRING_APPLICATION_CONTEXT_PROVIDER)
        @ConditionalOnMissingBean(name = {BEAN_NAME_SPRING_APPLICATION_CONTEXT_PROVIDER})
        public org.apache.seata.spring.boot.autoconfigure.provider.SpringApplicationContextProvider springApplicationContextProvider() {
            return new SpringApplicationContextProvider();
        }

    }
}