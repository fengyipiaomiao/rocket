package com.itheima.safeguard;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.itheima.safeguard.db.LocationDAO;

public class QueryNumLocationActivity
  extends Activity
{
  private EditText et;
  private String num;
  private TextView tv;
  
  private void initData() {}
  
  private void initEvent()
  {
    this.et.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable) {}
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        paramAnonymousCharSequence = LocationDAO.getLocation(QueryNumLocationActivity.this, paramAnonymousCharSequence.toString());
        QueryNumLocationActivity.this.tv.setText("归属地:" + paramAnonymousCharSequence);
      }
    });
  }
  
  private void initView()
  {
    this.et = ((EditText)findViewById(2131558471));
    this.tv = ((TextView)findViewById(2131558472));
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903056);
    initView();
    initData();
    initEvent();
  }
  
  public void queryLocation(View paramView)
  {
    this.num = this.et.getText().toString().trim();
    if (TextUtils.isEmpty(this.num))
    {
      paramView = new TranslateAnimation(0.0F, 10.0F, 0.0F, 0.0F);
      paramView.setInterpolator(new CycleInterpolator(7.0F));
      paramView.setDuration(1000L);
      this.et.setAnimation(paramView);
      paramView.start();
      Toast.makeText(this, "号码不能为空", 0).show();
      return;
    }
    paramView = LocationDAO.getLocation(this, this.num);
    this.tv.setText("归属地:" + paramView);
  }

  public void add(){
  	System.out.println("hello");
  }
}
