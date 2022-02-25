package com.mactrix.www.readingnotepro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.ListView;
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

public class StudyStore extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ListView listView;
    Studylist studylist;
    StudyDatabase database;
    ImageView imageView;

    ImageView studystore;
    ImageView videorecorder;
    ImageView newnote;
    ImageView audiorecorder;
    ImageView mediastore;
    private CountDownTimer downTimer;
    //private StartAppAd startAppAd;
   // private AdView adView;
    //private AdRequest bannerRequest;
    //private AdRequest interRequest;
    //private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // StartAppSDK.init(this,"204716271",true);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_study_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        registerDelete();

        database = new StudyDatabase(this,null,null,1);
        listView = (ListView)findViewById(R.id.bookgrid);
        studylist = new Studylist(this,database);
        listView.setAdapter(studylist);
        imageView = (ImageView)findViewById(R.id.backing);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




       /* MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

        adView = (AdView)findViewById(R.id.adView);
        bannerRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/2805993837");
        interRequest = new AdRequest.Builder().build();*/



        downTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(StudyStore.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
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





                /*startAppAd = new StartAppAd(StudyStore.this);
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




        studystore = (ImageView)findViewById(R.id.studystore);
        studystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StudyStore.this,ProNote.class));

            }
        });



        videorecorder = (ImageView)findViewById(R.id.videorecorder);
        videorecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StudyStore.this,VideoRecorder.class));

            }
        });



        newnote = (ImageView)findViewById(R.id.newnote);
        newnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StudyStore.this,Notepad.class));

            }
        });



        audiorecorder = (ImageView)findViewById(R.id.audiorecorder);
        audiorecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StudyStore.this,AudioRecorder.class));

            }
        });

        mediastore = (ImageView)findViewById(R.id.mediastore);
        mediastore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StudyStore.this,MediaStore.class));

            }
        });











        /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
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
        getMenuInflater().inflate(R.menu.study_store, menu);
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

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            onResume();

        }
    };

    public void registerDelete(){
        IntentFilter intentFilter = new IntentFilter("delete");
        registerReceiver(receiver,intentFilter);
    }

    @Override
    public void onResume(){
        super.onResume();

        studylist = new Studylist(this,database);
        listView.setAdapter(studylist);


    }

    @Override
    public void onStop(){
        super.onStop();


        downTimer.cancel();


        //downTimer.cancel();
    }
}
