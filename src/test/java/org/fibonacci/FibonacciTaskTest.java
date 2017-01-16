package org.fibonacci;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FibonacciTaskTest {
    
    private FibonacciServiceTestListener serviceListener;
    
    @Before
    public void setup(){
       this.serviceListener = new FibonacciServiceTestListener(); 
    }

    @Test
    public void testRunSuccessfull() throws Exception {
        
        String jsonRequestObject = "{\"requestId\": \"1001\", \"number\": \"13\"}";
        String expectedJsonResponse = "{\"requestId\":\"1001\",\"fib_element\":\"7\"}";
        
        FibonacciTask.execute(jsonRequestObject, serviceListener);

        assertEquals(expectedJsonResponse, serviceListener.jsonSuccessResponse);
        assertNull(serviceListener.jsonErrorResponse);
    }
    
    @Test
    public void testRunError_MissingField() throws Exception {
        
        String jsonRequestObject       = "{\"requestId\":\"1001\"}";
        String expectedJsonErrorObject = "{\"requestId\":\"1001\",\"error\":\"field number is required in json request object\"}";

        FibonacciTask.execute(jsonRequestObject, serviceListener);

        assertEquals(expectedJsonErrorObject, serviceListener.jsonErrorResponse);
        assertNull(serviceListener.jsonSuccessResponse);
    }
    
    @Test
    public void testRunError_WrongFormat() throws Exception {
        
        String jsonRequestObject       = "{requestId}";
        String expectedJsonErrorObject = "{\"requestId\":\"-1\",\"error\":\"Unexpected char 114 at (line no=1, column no=2, offset=1)\"}";

        FibonacciTask.execute(jsonRequestObject, serviceListener);

        assertNull(serviceListener.jsonSuccessResponse);
        assertEquals(expectedJsonErrorObject, serviceListener.jsonErrorResponse);
    }
    
    private class FibonacciServiceTestListener implements FibonacciServiceListener{
        
        String jsonSuccessResponse;
        String jsonErrorResponse;
        
        @Override
        public void success(String jsonSuccessResponse) {
            this.jsonSuccessResponse = jsonSuccessResponse;
        }

        @Override
        public void error(String jsonErrorResponse) {
            this.jsonErrorResponse = jsonErrorResponse;
        }    
    };
    
}
