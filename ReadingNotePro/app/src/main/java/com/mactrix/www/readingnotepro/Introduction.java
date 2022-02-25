package com.mactrix.www.readingnotepro;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
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
import android.widget.Toast;

import java.io.File;
import java.util.Set;

public class Introduction extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    Intent intent;
    String actioncall;
    String type;

    Uri uri;
    private SharePref sharePref;
    private Set<String> stringSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introduction);

        sharePref = new SharePref(this,"Recent");





        if(!sharePref.getStringSet().isEmpty()){
            stringSet = sharePref.getStringSet();
        }else{
            stringSet = new ArraySet<>();
        }


        intent = getIntent();



        if(intent != null){
            actioncall = intent.getAction();
            type = intent.getType();

            if(Intent.ACTION_VIEW.contains(actioncall)&&type.contains("pdf")){

                uri = intent.getData();


               // Toast.makeText(this, ""+new File(intent.toString()).getAbsolutePath(), Toast.LENGTH_LONG).show();



            }else if(Intent.ACTION_SEND.contains(actioncall)&&type.contains("pdf")){

                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

            }
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File");
        file.mkdir();

        File vFile =  new File (Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File/Video");
        vFile.mkdir();

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(Intent.ACTION_MAIN.contains(actioncall)) {
                        startActivity(new Intent(Introduction.this, ProNote.class));
                    }else if(Intent.ACTION_SEND.contains(actioncall)||Intent.ACTION_VIEW.contains(actioncall)){

                        Bundle bundle = new Bundle();
                        bundle.putString("selectedbook",uri.toString());
                        bundle.putString("action","ActionCall");
                        Intent intent = new Intent(Introduction.this,ReadingRoom.class);
                        intent.putExtra("selectedbook",bundle);

                      //  stringSet.add(new File(uri.getPath()).getAbsolutePath());
                       // sharePref.setStringSet(stringSet);



                        startActivity(intent);

                    }

                    finish();
                }
            }, 3000);
        }else{

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},10);


        }



        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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

    @Override
    public void onRequestPermissionsResult(int requestCode,String permission[],int[] grantResult){
        switch(requestCode){
            case 10:

                if(grantResult[0]== PackageManager.PERMISSION_GRANTED){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Introduction.this,ProNote.class));

                            finish();
                        }
                    }, 2000);

                }else{
                    requestAgain();

                }

                break;


        }
    }

    public void makeRequest(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},10);

    }

    public void requestAgain(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)||ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Hello Users, We could see from your choose of Option, you do not want to give permission to this Features.\nBut this permissions are really necessary for the Proper functioning of the app.\nFunctions which include fetching all PDF files in your devices and the Audio recording of voice.. ");
            builder.setPositiveButton("Request Permission", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   makeRequest();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            builder.create().show();
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Hello Users, We could see from your choose of Option, you do not want to give permission to this Features.\nBut this permissions are really necessary for the Proper functioning of the app.\nFunctions which include fetching all PDF files in your devices and the Audio recording of voice.. ");
            builder.setPositiveButton("Request Permission", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    makeRequest();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            builder.create().show();

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
        getMenuInflater().inflate(R.menu.introduction, menu);
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
}
