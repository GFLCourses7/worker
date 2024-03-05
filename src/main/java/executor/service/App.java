package executor.service;

import executor.service.executionService.ParallelFlowExecutorService;
import executor.service.factory.AbstractFactory;
import executor.service.factory.DIFactory;

public class App
{
    public static void main( String[] args ) {
        AbstractFactory factory = new DIFactory();
        ParallelFlowExecutorService parallelFlowExecutorService = factory.create(ParallelFlowExecutorService.class);
        parallelFlowExecutorService.startThreads();
    }
}
