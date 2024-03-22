package executor.service;

import executor.service.executor.parallelflowexecution.ParallelFlowExecutorService;
import executor.service.factory.difactory.AbstractFactory;
import executor.service.factory.difactory.DIFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }

    public void start() {
        AbstractFactory factory = new DIFactory();
        ParallelFlowExecutorService parallelFlowExecutorService = factory.create(ParallelFlowExecutorService.class);
        parallelFlowExecutorService.startThreads();
    }
}