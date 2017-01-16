package org.fibonacci;

import static org.fibonacci.FibonacciJsonFields.JSON_FIELD_ERROR;
import static org.fibonacci.FibonacciJsonFields.JSON_FIELD_FIB_ELEMENT;
import static org.fibonacci.FibonacciJsonFields.JSON_FIELD_NUMBER;
import static org.fibonacci.FibonacciJsonFields.JSON_FIELD_REQUEST_ID;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Class which runs the fibonacci index calculation.
 * See {@link FibonacciTask#run()}
 * <br>
 * For some examples have a look at {@link FibonacciTaskTest}.
 * 
 * @author Jacek Golek
 */
class FibonacciTask implements Runnable {

    private String jsonRequestString;
    private FibonacciServiceListener serviceListener;

    /**
     * The task use the jsonRequestString as input parameter and the {@link FibonacciServiceListener} to return the result 
     * or an error message. 
     * 
     * @param jsonRequestString
     *            Format: <code> { "requestId": "< Request Id >", "number": "< Fibonacci number >" } </code>
     * 
     * @param serviceListener
     *            {@link FibonacciServiceListener}
     * 
     */
    FibonacciTask(String jsonRequestString, FibonacciServiceListener serviceListener) {
        super();
        this.jsonRequestString = jsonRequestString;
        this.serviceListener = serviceListener;
    }

    /* 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        
        String requestId = "-1";
        String number = null;
        
        try{
            JsonReader jsonReader = Json.createReader(new StringReader(jsonRequestString));
            JsonObject jsonRequestObject = jsonReader.readObject();

            validateThatJsonContainsField(JSON_FIELD_REQUEST_ID, jsonRequestObject);
            requestId = jsonRequestObject.getString(JSON_FIELD_REQUEST_ID);

            validateThatJsonContainsField(JSON_FIELD_NUMBER, jsonRequestObject);
            number = jsonRequestObject.getString(JSON_FIELD_NUMBER);

            long fibonacciNumber = Long.parseLong(number);
            long fibonacciIndex = FibonacciSequence.calculateIndexForNumber(fibonacciNumber);

            JsonObject jsonResponseObject = Json.createObjectBuilder()
              .add(JSON_FIELD_REQUEST_ID,  requestId)
              .add(JSON_FIELD_FIB_ELEMENT, String.valueOf(fibonacciIndex))
              .build();

            serviceListener.success(jsonResponseObject.toString());
            
        }catch(Exception exc){
            JsonObject jsonErrorObject = Json.createObjectBuilder()
              .add(JSON_FIELD_REQUEST_ID, requestId)
              .add(JSON_FIELD_ERROR, exc.getMessage())
              .build();
            
            serviceListener.error(jsonErrorObject.toString());
        }
    }
    
    /**
     * Checks that jsonRequestObject contains the fieldName. 
     * 
     * @param fieldName
     * @param jsonObject
     * 
     * @throws {@link IllegalArgumentException} if jsonObject doesn't contain the fieldName.
     */
    private void validateThatJsonContainsField(String fieldName, JsonObject jsonObject) {
        if (!jsonObject.containsKey(fieldName)) {
            throw new IllegalArgumentException("field " + fieldName + " is required in json request object");
        }
    }
    
    /**
     * Helper method. 
     * For parameter definition have a look at {@link FibonacciTask#FibonacciTask(String, FibonacciServiceListener)}
     * 
     * @param jsonRequestString
     * @param serviceListener
     */
    public static void execute(String jsonRequestString, FibonacciServiceListener serviceListener){
            new FibonacciTask(jsonRequestString, serviceListener).run();
    }
}
