package com.mactrix.www.readingnotepro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
//import com.mopub.mobileads.MoPubErrorCode;
//import com.mopub.mobileads.MoPubInterstitial;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public class ReadingRoom extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TextToSpeech.OnInitListener,TextToSpeech.OnUtteranceCompletedListener {

    ImageView readingspace;
    BottomSheetBehavior bottomSheetBehavior;
    View view;

    int counter = 0;
    boolean truth=false;
    String action=null;

    RelativeLayout smalllayout;
    ImageView changeview;
    ImageView zoomout;
    ImageView zoomin;
    ImageView gointo;
    ImageView writenote;

    ImageView readingcancel;
    ImageView readingback;

    RelativeLayout largelayout;
    EditText readingnote;
    ImageButton donewriting;
    EditText readingtitle;

    AppBarLayout appBarLayout;
    ImageView previous;
    ImageView next;

    PDFView pdfView;




    int pageNo;

    int zoomwideLand=0;
    int zoomhighLand=900;

    int zoomwideport;
    int zoomhighport;

    String bookSource;

    ParcelFileDescriptor pfd;
    PdfRenderer pdfRenderer;
    PdfRenderer.Page page;

    Bitmap bitmap;

    StudyDatabase studyDatabase;
    private CountDownTimer downTimer;
   // private MoPubInterstitial interstitial;
    //private StartAppAd startAppAd;
    //private AdView adView;
    //private AdRequest bannerRequest;
    //private InterstitialAd interstitialAd;
    //private AdRequest interRequest;
    private TextToSpeech textToSpeech;

    SharePref readoutpref;




    enum Storage{SAVE,UPDATE}
    Storage storage;
    enum UpdateMode{DIRECT,SENT}
    UpdateMode updateMode;
    enum Visibility{VISIBLE,GONE}
    Visibility visibility;

    enum NightMode{NIGHT,NORMAL}
    NightMode nightMode;

    long id;
    int sentIndex;

    SharePref sharePref;

    int pagerecord;

    Bundle bundle;

    ImageView readingmenu;

    SharePref sharePrefe;

    Set<String> stringSet;
    //Dialog dialog;
    Object[] object;


    RecentDatabase database;

    PdfReader pdfReader;
    PdfTextExtractor textExtractor;
    String extracttext="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // StartAppSDK.init(this,"204716271",true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reading_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textToSpeech = new TextToSpeech(this,this);
        textToSpeech.setOnUtteranceCompletedListener(this);

        readoutpref = new SharePref(this,"readout");


        truth = readoutpref.getBoolean();


        storage= Storage.SAVE;
        updateMode = UpdateMode.DIRECT;
        visibility = Visibility.GONE;
        nightMode = NightMode.NORMAL;

        sharePref = new SharePref(this,"scrolltype");

        sharePrefe = new SharePref(this,"Recent");


        stringSet = new ArraySet<>();

        if(!sharePrefe.getStringSet().isEmpty()) {
            object = sharePrefe.getStringSet().toArray();

            for(int i=0;i<sharePrefe.getStringSet().size();i++){

                stringSet.add((String)object[i]);


            }

        }

        database = new RecentDatabase(this,null,null,1);


        //Toast.makeText(this, "To use other features, just click on the Screen and more control button will appear, then carry out any task you want", Toast.LENGTH_LONG).show();



        readingspace = (ImageView)findViewById(R.id.readingspace);
        readingcancel = (ImageView)findViewById(R.id.readingcance);
        readingback = (ImageView)findViewById(R.id.readingback);
        readingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        view = findViewById(R.id.bottomsheetreading);
        view.setVisibility(View.GONE);

        smalllayout = (RelativeLayout)findViewById(R.id.smalllayou);
        changeview = (ImageView)findViewById(R.id.changevie);
        zoomout =(ImageView)findViewById(R.id.zoomou);
        zoomin = (ImageView)findViewById(R.id.zoomi);
        gointo  = (ImageView)findViewById(R.id.goint);
        writenote = (ImageView)findViewById(R.id.writenot);
        readingmenu = (ImageView)findViewById(R.id.readingmenu);


        largelayout = (RelativeLayout)findViewById(R.id.largerlayout);
        readingnote = (EditText)findViewById(R.id.readingnot);
        donewriting = (ImageButton)findViewById(R.id.donewritin);
        readingtitle= (EditText)findViewById(R.id.readingtitl);

        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        previous = (ImageView)findViewById(R.id.previous);
        next = (ImageView)findViewById(R.id.next);

        studyDatabase = new StudyDatabase(this,null,null,1);

        pdfView = (PDFView)findViewById(R.id.readviewer);





       // MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

        //adView = (AdView)findViewById(R.id.adView);
        //bannerRequest = new AdRequest.Builder().build();
      //  adView.loadAd(bannerRequest);

        //interstitialAd = new InterstitialAd(this);
        //interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/6891636556");
        //interRequest = new AdRequest.Builder().build();







        downTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(ReadingRoom.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
                }

                if (millisUntilFinished == 15000){
                    Toast.makeText(ReadingRoom.this, "Sorry, Ads will be shown soon..", Toast.LENGTH_LONG).show();
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


               /* startAppAd = new StartAppAd(ReadingRoom.this);
                startAppAd.loadAd(new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {

                        startAppAd.showAd();
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad ad) {

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



        bundle = getIntent().getBundleExtra("selectedbook");
        Bundle sentPosition = getIntent().getBundleExtra("position");


        if(savedInstanceState!=null){

            id = savedInstanceState.getLong("id");
            sentIndex = savedInstanceState.getInt("sentIndex");


            pageNo = savedInstanceState.getInt("pageNo");
            bookSource = savedInstanceState.getString("bookSource");
            openBook(pageNo,bookSource);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pdfView.jumpTo(pageNo);
                }
            }, 2000);


        }

        if(sentPosition!=null){

            sentIndex = sentPosition.getInt("position");
            unvailSentStudy(sentIndex);

        }

        if(bundle!=null){
            bookSource=bundle.getString("selectedbook");
            openBook(pageNo,bookSource);

        }



        bottomSheetBehavior = BottomSheetBehavior.from(view);

        bottomSheetBehavior.setPeekHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
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


        readingmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ReadingRoom.this,v);
                popupMenu.inflate(R.menu.readingmenu);
                popupMenu.show();

                Menu menu = popupMenu.getMenu();
                MenuItem item = menu.findItem(R.id.onreadout);
                MenuItem menuItem = menu.findItem(R.id.offreadout);


                if(readoutpref.getBoolean()){
                    item.setChecked(true);
                }else{
                    menuItem.setChecked(true);
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.pageno:

                                Toast.makeText(ReadingRoom.this, ""+pdfView.getPageCount(), Toast.LENGTH_LONG).show();



                                break;
                            case R.id.currentpage:

                                Toast.makeText(ReadingRoom.this, ""+(pdfView.getCurrentPage()+1), Toast.LENGTH_LONG).show();
                                break;

                            case R.id.readout:
                                speakout(extracttext);


                                break;
                            case R.id.stopread:

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }
                                break;

                            case R.id.onreadout:
                                item.setChecked(true);
                                readoutpref.setBoolean(true);
                                truth = true;


                                break;
                            case R.id.offreadout:
                                item.setChecked(true);
                                readoutpref.setBoolean(false);
                                truth = false;
                                break;
                        }


                        return true;
                    }
                });
            }
        });



        changeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Display display = getWindowManager().getDefaultDisplay();
                if(display.getWidth()>display.getHeight()){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else if(display.getHeight()>display.getWidth()){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

                //Toast.makeText(ReadingRoom.this, "Total page is"+pdfRenderer.getPageCount(), Toast.LENGTH_SHORT).show();

            }
        });

        zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This method will be used to change the Scroll Type of the Pdf

                PopupMenu popupMenu = new PopupMenu(ReadingRoom.this,v);
                popupMenu.inflate(R.menu.scrolltypemenu);
                popupMenu.show();

                Menu menu = popupMenu.getMenu();
                MenuItem continuous = menu.findItem(R.id.continuous);
                MenuItem singlepage = menu.findItem(R.id.singlepage);
                MenuItem continuous2 = menu.findItem(R.id.continuous2);

                pageNo = pagerecord;


                if(sharePref.getInt()==0){
                    singlepage.setChecked(true);
                }else if(sharePref.getInt()==1){
                    continuous.setChecked(true);
                }else{
                    continuous2.setChecked(true);
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.singlepage:
                                sharePref.setInt(0);
                                item.setChecked(true);
                                openBook(pageNo);




                                break;
                            case R.id.continuous:
                                sharePref.setInt(1);
                                item.setChecked(true);
                                openBook(pageNo);


                                break;
                            case R.id.continuous2:
                                sharePref.setInt(2);
                                item.setChecked(true);
                                openBook(pageNo);
                                break;
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pdfView.jumpTo(pageNo);
                            }
                        }, 2000);

                        return true;
                    }
                });





              /*  Display display = getWindowManager().getDefaultDisplay();
                if(display.getWidth()>display.getHeight()){
                    if(zoomhighLand<=900){
                        zoomhighLand=900;
                        Toast.makeText(ReadingRoom.this,"Page at its original size, no more zoom effect.",Toast.LENGTH_LONG).show();

                    }else {
                        zoomhighLand-=900;

                    }


                    if(zoomwideLand<=0){
                        zoomwideLand=0;

                        Toast.makeText(ReadingRoom.this,"Page at its original size, no more zoom effect.",Toast.LENGTH_LONG).show();
                    }else{
                        zoomwideLand-=100;

                        Toast.makeText(ReadingRoom.this, "Zoom Out", Toast.LENGTH_SHORT).show();
                    }


                    openBook(pageNo);




                }else if(display.getHeight()>display.getWidth()){


                    if(zoomwideport<=500){
                        zoomwideport=500;

                        Toast.makeText(ReadingRoom.this,"Page at its original size, no more zoom effect.",Toast.LENGTH_LONG).show();

                    }else{
                        zoomwideport-=500;


                    }

                    if(zoomhighport<=0){
                        zoomhighport=0;

                        Toast.makeText(ReadingRoom.this,"Page at its original size, no more zoom effect.",Toast.LENGTH_LONG).show();
                    }else{
                        zoomhighport-=50;


                        Toast.makeText(ReadingRoom.this, "Zoom Out", Toast.LENGTH_SHORT).show();
                    }

                    openBook(pageNo);


                }*/


            }
        });

        zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This will be used to change from normal mode to Night Mode

                if(nightMode==NightMode.NORMAL){
                    pdfView.setNightMode(true);
                    nightMode = NightMode.NIGHT;
                }else{
                    pdfView.setNightMode(false);
                    nightMode = NightMode.NORMAL;
                }






                /*Display display = getWindowManager().getDefaultDisplay();
                if(display.getWidth()>display.getHeight()){

                        zoomhighLand+=900;
                        Toast.makeText(ReadingRoom.this,"Zoom In",Toast.LENGTH_SHORT).show();
                        zoomwideLand+=100;
                    openBook(pageNo);




                }else if(display.getHeight()>display.getWidth()){


                    zoomwideport+=500;
                    Toast.makeText(ReadingRoom.this,"Zoom In",Toast.LENGTH_SHORT).show();
                    zoomhighport+=50;

                    openBook(pageNo);



                }*/

            }
        });

        gointo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToaDialog();
                //Toast.makeText(ReadingRoom.this, "Total page is"+pdfRenderer.getPageCount(), Toast.LENGTH_SHORT).show();

            }
        });
        writenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smalllayout.setVisibility(View.GONE);
                largelayout.setVisibility(View.VISIBLE);

                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
               // bottomSheetBehavior.setState(BottomSheetBehavior.PEEK_HEIGHT_AUTO);


            }
        });

        readingcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                largelayout.setVisibility(View.GONE);
                smalllayout.setVisibility(View.VISIBLE);

                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


        donewriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = String.valueOf(readingtitle.getText());
                String note = String.valueOf(readingnote.getText());

                if(storage==Storage.SAVE){

                    id = studyDatabase.addNote(title,note,bookSource);

                    storage=Storage.UPDATE;

                    Toast.makeText(ReadingRoom.this, "Note has been Saved", Toast.LENGTH_LONG).show();
                }else if(storage==Storage.UPDATE){

                    if(updateMode==UpdateMode.DIRECT){

                        studyDatabase.updateNote(id,title,note,bookSource);

                    }else if(updateMode==UpdateMode.SENT){

                        Cursor cursor = studyDatabase.getAllNote();
                        if(cursor!=null){
                            if(cursor.getCount()>0){
                                cursor.moveToPosition(sentIndex);

                                long ID = cursor.getLong(cursor.getColumnIndex(StudyDatabase.ID));

                                studyDatabase.updateNote(ID,title,note,bookSource);
                            }
                        }

                    }

                    Toast.makeText(ReadingRoom.this, "Note has been Updated", Toast.LENGTH_LONG).show();


                }




            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pageNo--;
                openBook(pageNo);

                //Toast.makeText(ReadingRoom.this, ""+pageNo+"/"+pdfRenderer.getPageCount(), Toast.LENGTH_SHORT).show();

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo++;
                openBook(pageNo);
               // Toast.makeText(ReadingRoom.this, ""+pageNo+"/"+pdfRenderer.getPageCount(), Toast.LENGTH_SHORT).show();
            }
        });

        readingspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if(visibility == Visibility.GONE){

                    view.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);


                    visibility=Visibility.VISIBLE;
                }else if(visibility==Visibility.VISIBLE){

                    view.setVisibility(View.GONE);
                    appBarLayout.setVisibility(View.GONE);
                    previous.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);

                    visibility=Visibility.GONE;
                }*/

            }
        });

        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibility == Visibility.GONE){

                    smalllayout.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    //previous.setVisibility(View.VISIBLE);
                   // next.setVisibility(View.VISIBLE);


                    visibility=Visibility.VISIBLE;
                }else if(visibility==Visibility.VISIBLE){

                    smalllayout.setVisibility(View.GONE);
                    appBarLayout.setVisibility(View.GONE);
                    //previous.setVisibility(View.GONE);
                   // next.setVisibility(View.GONE);

                    visibility=Visibility.GONE;
                }
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void unvailSentStudy(int sentIndex){
        Cursor cursor = studyDatabase.getAllNote();
        pageNo=0;

        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToPosition(sentIndex);

                String title = cursor.getString(cursor.getColumnIndex(StudyDatabase.TITLE));
                String note = cursor.getString(cursor.getColumnIndex(StudyDatabase.NOTE));
                bookSource = cursor.getString(cursor.getColumnIndex(StudyDatabase.MEDIAFILE));

                readingtitle.setText(title);
                readingnote.setText(note);
                openBook(pageNo,bookSource);

                storage=Storage.UPDATE;
                updateMode = UpdateMode.SENT;

            }
        }
    }


    public void openBook(int index,String bookSource){

        action=null;
        if(bundle!=null){
            action = bundle.getString("action");
        }


        try {
            if(action==null) {

                pdfReader = new PdfReader(bookSource);
                extracttext = PdfTextExtractor.getTextFromPage(pdfReader, 0).trim() + "\n";
                if (truth && pdfView.getCurrentPage()==0) {
                    speakout(extracttext);
                }

            }
        } catch (IOException e) {

            long y = 0;
            extracttext ="";
            //onBackPressed();


        }catch (OutOfMemoryError error){
            extracttext = "";
            int i=0;
            //onBackPressed();
        }catch (NoClassDefFoundError e){

            extracttext = "";
            double g = 0;

        }catch (Exception e){
            extracttext ="";
        }




        try {
            File file = new File(bookSource);

            stringSet.add(bookSource);
            sharePrefe.setStringSet(stringSet);

            if(action==null){

                Cursor cursor = database.getAllNote();


                if(cursor!=null){
                    if(cursor.getCount()>0){

                        for(int i =0;i<cursor.getCount();i++){
                            cursor.moveToPosition(i);

                            if(!cursor.getString(cursor.getColumnIndex(RecentDatabase.MEDIAFILE)).equalsIgnoreCase(bookSource)){

                                if(i == cursor.getCount()-1) {
                                    database.addNote(bookSource, bookSource, bookSource);
                                }
                            }else {
                                break;
                            }
                        }

                    }else{
                        database.addNote(bookSource,bookSource,bookSource);
                    }
                }

            }





            Display display = getWindowManager().getDefaultDisplay();





            if(sharePref.getInt()==0){



                if(display.getWidth()>display.getHeight()){



                    if(action !=null){
                        Uri uri = Uri.parse(bookSource);

                        pdfView.fromUri(uri).autoSpacing(true).pageFling(true).swipeHorizontal(true).pageFitPolicy(FitPolicy.HEIGHT).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/
                            }
                        }).load();


                    }else {

                        pdfView.fromFile(file).autoSpacing(true).pageFling(true).swipeHorizontal(true).pageFitPolicy(FitPolicy.HEIGHT).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/

                            }
                        }).load();

                    }
                    //pdfView.jumpTo(index);



                }else {

                    if(action !=null){
                        Uri uri = Uri.parse(bookSource);


                        pdfView.fromUri(uri).pageSnap(true).swipeHorizontal(true).autoSpacing(true).pageFling(true).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/

                            }
                        }).load();

                    }else {


                        pdfView.fromFile(file).pageSnap(true).swipeHorizontal(true).autoSpacing(true).pageFling(true).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/
                            }
                        }).load();
                    }
                    //pdfView.jumpTo(index);


                }




            }else if(sharePref.getInt()==1){

                if(display.getWidth()>display.getHeight()){


                    if(action != null) {
                        Uri uri = Uri.parse(bookSource);

                        pdfView.fromUri(uri).spacing(30).pageFling(true).swipeHorizontal(false).pageFitPolicy(FitPolicy.WIDTH).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/

                            }
                        }).load();

                    }else {


                        pdfView.fromFile(file).spacing(30).pageFling(true).swipeHorizontal(false).pageFitPolicy(FitPolicy.WIDTH).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();

                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/

                            }
                        }).load();

                    }
                    //pdfView.jumpTo(index);



                }else {

                        if(action != null) {
                            Uri uri = Uri.parse(bookSource);

                            pdfView.fromUri(uri).pageSnap(true).swipeHorizontal(false).spacing(10).pageFling(true).onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    pagerecord = page;

                                    if(textToSpeech.isSpeaking()){
                                        textToSpeech.stop();
                                    }

                                    new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/

                                }
                            })
                                    .load();


                        }else {


                            pdfView.fromFile(file).pageSnap(true).swipeHorizontal(false).spacing(10).pageFling(true).onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    pagerecord = page;

                                    if(textToSpeech.isSpeaking()){
                                        textToSpeech.stop();
                                    }

                                    new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/
                                }
                            })
                                    .load();

                        }
                    //pdfView.jumpTo(index);


                }



            }else if(sharePref.getInt()==2){

                if(display.getWidth()>display.getHeight()){


                    if(action != null) {
                        Uri uri = Uri.parse(bookSource);

                        pdfView.fromUri(uri).spacing(10).pageFling(true).swipeHorizontal(true).pageFitPolicy(FitPolicy.BOTH).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/

                            }
                        }).load();

                    }else {

                        pdfView.fromFile(file).spacing(10).pageFling(true).swipeHorizontal(true).pageFitPolicy(FitPolicy.BOTH).onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                pagerecord = page;

                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }

                                new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/
                            }
                        }).load();

                    }
                    //pdfView.jumpTo(index);





                }else {


                        if(action != null) {
                            Uri uri = Uri.parse(bookSource);

                            pdfView.fromUri(uri).pageSnap(true).swipeHorizontal(false).spacing(10).pageFling(true).onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    pagerecord = page;

                                    if(textToSpeech.isSpeaking()){
                                        textToSpeech.stop();
                                    }

                                    new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/

                                }
                            })
                                    .load();

                        }else {


                            pdfView.fromFile(file).pageSnap(true).swipeHorizontal(false).spacing(10).pageFling(true).onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    pagerecord = page;
                                    if(textToSpeech.isSpeaking()){
                                        textToSpeech.stop();
                                    }

                                    new AsynchronousClass(page).execute();

                                   /* try {
                                        extracttext = PdfTextExtractor.getTextFromPage(pdfReader,page+1).trim()+"\n";
                                        if (truth) {
                                            speakout(extracttext);
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/
                                }
                            })
                                    .load();

                        }
                    //pdfView.jumpTo(index);


                }

            }












            /*pfd = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);

            pdfRenderer = new PdfRenderer(pfd);

            if(index>=pdfRenderer.getPageCount()){
                index = pdfRenderer.getPageCount()-1;
                pageNo = index;
                Toast.makeText(this, "Book is at it's Last page", Toast.LENGTH_LONG).show();
            }

            if(index<=0){
                index=0;
                pageNo = index;
                Toast.makeText(this, "Book is at it's First page", Toast.LENGTH_LONG).show();
            }


            page = pdfRenderer.openPage(index);

            Display display = getWindowManager().getDefaultDisplay();

            int defHeight=display.getHeight();
            int defWidth=display.getWidth();



            if(display.getWidth()>display.getHeight()){ //Landscape mode

                bitmap = Bitmap.createBitmap(defWidth+zoomwideLand,defHeight+zoomhighLand, Bitmap.Config.ARGB_8888);
                page.render(bitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                readingspace.setImageBitmap(bitmap);

            }else if(display.getHeight()>display.getWidth()){ // Portrait mode
                bitmap = Bitmap.createBitmap(defWidth+zoomwideport,defHeight+zoomhighport, Bitmap.Config.ARGB_8888);
                page.render(bitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                readingspace.setImageBitmap(bitmap);
            }*/









        } catch (Exception e){
            e.getMessage();
        }



    }
    public void openBook(int pageNo){
        openBook(pageNo,bookSource);
    }

    public void goToaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // AlertDialog dialog = builder.show();
        final EditText edit = new EditText(this);
        builder.setIcon(R.drawable.ic_last_page_black_24dp)
                .setTitle("Enter Page number")
                .setView(edit);

        builder.setPositiveButton("GoTo page", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try{

                    String index = String.valueOf(edit.getText());
                    int predex = Integer.parseInt(index);
                    pageNo=predex;
                    //openBook(predex);
                    pdfView.jumpTo(predex);

                }catch (NumberFormatException e){

                    Toast.makeText(ReadingRoom.this, "Input is Invalid, Please enter an Integer", Toast.LENGTH_LONG).show();
                }


            }
        });

        builder.setNeutralButton("Move by", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try{

                    String index = String.valueOf(edit.getText());
                    int predex = Integer.parseInt(index);
                    pageNo+=predex;
                    //openBook(pageNo);
                    pdfView.jumpTo(pageNo);

                }catch (NumberFormatException e){

                    Toast.makeText(ReadingRoom.this, "Input is Invalid, Please enter an Integer", Toast.LENGTH_LONG).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.create().show();

        // dialog.show();

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
     super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString("bookSource",bookSource);
        outState.putInt("pageNo",pagerecord);
        outState.putLong("id",id);
        outState.putInt("sentIndex",sentIndex);

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
        getMenuInflater().inflate(R.menu.reading_room, menu);
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

           // Toast.makeText(ReadingRoom.this, "Current page: "+pageNo+" / Total Page number:"+pdfRenderer.getPageCount(), Toast.LENGTH_LONG).show();

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
            startActivity(new Intent(this,Notepad.class));

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, com.mactrix.www.readingnotepro.MediaStore.class));

        }else if(id==R.id.studystore){
            startActivity(new Intent(this,StudyStore.class));

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Not Yet in Playstore, Check back in few Days", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_send) {

            Toast.makeText(this, "Not Yet in Playstore, Check back in few Days", Toast.LENGTH_LONG).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStop(){
        super.onStop();


        downTimer.cancel();
        textToSpeech.shutdown();


        //downTimer.cancel();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        textToSpeech.shutdown();
    }

    @Override
    public void onInit(int status) {




        if(status==TextToSpeech.SUCCESS){
            int result = textToSpeech.setLanguage(Locale.ENGLISH);
            textToSpeech.setSpeechRate((float)0.86);
            textToSpeech.setOnUtteranceCompletedListener(this);




            if(result==TextToSpeech.LANG_NOT_SUPPORTED||result==TextToSpeech.LANG_MISSING_DATA){
                Toast.makeText(this, "This language is not Supported", Toast.LENGTH_LONG).show();
            }else{

            }

        }else{
            Toast.makeText(this, "Initialization Failed", Toast.LENGTH_LONG).show();
        }



    }

    public void speakout(String script){
        if(script==null||script.isEmpty()){

            speakout("Sorry, I can not read out this PDF file, because it's Text can not be extracted. Please bear with me or try another PDf file that is not Scanned. Thank you. ");

            Toast.makeText(this, "Sorry, I can not read out this PDF file, because it's Text can not be extracted. Please bear with me or try another PDf file that is not Scanned. Thank you.", Toast.LENGTH_LONG).show();


            return;
        }



        if(Build.VERSION.SDK_INT== Build.VERSION_CODES.LOLLIPOP){
            String utteranceID = hashCode()+"";
            textToSpeech.speak(script,TextToSpeech.QUEUE_FLUSH,null,utteranceID);



        }else{
            textToSpeech.speak(script,TextToSpeech.QUEUE_FLUSH,null);


        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {

        counter = pagerecord;

        if(counter != pdfView.getPageCount()-1){
            counter++;
            pdfView.jumpTo(10);

            try {
                extracttext = PdfTextExtractor.getTextFromPage(pdfReader,counter).trim()+"\n";
                speakout(extracttext);
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }



    private class AsynchronousClass extends AsyncTask<Void,Void,String>{

        int pages;

        public AsynchronousClass(int page){
            this.pages = page;
        }



        @Override
        protected String doInBackground(Void... voids) {

            if(action==null) {

                try {
                    extracttext = PdfTextExtractor.getTextFromPage(pdfReader, pages + 1);
                } catch (IOException e) {
                    extracttext ="";

                } catch (Exception e){

                    extracttext="";


                }
            }

            return extracttext;
        }

        @Override
        protected void onPostExecute(String result){

            if(action==null) {

                if (truth) {
                    speakout(result);
                }
            }


        }
    }



}
