package com.mactrix.www.readingnotepro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
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
//import com.mopub.mobileads.MoPubErrorCode;
//import com.mopub.mobileads.MoPubInterstitial;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class Notepad extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TextToSpeech.OnInitListener {


    ImageView add;
    ImageView check;
    ImageView list;
    ImageView delete;
    ImageView video;
    ImageView record;
    ImageView audiostore;
    ImageView videostore;
    ImageView listenpad;
    ImageView speakpad;

    EditText padtitle;
    EditText padnote;
    private CountDownTimer downTimer;
   // private MoPubInterstitial interstitial;

    TextToSpeech textToSpeech;
   // private AdView adView;
    //private AdRequest bannerRequest;
    //InterstitialAd interstitialAd;
    //AdRequest interRequest;


    enum Storage{SAVE,UPDATE}
    Storage storage;
    enum UpdateMode{DIRECT,SENT}
    UpdateMode updateMode;

    StudyDatabase studyDatabase;

    long id;
    int sentIndex=-1;

    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // StartAppSDK.init(this,"204716271",true);
        setContentView(R.layout.activity_notepad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        storage = Storage.SAVE;
        updateMode = UpdateMode.DIRECT;


        dialog = new Dialog(this);
        textToSpeech = new TextToSpeech(this,this);

        add = (ImageView)findViewById(R.id.add);
        check = (ImageView)findViewById(R.id.check);
        list = (ImageView)findViewById(R.id.list);
        delete = (ImageView)findViewById(R.id.delete);
        video = (ImageView)findViewById(R.id.video);
        record = (ImageView)findViewById(R.id.record);
        audiostore = (ImageView)findViewById(R.id.audiostore);
        videostore = (ImageView)findViewById(R.id.videostore);
        listenpad = (ImageView)findViewById(R.id.listenpad);
        speakpad = (ImageView)findViewById(R.id.speakpad);

        padtitle = (EditText)findViewById(R.id.padtitle);
        padnote = (EditText)findViewById(R.id.padnote);

        studyDatabase = new StudyDatabase(this,null,null,1);


//        MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

       // adView = (AdView)findViewById(R.id.adView);
       // bannerRequest = new AdRequest.Builder().build();
        //adView.loadAd(bannerRequest);

        //interstitialAd = new InterstitialAd(this);
        //interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/1395767294");
        //interRequest = new AdRequest.Builder().build();


        downTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 10000) {
                    Toast.makeText(Notepad.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
                }

                if(millisUntilFinished == 20000){
                    Toast.makeText(Notepad.this, "Sorry, Ads will be shown soon", Toast.LENGTH_LONG).show();
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


                /*interstitial = new MoPubInterstitial(Notepad.this,"e26e9286ce3d4b8994a98c22c1066868");
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
                               // public void run() {
                                 //   startActivity(new Intent(Musical.this,LovedThings.class));
                                //}
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



       final Bundle bundle = getIntent().getBundleExtra("position");

       if(bundle!=null){
           sentIndex = bundle.getInt("position");
           fillNotepad(sentIndex);
       }



       listenpad.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              listening();
           }
       });

       speakpad.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               speak(String.valueOf(padnote.getText()));
           }
       });

       add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Notepad.this, Notepad.class));

                }
            });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu popupMenu = new PopupMenu(Notepad.this,v);
                popupMenu.inflate(R.menu.savedelmenu);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()){
                            case R.id.savenote:

                                String title= String.valueOf(padtitle.getText());
                                String note= String.valueOf(padnote.getText());

                                if(storage==Storage.SAVE){

                                    id = studyDatabase.addNote(title,note,"writtennote");
                                    storage = Storage.UPDATE;
                                    Toast.makeText(Notepad.this, "Note has been Saved", Toast.LENGTH_LONG).show();


                                }else if(storage==Storage.UPDATE){


                                    if(updateMode==UpdateMode.DIRECT){

                                        studyDatabase.updateNote(id,title,note,"writtennote");

                                    }else if(updateMode==UpdateMode.SENT){

                                        Cursor cursor = studyDatabase.getAllNote();

                                        if(cursor!=null){
                                            if(cursor.getCount()>=0){

                                                cursor.moveToPosition(sentIndex);

                                                long ID = cursor.getLong(cursor.getColumnIndex(StudyDatabase.ID));
                                                studyDatabase.updateNote(ID,title,note,"writtennote");



                                            }
                                        }



                                    }

                                    Toast.makeText(Notepad.this, "Note has been Updated", Toast.LENGTH_SHORT).show();
                                }


                                break;
                            case R.id.deletenote:



                                AlertDialog.Builder builder = new AlertDialog.Builder(Notepad.this);
                                builder.setIcon(R.drawable.cancel);
                                builder.setMessage("Are you sure you want to Delete this Note?");
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
                                            if(cursor.getCount()>=0 && sentIndex != -1){

                                                cursor.moveToPosition(sentIndex);

                                                long ID = cursor.getLong(cursor.getColumnIndex(StudyDatabase.ID));
                                                //studyDatabase.updateNote(ID,title,note,"writtennote");
                                                studyDatabase.deleteNote(ID);
                                                Toast.makeText(Notepad.this, "Note has been Deleted..", Toast.LENGTH_SHORT).show();
                                                onBackPressed();



                                            }else{
                                                Toast.makeText(Notepad.this, "You can not Delete this note Because it is not yet Saved. Thank you", Toast.LENGTH_LONG).show();
                                                speak("You can not Delete this note because it is not yet Saved. Thank you");
                                            }
                                        }


                                    }
                                });
                                builder.create().show();

                                break;


                            case R.id.stopreading:
                                if(textToSpeech.isSpeaking()){
                                    textToSpeech.stop();
                                }


                                break;
                        }


                        return true;
                    }
                });













            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notepad.this,StudyStore.class));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               padnote.setText("");




            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notepad.this,VideoRecorder.class));
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notepad.this,AudioRecorder.class));
            }
        });
        audiostore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notepad.this,MediaStore.class));

            }
        });
        videostore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundling = new Bundle();
                bundling.putInt("mediastore",1);


                startActivity(new Intent(Notepad.this,MediaStore.class).putExtra("mediastore",bundling));
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


    public void listening(){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());

        dialog.setContentView(R.layout.listeningdialog);
        dialog.show();

        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

                dialog.dismiss();
            }

            @Override
            public void onResults(Bundle results) {

                dialog.dismiss();

                ArrayList<String> voiceresult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                for(String match : voiceresult){

                    padnote.append(match+" ");

                }


            }

            @Override
            public void onPartialResults(Bundle partialResults) {



            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        };

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(intent);


    }


    public void fillNotepad(int sentPosition){
        Cursor cursor = studyDatabase.getAllNote();

        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToPosition(sentPosition);

                String title=cursor.getString(cursor.getColumnIndex(StudyDatabase.TITLE));
                String note=cursor.getString(cursor.getColumnIndex(StudyDatabase.NOTE));

                padtitle.setText(title);
                padnote.setText(note);

                storage = Storage.UPDATE;
                updateMode = UpdateMode.SENT;


            }
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
       // getMenuInflater().inflate(R.menu.notepad, menu);
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

    @Override
    public void onInit(int status) {

        if(status== TextToSpeech.SUCCESS){

            int result = textToSpeech.setLanguage(Locale.US);
            textToSpeech.setSpeechRate((float) 0.92);

            if(result==TextToSpeech.LANG_NOT_SUPPORTED||result==TextToSpeech.LANG_MISSING_DATA){
                Toast.makeText(this, "Languague is not Supported", Toast.LENGTH_SHORT).show();
            }else{

            }


        }else{
            Toast.makeText(this, "Initialization Failed", Toast.LENGTH_SHORT).show();
        }

    }

    public void speak(String script){

        if(script==null||script.isEmpty()){
            return;
        }

        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP){

            String utteranceID = this.hashCode()+"";
            textToSpeech.speak(script,TextToSpeech.QUEUE_FLUSH,null,utteranceID);


        }else{
            textToSpeech.speak(script,TextToSpeech.QUEUE_FLUSH,null);
        }


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        textToSpeech.shutdown();
    }
}
