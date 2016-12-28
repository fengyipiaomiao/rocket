
package com.example.rocket;

import android.R.anim;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.SystemClock;
import android.test.suitebuilder.annotation.Smoke;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName:RocketView <br/>
 * Function: 小火箭. <br/>
 * Date: 2016年12月13日 下午1:08:57 <br/>
 * 
 * @author Administrator
 * @version
 */
public class RocketView implements OnTouchListener {

    private Context context;
    private WindowManager wManager;
    private WindowManager.LayoutParams paramsRocket = new WindowManager.LayoutParams();
    private WindowManager.LayoutParams paramsBottom = new WindowManager.LayoutParams();
    private float startX;
    private float startY;
    private ImageView ivRocket;
    private ImageView ivBottom;
    private int width;
    private int height;

    public RocketView(Context context) {
        super();
        this.context = context;
        wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        paramsRocket.width = WindowManager.LayoutParams.WRAP_CONTENT;
        paramsRocket.height = WindowManager.LayoutParams.WRAP_CONTENT;
        paramsRocket.gravity = Gravity.CENTER_VERTICAL;
        paramsRocket.gravity = Gravity.RIGHT;

        // params.type = WindowManager.LayoutParams.TYPE_TOAST;//该类型无法拖动
        paramsRocket.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

        paramsRocket.format = PixelFormat.TRANSLUCENT;// 图片样式透明
        paramsRocket.setTitle("Toast");

        paramsRocket.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | // 保持屏幕常亮
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 禁止获取焦点
        
        //****************************底部的控件***********************************
        
        paramsBottom.width = WindowManager.LayoutParams.WRAP_CONTENT;
        paramsBottom.height = WindowManager.LayoutParams.WRAP_CONTENT;
        paramsBottom.gravity = Gravity.CENTER_HORIZONTAL;
        paramsBottom.gravity = Gravity.BOTTOM;

        //不能与上面的相同,否则后定义的控件会挡住先定义的控件,而Toast的优先级比PHINE低
         paramsBottom.type = WindowManager.LayoutParams.TYPE_TOAST;//该类型无法拖动
//        paramsBottom.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
         

        paramsBottom.format = PixelFormat.TRANSLUCENT;// 图片样式透明,不设置就是黑色
        paramsBottom.setTitle("Toast");

        paramsBottom.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | // 保持屏幕常亮
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 禁止获取焦点

    }

    // 显示火箭
    public void showRocket() {

        if (ivRocket == null) {
            // 加载布局
            ivRocket = (ImageView) View.inflate(context, R.layout.rocket_view, null);
            // 设置内容
            ivRocket.setImageResource(R.drawable.desktop_bg_2);
            // 设置监听器
            ivRocket.setOnTouchListener(this);
        }
        // 将view加载到窗口管理器
        wManager.addView(ivRocket, paramsRocket);
    }

    // 隐藏火箭
    public void hideRocket() {
        if (ivRocket != null) {
            if (ivRocket.getParent() != null) {
                wManager.removeView(ivRocket);
            }
            ivRocket = null;
        }
    }
    
    // 显示底部图片
    public void showBottom() {

        if (ivBottom == null) {
            // 创建一个ImageView控件
            ivBottom = new ImageView(context);
            // 播放动画
            buttonAnim();
        }
        // 将view加载到窗口管理器
        wManager.addView(ivBottom, paramsBottom);
    }
    
    //底部的动画
    private void buttonAnim() {
        //创建帧动画对象
        AnimationDrawable drawable = new AnimationDrawable();
        //添加2帧动画
        drawable.addFrame(context.getResources().getDrawable(
                R.drawable.desktop_bg_tips_1),200);
        drawable.addFrame(context.getResources().getDrawable(
                R.drawable.desktop_bg_tips_2),200);
        drawable.setOneShot(false);//非单次模式(一直循环)
        //向控件添加动画
        ivBottom.setImageDrawable(drawable);
        //开始播放动画
        drawable.start();
    }

    // 隐藏底部图片
    public void hideBottom() {
        if (ivBottom != null) {
            if (ivBottom.getParent() != null) {
                wManager.removeView(ivBottom);
            }
            ivBottom = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();// 获取事件的action
        if (action == MotionEvent.ACTION_DOWN) {// 按下事件
            startX = event.getRawX();
            startY = event.getRawY();
            // 播放火箭图片动画
            rocketAnim();
            //播放底部图片动画
            showBottom();
        } else if (action == MotionEvent.ACTION_MOVE) {// 移动事件
            //判断此时火箭位置
            if (isRocketInBottom()) {
              //更改底部图片
                ivBottom.setImageResource(R.drawable.desktop_bg_tips_3);
            }else {
                //播放底部图片动画
                buttonAnim();
            }
            //移动火箭控件
            moveRocket(event);
        } else if (action == MotionEvent.ACTION_UP) {// 抬起
            //判断火箭是否在底部图范围内
            if (isRocketInBottom()) {
                //火箭移动到底部图片的中心
                moveCenter();
                //冒烟的动画
                Smoke();
                //发射火箭动画
                launcherRocket();
                System.out.println("发射火箭");
                
            }else {
                //回归原位
                paramsRocket.x = 0;
                //恢复图像
                ivRocket.setImageResource(R.drawable.desktop_bg_2);
                wManager.updateViewLayout(ivRocket, paramsRocket);
            }
            
            //隐藏底部图片
            hideBottom();
            
        }
        return true;
    }
    
    //火箭移动到底部图片的中心
    private void moveCenter() {
        System.out.println("火箭移动到底部图片的中心");
        //获取屏幕的宽高
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        width = display.widthPixels;
        height = display.heightPixels;
        //改变火箭位置
        paramsRocket.x = width/2 - ivRocket.getWidth()/2;
        paramsRocket.y = height - ivRocket.getHeight();
        //更新视图
        wManager.updateViewLayout(ivRocket, paramsRocket);
     
    }


    
    //冒烟的动画
    private void Smoke() {
        Intent intent = new Intent(context,SmokeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //发射火箭
    private void launcherRocket() {
        //获取火箭的起点终点Y坐标
        int startY = paramsRocket.y;
        int endY = -height/2;
        // 不参加动画的执行,只负责动画值的变化
        ValueAnimator animator = ValueAnimator.ofInt(startY,endY);
        animator.setDuration(500);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 动画执行过程中当前变化的值
                int animatedValue = (Integer) animation.getAnimatedValue();

                paramsRocket.y = animatedValue;
                wManager.updateViewLayout(ivRocket, paramsRocket);
            }
        });
        animator.addListener(new AnimatorListener() {
            
            @Override
            public void onAnimationStart(Animator animation) {}
            
            @Override
            public void onAnimationRepeat(Animator animation) {}
            
            @Override
            public void onAnimationEnd(Animator animation) {
                System.out.println("end");
                //回归原位
                paramsRocket.x = 0;
                paramsRocket.y = 0;
                //恢复图像
                ivRocket.setImageResource(R.drawable.desktop_bg_2);
                wManager.updateViewLayout(ivRocket, paramsRocket);
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {}
        });
        animator.start();//开启动画
        
    }

    //移动控件
    private void moveRocket(MotionEvent event) {
        // 获取当前坐标
        float currentX = event.getRawX();
        float currentY = event.getRawY();
        // 相减得到偏移量
        float moveX = currentX - startX;
        float moveY = currentY - startY;
        // 根据偏移量移动控件
        paramsRocket.x -= moveX;
        //System.out.println("moveX=" + moveX + ",params.x=" + paramsRocket.x);
        paramsRocket.y += moveY;
        // 更新视图
        wManager.updateViewLayout(ivRocket, paramsRocket);
        // 重置起点坐标
        startX = currentX;
        startY = currentY;
    }

    // 火箭的动画
    private void rocketAnim() {
        //创建帧动画对象
        AnimationDrawable drawable = new AnimationDrawable();
        //添加2帧动画
        drawable.addFrame(context.getResources().getDrawable(
                R.drawable.desktop_rocket_launch_1),200);
        drawable.addFrame(context.getResources().getDrawable(
                R.drawable.desktop_rocket_launch_2),200);
        drawable.setOneShot(false);//非单次模式(一直循环)
        //向控件添加动画
        ivRocket.setImageDrawable(drawable);
        //开始播放动画
        drawable.start();
    }
    
    //判断火箭是否在底部图片的区域内
    private boolean isRocketInBottom(){
        //获取当前火箭中心点的位置
        int[] locationRocket = new int[2];
        ivRocket.getLocationOnScreen(locationRocket);//获取一个控件原点在屏幕上的坐标
        float centerX = locationRocket[0] + ivRocket.getWidth()/2;
        float centerY = locationRocket[1] + ivRocket.getHeight()/2;
        //获取底部图片的左上角坐标
        int[] locationBottom = new int[2];
        ivBottom.getLocationOnScreen(locationBottom);//获取一个控件原点在屏幕上的坐标
        float bottomX = locationBottom[0];
        float bottomY = locationBottom[1];
        //判断X坐标是否在范围内
        boolean isInX = false;
        if (centerX < (bottomX + ivBottom.getWidth()) && 
                centerX > bottomX) {
            isInX = true;
        }
        //判断Y坐标是否在范围内
        boolean isInY = false;
        if (centerY > bottomY){
            isInY = true;
        }
        
        //当两个坐标都在范围内,火箭图标在底部图片内
        return isInX & isInY;
    }

}
