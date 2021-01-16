package util;

/**
 * constraint the types of failure
 */
public enum FailureCause{
    FILE_NOT_FOUND(1, "file not found"),
    HASH_NOT_MATCH(2, "hash not match"),
    ALREADY_EXIST(3, "already exist");

    int code;
    String message;

    /**
     * constructor of the class.
     * @param code status code the response.
     * @param message detailed error message of the response.
     */
    FailureCause(int code, String message) {
        this.code = code;
        this.message = message;
    }
}