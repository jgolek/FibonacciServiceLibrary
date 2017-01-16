package org.fibonacci;

/**
 * The listener interface for receiving result of fibonacci element calculation.
 * When the calculation is over the <code> success </code> method is invoked.
 * When an error occurs then the <code> error </code> method is invoked.
 * 
 * @author Jacek Golek
 */
public interface FibonacciServiceListener {

    /**
     * Invoked when calculation is successfull.
     * 
     * @param jsonSuccessResponse
     *            Format: <code>{ "requestId": "< Request Id >", "fib_element": "< Fibonacci index >"}</code>
     */
    public void success(String jsonSuccessResponse);

    /**
     * Invoked when an error occurs.
     * 
     * @param jsonErrorResponse
     *            Format:  <code>{ "requestId": "< Request Id >",  "error": "< error message >" }</code>
     */
    public void error(String jsonErrorResponse);

}
