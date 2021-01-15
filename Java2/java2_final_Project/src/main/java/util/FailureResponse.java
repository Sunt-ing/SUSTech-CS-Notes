package util;

/**
 * Failure response. It's the subclass of Response.
 */
public class FailureResponse extends Response {

    /**
     * Constructor of FailureResponse
     * @param code response code
     * @param message response message
     */
    public FailureResponse(int code, String message) {
        super(code, message);
    }

    /**
     * Constructor of FailureResponse
     * @param failureCause package some types of failures.
     */
    public FailureResponse(FailureCause failureCause){
        super(failureCause.code, failureCause.message);
    }

}

