package com.mactrix.www.readingnotepro;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.*;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
//import com.mopub.mobileads.MoPubErrorCode;
//import com.mopub.mobileads.MoPubInterstitial;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ProNote extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<String> listOfBook;
    private CountDownTimer downTimer;
   // private MoPubInterstitial interstitial;

    enum Error{ERROR,WELL};
    Error error;
    GridView gridView;
    ImageView studystore;
    ImageView videorecorder;
    ImageView newnote;
    ImageView audiorecorder;
    ImageView mediastore;
    PDFAdapter pdfAdapter;
    StartAppAd startAppAd;

    private File file;
    private ParcelFileDescriptor pfd;
    private PdfRenderer renderer;
    private PdfRenderer.Page page;
    private Bitmap map;
    ListView listpdf;

    RelativeLayout showcaselayout;
    ImageView showcasepdf;
    ImageView cancelshowcase;

    ImageView searchLib;
    ImageView librarymenu;

    TabLayout protab;
    ViewPager proviewer;


   // InterstitialAd interstitialAd;
    //AdRequest interRequest;

    //AdView adView;
    //AdRequest bannerRequest;
    boolean split=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // StartAppSDK.init(this,"204716271",true);
        //StartAppAd.disableSplash();
       /* StartAppAd.showSplash(this,savedInstanceState,new SplashConfig().setTheme(SplashConfig.Theme.ASHEN_SKY)
                .setAppName("ReadingNote ProNote")
                .setLogo(R.mipmap.proiconing));*/
        setContentView(R.layout.activity_pro_note);


       // registerBroad();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        error = Error.WELL;
        //gridView = (GridView) findViewById(R.id.bookgrid);
        listpdf = (ListView)findViewById(R.id.listpdf);
        studystore = (ImageView)findViewById(R.id.studystore);
        videorecorder=(ImageView)findViewById(R.id.videorecorder);
        newnote=(ImageView)findViewById(R.id.newnote);
        audiorecorder = (ImageView)findViewById(R.id.audiorecorder);
        mediastore=(ImageView)findViewById(R.id.mediastore);

        showcaselayout = (RelativeLayout)findViewById(R.id.showcaselayout);
        showcasepdf = (ImageView)findViewById(R.id.pdfshowcase);
        cancelshowcase = (ImageView)findViewById(R.id.cancelshowcase);

        cancelshowcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaselayout.setVisibility(View.GONE);
            }
        });

        searchLib = (ImageView)findViewById(R.id.searchlib);



        if(searchLib != null) {

            searchLib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(ProNote.this, SearchClass.class));

                }
            });

        }

        librarymenu = (ImageView)findViewById(R.id.librarymenu);


        if (!split) {

            librarymenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                    if(drawer.isDrawerOpen(GravityCompat.START)){
                        drawer.closeDrawer(GravityCompat.START);
                    }else{
                        drawer.openDrawer(GravityCompat.START);
                    }

                }
            });


        }



        //new ProAsync().execute();

        protab = (TabLayout)findViewById(R.id.protab);
        proviewer = (ViewPager)findViewById(R.id.proviewer);

        protab.addTab(protab.newTab().setText("LIBRARY"));
        protab.addTab(protab.newTab().setText("VOICE SEARCH"));
        protab.addTab(protab.newTab().setText("RECENT"));

        PagerAdapter pagerAdapter = new ProPagerAdapter(getSupportFragmentManager(),protab.getTabCount());
        proviewer.setAdapter(pagerAdapter);

        proviewer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(protab));

        protab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                proviewer.setCurrentItem(tab.getPosition(),true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });


        // Ads Region

       // MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

        //adView = (AdView)findViewById(R.id.adView);
       // bannerRequest = new AdRequest.Builder().build();
        //adView.loadAd(bannerRequest);

        //interstitialAd = new InterstitialAd(this);
        //interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/3649012651");
        //interRequest = new AdRequest.Builder().build();




        downTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(ProNote.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
                }

                if(millisUntilFinished == 14000){
                    Toast.makeText(ProNote.this, "Sorry, Ads will be shown very soon", Toast.LENGTH_LONG).show();
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


                /*interstitial = new MoPubInterstitial(ProNote.this,"e26e9286ce3d4b8994a98c22c1066868");
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
                               // @Override
                               // public void run() {
                              //      startActivity(new Intent(Musical.this,LovedThings.class));
                             //   }
                           // }, 2000);

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



        //listOfBook = new ArrayList<>();

        //getPDFBook();

        //pdfAdapter = new PDFAdapter(this,listOfBook);
        //gridView.setAdapter(pdfAdapter);
        //listpdf.setAdapter(pdfAdapter);


        studystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProNote.this,StudyStore.class));

            }
        });

        videorecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProNote.this,VideoRecorder.class));

            }
        });

        newnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProNote.this,Notepad.class));

            }
        });
        audiorecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProNote.this,AudioRecorder.class));

            }
        });

        mediastore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProNote.this, com.mactrix.www.readingnotepro.MediaStore.class));

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void findPDFBook(File file){
        String pattern = ".pdf";
        File[] filelist = file.listFiles();

        if(filelist!=null){
            for(int i=0;i<filelist.length;i++){
                if(filelist[i].isDirectory()){
                    findPDFBook(filelist[i]);
                }else{
                    if(filelist[i].getName().endsWith(pattern)){

                       /* if(filelist[i].canExecute()){
                            error=Error.WELL;

                            if(filelist[i].canRead()){
                                error=Error.WELL;
                            }else{
                                error = Error.ERROR;
                            }

                        }else {
                           error=Error.ERROR;
                        }*/


                        //if(error==Error.WELL){
                            listOfBook.add(filelist[i].getAbsolutePath());
                        //}
                    }
                }
            }
        }
    }

    public void getPDFBook(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            findPDFBook(files);

            String state = Environment.getExternalStorageState();

            if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equalsIgnoreCase(state)){
                File filing = getExternalFilesDir(null);
                findPDFBook(filing);

            }


            /*if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)){
            File[] file = null;

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                file = getExternalFilesDirs(null);


            }
            if(file!=null) {
                if(file.length>1) {
                    if(Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
                        findPDFBook(file[1]);}
                }
                findPDFBook(file[0]);
            }



            }*/



        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        }

    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String path = intent.getStringExtra("showcase");

            File file = new File(path);
            showcaselayout.setVisibility(View.VISIBLE);
            showcasepdf.setImageBitmap(pdfImage(file));


        }
    };


    public void registerBroad(){
        IntentFilter intentFilter = new IntentFilter("showfilter");
        registerReceiver(receiver,intentFilter);
    }

    public Bitmap pdfImage(File file){

        Bitmap bit=null;




       /* if(searchbooks!=null){
            file = new File(searchbooks.get(i));

            //context.startActivity(new Intent(context,MyLibrary.class));
        }

        if(file==null){
            context.startActivity(new Intent(context,ProNote.class));
        }*/



        try {
            pfd = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                renderer = new PdfRenderer(pfd);

                page = renderer.openPage(0);
                map = Bitmap.createBitmap(200,400, Bitmap.Config.ARGB_8888);
                page.render(map,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                //page.render(map,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                bit = map;
                page.close();
                renderer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e){
            //array.remove(i);
            // pic=PIC.LOCKED;
        }
        //Integer integer[] = new Integer[]{i};

        // DoBackgroungRender backgroungRender = (DoBackgroungRender) new DoBackgroungRender().execute(integer);
        // bit =backgroungRender.doInBackground(integer);

       /* try {
            //bit=backgroungRender.get();
            bit = new DoBackgroungRender().execute(integer).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        return bit;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                builder.setMessage("Are you sure, you want to Exit this Awesome App.");
                builder.setPositiveButton("Yes, Of course", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            StartAppAd.onBackPressed(ProNote.this);

                        ProNote.super.onBackPressed();
                        Toast.makeText(ProNote.this, "Then Goodbye Fella. See ya when I see ya..", Toast.LENGTH_LONG).show();

                    }
                });

                builder.setNegativeButton("No !!!!, I want more fun", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(ProNote.this, "Ha!!!, Then what are you waiting for, Have more Fun!!!", Toast.LENGTH_LONG).show();

                    }
                });

                builder.setNeutralButton("Check out Our other Apps", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/Mactrix"));
                        startActivity(intent);
                        Toast.makeText(ProNote.this, "Thanks for trying to know more about Us. Have fun with your Music...", Toast.LENGTH_LONG).show();
                    }
                });



                builder.create().show();




            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pro_note, menu);
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

            startActivity(new Intent(this,SearchClass.class));
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

            startActivity(new Intent(this,AudioRecorder.class));

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this,VideoRecorder.class));

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(this, Notepad.class));

        }else if(id == R.id.camerarecog){

            startActivity(new Intent(this,TextRecognition.class));

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, com.mactrix.www.readingnotepro.MediaStore.class));

        }else if(id==R.id.studystore){
            startActivity(new Intent(this,StudyStore.class));

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.mactrix.www.readingnotepro");
            startActivity(Intent.createChooser(intent,"Complete this Action with one of the Following..."));
            //Toast.makeText(this, "Not Yet in Playstore, Check back in few Days", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_send) {

            Intent send = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.mactrix.www.readingnotepro"));
            startActivity(send);
            //Toast.makeText(this, "Not Yet in Playstore, Check back in few Days", Toast.LENGTH_LONG).show();

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

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode){
        super.onMultiWindowModeChanged(isInMultiWindowMode);

        if(isInMultiWindowMode){
            split = true;
        }else{
            split = false;
        }
    }


    public class ProAsync extends AsyncTask<Void, PagerAdapter,PagerAdapter>{

        @Override
        protected PagerAdapter doInBackground(Void... voids) {

            protab = (TabLayout)findViewById(R.id.protab);
            proviewer = (ViewPager)findViewById(R.id.proviewer);

            protab.addTab(protab.newTab().setText("LIBRARY"));
            protab.addTab(protab.newTab().setText("VOICE SEARCH"));
            protab.addTab(protab.newTab().setText("RECENT"));

            PagerAdapter pagerAdapter = new ProPagerAdapter(getSupportFragmentManager(),protab.getTabCount());

            proviewer.setAdapter(pagerAdapter);

            return pagerAdapter;
        }

        @Override
        public void onPostExecute(PagerAdapter pagerAdapter){



            proviewer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(protab));

            protab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    proviewer.setCurrentItem(tab.getPosition(),true);

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {


                }
            });

        }
    }

}
