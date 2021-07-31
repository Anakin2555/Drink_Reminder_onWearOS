package com.Anakin.drink_reminder.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.Anakin.drink_reminder.R;
import com.Anakin.drink_reminder.Water;


public class Circle_small extends View {

    private Paint mPaint;


    /**
     * 圆的宽度
     */
    private int mCircleWidth = 24;

    public float angle=0;
    public float getAngle(){
        return this.angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Circle_small(Context context) {
        this(context, null);
    }

    public Circle_small(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Circle_small(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setShadowLayer(0,0,0,Color.BLACK);



        /**
         * 这是一个居中的圆
         */
        float x = 64;
        float y =20;

        RectF oval = new RectF( x, y,getWidth()-x,(getWidth()-2*x)+y );
        mPaint.setColor(getResources().getColor(R.color.skyblue_4));
        canvas.drawArc(oval,135,270,false,mPaint);
        /*if(angle==0)
        {

            canvas.drawArc(oval,135, (float) 0.1,false,mPaint);
        }*/
        if(angle>=270){
            mPaint.setColor(getResources().getColor(R.color.skyblue_2));
            canvas.drawArc(oval,135,270,false,mPaint);
        }
        else {
            mPaint.setColor(getResources().getColor(R.color.skyblue_2));
            canvas.drawArc(oval, 135, angle, false, mPaint);
        }

    }
}
