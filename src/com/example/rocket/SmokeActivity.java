package com.example.rocket;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class SmokeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke);
        
        ImageView ivBottom = (ImageView) findViewById(R.id.smoke_bottom);
        ImageView ivMiddle = (ImageView) findViewById(R.id.smoke_middle);
        
        //冒烟的动画-透明度
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(ivBottom, "alpha", 1.0f,0.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ivMiddle, "alpha", 1.0f,0.0f);
        
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim1,anim2);//一起播放
        set.setDuration(1000);//动画时长
        //设置监听器,播放完销毁界面
        set.addListener(new AnimatorListener() {
            
            @Override
            public void onAnimationStart(Animator animation) {}
            
            @Override
            public void onAnimationRepeat(Animator animation) {}
            
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {}
        });
        set.start();//开始动画
    }
}
