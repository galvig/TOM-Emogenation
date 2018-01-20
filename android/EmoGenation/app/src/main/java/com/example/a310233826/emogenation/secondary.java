package com.example.a310233826.emogenation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class secondary extends MainActivity {

    HashMap<ImageButton, Emotions> emotionsSet = new HashMap();


    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    public static boolean isBlueToothEstablished = false;

    public enum Emotions
    {
        Love,
        Pain,
        Anger,
        Sad,
        Happy
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        HideStatusBar();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        HideStatusBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Creation",  " no bluetooth established1");
        HideStatusBar();

        setContentView(R.layout.activity_secondary);

        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent page = new Intent(secondary.this, MainActivity.class);
                MainActivity.currentMode = ActiveMode.MainScreen;
                startActivity(page);
            }
        });

        List<ImageButton> emotionButtonsLst = GetEmotionButtons();


        ConstraintLayout layout = findViewById(R.id.secondary_activity_id);
        if (currentMode == ActiveMode.LearningScreen)
        {
            layout.setBackgroundResource(R.drawable.background_learning);
            InitEmotionToButton(emotionsSet, emotionButtonsLst, false);
            SetButtonImg(emotionsSet);

        }
        else if (currentMode == ActiveMode.TrainingScreen)
        {
            layout.setBackgroundResource(R.drawable.background_training);
            InitEmotionToButton(emotionsSet, emotionButtonsLst, true);
            SetButtonImg(emotionsSet);
        }

        int btnIdx = 0;
        for (ImageButton btn: emotionsSet.keySet()){
            setOnClick(btn, emotionsSet.get(btn));
        }



        try
        {
            if (!isBlueToothEstablished) {
                findBT();
                openBT();
                isBlueToothEstablished = true;

                Log.d("Creation", "bluetooth established");
                for (int i = 0; i < 5; ++i) {
                        try {

                            sendData( Integer.toString(i) + "G");
                            int index = 0;
                            while(index++ < 10000000);
                            sendData( Integer.toString(i) + "R");
                            index = 0;
                            while(index++ < 10000000);
                            sendData( Integer.toString(i) + "0");
                            index = 0;
                            while(index++ < 10000000);

                        } catch (IOException ex) {
                    }
                }
            }
        }
        catch (IOException ex) { }
    }

    private void setOnClick(final ImageButton btn, final Emotions emotion){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoView vv = findViewById(R.id.videoView);
                List<Uri> fileLoveLst = GetFileList(emotion);
                Utils.ShuffleList(fileLoveLst);
                vv.setVideoURI(fileLoveLst.get(0));
                vv.start();
            }
        });
    }

    private void InitEmotionToButton(HashMap<ImageButton, Emotions> emotionsSet, List<ImageButton> emotionButtonsLst, boolean randomize) {
        if (randomize)
            Utils.ShuffleList(emotionButtonsLst);

        int btnIdx = 0;
        for (ImageButton btn: emotionButtonsLst) {
            emotionsSet.put(btn, Emotions.values()[btnIdx++]);
        }
    }

    private void SetButtonImg(HashMap<ImageButton, Emotions> emotionsSet) {

        for (ImageButton btn: emotionsSet.keySet()) {

            Emotions emotion = emotionsSet.get(btn);

            switch (emotion) {
                case Love:
                    btn.setBackgroundResource(R.drawable.love_s);
                    break;
                case Pain:
                    btn.setBackgroundResource(R.drawable.pain_s);
                    break;
                case Anger:
                    btn.setBackgroundResource(R.drawable.anger_s);
                    break;
                case Sad:
                    btn.setBackgroundResource(R.drawable.sorrow_s);
                    break;
                case Happy:
                    btn.setBackgroundResource(R.drawable.happiness_s);
            }
        }
    }

    private Drawable getEmmotionImagePerIndex(int index){
        return getResources().getDrawable(R.drawable.close_btn, getTheme());
    }

    private List<ImageButton> GetEmotionButtons()
    {
        List<ImageButton> emotionButtonsLst = new ArrayList<ImageButton>();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.EmotionBtns);
        int buttonsCnt = linearLayout.getChildCount();
        for (int i=0; i<buttonsCnt; i++)
        {
            View v = linearLayout.getChildAt(i);
            if (v instanceof ImageButton)
                emotionButtonsLst.add((ImageButton)v);
        }

        return emotionButtonsLst;
    }

    public List<Uri> GetFileList(Emotions emotion){

        List<Uri> retPaths = new ArrayList<Uri>();

        switch (emotion) {
            case Love:
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.love1));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.love2));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.love3));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.love4));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.love5));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.love6));
                break;
            case Pain:
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pain1));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pain2));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pain3));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pain4));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pain5));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pain6));
                break;
            case Anger:
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger1));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger2));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger3));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger4));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger5));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger6));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger7));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.anger8));
                break;
            case Sad:
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sad1));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sad2));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sad3));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sad4));
                break;
            case Happy:
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happy1));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happy2));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happy3));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happy4));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happy5));
                retPaths.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happy6));
                break;
        }

        return retPaths;
    }

    public void onPostButtonClick(String data){

        Integer btnIdx = Integer.parseInt(data);

        for (ImageButton btn : emotionsSet.keySet()) {
            if (emotionsSet.get(btn).equals(Emotions.values()[btnIdx])) {
                btn.callOnClick();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        isBlueToothEstablished = false;
    }

    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                String deviceName = device.getName();
                if(deviceName.equals("HC-06")) //Todo:
                {
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {

                                            //((secondary ).onPostButtonClick(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    void sendData(String msg) throws IOException
    {
//        ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());
//        bb.order( ByteOrder.LITTLE_ENDIAN);
//        msg += "\n";
        mmOutputStream.write(msg.getBytes());
    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
    }
}
