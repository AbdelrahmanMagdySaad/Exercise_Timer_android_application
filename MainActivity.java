package com.example.abdelrahman.timertry;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import android.widget.NumberPicker;

import static android.media.AudioManager.FLAG_SHOW_UI;
import static com.example.abdelrahman.timertry.R.drawable.circlework;
import static com.example.abdelrahman.timertry.R.drawable.linework;

public class MainActivity extends AppCompatActivity    {


    Button start,stop;
    TextView etnum,tvwork;
    ProgressBar pbtime,pbtot,line;
    long init,now,elapsedtime,time,nowt,elapsedtimet,timet,paused,percent,percenttot,p,totaltime,shift;
    TextView display,tvset,tvstate;
    boolean firsttime,resumed,pause,finish,three,two,one,stopped;
    Handler handler;
    int counterdet,value,valuet,max,count,minwork,secwork,minrest,secrest,sets,totcounter;
    Runnable updater;
    private ObjectAnimator progressAnimator;
    NumberPicker nump;
    String state;
    String timeformatmin,timeformatmin2;
    int workdraw;
    MediaPlayer mysong,mysongrest,songbtn,songgo,songrest;
    Boolean done3,done2,done1,release,startfullbar;
    ToneGenerator tg;
    AudioManager amanager;
    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint({"ObjectAnimatorBinding", "ResourceAsColor"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        amanager=(AudioManager)getSystemService(MainActivity.AUDIO_SERVICE);
       // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager);
        //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
        //mp.start();
        tg  = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        release=false;
        stopped=true;
        resumed=true;
        done3=false;
        done2= false;
        done1 =false;
        setContentView(R.layout.activity_main);
        mysong = MediaPlayer.create(this,R.raw.count321);
        mysongrest = MediaPlayer.create(this,R.raw.count321);
       // songbtn = MediaPlayer.create(this,R.raw.buttonclick);
        songgo = MediaPlayer.create(this,R.raw.go);
        songrest = MediaPlayer.create(this,R.raw.rest);

        display = (TextView) findViewById(R.id.display);
        workdraw = R.drawable.circlework;
        tvwork = findViewById(R.id.tvwork);
        pbtime = (ProgressBar) findViewById(R.id.pbtime);
        pbtot = (ProgressBar) findViewById(R.id.total);
        line = (ProgressBar) findViewById(R.id.linebar);
        tvstate = findViewById(R.id.state);
        tvset    = findViewById(R.id.tvsets);
       // nump = findViewById(R.id.np);
       // nump.setMaxValue(60);
       // nump.setMinValue(1);
       // nump.setWrapSelectorWheel(true);
        sets = getIntent().getIntExtra("setcounter", 1);
        minwork = getIntent().getIntExtra("workcountermin", 0);
        secwork = getIntent().getIntExtra("workcountersec", 1);
        minrest = getIntent().getIntExtra("restcountermin", 0);
        secrest = getIntent().getIntExtra("restcountersec", 1);

        max=10000;
        totcounter=0;
        pbtime.setMax(max);
        pbtot.setMax(max);
        display.setTextColor(Color.WHITE);
        tvwork.setTextColor(Color.WHITE);
        tvset.setTextColor(Color.WHITE);
        tvstate.setTextColor(Color.WHITE);
        counterdet=0;
        finish=false;
        value=0;
        valuet=0;
        count=0;
        totaltime =(sets*(minwork*60+secwork)+(sets-1)*(minrest*60+secrest))*1000;
        state = "ready";

        three=false;
        two= false;
        one=false;
        tvwork.setText("00:03");
        startfullbar=false;
        long t=3000;
        percenttot= sets*((minwork*60+secwork)*1000)+(sets-1)*(minrest*60+secrest)*1000+t;
        final ToggleButton passTog = (ToggleButton) findViewById(R.id.onoff);
         final Runnable updater = new Runnable() {
            @Override
            public void run() {
                if (passTog.isChecked()) {
                    passTog.setBackgroundColor(R.color.darkindigo);

                    if(!startfullbar)
                    {
                        elapsedtimet=System.currentTimeMillis()+percenttot;
                        nowt = System.currentTimeMillis();
                        timet=elapsedtimet-nowt;
                        pbtot.clearAnimation();
                        ProgressBarAnimation anim2 = new ProgressBarAnimation(pbtot, 0, (float)(max));
                        anim2.setDuration(percenttot);
                        anim2.setInterpolator(new LinearInterpolator());
                        pbtot.startAnimation(anim2);
                        startfullbar=true;

                    }

                        if(firsttime)
                        {
                            elapsedtime=System.currentTimeMillis()+percent;
                            now = System.currentTimeMillis();
                            time=elapsedtime-now;
                            counterdet++;
                            firsttime=false;
                            three=false;
                            if(state.equals("work")) {
                               // songgo.start();
                                pbtime.setProgressDrawable(getResources().getDrawable(R.drawable.circlework));

                               // line.setProgressDrawable(getResources().getDrawable(R.drawable.linework));
                                mysong = MediaPlayer.create(MainActivity.this,R.raw.count321);
                                mysongrest = MediaPlayer.create(MainActivity.this,R.raw.count321);

                                ProgressBarAnimation anim2 = new ProgressBarAnimation(pbtot, shift, (float)(max));
                                anim2.setDuration(sets*((minwork*60+secwork)*1000)+(sets-1)*(minrest*60+secrest)*1000-40);
                                anim2.setInterpolator(new LinearInterpolator());
                                pbtot.startAnimation(anim2);


                            }
                            else if(state.equals("rest"))
                            {

                                pbtime.setProgressDrawable(getResources().getDrawable(R.drawable.circlerest));

                                ProgressBarAnimation anim2 = new ProgressBarAnimation(pbtot, shift, (float)(max));
                                anim2.setDuration(sets*((minwork*60+secwork)*1000)+(sets)*(minrest*60+secrest)*1000-40);
                                anim2.setInterpolator(new LinearInterpolator());
                                pbtot.startAnimation(anim2);

                               // line.setProgressDrawable(getResources().getDrawable(R.drawable.linerest));
                            }
                            else
                            {

                                pbtime.setProgressDrawable(getResources().getDrawable(R.drawable.circleready));
                            }


                           // line.setVisibility(View.VISIBLE);


                            ProgressBarAnimation anim = new ProgressBarAnimation(pbtime, 0, (float)max);
                            long minus=0;
                            if(state.equals("work"))
                                minus=50;
                            else
                                minus=0;
                            anim.setDuration(percent-minus);
                            anim.setInterpolator(new LinearInterpolator());
                            pbtime.startAnimation(anim);

                            tvstate.setText(state);
                            tvset.setText(""+sets);
                            pbtime.setVisibility(View.VISIBLE);


                        }
                        else if(pause)
                        {
                            now = System.currentTimeMillis();
                            elapsedtime += System.currentTimeMillis() - paused;
                            time=elapsedtime-now;
                            nowt = System.currentTimeMillis();
                            elapsedtimet += System.currentTimeMillis() - paused;
                            if(state.equals("work"))
                            {
                                timet=sets*time+(sets-1)*(minrest*60+secrest)*1000;
                            }
                            else if(state.equals("rest"))
                            {
                                timet=sets*time+(sets)*(minwork*60+secwork)*1000;
                            }
                            else
                            {

                                timet=totaltime+time;
                            }
                            pause=false;
                            three=false;
                            ProgressBarAnimation anim = new ProgressBarAnimation(pbtime, value, (float)max);
                            anim.setDuration(time);
                            anim.setInterpolator(new LinearInterpolator());
                            pbtime.startAnimation(anim);

                            ProgressBarAnimation anim2 = new ProgressBarAnimation(pbtot, valuet, (float)max);
                            anim2.setDuration(timet);
                            anim2.setInterpolator(new LinearInterpolator());
                            pbtot.startAnimation(anim2);

                            mysong = MediaPlayer.create(MainActivity.this,R.raw.count321);
                            mysongrest = MediaPlayer.create(MainActivity.this,R.raw.count321);
                            // passTog.setBackgroundResource(R.drawable.pause);
                            display.setTextColor(Color.WHITE);
                            tvwork.setTextColor(Color.WHITE);
                            tvset.setTextColor(Color.WHITE);
                            tvstate.setTextColor(Color.WHITE);
                        }
                        else
                        {
                            now = System.currentTimeMillis();
                            time=elapsedtime-now;

                            if(state.equals("work"))
                            {
                                timet=(sets-1)*((minwork*60+secwork)*1000)+(sets-1)*(minrest*60+secrest)*1000+time;
                            }
                            else if(state.equals("rest"))
                            {
                                timet=sets*((minwork*60+secwork)*1000)+(sets-1)*(minrest*60+secrest)*1000+time;
                            }
                            else
                            {

                                timet=totaltime+time;
                            }

                        }

                    if(time<=15)
                    {
                        //handler.removeCallbacks(this);
                        counterdet=0;
                        finish=false;
                        firsttime=true;
                        boolean b=false;
                        three=false;
                        two=false;
                        one=false;
                        done3=false;
                        done2=false;
                        done1=false;
                        shift=pbtot.getProgress();
                        pbtot.clearAnimation();
                        handler.removeCallbacks(this);
                        if(counterdet==0)
                        {   firsttime=true;
                            //percent=nump.getValue()*1000;

                           b = decide();
                           if(!b)
                           {
                               passTog.setVisibility(View.INVISIBLE);


                           }
                           else
                           {
                               if(state.equals("rest")&&!release)
                                   tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                               else if(!release)
                               tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
                           }


                        }
                        if(!finish&& this!=null&&b)
                            handler.post(this);
                        else
                        {
                            firsttime=true;
                            finish=false;
                            display.setTextColor(Color.WHITE);
                            tvwork.setTextColor(Color.WHITE);
                        }
                        //pbtime.setVisibility(View.INVISIBLE);


                    }

                    else
                    {
                        long time2=time+999;
                        long t =(timet-((int)(timet/1000)*1000))/10;
                        int min = (int)((timet/1000)/60);
                        int hours = min/60;

                        long sec = (int)(timet/1000)-min*60;
                        min=min-hours*60;
                        int min2 = (int)((time2/1000)/60);
                        long sec2 = (int)(time2/1000)-min2*60;
                        if(sec2==3&&min2==0)
                        {
                            if(!done3&&!release)
                            {
                                done3 = true;
                                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            }

                        }

                        if(!done2&&sec2==2&&min2==0&&!release)
                        {

                                done2 = true;
                                tg.startTone(ToneGenerator.TONE_PROP_BEEP);


                        }
                        if(!done1&&sec2==1&&min2==0&&!release)
                        {

                            done1 = true;
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);


                        }




                        if(hours==0)
                            timeformatmin = String.format(Locale.getDefault(), "%02d:%02d.%02d", min, sec, t);
                        else if(hours<10)
                            timeformatmin = String.format(Locale.getDefault(), "%01d:%02d:%02d.%02d", hours,min, sec, t);
                        else
                            timeformatmin = String.format(Locale.getDefault(), "%02d:%02d:%02d.%02d", hours,min, sec, t);
                        timeformatmin2 = String.format(Locale.getDefault(), "%02d:%02d", min2,sec2);
                        handler.postDelayed(this, 30);
                        tvwork.setText(timeformatmin2);
                        display.setText(timeformatmin);


                    }
                    }
                else
                {
                    if(time>100) {
                        display.setTextColor(Color.RED);
                        tvwork.setTextColor(Color.RED);
                        tvset.setTextColor(Color.RED);
                        tvstate.setTextColor(Color.RED);
                        passTog.setBackgroundColor(Color.RED);
                        pbtime.clearAnimation();
                        pbtot.clearAnimation();
                        value = pbtime.getProgress();
                        valuet = pbtot.getProgress();
                        long time2 = time+999 ;

                        long t = (timet - ((int) (timet / 1000) * 1000)) / 10;
                        int min = (int) ((timet / 1000) / 60);
                        int hours = min/60;

                        long sec = (int) (timet/ 1000) - min * 60;
                        min=min-hours*60;
                        int min2 = (int) ((time2 / 1000) / 60);
                        long sec2 = (int) (time2 / 1000) - min2 * 60;
                        if(hours==0)
                            timeformatmin = String.format(Locale.getDefault(), "%02d:%02d.%02d", min, sec, t);
                        else if(hours<10)
                            timeformatmin = String.format(Locale.getDefault(), "%01d:%02d:%02d.%02d", hours,min, sec, t);
                        else
                            timeformatmin = String.format(Locale.getDefault(), "%02d:%02d:%02d.%02d", hours,min, sec, t);
                        timeformatmin2 = String.format(Locale.getDefault(), "%02d:%02d", min2, sec2);
                        tvwork.setText(timeformatmin2);
                        display.setText(timeformatmin);
                        pause = true;



                        paused = System.currentTimeMillis();
                    }

                }
            }

        };
        passTog.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
             //   songbtn.start();
                if(counterdet==0)
                {   firsttime=true;
                    //percent=nump.getValue()*1000;
                    percent=3*1000;





                }
                if(!finish&& updater!=null)
                     handler.post(updater);
                else
                {
                    firsttime=true;
                    finish=false;
                    display.setTextColor(Color.WHITE);
                    tvwork.setTextColor(Color.WHITE);
                    tvset.setTextColor(Color.WHITE);
                    tvstate.setTextColor(Color.WHITE);
                }
            }
        });
    }

    @Override


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            handler.removeCallbacks(updater);
            mysong.stop();
            mysongrest.stop();
            tg.stopTone();

            release=true;
        }
        return super.onKeyDown(keyCode, event);

    }
    @Override
    protected void onPause() {
        super.onPause();
        paused = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //init += System.currentTimeMillis() - paused;
    }

    protected  void go()
    {
        handler.post(updater);

    }

    protected boolean decide()
    {
        if(state.equals("ready"))
        {
            state = "work";
            {
                percent=(minwork*60+secwork)*1000;
                p=percent/max;

            }
        }
        else if(state.equals("work"))
        {
            if (sets==1)
            {
                tvwork.setText("finish");
                display.setText("finish");
                //tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                pbtot.setProgress(max);

                if(!release)
                tg.startTone(ToneGenerator.TONE_PROP_ACK);
               // tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                tvset.setVisibility(View.INVISIBLE);
                tvstate.setVisibility(View.INVISIBLE);
                mysong.stop();
                return false;

            }
            else if(minrest>0||secrest>0)
            {
                sets--;
                state = "rest";
                percent=(minrest*60+secrest)*1000;
                p=percent/max;

            }
            else
            {
                state = "work";
                {
                    percent=(minwork*60+secwork)*1000;
                    p=percent/max;
                    sets--;

                }
            }
        }
        else //rest
        {
            state = "work";
            percent=(minwork*60+secwork)*1000;
            p=percent/max;

        }
        tvstate.setText(state);
        tvset.setText(""+sets);
        return true;

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    amanager.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    amanager.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}