package com.mactrix.www.readingnotepro;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
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

public class VideoRecorder extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageView videorecord;
    ImageView studystore;
    ImageView listmain;
    ImageView newnote;
    ImageView audiorecorder;
    ImageView mediastore;
    ImageView backing;

    public static final int VIDEO_CAPTURE=101;
    private CountDownTimer downTimer;
   // private StartAppAd startAppAd;
   // private InterstitialAd interstitialAd;
    //private AdView adView;
    //private AdRequest bannerRequest;
    //private AdRequest interRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // StartAppSDK.init(this, "204716271", true);
        setContentView(R.layout.activity_video_recorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        backing = (ImageView)findViewById(R.id.backing);
        backing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videorecord = (ImageView)findViewById(R.id.videorecord);
        videorecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String path = String.valueOf(editText.getText());

                //String pathing = Environment.getExternalStorageState()+"/ReadingNote Pro File/Video/"+path+".mp4";

                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(intent,VIDEO_CAPTURE);
                }

                //alertDialog();

            }
        });
        studystore = (ImageView)findViewById(R.id.studystore);
        studystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VideoRecorder.this,ProNote.class));

            }
        });
        listmain = (ImageView)findViewById(R.id.listmain);
        listmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VideoRecorder.this,StudyStore.class));

            }
        });
        newnote = (ImageView)findViewById(R.id.newnote);
        newnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VideoRecorder.this,Notepad.class));

            }
        });
        audiorecorder  = (ImageView)findViewById(R.id.audiorecorder);
        audiorecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VideoRecorder.this,AudioRecorder.class));

            }
        });
        mediastore = (ImageView)findViewById(R.id.mediastore);
        mediastore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VideoRecorder.this,MediaStore.class));

            }
        });



       /* MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

        //adView = (AdView)findViewById(R.id.adView);
        //bannerRequest = new AdRequest.Builder().build();
        //adView.loadAd(bannerRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/1513803903");
        interRequest = new AdRequest.Builder().build();*/



        downTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(VideoRecorder.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
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



                /*startAppAd = new StartAppAd(VideoRecorder.this);
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

        builder.setPositiveButton("Video Record", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String path = String.valueOf(editText.getText());

                String pathing = Environment.getExternalStorageState()+"/ReadingNote Pro File/Video/"+path+".mp4";

                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(intent,VIDEO_CAPTURE);
                }




            }
        });



        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){


        if(data!=null){
            Uri uri  = data.getData();

            if(requestCode==VIDEO_CAPTURE){
                if(resultCode==RESULT_OK){

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Video was Successfully Recorded, go ahead to Watch it....");

                    builder.setPositiveButton("Watch Video", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Bundle bundling = new Bundle();
                            bundling.putInt("mediastore",1);
                            startActivity(new Intent(VideoRecorder.this,MediaStore.class).putExtra("mediastore",bundling));

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });


                    builder.create().show();

                    Toast.makeText(this, "Video is saved at:\n"+uri.getPath(), Toast.LENGTH_LONG).show();

                }else if(resultCode==RESULT_CANCELED){

                    Toast.makeText(this, "Video recording cancelled", Toast.LENGTH_LONG).show();

                }else{


                    Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(this, "Video Recording was Unsuccessful...", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.video_recorder, menu);


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

    @Override
    public void onStop(){
        super.onStop();


        downTimer.cancel();


        //downTimer.cancel();
    }
}
