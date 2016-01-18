package hcicourse.hciproject.interfaces;

/**
 * Created by alanwu on 2015-11-08.
 */
public interface PasswordResultListener {

    /**
     * Event handler to pass the test result
      */
    void NotifyPasswordResult(boolean result);
    void NotifyInputBegin();
}
