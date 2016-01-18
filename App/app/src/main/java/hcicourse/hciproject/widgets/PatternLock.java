package hcicourse.hciproject.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import haibison.android.lockpattern.collect.Lists;
import haibison.android.lockpattern.widget.LockPatternUtils;
import haibison.android.lockpattern.widget.LockPatternView;
import hcicourse.hciproject.interfaces.LockViewControl;
import hcicourse.hciproject.interfaces.PasswordResultListener;

/**
 * Created by alanwu on 2015-11-10.
 */
public class PatternLock extends CustomPatternLock implements LockViewControl, CustomPatternLock.OnPatternListener{

    private ArrayList<PasswordResultListener> passwordResultListeners;
    private String refPasscode = "";

    public PatternLock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PatternLock(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setOnPatternListener(this);
        passwordResultListeners = new ArrayList<PasswordResultListener>();
    }


    @Override
    public void InstallPassword(String password) {
        refPasscode = password;
//        List<LockPatternView.Cell> pattern = stringToPattern(password);
//        this.setPattern(DisplayMode.Animate, pattern);
    }

    /**
     * Below shows the mapping
     * 0 1 2
     * 3 4 5
     * 6 7 8
     * @param string
     * @return pattern from the string
     */
    public static List<LockPatternView.Cell> stringToPattern(@NonNull String string) {
        List<LockPatternView.Cell> result = Lists.newArrayList();

        try {
            final byte[] bytes = string.getBytes("UTF-8");
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i] - "0".getBytes("UTF-8")[0];
                result.add(LockPatternView.Cell.of(b / 3, b % 3));
            }
        } catch (UnsupportedEncodingException e) {
            // never catch this
        }

        return result;
    }// stringToPattern()

    /**
     * Below shows the mapping
     * 0 1 2
     * 3 4 5
     * 6 7 8
     * @param pattern
     * @return string from the pattern
     */
    public static String patternToString(List<LockPatternView.Cell> pattern){
        String result = "";

        for (LockPatternView.Cell cell : pattern){
            int val = cell.column + cell.row * 3;
            result += Integer.toString(val);
        }

        return result;
    }


    @Override
    public void AddPasswordResultListener(PasswordResultListener listener) {
        passwordResultListeners.add(listener);
    }

    @Override
    public void ClearPasswordResultListeners() {
        passwordResultListeners.clear();
    }

    @Override
    public void ViewInit() {
        refPasscode = "";
        this.clearPattern();
    }

    @Override
    public void onPatternStart() {
        NotifyPasswordInputBegin();
    }

    @Override
    public void onPatternCleared() {
        // Do nothing
        return;
    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
        // Do nothing
        return;
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {

        String passcode = patternToString(pattern);

        boolean result = passcode.equals(refPasscode);
        if (result){
            this.setDisplayMode(DisplayMode.Correct);

        } else {
            this.setDisplayMode(DisplayMode.Wrong);
        }

        // Let the pattern sits a bit before it gets cleared
        disableInput();

        final Handler handler_dot = new Handler();
        handler_dot.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableInput();
                clearPattern();
            }
        }, 500);

        NotifyPasswordResults(result);
        return;
    }

    private void NotifyPasswordInputBegin() {
        for (PasswordResultListener l : passwordResultListeners){
            l.NotifyInputBegin();
        }
    }

    private void NotifyPasswordResults(boolean result) {

        for (PasswordResultListener l : passwordResultListeners){
            l.NotifyPasswordResult(result);
        }
    }
}
