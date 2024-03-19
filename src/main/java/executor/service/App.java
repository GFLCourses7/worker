package executor.service;

import executor.service.executor.parallelflowexecution.ParallelFlowExecutorService;
import executor.service.factory.difactory.AbstractFactory;
import executor.service.factory.difactory.DIFactory;


public class App {

    public static void main( String[] args ) {
        new App().start();
    }

    public void start() {
        AbstractFactory factory = new DIFactory();
        ParallelFlowExecutorService parallelFlowExecutorService = factory.create(ParallelFlowExecutorService.class);
        parallelFlowExecutorService.startThreads();
    }
}