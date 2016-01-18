package hcicourse.hciproject.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.ArrayList;

/**
 * Created by alanwu on 2015-11-08.
 */
public class PasswordViewManger {

    private int passwordSize = DEFAULT_PASSWORD_SIZE;

    private View lockView;

    // the reference password to compare against
    private String refPass = "";
    private ArrayList<Dot> password;

    // Current index dot being modified
    // 0 0 - -, then current index is 2
    private int currentDotIndex;

    private static final float DOT_STROKE_SIZE = 4f;
    private static final float DOT_DISTANCE = 60f;
    private static final int DEFAULT_PASSWORD_SIZE = 4;

    private ShakeState shakeState;

    private Interpolator mFastOutSlowInInterpolator;

    public enum PasswordState {

        Empty,

        Filled
    }

    private static class ShakeState{
        public float PosDeltaX = 0;
    }

    public class Dot{

        private PasswordState state;

        private int value = -1;

        public static final int DOT_SIZE = 20;

        public void drawDot(Canvas canvas, int posX, int posY, Paint paint) {
            canvas.translate(shakeState.PosDeltaX, 0);

            canvas.drawCircle(posX, posY, DOT_SIZE, paint);

            canvas.translate(-shakeState.PosDeltaX, 0);
        }

        public void Clear() {
            state = PasswordState.Empty;
            value = -1;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public PasswordState getState() {
            return state;
        }

        public void setState(PasswordState state) {
            this.state = state;
        }

        public Dot(PasswordState s){
            this.state = s;
        }
    }

    /**
     * Convert the list of dots to a string
     * For exampple 0-1-2-3 will become "0123"
     * @return
     * String contrains the value of hte password
     */
    private String PasswordDotsToString() {
        String result = "";

        for (Dot dot : password)
        {
            result += Integer.toString( dot.getValue());
        }
        return result;
    }

    private void startShakeAnimation(float start, float end, long duration,
                                        Interpolator interpolator,
                                        final Runnable endRunnable) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                shakeState.PosDeltaX = (float) animation.getAnimatedValue();
                lockView.invalidate();
            }
        });

        if (endRunnable != null) {
            valueAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (endRunnable != null)
                        endRunnable.run();
                }

            });
        }
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public void ShakeDot() {

        startShakeAnimation(0, 50, 50, mFastOutSlowInInterpolator, new Runnable() {
            @Override
            public void run() {
                startShakeAnimation(50, -50, 50, mFastOutSlowInInterpolator, new Runnable() {
                    @Override
                    public void run() {
                        startShakeAnimation(-50, 0, 50, mFastOutSlowInInterpolator, null);
                    }
                });
            }
        });
    }

    public void drawPasswordDots(Canvas canvas, int posY, int contentWidth, int col, int alpha){
        // create a paint
        Paint paint = new Paint();

        paint.setStrokeWidth(DOT_STROKE_SIZE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(col);
        paint.setAlpha(alpha);

        int PosX = (int)((contentWidth - DOT_DISTANCE * password.size())/2
                    + Dot.DOT_SIZE + (DOT_STROKE_SIZE));

        // Draw the dots
        for (Dot dot : password)
        {
            Paint.Style s = (dot.getState()== PasswordState.Filled)? Paint.Style.FILL : Paint.Style.STROKE;
            paint.setStyle(s);
            dot.drawDot(canvas, PosX, posY, paint);
            PosX += DOT_DISTANCE;
        }
    }

    public void AddDigit(int passwordDigit) {
        if (currentDotIndex < passwordSize){
            // add to the password
            Dot curDot = password.get(currentDotIndex);
            curDot.setValue(passwordDigit);
            // update the state of password dots
            curDot.setState(PasswordState.Filled);

            // start animation for adding the dot

            // move index
            currentDotIndex ++;
        }
    }

    /**
     * Validates the password and fires the event
     *
     * @return
     * return the validation result
     */
    public boolean ValidatePassword(){
        String passwordInput = PasswordDotsToString();
        boolean result = (passwordInput.equals(refPass));

        // Reset password states
        // ClearPasswordStates();

        return  result;
    }


    public PasswordViewManger(View view) {
        lockView = view;
        shakeState = new ShakeState();
        shakeState.PosDeltaX = 0;

        mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(
                lockView.getContext(), android.R.interpolator.fast_out_slow_in);

        PasswordInit();
        BuildPasswordDisplayAndSetLength();
    }

    /**
     * Initialization of default values
     */
    public void PasswordInit() {
        shakeState.PosDeltaX = 0;
        passwordSize = DEFAULT_PASSWORD_SIZE;
        password = new ArrayList<Dot>();
    }

    /**
     * Clear the current password values reset password states
     */
    public void ClearPasswordStates() {
        for (Dot dot : password){
            dot.Clear();
        }
        currentDotIndex = 0;
        shakeState.PosDeltaX = 0;
    }

    public void BuildPasswordDisplayAndSetLength()
    {
        passwordSize = refPass.length();
        int size = passwordSize;
        while (size > 0){
            password.add(new Dot(PasswordState.Empty));
            size--;
        }
    }

    /**
     * True if all password digits have been filled out and ready to be validated
     */
    public boolean isPasswordFull()
    {
        return (passwordSize == currentDotIndex);
    }

    /**
     * True if all password digits are empty
     */
    public boolean isPasswordEmpty()
    {
        return ( currentDotIndex == 0);
    }

   /*Getters and Setters*/

    public String getRefPass() {
        return refPass;
    }

    public void setRefPass(String refPass) {
        this.refPass = refPass;
    }


}
