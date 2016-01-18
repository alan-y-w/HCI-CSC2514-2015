package hcicourse.hciproject.widgets;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by alanwu on 2015-11-10.
 */
public class PatternDisplay extends CustomPatternLock{
    public PatternDisplay(Context context) {
        super(context);
        init();
    }

    public PatternDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        disableInput();
    }

    public void ShowPassword(String password){
        if (password.length() != 0){
            setPattern(DisplayMode.Animate, PatternLock.stringToPattern(password));
        }
    }
}
