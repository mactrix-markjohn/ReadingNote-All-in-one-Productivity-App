package com.mactrix.www.readingnotepro;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.InflateException;
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
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
//import com.mopub.mobileads.MoPubErrorCode;
//import com.mopub.mobileads.MoPubInterstitial;

import java.io.File;

public class VideoStudy extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    ImageView backing;
    ImageView save;
    ImageView mainlist;
    ImageView delete;
    ImageView record;
    ImageView recordlist;
    ImageView videonote;
    Button closenote;


    RelativeLayout controllayout;
    VideoView videostudy;
    TextView counter;
    TextView counterFinish;
    ImageView fullscreen;
    SeekBar recordseek;
    TextView recordtitle;
    ImageView recordplaypause;

    EditText readingnote;
    EditText studytitle;

    ImageView writing;

    BottomSheetBehavior bottomSheetBehavior;
    View view;
    AppBarLayout appBarLayout;

    RelativeLayout smalllayout;
    RelativeLayout largelayout;

    RelativeLayout writeLayout;

    RelativeLayout videolayout;
    Handler handler;
    VideoView videoView;
    int resumePosition;
    int duration;
    int position;
    StudyDatabase studyDatabase;
     long id;
    String pathing;
    private CountDownTimer downTimer;
    //private MoPubInterstitial interstitial;
   // private AdView adView;
    //private AdRequest bannerRequest;
    //private InterstitialAd interstitialAd;
    //private AdRequest interRequest;

    enum Period{NOW,LATER}
    Period period;
    enum Storage{SAVE,UPDATE}
    Storage storage;
    enum UpdateMode{DIRECT,SENT}
    UpdateMode updateMode;
    enum Torch{TORCH,NOT}
    Torch torch;

    String path;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_study);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();
        period = Period.NOW;
        torch = Torch.NOT;
        storage = Storage.SAVE;
        updateMode = UpdateMode.DIRECT;

        writeLayout = (RelativeLayout)findViewById(R.id.writelayout);

        studyDatabase = new StudyDatabase(this,null,null,1);



       /* MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

       // adView = (AdView)findViewById(R.id.adView);
        //bannerRequest = new AdRequest.Builder().build();
        //adView.loadAd(bannerRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/8971004983");
        interRequest = new AdRequest.Builder().build();*/





        downTimer = new CountDownTimer(40000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(VideoStudy.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
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


                /*interstitial = new MoPubInterstitial(VideoStudy.this,"e26e9286ce3d4b8994a98c22c1066868");
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
                           // new Handler().postDelayed(new Runnable() {
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

        backing = (ImageView)findViewById(R.id.backing);
        backing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        appBarLayout = (AppBarLayout)findViewById(R.id.videoAppBar);

        videonote = (ImageView)findViewById(R.id.videonote);
        videonote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                writeLayout.setVisibility(View.VISIBLE);

            }
        });

        closenote = (Button)findViewById(R.id.closenote);
        closenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeLayout.setVisibility(View.GONE);
            }
        });

        videolayout = (RelativeLayout)findViewById(R.id.videolayout);
        videolayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(torch==Torch.NOT){
                    try{
                        controllayout.setVisibility(View.VISIBLE);

                        Display display = getWindowManager().getDefaultDisplay();

                       if(display.getWidth()>display.getHeight()){
                            view.setVisibility(View.VISIBLE);
                        }



                        appBarLayout.setVisibility(View.VISIBLE);



                    }catch (NullPointerException e){

                        controllayout.setVisibility(View.VISIBLE);
                        //appBarLayout.setVisibility(View.VISIBLE);

                    }
                    torch = Torch.TORCH;

                }else {

                    try{
                        controllayout.setVisibility(View.INVISIBLE);
                        Display display = getWindowManager().getDefaultDisplay();

                        if(display.getWidth()>display.getHeight()){
                            view.setVisibility(View.INVISIBLE);
                        }
                        appBarLayout.setVisibility(View.INVISIBLE);

                    }catch (NullPointerException e){
                        controllayout.setVisibility(View.INVISIBLE);
                        //appBarLayout.setVisibility(View.INVISIBLE);

                    }



                    torch = Torch.NOT;
                }

            }
        });
        save = (ImageView)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title= String.valueOf(studytitle.getText());
                String note= String.valueOf(readingnote.getText());


                if(storage== Storage.SAVE){

                    id = studyDatabase.addNote(title,note,path);
                    Toast.makeText(VideoStudy.this, "Note has been Saved", Toast.LENGTH_LONG).show();

                    storage= Storage.UPDATE;
                }else if(storage== Storage.UPDATE){

                    if(updateMode== UpdateMode.DIRECT){

                        studyDatabase.updateNote(id,title,note,path);

                    }else if(updateMode== UpdateMode.SENT){

                        Cursor cursor = studyDatabase.getAllNote();

                        if(cursor!=null){
                            if(cursor.getCount()>=0){

                                cursor.moveToPosition(position);

                                long ID = cursor.getLong(cursor.getColumnIndex(StudyDatabase.ID));
                                studyDatabase.updateNote(ID,title,note,path);



                            }
                        }



                    }
                    Toast.makeText(VideoStudy.this, "Note has been Updated", Toast.LENGTH_LONG).show();

                }

            }
        });
        mainlist = (ImageView)findViewById(R.id.mainlist);
        mainlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VideoStudy.this,StudyStore.class));

            }
        });
        delete = (ImageView)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(VideoStudy.this);
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
                                Toast.makeText(VideoStudy.this, "Note has been Deleted..", Toast.LENGTH_LONG).show();
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

                startActivity(new Intent(VideoStudy.this,VideoRecorder.class));


            }
        });
        recordlist = (ImageView)findViewById(R.id.recordlist);
        recordlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VideoStudy.this,MediaStore.class));

            }
        });

        controllayout = (RelativeLayout)findViewById(R.id.contollayout);
        videoView = (VideoView)findViewById(R.id.videostudy);
        counter = (TextView)findViewById(R.id.counter);
        counterFinish = (TextView)findViewById(R.id.counterfinish);
        fullscreen = (ImageView)findViewById(R.id.fullscreen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Display display = getWindowManager().getDefaultDisplay();

                if(display.getWidth()>display.getHeight()){

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(videoView.getLayoutParams());
                    layoutParams.alignWithParent = true;
                    layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;

                    // layoutParams.setMargins(metrics.widthPixels/6,0,metrics.widthPixels/8,0);
                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //   layoutParams.setMarginStart(RelativeLayout.LayoutParams.MATCH_PARENT);
                    //  layoutParams.setMarginEnd(RelativeLayout.LayoutParams.MATCH_PARENT);
                    //}

                    //layoutParams.bottomMargin=0;
                    layoutParams.leftMargin = metrics.widthPixels / 6;
                    layoutParams.rightMargin = metrics.widthPixels / 8;
                    //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                    videoView.setLayoutParams(layoutParams);

                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

            }
        });
        recordseek = (SeekBar)findViewById(R.id.recordseek);
        recordtitle = (TextView) findViewById(R.id.recordtitle);
        recordplaypause = (ImageView)findViewById(R.id.recordplaypause);
        recordplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isPlaying()) {
                    //downTimer.cancel();
                    resumeMedia();
                    recordplaypause.setImageResource(R.drawable.pause_round_button);
                    //fab.setImageResource(R.drawable.pause);
                    updateSeekBar();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            duration = videoView.getDuration();
                        }
                    }, 300);

                } else {

                    //downTimer.start();
                    pauseMedia();
                    recordplaypause.setImageResource(R.drawable.playbutton_circle);
                    callAds();
                    //fab.setImageResource(R.drawable.play);
                }





            }
        });

        readingnote = (EditText)findViewById(R.id.readingnot);
        studytitle = (EditText)findViewById(R.id.studytitl);

        smalllayout = (RelativeLayout)findViewById(R.id.smalllayout);
        largelayout = (RelativeLayout)findViewById(R.id.largelayout);

        view = findViewById(R.id.videobottomsheet);

        writing = (ImageView)findViewById(R.id.writing);
        writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smalllayout.setVisibility(View.GONE);
                largelayout.setVisibility(View.VISIBLE);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });





        bottomSheetBehavior = BottomSheetBehavior.from(view);

        bottomSheetBehavior.setPeekHeight(270);
        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        largelayout.setVisibility(View.GONE);
                        smalllayout.setVisibility(View.VISIBLE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        smalllayout.setVisibility(View.GONE);
                        largelayout.setVisibility(View.VISIBLE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        recordseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                handler.removeCallbacks(runnable);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        duration = videoView.getDuration();
                    }
                }, 200);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                videoView.seekTo(seekBar.getProgress()*1000);
                resumePosition = seekBar.getProgress()*1000;
                recordseek.setProgress(seekBar.getProgress());
                updateSeekBar();

            }
        });


        Bundle bundle = getIntent().getBundleExtra("position");



        Uri uri = getIntent().getData();


        Bundle bundling = getIntent().getBundleExtra("videostore");


        if (savedInstanceState != null) {


            if(uri!=null){
                pathing = savedInstanceState.getString("titles");
                path= savedInstanceState.getString("datas");
                recordtitle.setText(pathing);
                playmedia(path);
                resumePosition = savedInstanceState.getInt("currenttime");
                resumeMedia();

            }else {

                pathing = savedInstanceState.getString("titles");
                path= savedInstanceState.getString("datas");
                recordtitle.setText(pathing);

                //currentIndex = savedInstanceState.getInt("CurrentIndex");
                //videoTitle.setText(title.get(currentIndex));
                playmedia(path);
                resumePosition = savedInstanceState.getInt("currenttime");
                resumeMedia();
            }
        }else if (uri != null) {
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(uri, null, null, null, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    path = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.Video.Media.DATA));
                    pathing = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.Video.Media.TITLE));
                    playmedia(path);
                    recordtitle.setText(pathing);
                }
            }


        } else if(bundling!=null){
            path = bundling.getString("videostore");

            pathing = path.substring(path.lastIndexOf("/")+1,path.length());
            recordtitle.setText(pathing);

            playmedia(path);
            updateSeekBar();
            duration = videoView.getDuration();


        }else if(bundle!=null){

            position = bundle.getInt("position");

            Cursor cursor = studyDatabase.getAllNote();

            if(cursor!=null){
                if(cursor.getCount()>0){
                    cursor.moveToPosition(position);

                    path = cursor.getString(cursor.getColumnIndex(StudyDatabase.MEDIAFILE));


                    pathing = path.substring(path.lastIndexOf("/")+1,path.length());
                    recordtitle.setText(pathing);

                    retrieveNote(cursor);

                    playmedia(path);

                    updateSeekBar();
                    duration = videoView.getDuration();


                }
            }


        }


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

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


    public void callAds(){



        downTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 14000) {
                    Toast.makeText(VideoStudy.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFinish() {

                /*interstitialAd.loadAd(interRequest);
                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdLoaded(){
                        if(interstitialAd.isLoaded()){
                            interstitialAd.show();
                        }
                    }
                });*/




            }

        };
        downTimer.start();

    }

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
    public void onSaveInstanceState(Bundle savedInstanceState){
        //savedInstanceState.putBoolean("ServiceState",serviceBound);
       // savedInstanceState.putInt("CurrentIndex",currentIndex);
        savedInstanceState.putInt("duration",duration);
        savedInstanceState.putInt("currenttime",videoView.getCurrentPosition());
        savedInstanceState.putString("titles",pathing);
        savedInstanceState.putString("datas",path);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState ){
        super.onRestoreInstanceState(savedInstanceState);
        // serviceBound=savedInstanceState.getBoolean("ServiceState");
        //currentIndex = savedInstanceState.getInt("CurrentIndex");
        duration = savedInstanceState.getInt("duration");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.video_study, menu);
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







    public void playmedia(String data){

        try{
            Uri uri = Uri.parse(data);
            videoView.setVideoURI(uri);
            videoView.start();


            /*mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setDataSource(data);
            mediaPlayer.prepareAsync();*/
            updateSeekBar();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    duration = videoView.getDuration();
                }
            }, 300);


        }catch (IllegalArgumentException e){
            Toast.makeText(this, "Corrupted File", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }catch (Exception e){
            Uri uri = Uri.parse(data);
            videoView.setVideoURI(uri);
            videoView.start();


            /*mediaPlayer.setDisplay(surfaceHolder);
            try {
                mediaPlayer.setDataSource(data);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mediaPlayer.prepareAsync();*/
            updateSeekBar();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    duration = videoView.getDuration();
                }
            }, 300);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                duration = videoView.getDuration();
            }
        }, 300);
    }



    public void updateSeekBar(){


        handler.postDelayed(runnable, 100);
    }

    int count;
    private Runnable runnable =new Runnable() {
        @Override
        public void run() {
            // int duration=musicService.getMusicDuration();

           // if(period==Period.NOW) {
                duration = videoView.getDuration();

             //    period= Period.LATER;
            //}
            int currentTime = getCurrentMusicTime();

            int secf = currentTime/1000;
            int secr = duration/1000;

            counter.setText(convertmillsecondstoTimer(currentTime));
            counterFinish.setText(convertmillsecondstoTimer(duration));
            recordseek.setMax(secr);
            // int progress = (secf/secr)*100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //musicSeek.incrementProgressBy((int)1.2);
                // musicSeek.setKeyProgressIncrement((int)1.2);

                recordseek.setProgress(secf);
            }else{
                recordseek.setProgress(secf);
            }

            handler.postDelayed(runnable,100);
            count++;
        }
    };



    public boolean isPlaying(){
        boolean isPlaying=false;
        try {
            isPlaying = videoView.isPlaying();
        }catch (IllegalStateException e){
            videoView = (VideoView)findViewById(R.id.videostudy);
            isPlaying = videoView.isPlaying();
        }catch (Exception e){
            videoView = (VideoView)findViewById(R.id.videostudy);
        }


        return isPlaying;
    }

    public int getCurrentMusicTime(){
        int c=0;


        try {
            c = videoView.getCurrentPosition();

        }catch (IllegalStateException e){
            videoView = (VideoView)findViewById(R.id.videostudy);
            // mediaPlayer.reset();
            //mediaPlayer.release();
            // Toast.makeText(this, "Error! trying to access Media in an Illegal state", Toast.LENGTH_SHORT).show();
        }
        return c;
    }
    public int getMusicDuration(){
        int d = 1;

        if(videoView!=null) {
            if (videoView.isPlaying()) {
                d =videoView.getDuration();
            }
        }
        return d;
    }
    public void reset(){
        videoView.stopPlayback();
    }

    public void stopMedia(){

        if(videoView==null) return;
        try {


            if (videoView.isPlaying()) {
                videoView.stopPlayback();

            }
        }catch (IllegalStateException e){
            videoView= (VideoView)findViewById(R.id.videostudy);
            // mediaPlayer.stop();
        }
    }


    public void pauseMedia(){

            if (videoView.isPlaying()) {
                videoView.pause();
                resumePosition = videoView.getCurrentPosition();
            }

            //Intent intent = new Intent("pause");
            //sendBroadcast(intent);


    }

    public void resumeMedia(){

            if (!videoView.isPlaying()) {
                videoView.seekTo(resumePosition);
                videoView.start();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    duration = videoView.getDuration();
                }
            }, 200);


    }

    /*public void skipToNext(){

        if(data!=null) {


            if (currentIndex == data.size() - 1) {
                currentIndex = 0;
                videoView.stopPlayback();
                videoTitle.setText(title.get(currentIndex));
                playmedia(data.get(currentIndex));
            } else {
                currentIndex++;
                videoView.stopPlayback();
                videoTitle.setText(title.get(currentIndex));
                playmedia(data.get(currentIndex));
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    duration = videoView.getDuration();
                }
            }, 200);

        }else{
            Toast.makeText(this, "Selete a song from the List", Toast.LENGTH_SHORT).show();
        }
    }

    public void skipToprevious(){
        if(data!=null) {



            if (currentIndex == 0) {
                currentIndex = data.size() - 1;
                videoView.stopPlayback();
                videoTitle.setText(title.get(currentIndex));
                playmedia(data.get(currentIndex));
            } else {
                currentIndex--;
                videoView.stopPlayback();
                videoTitle.setText(title.get(currentIndex));
                playmedia(data.get(currentIndex));
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    duration = videoView.getDuration();
                }
            }, 200);


        }else{
            Toast.makeText(this, "Selete a song from the List", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void sharing(String data,String title){
        //if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){

        //ContentValues contentValues = new ContentValues();
        //contentValues.put(MediaStore.Video.Media.DATA,data);
        //contentValues.put(MediaStore.Video.Media.MIME_TYPE,"video/mp4");
        //contentValues.put(MediaStore.Video.Media.DATE_ADDED,System.currentTimeMillis()/1000);

        // ContentResolver resolver =getContentResolver();
        // Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,contentValues);

        File file = new File(data);

        Uri uri = Uri.fromFile(file);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,title);
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(shareIntent,"Share Video with any of the Following..."));
        //}




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

    @Override
    public void onStop(){
        super.onStop();


        downTimer.cancel();


        //downTimer.cancel();
    }
}
