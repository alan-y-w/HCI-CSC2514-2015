package hcicourse.hciproject.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.ArrayList;

import haibison.android.lockpattern.utils.FloatAnimator;
import hcicourse.hciproject.R;
import hcicourse.hciproject.interfaces.LockViewControl;
import hcicourse.hciproject.interfaces.PasswordResultListener;
import hcicourse.hciproject.util.PasswordViewManger;

/**
 * TODO: document your custom view class.
 */
public class PINLock extends View implements LockViewControl {

    /* Fileds*/
    private int layoutColor = Color.RED;
    private int layoutAlpha = 255;
    private float textDimension = 0;

    private String promptText = "";

    private TextPaint labelTextPaint, promptTextPaint;

    //paint for drawing custom view
    private Paint layoutPaint, keyPressPaint;

    private float labelTextWidth, labelTextHeight, promptTextWidth, promptTextHeight = 0;

    private PasswordViewManger passwordMan;

    private ArrayList<PasswordResultListener> passwordResultListeners;

    private float den = getResources().getDisplayMetrics().density;

    private ArrayList< Key> keyboard = new ArrayList<Key>();

    private Interpolator mFastOutSlowInInterpolator;

    private boolean InputEnabled = true;

    /* Static members*/
    private static final int KEYPAD_RADIUS = 450;
    private static final int LAYOUT_STROKE_WIDTH = 4;
    private static final String CORRECT_TEXT = "Correct Passcode";
    private static final String WRONG_TEXT = "Wrong Passcode";
    private static final int NUMBER_OF_KEYS = 10;

    private final KeyState[] keyStates;

    public static class KeyState {
        // the absolute rotation angle of the canvas when drawing
        public int Alpha = 0;
    }

    public class Key{

        private int value = -1;

        private float x=0;
        private float y = 0;

        public static final float KEY_SIZE = KEYPAD_RADIUS/4.5f;

        public void drawKey(Canvas canvas, Paint paint) {
            canvas.drawCircle(x, y, KEY_SIZE, paint);
            keyPressPaint.setAlpha(keyStates[value].Alpha);
            canvas.drawCircle(x, y, KEY_SIZE, keyPressPaint);
            String s = Integer.toString(value);
            measureLabelText(s);

            float drawPositionX = x - (labelTextWidth) / 2;
            float drawPositionY = y + (labelTextHeight * den) / 2;


            canvas.drawText(s, drawPositionX, drawPositionY, labelTextPaint);
        }

        public void Clear() {
            value = -1;
            x = 0;
            y = 0;
        }

        public Key(int val, float posX, float posY){
            value = val;
            x = posX;
            y = posY;
        }

    }

    /* Constructors*/
    public PINLock(Context context) {
        super(context);
        init(null, 0);

        keyStates = new KeyState[NUMBER_OF_KEYS];
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            keyStates[i] = new KeyState();
            keyStates[i].Alpha = 0;
        }
    }

    public PINLock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

        keyStates = new KeyState[NUMBER_OF_KEYS];
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            keyStates[i] = new KeyState();
            keyStates[i].Alpha = 0;
        }
    }

    public PINLock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);

        keyStates = new KeyState[NUMBER_OF_KEYS];
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            keyStates[i] = new KeyState();
            keyStates[i].Alpha = 0;
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PINLock, defStyle, 0);

        layoutColor = a.getColor(
                R.styleable.PINLock_PINlock_layoutColor,
                layoutColor);

        layoutAlpha = a.getInteger(
                R.styleable.PINLock_PINlock_layoutAlpha,
                layoutAlpha);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        textDimension = a.getDimension(
                R.styleable.PINLock_PINlock_textDimension,
                textDimension);

        a.recycle();

        // Set up default TextPaint objects
        TextPaintInit();

        // Set up paint for drawing
        PaintInit();

        passwordResultListeners = new ArrayList<PasswordResultListener>();

        mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(
                this.getContext(), android.R.interpolator.fast_out_slow_in);

        ViewInit();
    }

    private void PaintInit() {
        layoutPaint = new Paint();
        layoutPaint.setStyle(Paint.Style.STROKE);
        layoutPaint.setStrokeWidth(LAYOUT_STROKE_WIDTH);
        layoutPaint.setAntiAlias(true);
        //set the paint color using the circle color specified
        layoutPaint.setColor(layoutColor);
        layoutPaint.setAlpha(layoutAlpha);

        keyPressPaint = new Paint();
        keyPressPaint.setStyle(Paint.Style.FILL);
        keyPressPaint.setColor(Color.GRAY);
        keyPressPaint.setAlpha(0);
        keyPressPaint.setAntiAlias(true);
    }

    private void TextPaintInit(){
        // Set up a default TextPaint object
        labelTextPaint = new TextPaint();
        labelTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        labelTextPaint.setTextAlign(Paint.Align.LEFT);

        promptTextPaint = new TextPaint();
        promptTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        promptTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    private void measureLabelText(String s)
    {
        labelTextPaint.setTextSize(textDimension);
        labelTextPaint.setColor(layoutColor);
        labelTextWidth = labelTextPaint.measureText(s);

        Paint.FontMetrics fontMetrics = labelTextPaint.getFontMetrics();
        labelTextHeight = fontMetrics.bottom;
    }

    private void measurePromptText(String s)
    {
        promptTextPaint.setTextSize(textDimension * 0.7f);
        promptTextPaint.setColor(layoutColor);
        promptTextWidth = promptTextPaint.measureText(s);

        Paint.FontMetrics fontMetrics = promptTextPaint.getFontMetrics();
        promptTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        //get half of the width and height as we are working with a circle
        float cx = contentWidth/2;

        //get the radius as half of the width or height, whichever is smaller
        float radius = KEYPAD_RADIUS;
        float cy = contentHeight - radius -32;

        // Draw numpad
        drawNumPad(canvas, radius, cx, cy, layoutPaint);

        // Draw the password dots
        float passPosY = (cy - radius)/2;
        passwordMan.drawPasswordDots(canvas, (int) passPosY, contentWidth, layoutColor, layoutAlpha);

        // Draw prompt text.
        // Draw the prompt text.
        measurePromptText(promptText);
        canvas.drawText(promptText,
                contentWidth / 2 - (promptTextWidth) / 2,
                passPosY - (promptTextHeight * den) * 1.2f,
                promptTextPaint);
    }

    /**
     * Draw num pad on the screen
     * @param canvas
     * @param radius the outter radius of the keypad
     * @param cx circle center x
     * @param cy circle center x
     */
    private void drawNumPad(Canvas canvas, float radius, float cx, float cy, Paint paint) {
        float h, w;

        // 3:2:sqrt(13)
        h = radius/3.605f * 3 * 2;
        w = radius/3.605f * 2 * 2;

        float x_gap = w/2f;
        float y_gap = h/3f;

        int digit = 1;
        int count = 0;

        float topLeft_x = cx - w/2f;
        float topLeft_y = cy - h/2f;

        keyboard.clear();
        for (int j = 0; j < 3; j += 1){
            for (int i =0; i < 3; i += 1){
                Key key = new Key(digit, topLeft_x + i * x_gap, topLeft_y + j * y_gap);
                keyboard.add(key);
                key.drawKey(canvas, paint);
                count++;
                digit++;
            }
        }

        // draw last digit 0
        Key lastKey = new Key(0, topLeft_x + 1 * x_gap, topLeft_y + 3 * y_gap);
        keyboard.add(lastKey);
        lastKey.drawKey(canvas, paint);
    }

    @Override
    public void InstallPassword(String password) {
        passwordMan.PasswordInit();
        passwordMan.setRefPass(password);
        passwordMan.BuildPasswordDisplayAndSetLength();
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
        // Initialize password manager
        passwordMan = new PasswordViewManger(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!InputEnabled) {
            return false;
        }

        int index = event.getActionIndex();
        int action = event.getActionMasked();

        // Finger position
        float fingerX = event.getX();
        float fingerY = event.getY();

        Key hitKey = HitTest(fingerX, fingerY);
        if (hitKey != null){
            switch(action)
            {
                case MotionEvent.ACTION_UP:
                    // Trigger animation
                    final KeyState state = keyStates[hitKey.value];
                    startKeyPressAnimation(0,50,100,
                            mFastOutSlowInInterpolator,
                            state,
                            new Runnable() {

                                @Override
                                public void run() {
                                    startKeyPressAnimation(50, 0, 100,
                                            mFastOutSlowInInterpolator, state, null);
                                }
                            });

                    passwordMan.AddDigit(hitKey.value);

                    if (passwordMan.isPasswordFull()) {
                        boolean result = passwordMan.ValidatePassword();

                        if (result){
                            promptText = CORRECT_TEXT;
                        }
                        else{
                            promptText = WRONG_TEXT;
                            passwordMan.ShakeDot();
                        }

                        // disable the input and show prompt
                        disableInput();
                        final Handler handler_dot = new Handler();
                        handler_dot.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                passwordMan.ClearPasswordStates();
                                enableInput();
                                invalidate();
                            }
                        }, 500);

                        final Handler handler_prompt = new Handler();
                        handler_prompt.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                promptText = "";
                                measurePromptText(promptText);
                                invalidate();
                            }
                        }, 500);
                        NotifyPasswordResults(result);
                    }

                    invalidate();

                    break;
                case MotionEvent.ACTION_DOWN:
                    buzzPhone();
                    if (passwordMan.isPasswordEmpty())
                    {
                        NotifyPasswordInputBegin();
                    }
                    break;

            }
        }



        return  true;
    }

    public void disableInput() {
        InputEnabled = false;
    }

    public void enableInput() {
        InputEnabled = true;
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

    private void buzzPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
            performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                            | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
    }

    // Animation for resetting the dial to initial position
    private void startKeyPressAnimation(int start, int end, long duration,
                                        Interpolator interpolator,
                                        final KeyState state,
                                        final Runnable endRunnable) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                state.Alpha = (int) animation.getAnimatedValue();
                invalidate();
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

    private Key HitTest(float x, float y)
    {
        for (Key k : keyboard){

            double distance = Math.pow((x - k.x), 2) + Math.pow((y - k.y), 2);
            distance = Math.sqrt(distance);

            if (distance < Key.KEY_SIZE) {
                return k;
            }
        }

        return null;
    }
}
