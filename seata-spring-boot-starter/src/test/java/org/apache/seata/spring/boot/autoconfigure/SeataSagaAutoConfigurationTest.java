package org.apache.seata.spring.boot.autoconfigure;

import org.apache.seata.saga.engine.StateMachineConfig;
import org.apache.seata.saga.engine.StateMachineEngine;
import org.apache.seata.saga.engine.config.DbStateMachineConfig;
import org.apache.seata.spring.boot.autoconfigure.mock.MockDataSource;
import org.apache.seata.spring.boot.autoconfigure.properties.SagaAsyncThreadPoolProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SeataSagaAutoConfigurationTest.TestConfig.class, SeataSagaAutoConfiguration.class})
public class SeataSagaAutoConfigurationTest extends AutoConfigurationBaseTest {

    @Autowired
    private StateMachineConfig stateMachineConfig;

    @Autowired
    private StateMachineEngine stateMachineEngine;

    @Resource(name = "seataSagaRejectedExecutionHandler")
    private RejectedExecutionHandler rejectedExecutionHandler;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;


    @Test
    public void assertDbStateMachineConfigNotNull() {
        assertNotNull(stateMachineConfig);
        Assertions.assertEquals(((DbStateMachineConfig) stateMachineConfig).getApplicationId(), "test");
    }

    @Test
    public void assertStateMachineEngine() {
        assertNotNull(stateMachineEngine);
    }

    @Test
    public void testSagaAsyncThreadPoolExecutorConfiguration() {
        assertNotNull(rejectedExecutionHandler);
        assertNotNull(threadPoolExecutor);
        Assertions.assertEquals(threadPoolExecutor.getCorePoolSize(), 1);
        Assertions.assertEquals(threadPoolExecutor.getMaximumPoolSize(), 20);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public DataSource mockDataSource() {
            return new MockDataSource();
        }

        @Bean
        public SagaAsyncThreadPoolProperties mockSagaAsyncThreadPoolProperties() {
            SagaAsyncThreadPoolProperties sagaAsyncThreadPoolProperties = new SagaAsyncThreadPoolProperties();
            sagaAsyncThreadPoolProperties.setCorePoolSize(1);
            sagaAsyncThreadPoolProperties.setMaxPoolSize(20);
            sagaAsyncThreadPoolProperties.setKeepAliveTime(60);
            return sagaAsyncThreadPoolProperties;
        }
    }

}