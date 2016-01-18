package hcicourse.hciproject.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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

import hcicourse.hciproject.R;
import hcicourse.hciproject.interfaces.LockViewControl;
import hcicourse.hciproject.interfaces.PasswordResultListener;
import hcicourse.hciproject.util.PasswordViewManger;

/**
 * TODO: document your custom view class.
 */
public class DialLock extends View implements LockViewControl {
    /* Private members*/
    // text displayed in the center
    private String initText = "HELLO WORLD";
    private String centerText = "HELLO WORLD";
    private String promptText = "HELLO WORLD";
    private int layoutCol = Color.RED;
    private int layoutAlph = 255;
    private int previousNum = -1;

    private int passwordSize = DEFAULT_PASSWORD_SIZE;
    private PasswordViewManger passwordMan;
    private float mExampleDimension = 0;

    // Circle center point
    private float cx, cy;

    // The reference angle in degrees, which is set when user first touch the interface
    private float baseAngle;

    // The previous angle in degrees for determining direction of rotation
    private float previousAngle;

    private DialDirection rotationDirection;

    private float den = getResources().getDisplayMetrics().density;

    private Paint circlePaint; //paint for drawing custom view

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private TextPaint centerTextPaint;
    private float centerTextWidth;
    private float centerTextHeight;

    private TextPaint promptTextPaint;
    private float promptTextWidth;
    private float promptTextHeight;
    private boolean InputEnabled = true;

    // State of the dial for animation
    private DialState mDialState;

    private android.view.animation.Interpolator mFastOutSlowInInterpolator;

    private ArrayList<PasswordResultListener> passwordResultListeners;

    /* Static members*/
    private static final int DIAL_SLICE_SIZE = 36;
    private static final int DEFAULT_PASSWORD_SIZE = 4;
    private static final int LAYOUT_STROKE_WIDTH = 4;
    private static final int DIAL_RADIUS = 400;
    private static final int POINTER_TRIANGLE_SIZE = 50;
    private static final String CORRECT_TEXT = "Correct Passcode";
    private static final String WRONG_TEXT = "Wrong Passcode";

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
        DataInit();
    }


    /* Internal Class and Enum*/
    public static class DialState {
        // the absolute rotation angle of the canvas when drawing
        public float rotate = 0.0f;
    }

    public enum DialDirection
    {
        ClockWise,
        CounterClockWise,
        Neutral
    }

    /* Constructors*/
    public DialLock(Context context) {
        super(context);
        init(null, 0);
    }

    public DialLock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DialLock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DialLock, defStyle, 0);

        initText = a.getString(
                R.styleable.DialLock_circleLabel);
        centerText = initText;

        promptText = a.getString(
                R.styleable.DialLock_promptText);

        layoutCol = a.getColor(
                R.styleable.DialLock_diallock_layoutColor,
                layoutCol);
        layoutAlph = a.getInteger(R.styleable.DialLock_diallock_layoutAlpha,
                layoutAlph);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.DialLock_textDimension,
                mExampleDimension);

        a.recycle();

        //paint object for drawing in onDraw
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(LAYOUT_STROKE_WIDTH);
        circlePaint.setAntiAlias(true);
        //set the paint color using the circle color specified
        circlePaint.setColor(layoutCol);
        circlePaint.setAlpha(layoutAlph);
        TextPaintInit();

        DataInit();

        mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(
                this.getContext(), android.R.interpolator.fast_out_slow_in);

        passwordResultListeners = new ArrayList<PasswordResultListener>();
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    public void DataInit()
    {
        // Initialize dial state
        mDialState = new DialState();
        mDialState.rotate = 0;

        // Initialize password manager
        passwordMan = new PasswordViewManger(this);
    }

    private void TextPaintInit(){
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        centerTextPaint = new TextPaint();
        centerTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        centerTextPaint.setTextAlign(Paint.Align.LEFT);

        promptTextPaint = new TextPaint();
        promptTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        promptTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    /* Methods*/
    private void invalidateTextPaintAndMeasurements() {
        measureLabelText();
        measurePromptText();
        measureCenterText(mExampleDimension);
        invalidate();
    }

    private void measureLabelText() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(layoutCol);
        mTextWidth = mTextPaint.measureText(centerText);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    private void measureCenterText(float dim)
    {
        centerTextPaint.setTextSize(dim);
        centerTextPaint.setColor(layoutCol);
        centerTextWidth = centerTextPaint.measureText(centerText);

        Paint.FontMetrics  fontMetrics = centerTextPaint.getFontMetrics();
        centerTextHeight = fontMetrics.bottom;
    }

    private void measurePromptText()
    {
        promptTextPaint.setTextSize(mExampleDimension * 0.7f);
        promptTextPaint.setColor(layoutCol);
        promptTextWidth = promptTextPaint.measureText(promptText);

        Paint.FontMetrics fontMetrics = promptTextPaint.getFontMetrics();
        promptTextHeight = fontMetrics.bottom;
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.max(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        //get half of the width and height as we are working with a circle
        cx = contentWidth/2;
        //cy = contentHeight/2;

        //get the radius as half of the width or height, whichever is smaller
        //subtract 12 so that it has some space around it
        //float radius= (Math.min(cx, cy)) * 0.8f;
        float radius=DIAL_RADIUS;
        cy = contentHeight - radius -32;

        canvas.drawCircle(cx, cy, radius, circlePaint);
        canvas.drawCircle(cx, cy, radius * 0.5f, circlePaint);

        drawNumPad(canvas, radius, cx, cy);

        int triangleX = (int) cx ;
        int triangleY = (int)(cy - radius * 0.5) + 8;
        int triangleSize = POINTER_TRIANGLE_SIZE;

        drawTriangle(canvas, new Point(triangleX, triangleY),
                new Point(triangleX + triangleSize / 2, triangleY + triangleSize),
                new Point(triangleX - triangleSize / 2, triangleY + triangleSize));

        // Draw the center text.
        canvas.drawText(centerText,
                cx - (centerTextWidth) / 2,
                cy + (centerTextHeight * 2.5f) / 2,
                centerTextPaint);

        // Draw the password dots
        float passPosY = (cy - radius)/2;
        passwordMan.drawPasswordDots(canvas, (int)passPosY, contentWidth, layoutCol, layoutAlph);

        // Draw prompt text.
        // Draw the prompt text.
        canvas.drawText(promptText,
                contentWidth / 2 - (promptTextWidth) / 2,
                passPosY - (promptTextHeight * den) * 1.2f,
                promptTextPaint);
    }

    private void drawNumPad(Canvas canvas, float radius, float cx, float cy){
        // Draw a small circle
        float padRadius = radius / 5f;

        float x, y;
        float i = (float)( Math.PI)/2;
        int digit = 0;

        //canvas.rotate(mDialState.rotate, cx, cy);

        while ( i < (2.5)*Math.PI )
        {
            x = (float) (radius * 0.75 * Math.cos(i));
            y = (float) (radius * 0.75 * Math.sin(i));

            x = cx - x;
            y = cy - y;

            canvas.drawCircle(x, y, padRadius, circlePaint);

            String digitString = Integer.toString(digit);
            mTextPaint.setTextSize(mExampleDimension);
            float digitWidth = mTextPaint.measureText(digitString);
            float digitHeight = mTextHeight;

            float drawPositionX = x - (digitWidth) / 2;
            float drawPositionY = y + (digitHeight * den) / 2;
            canvas.rotate((float) (i * 180f / Math.PI - 90), x, y);
            canvas.drawText(digitString,
                    drawPositionX,
                    drawPositionY,
                    mTextPaint);
            canvas.rotate((float) -(i * 180f / Math.PI - 90), x, y);
            i = i + (float)(DIAL_SLICE_SIZE/180f * Math.PI);
            digit ++;
        }

        //canvas.rotate(-mDialState.rotate, cx, cy);
    }

    private void drawTriangle(Canvas canvas, Point point1_draw, Point point2_draw, Point point3_draw)
    {
        canvas.rotate(mDialState.rotate, cx, cy);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStrokeWidth(LAYOUT_STROKE_WIDTH);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(layoutCol);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x,point3_draw.y);
        path.lineTo(point1_draw.x,point1_draw.y);
        path.close();

        canvas.drawPath(path, paint);
        canvas.rotate(-mDialState.rotate, cx, cy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!InputEnabled || !isEnabled()) {
            return false;
        }

        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        // Finger position
        float fingerX = event.getX();
        float fingerY = event.getY();

        double posX = fingerX - cx;
        double posY = cy - fingerY;

          // DEBUG: For display position
//        String msg = ("x:" + (int)posX + ", y:" + (int) posY);
//        setCircleText(msg);

        float r = (float)Math.sqrt(Math.pow(posX, 2) + Math.pow(posY, 2));

        // Get the absolute angle of finger position
        float val = (float)(Math.acos(posX/r) / Math.PI  * 180);
        float angle = (posY > 0)? val: (360f - val);

        float newRotation, directionDelta, delta;
        int newNumber;
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                // Set the base
//                baseAngle = angle;
                baseAngle = 0;
                // Set previous number
                previousNum = 0;
                // Clear rotation record
                rotationDirection = DialDirection.Neutral;

                // Set rotation
                mDialState.rotate = -(angle - 90 + 360)%360;

                newNumber = AngleToNumber(mDialState.rotate);
                if (previousNum != newNumber) {
                    buzzPhone();
                }

                // Add digit to password
                passwordMan.AddDigit(newNumber);

                // Show number
                centerText = Integer.toString(newNumber);
                measureCenterText(mExampleDimension * 2);
                invalidate();

                // Fire event for beginning drawing password
                NotifyPasswordInputBegin();
                break;
            case MotionEvent.ACTION_MOVE:

                newRotation = -(angle - 90 + 360)%360;

                delta = newRotation - mDialState.rotate;

                // The sign of directionDelta determines the rotation, plus is clockwise
                directionDelta = 180 - (newRotation - mDialState.rotate + 360)%360;

                // Update only when delta is large enough
                if (Math.abs(delta) > 0.5){
                    mDialState.rotate =  newRotation;
                    UpdateRotationDirectionAndGetNumber(directionDelta);

                    // Show number
                    newNumber = AngleToNumber(mDialState.rotate);
                    if (previousNum != newNumber) {
                        buzzPhone();
                    }
                    previousNum = newNumber;
                    centerText = Integer.toString(newNumber);
                    measureCenterText(mExampleDimension * 2);

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Roll back to original position
                centerText = initText;
                measureCenterText(mExampleDimension);
                startDialResetAnimation(mDialState.rotate, 0, 100, mFastOutSlowInInterpolator);

                newNumber = AngleToNumber(mDialState.rotate);
                // Add digit to password
                passwordMan.AddDigit(newNumber);

                boolean result = passwordMan.ValidatePassword();

                if (result){
                    promptText = CORRECT_TEXT;
                    measurePromptText();

                }
                else{
                    promptText = WRONG_TEXT;
                    measurePromptText();
                    passwordMan.ShakeDot();
                }

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
                        measurePromptText();
                        invalidate();
                    }
                }, 500);
                NotifyPasswordResults(result);

                previousNum = -1;
                break;
        }
        return true;
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
    private void startDialResetAnimation(float start, float end, long duration,
                                         Interpolator interpolator) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDialState.rotate = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    // Convert angle of rotation to a number on the numpad
    private int AngleToNumber(float rotation) {
        return (int)((rotation+DIAL_SLICE_SIZE/2 + 360)%360)/DIAL_SLICE_SIZE;
    }

    /**
     * Update the rotation direction and get the current number under dial if the direction changes
     * Plus is clockwise
     */
    private void UpdateRotationDirectionAndGetNumber(float delta) {

        if (delta == 0)
        {
            // Nothing to do here
            return;
        }

        // Init case, set the direction and return
        if (rotationDirection == DialDirection.Neutral)
        {
            rotationDirection = delta > 0 ? DialDirection.ClockWise: DialDirection.CounterClockWise;
        }
        else
        {
            DialDirection thisDirection = delta > 0 ? DialDirection.ClockWise: DialDirection.CounterClockWise;

            if (thisDirection != rotationDirection)
            {
                // Rotation direction change, log number, update direction and return
                int newDigit = AngleToNumber(mDialState.rotate);

                // Add digit to password
                passwordMan.AddDigit(newDigit);

                // Show it
                centerText = Integer.toString(newDigit);
                measureCenterText(mExampleDimension * 4);

                rotationDirection = thisDirection;
            }
        }
    }

    /* Getter Setters*/
    public int getPasswordSize() {
        return passwordSize;
    }

    public void setPasswordSize(int passwordSize) {
        this.passwordSize = passwordSize;
    }
}
