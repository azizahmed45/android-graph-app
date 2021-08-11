package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Graph extends View {
    Paint xAxisPaint;
    Paint yAxisPaint;

    Paint indicatorPaint;
    Paint indicatorTextPaint;

    Paint xSliderPaint;
    Paint ySliderPaint;

    Paint drawXPaint;
    Paint drawYPaint;

    Paint singleSliderPaint;

    Paint rectPaint;

    int padding = 150;

    int xAxisValue = 0;
    int yAxisValue = 0;

    float diff;

    int xAxisMax = 10;
    int yAxisMax = 15;

    float[] xAxisPositions = new float[xAxisMax + 1];
    float[] yAxisPositions = new float[yAxisMax + 1];

    float rawXValue;
    float rawYValue;

    Region xSliderRegion;
    Region ySliderRegion;
    Region singleSliderRegion;

    boolean xSliderEnabled = false;
    boolean ySliderEnabled = false;

    boolean singleSliderEnabled = false;

    AxisDataChangeListener mAxisDataChangeListener;

    boolean firstTimeDraw = true;

    SliderMode mSliderMode = SliderMode.DOUBLE_SLIDE;


    public Graph(Context context) {
        super(context);
        init();
    }

    public Graph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Graph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Graph(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        xAxisPaint = new Paint();
        xAxisPaint.setStyle(Paint.Style.STROKE);
        xAxisPaint.setColor(getResources().getColor(R.color.red));
        xAxisPaint.setStrokeWidth(5);


        yAxisPaint = new Paint();
        yAxisPaint.setStyle(Paint.Style.STROKE);
        yAxisPaint.setColor(getResources().getColor(R.color.yellow));
        yAxisPaint.setStrokeWidth(5);


        indicatorPaint = new Paint();
        indicatorPaint.setStyle(Paint.Style.STROKE);
        indicatorPaint.setColor(getResources().getColor(R.color.black));
        indicatorPaint.setStrokeWidth(5);

        indicatorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorTextPaint.setTextSize(30);


        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(getResources().getColor(R.color.orange));

        xSliderPaint = new Paint();
        xSliderPaint.setStyle(Paint.Style.FILL);
        xSliderPaint.setColor(getResources().getColor(R.color.red));
        xSliderPaint.setAlpha(150);


        ySliderPaint = new Paint();
        ySliderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        ySliderPaint.setColor(getResources().getColor(R.color.yellow));
        ySliderPaint.setAlpha(150);

        singleSliderPaint = new Paint();
        singleSliderPaint.setStyle(Paint.Style.FILL);
        singleSliderPaint.setColor(getResources().getColor(R.color.purple_700));
        singleSliderPaint.setStrokeWidth(5);

        drawXPaint = new Paint();
        drawXPaint.setStyle(Paint.Style.FILL);
        drawXPaint.setColor(getResources().getColor(R.color.red));
        drawXPaint.setTextSize(50);
        drawXPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        drawYPaint = new Paint();
        drawYPaint.setStyle(Paint.Style.FILL);
        drawYPaint.setColor(getResources().getColor(R.color.yellow));
        drawYPaint.setTextSize(50);
        drawYPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }


    @Override
    protected void onDraw(Canvas canvas) {

        float startXVertical = padding;
        float startYVertical = getHeight() - padding;
        float startXHorizontal = padding;
        float startYHorizontal = getHeight() - padding;

        float horizontalLength = getWidth() - padding * 2;
        float verticalLength = (15 * horizontalLength) / 10;



        //vertical line y axis
        canvas.drawLine(startXVertical, startYVertical + 20, startXVertical, startYVertical - verticalLength, yAxisPaint);

        //horizontal line x axis
        canvas.drawLine(startXHorizontal - 20, startYHorizontal, startXHorizontal + horizontalLength, startYHorizontal, xAxisPaint);

        //0 indicator
        canvas.drawText(String.valueOf(0), startXHorizontal - 50, startYHorizontal + 50, indicatorTextPaint);

        canvas.drawText("X", startXHorizontal + horizontalLength + 50, startYVertical + 25, drawXPaint);

        canvas.drawText("Y", startXVertical - 25, startYVertical - verticalLength - 50, drawYPaint);


        //draw indicator
        diff = (getWidth() - padding * 2) / 10f;


        for (int i = 0; i <= xAxisMax; i++) {
            xAxisPositions[i] = startXHorizontal + i * diff;

            //don't draw 0
            if (i == 0) continue;
            canvas.drawLine(xAxisPositions[i], startYHorizontal - 10, xAxisPositions[i], startYHorizontal + 10, indicatorPaint);
            canvas.drawText(String.valueOf(i), xAxisPositions[i] - 5, startYHorizontal + 50, indicatorTextPaint);
        }

        for (int i = 0; i <= yAxisMax; i++) {
            yAxisPositions[i] = startYVertical - i * diff;

            //don't draw 0
            if (i == 0) continue;
            canvas.drawLine(startXVertical - 10, yAxisPositions[i], startXVertical + 10, yAxisPositions[i], indicatorPaint);
            canvas.drawText(String.valueOf(i), startXHorizontal - 60, yAxisPositions[i] + 5, indicatorTextPaint);

        }


        //set to zero
        if (firstTimeDraw) {
            setAxis(0, 0);
            firstTimeDraw = false;
        }

        //rect paint
        canvas.drawRect(startXVertical, rawYValue, rawXValue, startYHorizontal, rectPaint);

        for(int i = 0; i <= xAxisMax; i++){
            if(xAxisPositions[i] > rawXValue) break;
            canvas.drawLine(xAxisPositions[i], startYVertical, xAxisPositions[i], rawYValue, indicatorPaint);
        }

        for(int i = 0; i <= yAxisMax; i++){
            if(yAxisPositions[i] < rawYValue) break;
            canvas.drawLine(startXHorizontal, yAxisPositions[i], rawXValue, yAxisPositions[i], indicatorPaint);
        }



        //slider

        if (mSliderMode == SliderMode.DOUBLE_SLIDE) {


            //draw slider x axis
            Path path = new Path();
            path.moveTo(rawXValue, startYHorizontal);
            path.lineTo(20 + rawXValue, startYHorizontal + 20);
            path.lineTo(20 + rawXValue, startYHorizontal + 80);
            path.lineTo(-20 + rawXValue, startYHorizontal + 80);
            path.lineTo(-20 + rawXValue, startYHorizontal + 20);
            path.lineTo(rawXValue, startYHorizontal);

            path.close();

            canvas.drawPath(path, xSliderPaint);

            xSliderRegion = new Region((int) (rawXValue - 20), (int) (startYHorizontal), (int) (rawXValue + 20), (int) (startYHorizontal + 80));


            //draw slider y axis
            Path path2 = new Path();
            path2.moveTo(startXHorizontal, rawYValue);
            path2.lineTo(startXHorizontal - 20, rawYValue - 20);
            path2.lineTo(startXHorizontal - 80, rawYValue - 20);
            path2.lineTo(startXHorizontal - 80, rawYValue + 20);
            path2.lineTo(startXHorizontal - 20, rawYValue + 20);
            path2.lineTo(startXHorizontal, rawYValue);

            path2.close();

            canvas.drawPath(path2, ySliderPaint);

            ySliderRegion = new Region((int) (startXHorizontal - 80), (int) (rawYValue - 20), (int) startXHorizontal, (int) (rawYValue + 20));
        }

        if (mSliderMode == SliderMode.SINGLE_SLIDE) {
            canvas.drawCircle(rawXValue, rawYValue, 30, singleSliderPaint);
            singleSliderRegion = new Region((int) (rawXValue - 30), (int) (rawYValue - 30), (int) (rawXValue + 30), (int) (rawYValue + 30));

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int)(width * 1.5);

        setMeasuredDimension(width, height);
    }


    public void setAxis(int x, int y) {
        xAxisValue = x;
        yAxisValue = y;
        rawXValue = xAxisPositions[x];
        rawYValue = yAxisPositions[y];

        if (mAxisDataChangeListener != null) {
            mAxisDataChangeListener.onChange(x, y);
        }
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                if (mSliderMode == SliderMode.DOUBLE_SLIDE) {
                    if (xSliderRegion.contains(x, y)) {
                        xSliderEnabled = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (ySliderRegion.contains(x, y)) {
                        ySliderEnabled = true;
                        getParent().requestDisallowInterceptTouchEvent(true);

                    }
                }

                if (mSliderMode == SliderMode.SINGLE_SLIDE) {
                    if (singleSliderRegion.contains(x, y)) {
                        singleSliderEnabled = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (xSliderEnabled) {
                    rawXValue = Math.min(Math.max(event.getX(), xAxisPositions[0]), xAxisPositions[xAxisMax]);
                }

                if (ySliderEnabled) {
                    rawYValue = Math.min(Math.max(event.getY(), yAxisPositions[yAxisMax]), yAxisPositions[0]);
                }

                if (singleSliderEnabled) {
                    rawXValue = Math.min(Math.max(event.getX(), xAxisPositions[0]), xAxisPositions[xAxisMax]);
                    rawYValue = Math.min(Math.max(event.getY(), yAxisPositions[yAxisMax]), yAxisPositions[0]);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (xSliderEnabled || ySliderEnabled || singleSliderEnabled) {
                    xSliderEnabled = false;
                    ySliderEnabled = false;
                    singleSliderEnabled = false;

                    getParent().requestDisallowInterceptTouchEvent(false);
                    setNearestValue();
                }
        }

        return true;
    }


    private void setNearestValue() {
        float distance = Math.abs(xAxisPositions[0] - rawXValue);
        int idx = 0;
        for (int c = 1; c < xAxisPositions.length; c++) {
            float cdistance = Math.abs(xAxisPositions[c] - rawXValue);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }


        //2nd
        float distance2 = Math.abs(yAxisPositions[0] - rawYValue);
        int idx2 = 0;
        for (int c = 1; c < yAxisPositions.length; c++) {
            float cdistance = Math.abs(yAxisPositions[c] - rawYValue);
            if (cdistance < distance2) {
                idx2 = c;
                distance2 = cdistance;
            }
        }

        setAxis(idx, idx2);
    }


    public void setAxisDataChangeListener(AxisDataChangeListener axisDataChangeListener) {
        mAxisDataChangeListener = axisDataChangeListener;
    }

    public interface AxisDataChangeListener {
        void onChange(int x, int y);
    }

    enum SliderMode {
        DOUBLE_SLIDE,
        SINGLE_SLIDE,
    }

    public void setSliderMode(SliderMode sliderMode) {
        mSliderMode = sliderMode;
        invalidate();
    }
}
