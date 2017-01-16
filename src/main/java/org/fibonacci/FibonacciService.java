package org.fibonacci;

import static org.fibonacci.FibonacciJsonFields.JSON_FIELD_ERROR;
import static org.fibonacci.FibonacciJsonFields.JSON_FIELD_REQUEST_ID;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Class which returns for a given fibonacci index the
 * corresponding fibonacci number. 
 * Use for that {@link FibonacciService#executeFibonacci(String, FibonacciServiceListener)}.
 * 
 * @author Jacek Golek
 */
public class FibonacciService {

    private ThreadPoolExecutor threadExecutor;

    /**
     * Default constructor.
     */
    public FibonacciService() {
        this((ThreadPoolExecutor) Executors.newCachedThreadPool());
    }

    /**
     * {@link FibonacciTask}s are executed with the given
     * {@link ThreadPoolExecutor}.
     * 
     * @param threadExecutor
     *            {@link ThreadPoolExecutor}
     */
    public FibonacciService(ThreadPoolExecutor threadExecutor) {
        super();
        this.threadExecutor = threadExecutor;
    }

    /**
     * This method calculates for the given 'number' ( see jsonRequestString ) a fibonacci index. 
     * This method is running asynchrone and non-blocking. 
     * Use the {@link FibonacciServiceListener} the get the result of the calculation. 
     * <br>
     * For an example have a look at {@link FibonacciServiceTest#testExecuteFibonacciNonBlocking()}.
     * 
     * @param jsonRequestString
     *            Format: <code> { "requestId": "< Request Id >", "number": "< Fibonacci number >" } </code>
     * 
     * @param serviceListener
     *            {@link FibonacciServiceListener}
     */
    public void executeFibonacci(String jsonRequestString, FibonacciServiceListener serviceListener) {
        try {

            FibonacciTask fibonacciTask = new FibonacciTask(jsonRequestString, serviceListener);
            this.threadExecutor.execute(fibonacciTask);

        } catch (Throwable throwable) {
            JsonObject jsonErrorObject = Json.createObjectBuilder()
              .add(JSON_FIELD_REQUEST_ID, "-1")
              .add(JSON_FIELD_ERROR, "Service error: " + throwable.getMessage())
              .build();

            serviceListener.error(jsonErrorObject.toString());
        }
    }
}
