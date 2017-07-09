package com.mob.mmsge;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mob.MobSDK;

import java.util.HashMap;
import java.util.UUID;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button sendmsg;

    EventHandler eventHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SMSSDK.setAskPermisionOnReadContact();
        MobSDK.init(this,"1ee397bf2fe3c","3e62f7d57af0599b9eb6bc4f47a19bc0");

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
        //初始化控件
        init();

    }
    public void init(){
        sendmsg= (Button) findViewById(R.id.sendmsg);

        sendmsg.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.sendmsg:
                RegisterPage registerPage=new RegisterPage();
                registerPage.setRegisterCallback(eventHandler);
                // 创建EventHandler对象
                eventHandler = new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                       if(result == SMSSDK.RESULT_COMPLETE){
                           HashMap<String , Object> maps= (HashMap<String, Object>) data;
                           String country = (String) maps.get("country");
                           String phone = (String) maps.get("phone");

                           submitUserInfo(country ,phone);
                       }
                    }
                };
                registerPage.show(MainActivity.this);
                break;
        }
    }

    public void submitUserInfo(String country ,String phone){
        String niname="info";
        //用户ID   昵称  null  国家信息  手机号
        SMSSDK.submitUserInfo(UUID.randomUUID().toString(),niname,null,country ,phone);
    }


    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }


}
