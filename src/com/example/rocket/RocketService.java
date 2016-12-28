  
package com.example.rocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/** 
 * ClassName:RocketService <br/> 
 * Function: 小火箭的服务. <br/> 
 * Date:     2016年12月13日 下午4:02:52 <br/> 
 * @author   Administrator 
 * @version       
 */
public class RocketService extends Service {

    private RocketView rocketView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("火箭服务启动");
        rocketView = new RocketView(this);
        rocketView.showRocket();
        System.out.println("火箭原始图标显示");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //隐藏小火箭
        rocketView.hideRocket();
    }

}
  