package com.mactrix.www.readingnotepro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import org.w3c.dom.Text;

public class AudioStudy extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageView save;
    ImageView mainlist;
    ImageView delete;
    ImageView record;
    ImageView recordlist;
    ImageView backing;

    SeekBar recordseek;
    TextView recordtitle;
    ImageView recordplaypause;
    ImageView fastrewind;
    ImageView fastforward;

    EditText readingnote;
    EditText studytitle;

    TextView counting;
    TextView finish;

    RelativeLayout audiopad;
    Button closenote;

    PlaybackStudio playbackStudio;

    Handler playhandler;

    StudyDatabase studyDatabase;

    int position;
    String path;
    private int currentt;
    private int durati;
    private Handler handler;
    private CountDownTimer downTimer;
    private StartAppAd startAppAd;
   // private AdView adView;
    //private AdRequest bannerRequest;
    //private InterstitialAd interstitialAd;
    //private AdRequest interRequest;

    enum Storage{SAVE,UPDATE}
    Storage storage;

    enum UpdateMode{DIRECT,SENT}
    UpdateMode updateMode;

    Handler handlewind;
    enum A{B,C}

    ImageView pencall;
    A a;

    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StartAppSDK.init(this,"204716271",true);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_audio_study);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storage = Storage.SAVE;
        updateMode = UpdateMode.DIRECT;
        a = A.B;

        playhandler = new Handler();
        handler = new Handler();
        handlewind = new Handler();

        studyDatabase=new StudyDatabase(this,null,null,1);
        playbackStudio = new PlaybackStudio(this);
        registerBroad();

        backing = (ImageView)findViewById(R.id.backing);
        backing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        audiopad = (RelativeLayout)findViewById(R.id.audiopad);
        closenote = (Button)findViewById(R.id.bottonclose);
        closenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audiopad.setVisibility(View.INVISIBLE);
            }
        });


        pencall = (ImageView)findViewById(R.id.pencall);
        pencall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audiopad.setVisibility(View.VISIBLE);
            }
        });




      //  MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

       // adView = (AdView)findViewById(R.id.adView);
        //bannerRequest = new AdRequest.Builder().build();
        //adView.loadAd(bannerRequest);

        //interstitialAd = new InterstitialAd(this);
        //interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/8643701920");
        //interRequest = new AdRequest.Builder().build();






        downTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(AudioStudy.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
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




                /*startAppAd = new StartAppAd(AudioStudy.this);
                startAppAd.loadAd(new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {

                        startAppAd.showAd();
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad ad) {

                    }
                });*/


                    /*AppLovinSdk.initializeSdk(Barcawall.this);
                    AppLovinSdk.getInstance(Barcawall.this).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd appLovinAd) {
                            appLovnAd = appLovinAd;

                        }

                        @Override
                        public void failedToReceiveAd(int i) {

                        }
                    });
                    AppLovinInterstitialAdDialog adDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(Barcawall.this), Barcawall.this);
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


        save = (ImageView)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title= String.valueOf(studytitle.getText());
                String note= String.valueOf(readingnote.getText());


                if(storage==Storage.SAVE){

                    id = studyDatabase.addNote(title,note,path);
                    Toast.makeText(AudioStudy.this, "Note has been Saved", Toast.LENGTH_LONG).show();

                    storage=Storage.UPDATE;
                }else if(storage==Storage.UPDATE){

                    if(updateMode==UpdateMode.DIRECT){

                        studyDatabase.updateNote(id,title,note,path);

                    }else if(updateMode==UpdateMode.SENT){

                        Cursor cursor = studyDatabase.getAllNote();

                        if(cursor!=null){
                            if(cursor.getCount()>=0){

                                cursor.moveToPosition(position);

                                long ID = cursor.getLong(cursor.getColumnIndex(StudyDatabase.ID));
                                studyDatabase.updateNote(ID,title,note,path);



                            }
                        }



                    }
                    Toast.makeText(AudioStudy.this, "Note has been Updated", Toast.LENGTH_LONG).show();

                }

            }
        });
        mainlist = (ImageView)findViewById(R.id.mainlist);
        mainlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AudioStudy.this,StudyStore.class));

            }
        });
        delete = (ImageView)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AudioStudy.this);
                builder.setIcon(R.drawable.cancel);
                builder.setMessage("Are you sure you want to Delete this Study?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Cursor cursor = studyDatabase.getAllNote();

                        if(cursor!=null){
                            if(cursor.getCount()>=0){

                                cursor.moveToPosition(position);

                                long ID = cursor.getLong(cursor.getColumnIndex(StudyDatabase.ID));
                                //studyDatabase.updateNote(ID,title,note,"writtennote");
                                studyDatabase.deleteNote(ID);
                                Toast.makeText(AudioStudy.this, "Study has been Deleted..", Toast.LENGTH_LONG).show();
                                onBackPressed();



                            }
                        }

                    }
                });
                builder.create().show();

            }
        });
        record = (ImageView)findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AudioStudy.this,AudioRecorder.class));

            }
        });
        recordlist = (ImageView)findViewById(R.id.recordlist);
        recordlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AudioStudy.this,MediaStore.class));

            }
        });

        recordseek = (SeekBar)findViewById(R.id.recordseek);
        recordtitle = (TextView)findViewById(R.id.recordtitle);
        recordplaypause = (ImageView)findViewById(R.id.recordplaypause);
        recordplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!playbackStudio.isPlaying()){

                    recordplaypause.setImageResource(R.drawable.pause_button);
                    //stopsignal.setImageResource(R.drawable.play_button);

                    //recordCounter.setText(convertmillsecondstoTimer(0));
                    playbackStudio.resumeMedia();
                    duration=playbackStudio.getMusicDuration();
                    updateSeek();

                }else if(playbackStudio.isPlaying()){

                    recordplaypause.setImageResource(R.drawable.play_button);
                    //stopsignal.setImageResource(R.drawable.pause_button);

                    playbackStudio.pauseMedia();

                }



            }
        });

        /*fastrewind = (ImageView)findViewById(R.id.fastrewind);



        fastrewind.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                currentt = playbackStudio.getCurrentMusicTime();
                durati = playbackStudio.getMusicDuration();

                //handler = new Handler();
                handlewind.postDelayed(runne,100);


                return true;
            }
        });

        fastforward = (ImageView)findViewById(R.id.fastforward);
        fastforward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                currentt = playbackStudio.getCurrentMusicTime();
                durati = playbackStudio.getMusicDuration();

                //handler = new Handler();
                handler.postDelayed(runn,100);


                return true;
            }
        });*/

        readingnote = (EditText)findViewById(R.id.readingnote);
        studytitle = (EditText)findViewById(R.id.studytitle);

        counting = (TextView)findViewById(R.id.counting);
        finish = (TextView)findViewById(R.id.finish);


        Bundle bundle = getIntent().getBundleExtra("position");

        if(bundle!=null){

            position = bundle.getInt("position");

            Cursor cursor = studyDatabase.getAllNote();

            if(cursor!=null){
                if(cursor.getCount()>0){
                    cursor.moveToPosition(position);

                    path = cursor.getString(cursor.getColumnIndex(StudyDatabase.MEDIAFILE));


                    String pathing = path.substring(path.lastIndexOf("/")+1,path.length());
                    recordtitle.setText(pathing);

                    retrieveNote(cursor);

                    playbackStudio.playMedia(path);

                    updateSeek();
                    duration = playbackStudio.getMusicDuration();


                }
            }


        }

        Bundle audiostore = getIntent().getBundleExtra("audiostore");
        if(audiostore!=null){

            path = audiostore.getString("audiostore");

            String pathing = path.substring(path.lastIndexOf("/")+1,path.length());
            recordtitle.setText(pathing);

            playbackStudio.playMedia(path);
            updateSeek();
            duration = playbackStudio.getMusicDuration();
        }



        recordseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                playhandler.removeCallbacks(runnable);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                playbackStudio.seekTo(seekBar.getProgress()*1000);
                recordseek.setProgress(seekBar.getProgress());
                updateSeek();

            }
        });
















       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    Runnable runne = new Runnable() {
        @Override
        public void run() {

            currentt-=5000;

            playbackStudio.seekTo(currentt);
            recordseek.setProgress(currentt/1000);



            handlewind.postDelayed(runn,100);

        }
    };


    Runnable runn = new Runnable() {
        @Override
        public void run() {

            currentt+=5000;

            playbackStudio.seekTo(currentt);
            recordseek.setProgress(currentt/1000);



            handler.postDelayed(runn,100);
        }
    };

    public void retrieveNote(Cursor cursor){

        String title = cursor.getString(cursor.getColumnIndex(StudyDatabase.TITLE));
        String note = cursor.getString(cursor.getColumnIndex(StudyDatabase.NOTE));

        readingnote.setText(note);
        studytitle.setText(title);

        storage = Storage.UPDATE;
        updateMode = UpdateMode.SENT;

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
        getMenuInflater().inflate(R.menu.audio_study, menu);
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

    public void updateSeek(){

        playhandler.postDelayed(runnable,100);

    }

    private int currenttime;

    private int duration;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try{
                currenttime = playbackStudio.getCurrentMusicTime();
            }catch (NullPointerException e){
                currenttime = 0;
            }


            if(a== A.B){
                duration = playbackStudio.getMusicDuration();
                a = A.C;
            }

            duration = playbackStudio.getMusicDuration();




            int secf = currenttime/1000;
            int secr = duration/1000;

            counting.setText(convertmillsecondstoTimer(currenttime));
            finish.setText(convertmillsecondstoTimer(duration));
            recordseek.setMax(secr);
            // int progress = (secf/secr)*100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //musicSeek.incrementProgressBy((int)1.2);
                // musicSeek.setKeyProgressIncrement((int)1.2);

                recordseek.setProgress(secf);
            }else{
                recordseek.setProgress(secf);
            }

            playhandler.postDelayed(runnable,100);
            //count++;

            if(secf==secr){
                sendBroadcast(new Intent("broad"));
            }

        }
    };


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            recordplaypause.setImageResource(R.drawable.play_button);



        }
    };

    public void registerBroad(){

        IntentFilter intentFilter = new IntentFilter("broad");

        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onStop(){
        super.onStop();
        downTimer.cancel();

        playbackStudio.stopMedia();
    }

}
