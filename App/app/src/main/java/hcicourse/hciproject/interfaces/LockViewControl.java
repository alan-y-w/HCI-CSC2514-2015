package hcicourse.hciproject.interfaces;

import java.util.ArrayList;

/**
 * Created by alanwu on 2015-11-08.
 */
public interface LockViewControl {

    /**
     * Set the target password to be validated
     * @param password, the password to be validated
     */
    public void InstallPassword(String password);

    /**
     * Add listener for handling the result of password validation
     * @param listener, listeners for handling result validation
     */
    public void AddPasswordResultListener(PasswordResultListener listener);

    public void ClearPasswordResultListeners();

    /**
     * Resets everything regarding the view, including data and states
     */
    public void ViewInit();

}
