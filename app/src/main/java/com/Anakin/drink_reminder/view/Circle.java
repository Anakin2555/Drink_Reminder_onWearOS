package com.Anakin.drink_reminder.view;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import com.Anakin.drink_reminder.R;
import com.Anakin.drink_reminder.Water;


public class Circle extends View {

    private Paint mPaint;
    private Paint cPaint;
    private Paint bPaint;


    /**
     * 圆的宽度
     */
    private int mCircleWidth = 32;

    public float angle=0;
    public float getAngle(){
        return this.angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Circle(Context context) {
        this(context, null);
    }

    public Circle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        cPaint=new Paint();
        bPaint=new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bPaint.setAntiAlias(true);//取消锯齿
        bPaint.setStyle(Paint.Style.STROKE);
        bPaint.setStrokeCap(Paint.Cap.ROUND);
        bPaint.setStrokeWidth(mCircleWidth);
        bPaint.setColor(getResources().getColor(R.color.darkBlue_2));
        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mCircleWidth);
        cPaint.setAntiAlias(true);//取消锯齿
        cPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        cPaint.setStrokeWidth(mCircleWidth);
        mPaint.setShadowLayer(0,0,0,Color.BLACK);
       /* cPaint.setColor(getResources().getColor(R.color.SkyBlue));*/






        /**
         * 这是一个居中的圆
         */
        float x = 44;
        float y =20;
        @SuppressLint("DrawAllocation") int color_1[]=new int[]{

                getResources().getColor(R.color.skyblue_4),
                getResources().getColor(R.color.skyblue_3),
                getResources().getColor(R.color.skyblue_2),
                getResources().getColor(R.color.skyblue_2)};
        @SuppressLint("DrawAllocation") int color_2[]=new int[]{
                getResources().getColor(R.color.skyblue_1),
                getResources().getColor(R.color.skyblue_0),
                getResources().getColor(R.color.skyblue_1),
                getResources().getColor(R.color.skyblue_0),
                getResources().getColor(R.color.skyblue_1)};
        float centerY=y+(getWidth()-2*x)/2;
        float radius=centerY-y;
        @SuppressLint("DrawAllocation")
        Shader shader_1=new RadialGradient(getWidth()/2,centerY,radius,color_1,null, Shader.TileMode.CLAMP);
        @SuppressLint("DrawAllocation")
        Shader shader_2=new SweepGradient(getWidth()/2,centerY,color_2,null);
        cPaint.setShader(shader_1);
        mPaint.setShader(shader_2);


        RectF oval = new RectF( x, y,getWidth()-x,(getWidth()-2*x)+y );
        canvas.drawArc(oval,135,270,false,bPaint);
        if(angle==0)
        {
            bPaint.setColor(getResources().getColor(R.color.skyblue_0));
            canvas.drawArc(oval,135, (float) 2,false,bPaint);
        }
        else if(angle>=270){

            canvas.drawArc(oval, 135, 270, false, mPaint);
        }
        else {

            canvas.drawArc(oval, 135, angle, false, mPaint);
        }

    }
}
