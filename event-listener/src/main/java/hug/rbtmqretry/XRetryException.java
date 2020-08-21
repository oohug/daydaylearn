package hug.rbtmqretry;


public class XRetryException extends RuntimeException {

    private String env;
    private String message;

    public XRetryException(String env, String message) {
        super(message);
        this.env = env;
        this.message = message;
    }

    public XRetryException(String message) {
        super(message);
        this.message = message;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public XRetryException(String message, Throwable cause){
        super(message,cause);
    }

    public XRetryException(Throwable cause){
        super(cause);
    }
}
