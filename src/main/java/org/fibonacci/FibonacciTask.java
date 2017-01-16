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
 * Class which runs the fibonacci calculation.
 * See {@link FibonacciTask#run()}
 * 
 * @author Jacek Golek
 */
class FibonacciTask implements Runnable {

    private String jsonRequestString;
    private FibonacciServiceListener serviceListener;

    public FibonacciTask(String jsonRequestString, FibonacciServiceListener serviceListener) {
        super();
        this.jsonRequestString = jsonRequestString;
        this.serviceListener = serviceListener;
    }

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

            long fibonacciIndex = Long.parseLong(number);
            long fibonacciNumber = FibonacciSequence.calculateIndexForNumber(fibonacciIndex);

            JsonObject jsonResponseObject = Json.createObjectBuilder()
              .add(JSON_FIELD_REQUEST_ID,  requestId)
              .add(JSON_FIELD_FIB_ELEMENT, String.valueOf(fibonacciNumber))
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
    
    private void validateThatJsonContainsField(String fieldName, JsonObject jsonRequestObject) {
        if (!jsonRequestObject.containsKey(fieldName)) {
            throw new IllegalArgumentException("field " + fieldName + " is required in json request object");
        }
    }
    
    public static void execute(String jsonRequestString, FibonacciServiceListener serviceListener){
            new FibonacciTask(jsonRequestString, serviceListener).run();
    }
}
