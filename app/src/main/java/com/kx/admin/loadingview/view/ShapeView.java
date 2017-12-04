package com.kx.admin.loadingview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class ShapeView extends View  {
    private LoadingShape currentShape =LoadingShape.CIRCLE ;
    private Paint circlePaint ,rectanglePaint,trianglePaint;
    private Path trianglePath ;
    private int min;

    public ShapeView(Context context) {
        this(context,null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.YELLOW);
        rectanglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setColor(Color.RED);
        trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setColor(Color.BLUE);
        trianglePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        min = Math.min(measuredHeight, measuredWidth);
        setMeasuredDimension(min, min);    //保证为正方形
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void exchange(){
        switch (currentShape){
            case CIRCLE:
                currentShape = LoadingShape.RECTANGLE ;
                break;
            case RECTANGLE:
                currentShape = LoadingShape.TRIANGLE ;
                break;
            case TRIANGLE:
                currentShape = LoadingShape.CIRCLE ;
                break;
        }
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        switch (currentShape){
            case CIRCLE:
                canvas.drawCircle(width/2,height/2,width/2,circlePaint);
                break;
            case RECTANGLE:
                canvas.drawRect(0,0,width,height,rectanglePaint);
                break;
            case TRIANGLE:
                trianglePath.reset();
                    trianglePath.moveTo(width/2,0);
                    trianglePath.lineTo(width, (float) ((width * Math.sqrt(3))/2));
                    trianglePath.lineTo(0, (float) ((width * Math.sqrt(3))/2));
                    trianglePath.close();
                canvas.drawPath(trianglePath,trianglePaint);
                break;
        }
    }

    public enum LoadingShape{
        //  圆形      矩形    三角形
            CIRCLE,RECTANGLE,TRIANGLE
    }

    public LoadingShape getCurrentShape() {
        return currentShape;
    }
}
