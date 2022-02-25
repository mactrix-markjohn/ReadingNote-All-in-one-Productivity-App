package com.mactrix.www.readingnotepro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
//import com.mopub.mobileads.MoPubErrorCode;
//import com.mopub.mobileads.MoPubInterstitial;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import org.w3c.dom.Text;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AudioRecorder extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView recordtext;
    ImageView recordImage;
    TextView recordCounter;
    TextView playbackCounter;
    TextView outputText;
    Button discardButton;
    Button saveButton;
    ImageView playbutton;
    SeekBar playseekbar;
    ImageView mainRecordbutton;
   // ImageView pauserecButton;
    ImageView backing;
    ImageView stopButton;
    ImageView stopsignal;

    //BottomSheet Views
    RelativeLayout smallLayout;
    ImageView mainlist;
    ImageView writing;


    RelativeLayout largeLayout;
    Button donewriting;
    EditText readingnote;
    EditText readingtitle;

    BottomSheetBehavior sheetBehavior;

    RecordingStudio recordingStudio;
    PlaybackStudio playbackStudio;
    String path;
    Random random;

    Timer timer;
    Handler handler;
    Handler seekHandler;

    long starttime;
    long stoptime;
    long pausetime;
    long maintime;
    long runtime;
    long id;
    StudyDatabase studyDatabase;
    private CountDownTimer downTimer;
   // private MoPubInterstitial interstitial;
   // private AdView adView;
   // private AdRequest bannerRequest;
   // private AdRequest interRequest;
   // private InterstitialAd interstitialAd;

    enum Stating{PAUSE,RECORD}
    Stating stating;

    int duration;
    int currenttime;

    enum PlayState{PLAY,RESUME}
    PlayState playState;

    enum Storage{SAVE,UPDATE}
    Storage storage;

    RelativeLayout audionote;
    Button cancelbutton;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // StartAppSDK.init(this,"204716271",true);
        //StartAppAd.disableSplash();


        setSupportActionBar(toolbar);
        random = new Random();
        handler = new Handler();
        seekHandler = new Handler();
        registerBroad();
        studyDatabase = new StudyDatabase(this,null,null,1);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File/AudioRecording");
        file.mkdir();

        storage=Storage.SAVE;
        stating = Stating.RECORD;
        playState = PlayState.PLAY;


       // MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

        //adView = (AdView)findViewById(R.id.adView);
        //bannerRequest = new AdRequest.Builder().build();
        //adView.loadAd(bannerRequest);

        //interstitialAd = new InterstitialAd(this);
        //interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/4467270309");
        //interRequest = new AdRequest.Builder().build();


        downTimer = new CountDownTimer(25000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(AudioRecorder.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFinish() {



               /* interstitialAd.loadAd(interRequest);
                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdLoaded(){
                        if(interstitialAd.isLoaded()){
                            interstitialAd.show();
                        }
                    }
                });*/





                /*interstitial = new MoPubInterstitial(AudioRecorder.this,"e26e9286ce3d4b8994a98c22c1066868");
                interstitial.load();

                interstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                    @Override
                    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                        interstitial.show();

                    }

                    @Override
                    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                        // startActivity(new Intent(Musical.this,LovedThings.class));

                    }

                    @Override
                    public void onInterstitialShown(MoPubInterstitial interstitial) {
                           //new Handler().postDelayed(new Runnable() {
                             //   @Override
                              //  public void run() {
                               //     startActivity(new Intent(Musical.this,LovedThings.class));
                               // }
                            //}, 2000);

                    }

                    @Override
                    public void onInterstitialClicked(MoPubInterstitial interstitial) {

                    }

                    @Override
                    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                        // startActivity(new Intent(Musical.this,LovedThings.class));

                    }
                });*/

                   /* AppLovinSdk.initializeSdk(Musical.this);
                    AppLovinSdk.getInstance(Musical.this).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd appLovinAd) {
                            appLovnAd = appLovinAd;

                        }

                        @Override
                        public void failedToReceiveAd(int i) {

                        }
                    });
                    AppLovinInterstitialAdDialog adDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(Musical.this), Musical.this);
                    adDialog.setAdClickListener(new AppLovinAdClickListener() {
                        @Override
                        public void adClicked(AppLovinAd appLovinAd) {

                        }
                    });
                    adDialog.setAdLoadListener(new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd appLovinAd) {
                        }

                        @Override
                        public void failedToReceiveAd(int i) {

                        }
                    });
                    adDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                        @Override
                        public void adDisplayed(AppLovinAd appLovinAd) {

                        }

                        @Override
                        public void adHidden(AppLovinAd appLovinAd) {

                        }
                    });
                    adDialog.showAndRender(appLovnAd);*/

            }

        };
        downTimer.start();









        audionote = (RelativeLayout)findViewById(R.id.audionote);
        cancelbutton = (Button)findViewById(R.id.cancelbutton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audionote.setVisibility(View.INVISIBLE);
            }
        });

        recordingStudio = new RecordingStudio(this);
        playbackStudio = new PlaybackStudio(this);
        backing = (ImageView)findViewById(R.id.backing);
        backing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        recordtext = (TextView)findViewById(R.id.recordtext);
        recordImage = (ImageView) findViewById(R.id.recordimage);
        stopsignal = (ImageView)findViewById(R.id.stopsignal);
        recordCounter = (TextView)findViewById(R.id.recordcounter);
        playbackCounter = (TextView)findViewById(R.id.playbackcounter);
        outputText = (TextView)findViewById(R.id.outputtext);
        discardButton = (Button)findViewById(R.id.discardbutton);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // playbackStudio.stopMedia();


                File file = new File(path);
                file.delete();




                disableSeek();
                disableTimer();


                recordCounter.setVisibility(View.VISIBLE);
                playbackCounter.setVisibility(View.INVISIBLE);

                recordtext.setVisibility(View.INVISIBLE);
                recordImage.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                stopsignal.setVisibility(View.INVISIBLE);
                String count = "00:00";
                recordCounter.setText(count);
                outputText.setVisibility(View.INVISIBLE);
                discardButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                playbutton.setVisibility(View.INVISIBLE);
                playseekbar.setVisibility(View.INVISIBLE);
                //pauserecButton.setVisibility(View.INVISIBLE);
                mainRecordbutton.setVisibility(View.VISIBLE);



            }
        });
        saveButton = (Button)findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              //  playbackStudio.stopMedia();


                disableTimer();
                disableSeek();

                recordCounter.setVisibility(View.VISIBLE);
                playbackCounter.setVisibility(View.INVISIBLE);

                recordtext.setVisibility(View.INVISIBLE);
                recordImage.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                stopsignal.setVisibility(View.INVISIBLE);
                String count = "00:00";
                recordCounter.setText(count);
                outputText.setVisibility(View.INVISIBLE);
                discardButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                playbutton.setVisibility(View.INVISIBLE);
                playseekbar.setVisibility(View.INVISIBLE);
                //pauserecButton.setVisibility(View.INVISIBLE);
                mainRecordbutton.setVisibility(View.VISIBLE);


                Toast.makeText(AudioRecorder.this, "Recording Sucessfully Saved", Toast.LENGTH_SHORT).show();

                recordCounter.setText("00:00");

            }
        });
        playbutton = (ImageView)findViewById(R.id.playbutton);
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recordCounter.setVisibility(View.INVISIBLE);
                playbackCounter.setVisibility(View.VISIBLE);

                if(!playbackStudio.isPlaying()){

                    if(playState==PlayState.PLAY){

                        playbutton.setImageResource(R.drawable.pause_button);
                        stopsignal.setImageResource(R.drawable.play_button);

                        recordCounter.setText("00:00");
                        playbackStudio.playMedia(path);
                        duration=playbackStudio.getMusicDuration();
                        updateSeeker();


                        playState=PlayState.RESUME;

                    }else if(playState==PlayState.RESUME){

                        playbutton.setImageResource(R.drawable.pause_button);
                        stopsignal.setImageResource(R.drawable.play_button);

                        //recordCounter.setText(convertmillsecondstoTimer(0));
                        playbackStudio.resumeMedia();
                        duration=playbackStudio.getMusicDuration();
                        updateSeeker();

                    }






                }else{
                    playbutton.setImageResource(R.drawable.play_button);
                    stopsignal.setImageResource(R.drawable.pause_button);

                    playbackStudio.pauseMedia();
                }



            }
        });
        playseekbar = (SeekBar)findViewById(R.id.playseekbar);
        mainRecordbutton = (ImageView)findViewById(R.id.mainrecordbutton);
        mainRecordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog();

                /*recordCounter.setVisibility(View.VISIBLE);
                playbackCounter.setVisibility(View.INVISIBLE);

                Random random = new Random();
                String[] namelist = {"recordingA","recordingB","recordingC"};


                path =Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File/AudioRecording/"+namelist[random.nextInt(2)]+random.nextInt(100)+".mp3";
                recordingStudio.record(path);
                String filepath = path.substring(path.lastIndexOf("/")+1,path.length());

                stopButton.setVisibility(View.VISIBLE);
                isVersion7();
                recordtext.setVisibility(View.VISIBLE);
                recordImage.setVisibility(View.VISIBLE);

                Date date = new Date();
                starttime = date.getTime();
                updateTimer();

                outputText.setVisibility(View.VISIBLE);
                outputText.setText(filepath);





                stating = Stating.RECORD;
                playState = PlayState.PLAY;
                storage = Storage.SAVE;
                mainRecordbutton.setVisibility(View.INVISIBLE);
                discardButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                playbutton.setVisibility(View.INVISIBLE);
                playseekbar.setVisibility(View.INVISIBLE);*/






            }
        });
        /*pauserecButton = (ImageView)findViewById(R.id.pauserecbutton);
        pauserecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){


                    if(stating == Stating.RECORD){

                        recordingStudio.pause();
                        pauserecButton.setImageResource(R.drawable.recorinding);

                        disableTimer();

                        recordtext.setText("Reading Paused");
                        recordImage.setEnabled(false);


                        stating=Stating.PAUSE;

                        Toast.makeText(AudioRecorder.this, "Recording Paused", Toast.LENGTH_SHORT).show();
                    }else if(stating==Stating.PAUSE){
                        recordingStudio.resume();
                        pauserecButton.setImageResource(R.drawable.pause_button);


                        updateTimer();

                        recordtext.setText("Recording");
                        recordImage.setEnabled(true);




                        stating = Stating.RECORD;

                        Toast.makeText(AudioRecorder.this, "Recording Resumed", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(AudioRecorder.this, "Sorry this feature is not supported by your Device, Please Stop the recording Instead." +
                            "Supported version :- Version 7 and above", Toast.LENGTH_LONG).show();
                }



            }
        });*/
        stopButton = (ImageView)findViewById(R.id.stopbutton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                recordCounter.setVisibility(View.VISIBLE);
                playbackCounter.setVisibility(View.INVISIBLE);

                disableTimer();
                recordingStudio.stop();

                stopsignal.setVisibility(View.VISIBLE);
                discardButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                playbutton.setVisibility(View.VISIBLE);
                playseekbar.setVisibility(View.VISIBLE);
                mainRecordbutton.setVisibility(View.VISIBLE);


                //pauserecButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                recordtext.setVisibility(View.INVISIBLE);
                recordImage.setVisibility(View.INVISIBLE);






            }
        });
        smallLayout = (RelativeLayout)findViewById(R.id.smalllayout);
        mainlist = (ImageView)findViewById(R.id.mainlist);
        mainlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(AudioRecorder.this, com.mactrix.www.readingnotepro.MediaStore.class));


            }
        });
        writing = (ImageView)findViewById(R.id.writing);
        writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AudioRecorder.this,VideoRecorder.class));

                //audionote.setVisibility(View.VISIBLE);
               // smallLayout.setVisibility(View.GONE);
                //largeLayout.setVisibility(View.VISIBLE);
                //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });
        largeLayout = (RelativeLayout)findViewById(R.id.largelayout);
        donewriting = (Button)findViewById(R.id.donewritin);
        donewriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = String.valueOf(readingtitle.getText());
                String note = String.valueOf(readingnote.getText());

                if(storage== Storage.SAVE){

                    id = studyDatabase.addNote(title,note,path);

                    storage=Storage.UPDATE;

                    Toast.makeText(AudioRecorder.this, "Note has been Saved", Toast.LENGTH_LONG).show();
                }else if(storage== Storage.UPDATE){


                        studyDatabase.updateNote(id,title,note,path);


                    Toast.makeText(AudioRecorder.this, "Note has been Updated", Toast.LENGTH_LONG).show();


                }



            }
        });
        readingnote= (EditText)findViewById(R.id.readingnot);
        readingtitle = (EditText)findViewById(R.id.readingtitl);

        View view = findViewById(R.id.bottomsheetaudiostudy);
        sheetBehavior = BottomSheetBehavior.from(view);
        sheetBehavior.setPeekHeight(270);
        sheetBehavior.setHideable(false);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:


                        largeLayout.setVisibility(View.GONE);
                        smallLayout.setVisibility(View.VISIBLE);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        smallLayout.setVisibility(View.GONE);
                        largeLayout.setVisibility(View.VISIBLE);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        break;
                }



            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        playseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                seekHandler.removeCallbacks(runnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                playbackStudio.seekTo(seekBar.getProgress()*1000);
                playseekbar.setProgress(seekBar.getProgress());
                updateSeeker();
            }
        });








        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    public void alertDialog(){
        final EditText editText = new EditText(this);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter name of Video Record");
        builder.setView(editText);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.setPositiveButton("Audio Record", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(recordingStudio.hasMicrophone()){


                    String pathy = String.valueOf(editText.getText());

                    String pathing = Environment.getExternalStorageState()+"/ReadingNote Pro File/Video/"+path+".mp4";

                    recordCounter.setVisibility(View.VISIBLE);
                    playbackCounter.setVisibility(View.INVISIBLE);

                    Random random = new Random();
                    String[] namelist = {"recordingA","recordingB","recordingC"};


                    path =Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File/AudioRecording/"+pathy+".3gpp";

                    recordingStudio.record(path);
                    String filepath = path.substring(path.lastIndexOf("/")+1,path.length());

                    stopButton.setVisibility(View.VISIBLE);
                    isVersion7();
                    recordtext.setVisibility(View.VISIBLE);
                    recordImage.setVisibility(View.VISIBLE);

                    Date date = new Date();
                    starttime = date.getTime();
                    updateTimer();

                    outputText.setVisibility(View.VISIBLE);
                    outputText.setText(filepath);





                    stating = Stating.RECORD;
                    playState = PlayState.PLAY;
                    storage = Storage.SAVE;
                    mainRecordbutton.setVisibility(View.INVISIBLE);
                    discardButton.setVisibility(View.INVISIBLE);
                    saveButton.setVisibility(View.INVISIBLE);
                    playbutton.setVisibility(View.INVISIBLE);
                    playseekbar.setVisibility(View.INVISIBLE);

                }







            }
        });



        builder.create().show();
    }


    public void isVersion7(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            //pauserecButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audio_recorder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String convertmillsecondstoTimer(long millisec){
        String timing="";
        String secondstime="";
        String minuting="";
        long hours = millisec/(1000*60*60);
        long munite= (millisec%(1000*60*60))/(1000*60);
        long seconds=(millisec%(1000*60*60))%(1000*60)/1000;


        if(hours>0){
            timing=hours+":";
        }

        if(seconds<10){
            secondstime="0"+seconds;
        }else{
            secondstime=""+seconds;
        }

        if(munite<10){
            minuting="0"+munite;
        }else{
            minuting=""+munite;
        }

        String theTime=timing+minuting+":"+secondstime;

        return theTime;

    }



    public void updateTimer(){

        handler.postDelayed(runnable, 100);
    }

    private Runnable runnable =new Runnable() {
        @Override
        public void run() {
            // int duration=musicService.getMusicDuration();


            Date runningtime = new Date();
            runtime = runningtime.getTime();
            maintime = runtime-starttime;

            recordCounter.setText(convertmillsecondstoTimer(maintime));




            handler.postDelayed(runnable,100);
            //count++;
        }
    };

    public void disableTimer(){
        handler.removeCallbacks(runnable);
       // pausetime=maintime;
        //stoptime = maintime;
    }




    public void updateSeeker(){

        seekHandler.postDelayed(seekRunnable,100);

    }

    Runnable seekRunnable = new Runnable() {
        @Override
        public void run() {


            try{
                currenttime = playbackStudio.getCurrentMusicTime();
            }catch (NullPointerException e){
                currenttime = 0;
            }


            int secf = currenttime/1000;
            int secr = duration/1000;

            playbackCounter.setText(convertmillsecondstoTimer(currenttime));
            //musiclength.setText(convertmillsecondstoTimer(duration));
            playseekbar.setMax(secr);
            // int progress = (secf/secr)*100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //musicSeek.incrementProgressBy((int)1.2);
                // musicSeek.setKeyProgressIncrement((int)1.2);

                playseekbar.setProgress(secf);
            }else{
                playseekbar.setProgress(secf);
            }

            handler.postDelayed(seekRunnable,100);
            //count++;

            if(secf==secr){
                sendBroadcast(new Intent("broad"));
            }


        }
    };


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            playbutton.setImageResource(R.drawable.play_button);
            stopsignal.setImageResource(R.drawable.pause_button);

        }
    };

    public void registerBroad(){
        registerReceiver(receiver,new IntentFilter("broad"));
    }

    public void disableSeek(){

        playbackStudio.stopMedia();
        seekHandler.removeCallbacks(seekRunnable);
        String count = "00:00";
        recordCounter.setText(count);


    }

    @Override
    public void onStop(){
        super.onStop();


            downTimer.cancel();


        //downTimer.cancel();
    }
}
