package com.mactrix.www.readingnotepro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
/*import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;*/

import java.io.IOException;
import java.util.Locale;

public class TextRecognition extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TextToSpeech.OnInitListener {


    ImageButton backPress;
    ImageView canceltext;
    ImageView speakbutton;

    SurfaceView cameraView;
    CameraSource cameraSource;

    TextView textRecog;

    RelativeLayout surfacelayout;
    RelativeLayout textlayout;

    TextRecognizer textRecognizer;

    ImageButton shutter;

   // FirebaseVisionTextRecognizer visionTextRecognizer;

   // FirebaseVisionImage visionImage;
    private TextToSpeech textToSpeech;

    String speaktext="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FirebaseApp.initializeApp(this);

        textToSpeech = new TextToSpeech(this,this);

        speakbutton = (ImageView)findViewById(R.id.speakbutton);
        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak(speaktext);


            }
        });




        shutter = (ImageButton)findViewById(R.id.shutter);

        backPress = (ImageButton) findViewById(R.id.backpress);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Toast.makeText(TextRecognition.this, "Working", Toast.LENGTH_SHORT).show();
            }
        });

        canceltext = (ImageView) findViewById(R.id.cancelbutton);
        canceltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textlayout.setVisibility(View.INVISIBLE);
                //surfacelayout.setVisibility(View.VISIBLE);




            }
        });

        cameraView = (SurfaceView)findViewById(R.id.cameraView);
        textRecog = (TextView)findViewById(R.id.textView);

        surfacelayout = (RelativeLayout)findViewById(R.id.surfacelayout);
        textlayout = (RelativeLayout)findViewById(R.id.textlayout);


        textRecognizer = new TextRecognizer.Builder(this).build();

        if(!textRecognizer.isOperational()){
            Toast.makeText(this, "Detecting is not yet Available on this Device", Toast.LENGTH_SHORT).show();
        }else{

            cameraSource = new CameraSource.Builder(this,textRecognizer)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f)
                    .build();


            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    if(ContextCompat.checkSelfPermission(TextRecognition.this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

                        try {
                            cameraSource.start(cameraView.getHolder());
                        }catch (IOException e){
                            Toast.makeText(TextRecognition.this, "An Exception is Occured", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        ActivityCompat.requestPermissions(TextRecognition.this,new String[]{Manifest.permission.CAMERA},100);

                    }


                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                    cameraSource.stop();

                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {


                   // surfacelayout.setVisibility(View.INVISIBLE);
                    //textlayout.setVisibility(View.VISIBLE);
                    //cameraSource.stop();






                    final SparseArray<TextBlock> item = detections.getDetectedItems();
                    if(item.size() != 0){

                        textRecog.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();

                                for(int i = 0; i<item.size();i++){

                                    TextBlock items = item.valueAt(i);
                                    stringBuilder.append(items.getValue());
                                    stringBuilder.append("\n");
                                }
                               // textRecog.setText(stringBuilder.toString());


                            }
                        });

                    }



                }
            });




        }

        shutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraSource.takePicture(new CameraSource.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {

                        try {



                            Bitmap normal = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            Frame frame = new Frame.Builder()
                                    .setBitmap(normal)
                                    .build();


                            final SparseArray<TextBlock> item = textRecognizer.detect(frame);
                            if(item.size() != 0){

                                textRecog.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        StringBuilder stringBuilder = new StringBuilder();

                                        for(int i = 0; i<item.size();i++){

                                            TextBlock items = item.valueAt(i);
                                            stringBuilder.append(items.getValue());
                                            stringBuilder.append("\n");
                                        }
                                        textRecog.setText(stringBuilder.toString());
                                        textlayout.setVisibility(View.VISIBLE);
                                        speak(stringBuilder.toString());
                                        speaktext = stringBuilder.toString();


                                    }
                                });

                            }

                            //Bitmap normal = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                           /* visionImage = FirebaseVisionImage.fromBitmap(normal);
                            visionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

                            visionTextRecognizer.processImage(visionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {

                                    textRecog.setText(firebaseVisionText.getText());
                                    textlayout.setVisibility(View.VISIBLE);
                                    speak(firebaseVisionText.getText());
                                    speaktext = firebaseVisionText.getText();


                                }
                            });*/

                        }catch (Exception e){


                            Toast.makeText(TextRecognition.this, "Not Working", Toast.LENGTH_SHORT).show();

                            

                        }


                    }
                });



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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){

        switch (requestCode){
            case 100:{
                if(grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }

                    try{

                        cameraSource.start(cameraView.getHolder());

                    }catch (IOException e){
                        Toast.makeText(TextRecognition.this, "IOExceptions ", Toast.LENGTH_SHORT).show();
                    }


                }
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
        getMenuInflater().inflate(R.menu.text_recognition, menu);
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
