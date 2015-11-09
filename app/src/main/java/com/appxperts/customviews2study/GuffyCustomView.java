package com.appxperts.customviews2study;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by gufran on 11/9/15.
 */
public class GuffyCustomView extends View implements View.OnTouchListener {


    int backColor, edgeColor;
    String displayText;

    int height, width;


    //Touch event related variables
    int touchState;
    final int PINCH = 2;
    float dist0, distCurrent;


    public GuffyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GuffyCustomView, 0, defStyleAttr);

        backColor = typedArray.getColor(R.styleable.GuffyCustomView_backColor, 0);
        edgeColor = typedArray.getColor(R.styleable.GuffyCustomView_edgeColor, 0);
        displayText = typedArray.getString(R.styleable.GuffyCustomView_displayText);

        typedArray.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                height += 20;
                width += 20;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        width, height);

                setLayoutParams(layoutParams);

                requestLayout();
                invalidate();
            }
        });

    }

    public GuffyCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ///---->> not overriding now
        height = View.resolveSize(getDesiredHeight(), heightMeasureSpec);
        width = View.resolveSize(getDesiredWidth(), widthMeasureSpec);

        setMeasuredDimension(width, height);
    }


    private int getDesiredHeight() {
        return getHeight() == 0 ? getMinimumHeight() : getHeight();
        // 10 for padding
    }

    private int getDesiredWidth() {
        return getWidth() == 0 ? getMinimumWidth() : getWidth();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        //p.setStrokeWidth(5);
        p.setStyle(Paint.Style.FILL);
        p.setColor(backColor);

        canvas.drawCircle((int) width / 2, (int) height / 2, ((int) width / 2), p);


        p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(edgeColor);

        canvas.drawCircle((int) width / 2, (int) height / 2, ((int) width / 2) - 10
                , p);

        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(20.5f);
        canvas.drawText(displayText, (int) width / 2, (int) height / 2, p);
    }

    private void drawMatrix() {
        float curScale = distCurrent / dist0;
//        if (curScale < 0.1) {
//            curScale = 0.1f;
//        }

        height += 20;
        width += 20;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width, height);
        setLayoutParams(layoutParams);
        requestLayout();
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float distx, disty;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //Get the distance when the second pointer touch
                distx = event.getX(0) - event.getX(1);
                disty = event.getY(0) - event.getY(1);
                dist0 = (float) Math.sqrt(distx * distx + disty * disty);


                break;
            case MotionEvent.ACTION_MOVE:

                if (touchState == PINCH) {
                    //Get the current distance
                    distx = event.getX(0) - event.getX(1);
                    disty = event.getY(0) - event.getY(1);
                    distCurrent = (float) Math.sqrt(distx * distx + disty * disty);

                    drawMatrix();
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }

        return true;
    }

}
