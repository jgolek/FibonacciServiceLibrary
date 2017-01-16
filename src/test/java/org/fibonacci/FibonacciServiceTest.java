package org.fibonacci;

import static org.junit.Assert.*;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


public class FibonacciServiceTest 
{
    
    @Test
    public void testExecuteFibonacciNonBlocking() throws Exception {
        
        String jsonRequestObject = "{\"requestId\": \"1001\", \"number\": \"13\"}";
        
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        FibonacciServiceNonBlockingTestListener serviceListener = new FibonacciServiceNonBlockingTestListener();
 
        //execute fibonacci service
        FibonacciService service = new FibonacciService(threadPoolExecutor);
        service.executeFibonacci(jsonRequestObject, serviceListener);
        
        // check that fibonacci method isn't blocking 
        assertFalse("Expected: Service Listener success methode wasn't called yet", serviceListener.wasSuccessMethodeCalled);
        
        // wait some millisec while task is running.
        threadPoolExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        
        // check if no exception was thrown
        assertNull(serviceListener.jsonErrroResponse);
        
        // check that success method was called
        assertTrue("Expected: Service Listener success methode was called", serviceListener.wasSuccessMethodeCalled);
        
        String jsonExpectedResponse = "{\"requestId\":\"1001\",\"fib_element\":\"7\"}";
        assertEquals(jsonExpectedResponse, serviceListener.jsonSuccessResponse);
    }
    
    @Test
    public void testExecuteFibonacciServiceError() throws Exception {
        
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,  60L, TimeUnit.SECONDS,  new SynchronousQueue<Runnable>()){
            @Override
            public void execute(Runnable command) {
                throw new RuntimeException("Runtime Error");
            }
        };
        FibonacciServiceNonBlockingTestListener serviceListener = new FibonacciServiceNonBlockingTestListener();
 
        //execute fibonacci service
        FibonacciService service = new FibonacciService(threadPoolExecutor);
        service.executeFibonacci("", serviceListener);
        
        assertEquals("{\"requestId\":\"-1\",\"error\":\"Service error: Runtime Error\"}", serviceListener.jsonErrroResponse);
    }
    
    private class FibonacciServiceNonBlockingTestListener implements FibonacciServiceListener{
        boolean wasSuccessMethodeCalled = false;
        String jsonSuccessResponse;
        String jsonErrroResponse;
        
        @Override
        public void success(String jsonSuccessResponse) {
            // wait some millisec to test non-blocking call
            try {  Thread.sleep(10); } catch (Exception e) { throw new RuntimeException(e); }
            this.wasSuccessMethodeCalled = true;
            this.jsonSuccessResponse = jsonSuccessResponse;
        }

        @Override
        public void error(String jsonErrorResponse) {
            this.jsonErrroResponse = jsonErrorResponse;
        }    
    };
}
