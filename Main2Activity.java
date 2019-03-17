package com.example.abdelrahman.timertry;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main2Activity extends AppCompatActivity {

    NumberPicker npset,npworkmin,npworksec,nprestmin,nprestsec;
    Button btngo;
    MediaPlayer songbtn;
    boolean funny;
    AudioManager amanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        npset = findViewById(R.id.npset);
        npworkmin = findViewById(R.id.workmin);
        npworksec = findViewById(R.id.worksec);
        nprestmin  =findViewById(R.id.restmin);
        nprestsec = findViewById(R.id.restsec);
        btngo = findViewById(R.id.btngo);
        funny = false;
        npset.setMaxValue(20);
        npset.setMinValue(1);
        amanager=(AudioManager)getSystemService(MainActivity.AUDIO_SERVICE);
        npworkmin.setMaxValue(59);
        npworkmin.setMinValue(0);
        final String[] nums = new String[12];

        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i*5);

        npworksec.setMaxValue(nums.length-1);
        npworksec.setMinValue(0);
        npworksec.setWrapSelectorWheel(true);
        npworksec.setDisplayedValues(nums);


        nprestmin.setMaxValue(59);
        nprestmin.setMinValue(0);

        nprestsec.setMaxValue(nums.length-1);
        nprestsec.setMinValue(0);
        nprestsec.setWrapSelectorWheel(true);
        nprestsec.setDisplayedValues(nums);




        songbtn = MediaPlayer.create(this,R.raw.buttonclick);


        read();
        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(npworksec.getValue()==0&& npworkmin.getValue()==0)
                {
                    Toast.makeText(Main2Activity.this, "Be a Man! and Enter work time ", Toast.LENGTH_SHORT).show();
                    funny=true;
                }
                else {
                    if(funny)
                    {
                        Toast.makeText(Main2Activity.this, "Thank You , Get ready to real Work", Toast.LENGTH_SHORT).show();
                        funny =false;
                    }


                    int number1= Integer.parseInt(nums[npworksec.getValue()]);
                    int number2= Integer.parseInt(nums[nprestsec.getValue()]);
                    Intent intent = new Intent(Main2Activity.this, com.example.abdelrahman.timertry.MainActivity.class);
                    intent.putExtra("setcounter", npset.getValue());
                    intent.putExtra("workcountermin", npworkmin.getValue());
                    intent.putExtra("workcountersec", number1);
                    intent.putExtra("restcountermin", nprestmin.getValue());
                    intent.putExtra("restcountersec", number2);

                    write();
                    startActivity(intent);
                }
            }
        });

    }


    public void read() {
        try {
            FileInputStream fileInputStream= openFileInput("xtimer.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                stringBuffer.append(lines+"\n");
            }
            String Mytextmessage =stringBuffer.toString();
            String ets=Mytextmessage.substring(0,2);
            String etminw=Mytextmessage.substring(2,4);
            String etsecw=Mytextmessage.substring(4,6);
            String etminr=Mytextmessage.substring(6,8);
            String etsecr=Mytextmessage.substring(8,10);
            if(ets.substring(0,1).equals("0"))
            {
                ets=ets.substring(1,2);
            }
            npset.setValue(Integer.parseInt(ets));
            npworkmin.setValue(Integer.parseInt(etminw));
            npworksec.setValue((Integer.parseInt(etsecw)/5));
            nprestmin.setValue(Integer.parseInt(etminr));
            nprestsec.setValue((Integer.parseInt(etsecr)/5));



            //textView.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            npset.setValue(1);
            nprestmin.setValue(0);
            nprestsec.setValue(0);

            npworksec.setValue(1);
            npworkmin.setValue(0);
            e.printStackTrace();
            //default i will write it
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write() {
        String workmin,worksec,restmin,restsec,set;

        if(npset.getValue()<10)
        {
            set="0"+npset.getValue();
        }
        else
        {
            set=""+npset.getValue();
        }

        if(npworkmin.getValue()<10)
        {
            workmin="0"+npworkmin.getValue();
        }
        else
        {
            workmin=""+npworkmin.getValue();
        }


        if(npworksec.getValue()*5<10)
        {
            worksec="0"+npworksec.getValue()*5;
        }
        else
        {
            worksec=""+npworksec.getValue()*5;
        }
        if(nprestmin.getValue()<10)
        {
            restmin="0"+nprestmin.getValue();
        }
        else
        {
            restmin=""+nprestmin.getValue();
        }
        if(nprestsec.getValue()*5<10)
        {
            restsec="0"+nprestsec.getValue()*5;
        }
        else
        {
            restsec=""+nprestsec.getValue()*5;
        }

        String Mytextmessage  =set+workmin+worksec+restmin+restsec;


        try {
            FileOutputStream fileOutputStream = openFileOutput("xtimer.txt",MODE_PRIVATE);
            fileOutputStream.write(Mytextmessage.getBytes());
            fileOutputStream.close();
            // Toast.makeText(getApplicationContext(),"Text Saved",Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
