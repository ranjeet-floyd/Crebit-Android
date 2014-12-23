package in.crebit.app.WebView.crebit.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import in.crebit.app.WebView.R;
import in.crebit.app.WebView.crebit.loginpage.LoginActivity;


public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        final Thread splashScreen=new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent openLoginPage=new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(openLoginPage);
                    finish();
                }
            }

        };
        splashScreen.start();
    }



}
