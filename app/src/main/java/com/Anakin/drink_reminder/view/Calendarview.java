package com.Anakin.drink_reminder.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.Anakin.drink_reminder.R;


public class Calendarview extends View {
    public float goal=0;
    public int days=0;

    private Paint paint;
    private Paint textPaint;
    private Paint paint2;
    private Paint linePaint;

    public void setGoal(float goal) {
        this.goal = goal;
    }

    public void setDays(int days){
        this.days=days;
    }


    public float[] record =new float[days];

    public void setRecord(float[] record) {
        this.record = record;
    }

    public Calendarview(Context context) {
        this(context,null);
    }

    public Calendarview(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public Calendarview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
        textPaint=new Paint();
        paint2=new Paint();
        linePaint=new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        float x=24;
        int date=1;

        float from_height=108;
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.darkBlue_2));
        paint.setStrokeWidth(16);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint2.setAntiAlias(true);
        paint2.setColor(getResources().getColor(R.color.SkyBlue));
        paint2.setStrokeWidth(16);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(20);
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.skyblue_1));
        linePaint.setStrokeWidth(0.7f);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        PathEffect effects = new DashPathEffect(new float[] { 2, 2}, 1);
        linePaint.setPathEffect(effects);
        for(int i=1;i<=2;i++) {
            x=24;
            canvas.drawLine(10,from_height-46,358,from_height-46,linePaint);
            for(int j=1;j<=11;j++) {
                canvas.drawLine(x, from_height, x, from_height-60, paint);
                if(record[date-1]!=0&&record[date-1]<=goal*1.5) {


                    canvas.drawLine(x, from_height, x, from_height - 40 * (record[date - 1] / goal), paint2);
                }
                if(record[date-1]>goal*1.5){
                    canvas.drawLine(x, from_height, x, from_height - 60 , paint2);
                }
                Log.d("goal",String.valueOf(goal));
                Log.d("water"+date
                        +record[date-1]/goal,"!!!");
                if(date>=10)
                canvas.drawText(String.valueOf(date),x-10,from_height+36,textPaint);
                else
                    canvas.drawText(String.valueOf(date),x-6,from_height+36,textPaint);
                x += 32;
                date++;
            }
            from_height+=132;

        }
        x=24;
        canvas.drawLine(10,from_height-46,358,from_height-46,linePaint);
        for(int i=23;i<=days;i++){

            canvas.drawLine(x, from_height, x, from_height-60, paint);
            if(record[date-1]!=0&&record[date-1]<=goal*1.5) {
                canvas.drawLine(x, from_height, x, from_height - 40 * (record[date - 1] / goal), paint2);
            }
            if(record[date-1]>goal*1.5){
                canvas.drawLine(x, from_height, x, from_height - 60 , paint2);
            }
            canvas.drawText(String.valueOf(date),x-10,from_height+36,textPaint);
            x+=32;
            date++;

        }


    }
}
