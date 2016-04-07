package com.example.administrator.plantvsgui;

/**
 * Created by Administrator on 2016/4/7.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Sun{
    public float left = 200; //图片左边距离
    public float top = -20;// 图片上边距离
    public static final int ONE = 0;// 图片下落状态
    public static final int TWO = 1;//图片点击返回状态
    public static int current = 1;//图片当前状态
    Bitmap sun;// 太阳
    Paint paint;// 画笔
    Context context;

    public Sun(MainActivity context) {// 构造器
        this.context = context;
        initBitmap();
    }

    /**
     * 图片点击事件
     *
     * @param event
     * @return
     */
    protected boolean onTouchEvent(MotionEvent event) {
        boolean flag = true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            if ((x - left) < 55 && (x - left) > 0
                    && (y - top) < 55 && (y - top) > 0) {
                flag = true;
                current = 2;
                GameView.suncount += 25;
            }
        }
        return true;
    }

    /**
     * 画图
     *
     * @param canvas
     */
    protected void Draw(Canvas canvas) {
        paint = new Paint();
        canvas.drawBitmap(sun, left, top, paint);

    }

    /**
     * 初始化图片资源
     */
    protected void initBitmap() {
        sun = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.sun);
        sun = GameView.zoomImage(sun, 40, 40);

    }

    protected void move() {
        if (current == 1) {
            if (this.top < 260) {
                this.top += 8;
            }
        } else if (current == 2) {
            if (top > -50) {
                float yleft = left;
                float ytop = 200;
                top -= 6;
                left -= yleft / ytop * 5;
            }

        }
    }

}