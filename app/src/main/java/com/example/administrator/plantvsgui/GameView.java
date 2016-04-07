package com.example.administrator.plantvsgui;

/**
 * Created by Administrator on 2016/4/7.
 */

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    MainActivity activity;
    ApahaThread ap ;
    static int suncount = 500;
    private TutorialThread thread;// 刷帧的线程
    MoveThread moveThread;// 移动物体的线程
    BulletMoveThread bulletThread;
    float currentx;
    float currenty;
    boolean isdown;
    boolean plantmoveFlag = false;

    int status = 1;// 游戏的状态1表示游戏中,2表示游戏失败
    Bitmap[] BestZomebieBitmap;// 僵尸1
    Bitmap[] MidlerZomebieBitmap;;// 僵尸2
    Bitmap[] LessMidZomebieBitmap;// 僵尸3
    Bitmap[] BadestZomebieBitmap;// 僵尸4
    static Bitmap[] Plant1;// 植物1
    Bitmap background1, background;// 背景图片
    Bitmap seedbank;
    Bitmap[] seedImage = new Bitmap[6];
    Bitmap[] seedPlants = new Bitmap[4];
    int[] image = { R.drawable.seed_00, R.drawable.seed_01, R.drawable.seed_02,
            R.drawable.seed_03, R.drawable.seed_04, R.drawable.seed_05 };
    int[] seedPs ={R.drawable.plant1, R.drawable.plant2, R.drawable.plant3, R.drawable.plant4};
    ArrayList<Bullets> goodBollets1 = new ArrayList<Bullets>();// 植物发出的子弹
    ArrayList<Bullets> goodBollets2 = new ArrayList<Bullets>();
    ArrayList<Bullets> goodBollets3 = new ArrayList<Bullets>();
    ArrayList<BestZomebie> zombies;// 僵尸群
    static ArrayList<Plants> plants;// 植物群
    Sun sun;
    SeedBank sb;
    Paint paint;// 画笔
    private Matrix martic;

    public GameView(MainActivity context) {
        super(context);
        this.activity = context;
        martic = new Matrix();
        martic.setScale(0.55f, 0.55f);
        getHolder().addCallback(this);// 注册接口
        this.thread = new TutorialThread(getHolder(), this);// 初始化刷帧线程
        this.moveThread = new MoveThread(this);
        this.bulletThread = new BulletMoveThread(this);
        plants = new ArrayList<Plants>();
        if (activity.processView != null) {
            activity.processView.process += 20;
        }
        zombies = Maps.getFirst();// 取第一关的僵尸
        // plants = Maps.getPlants();
        sb = new SeedBank(this.activity);
        initBitmap();
        ap = new ApahaThread(sb);
        sun = new Sun(this.activity);
    }

    public static Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
        // 获取这个图片的宽和高
        int width = bgimage.getWidth();
        int height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
                matrix, true);
        return bitmap;

    }

    /**
     * 初始化所有图片
     */
    public void initBitmap() {

        /**
         * 初始化背景图
         */
        background1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg);
        background = zoomImage(background1, 480, 320);
        if (activity.processView != null) {
            activity.processView.process += 20;
        }

        /**
         * 初始化僵尸1代
         */
        BestZomebieBitmap = new Bitmap[] {
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_00_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_00_02),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_00_03),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_00_04),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_00_05),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_00_06),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_00_07) };

        for (int i = 0; i < BestZomebieBitmap.length; i++) {
            BestZomebieBitmap[i] = zoomImage(BestZomebieBitmap[i], 40, 58);
            // BestZomebieBitmap[i] = Bitmap.createBitmap(BestZomebieBitmap[i],
            // 0, 0, BestZomebieBitmap[i].getWidth(),
            // BestZomebieBitmap[i].getHeight(), martic, true);
        }
        if (activity.processView != null) {
            activity.processView.process += 20;
        }

        /**
         * 初始化僵尸3代
         */
        MidlerZomebieBitmap = new Bitmap[] {
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_02_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_02_02),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_02_03),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_02_04),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_02_05),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_02_06),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_02_07) };
        for (int i = 0; i < MidlerZomebieBitmap.length; i++) {
            MidlerZomebieBitmap[i] = zoomImage(MidlerZomebieBitmap[i], 40, 58);
        }
        if (activity.processView != null) {
            activity.processView.process += 20;
        }

        /**
         * 初始化僵尸2代
         */
        LessMidZomebieBitmap = new Bitmap[] {
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_01_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_01_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_01_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_01_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_01_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_01_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.z_01_01) };

        /**
         * 实体类中的图片数组初始化
         */
        for (BestZomebie eep : zombies) {
            eep.nomalBitmap = BestZomebieBitmap;

        }

        /**
         * 初始化植物
         */
        Plant1 = new Bitmap[] {
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_01),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_02),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_03),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_04),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_05),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_06),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_07),
                BitmapFactory.decodeResource(this.activity.getResources(),
                        R.drawable.p_01_08) };
        for (int i = 0; i < Plant1.length; i++) {
            Plant1[i] = Bitmap.createBitmap(Plant1[i], 0, 0, Plant1[i]
                    .getWidth(), Plant1[i].getHeight(), martic, true);
            // Plant1[i] = zoomImage(Plant1[i],40,40);
        }

        /**
         * 初始化道具栏
         */
        seedbank = BitmapFactory.decodeResource(getResources(),
                R.drawable.seedbank);
        martic.setScale(0.78f, 0.66f);
        seedbank = Bitmap.createBitmap(seedbank, 0, 0, seedbank.getWidth(),
                seedbank.getHeight(), martic, true);
        martic.setScale(0.55f, 0.55f);
        for(int i=0;i<seedPlants.length;i++){
            seedPlants[i] =BitmapFactory.decodeResource(getResources(), seedPs[i]);
        }
        for (int i = 0; i < seedImage.length; i++) {
            seedImage[i] = BitmapFactory.decodeResource(getResources(),
                    image[i]);
            seedImage[i] = zoomImage(seedImage[i],40,50 );//27, 38
//			 seedImage[i] = Bitmap.createBitmap(seedImage[i], 0, 0,
//			 seedImage[i].getWidth(), seedImage[i].getHeight(), martic, true);
        }
        sb.seedImage = seedImage;
        sb.seedbank = seedbank;
        //sb.seedPlants = seedPlants;
        if (activity.processView != null) {
            activity.processView.process += 20;
        }

    }

    public void onDraw(Canvas canvas) {// 自己写的绘制方法,并非重写的
        canvas.drawBitmap(background, 0, 0, paint);// 绘制背景
        sb.draw(canvas);// 绘制工具栏
        if(status == 1 || status == 3){//游戏中时,关口中时
//		if (plantmoveFlag) {
//			sb.drawMoveIma(canvas, currentx, currenty, isdown);// 让植物跟着鼠标动
//		}
            try {// 绘制我方子弹
                for (Bullets b : goodBollets1) {
                    b.draw(canvas);
                }
            } catch (Exception e) {
            }
            try {// 绘制我方子弹
                for (Bullets b : goodBollets2) {
                    b.draw(canvas);
                }
            } catch (Exception e) {
            }
            try {// 绘制我方子弹
                for (Bullets b : goodBollets3) {
                    b.draw(canvas);
                }
            } catch (Exception e) {
            }
            try {// 绘制僵尸
                for (BestZomebie ep : zombies) {
                    ep.draw(canvas);
                }
            } catch (Exception e) {

            }
            try {// 绘制植物
                for (Plants ps : plants) {
                    ps.draw(canvas);
                }
            } catch (Exception e) {
            }
            try{
                sun.Draw(canvas);
            }catch(Exception e){

            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sb.touchEvent(event);
        sun.onTouchEvent(event);

        return true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.thread.setFlag(true);// 启动刷帧线程
        this.thread.start();
        this.moveThread.setFlag(true);
        this.moveThread.start();// 启动所以移动物的移动线程
        this.bulletThread.setFlag(true);
        this.bulletThread.start();
        ap.start();

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;// 循环标记
        thread.setFlag(false);
        while (retry) {
            try {
                thread.join();// 等待线程的结束
                retry = false;// 设置循环标记停止循环
            } catch (InterruptedException e) {
            }// 不断地循环，直到刷帧线程结束
        }
        ap.flag = false ;
    }

    class TutorialThread extends Thread {// 刷帧线程
        private int sleepSpan = 70;// 睡眠的毫秒数
        private SurfaceHolder surfaceHolder;
        private GameView gameView;
        private boolean flag = false;

        public TutorialThread(SurfaceHolder surfaceHolder, GameView gameView) {// 构造器
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        public void setFlag(boolean flag) {// 设置循环标记位
            this.flag = flag;
        }

        @Override
        public void run() {
            if(zombies.size()<=0){
                activity.myHandler.sendEmptyMessage(5);
                Log.v("Message", "55555555555555");
            }
            Canvas c;
            while (this.flag) {
                c = null;
                try {
                    // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {
                        gameView.onDraw(c);// 绘制
                    }
                } finally {
                    if (c != null) {
                        // 更新屏幕显示内容
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                    if(zombies.size()<=0){
                        activity.myHandler.sendEmptyMessage(5);
                        Log.v("Message", "55555555555555");
                    }
                }
                try {
                    Thread.sleep(sleepSpan);// 睡眠指定毫秒数
                } catch (Exception e) {
                    e.printStackTrace();// 打印堆栈信息
                }
            }
        }
    }

}
