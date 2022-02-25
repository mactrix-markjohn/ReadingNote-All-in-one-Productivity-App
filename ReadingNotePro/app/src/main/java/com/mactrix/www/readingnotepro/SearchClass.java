package com.mactrix.www.readingnotepro;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class SearchClass extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView backsearch;
    EditText searchspace;
    ImageView searchbutton;

    private File file;
    private ParcelFileDescriptor pfd;
    private PdfRenderer renderer;
    private PdfRenderer.Page page;
    private Bitmap map;

    ArrayList<String> arrayList;
    private ArrayList<String> listOfBook;
    ListView gridView;

    RelativeLayout oppslayout;
    RelativeLayout searchresultlayout;

    RelativeLayout showcaselayout;
    ImageView showcasepdf;
    ImageView cancelshowcase;
   // private AdView adView;
    //private AdRequest bannerRequest;
    //private InterstitialAd interstitialAd;
    //private AdRequest interRequest;
    private CountDownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // StartAppSDK.init(this,"204716271",true);
        setContentView(R.layout.activity_search_class);
       // registerBroad();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        listOfBook = new ArrayList<>();
        oppslayout = (RelativeLayout)findViewById(R.id.oppslayout);
        searchresultlayout = (RelativeLayout)findViewById(R.id.searchresultlayout);
        gridView = (ListView)findViewById(R.id.bookgrid);

       // getPDFBook();
        new SearchAsync().execute();

        showcaselayout = (RelativeLayout)findViewById(R.id.showcaselayout);
        showcasepdf = (ImageView)findViewById(R.id.pdfshowcase);
        cancelshowcase = (ImageView)findViewById(R.id.cancelshowcase);


        cancelshowcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaselayout.setVisibility(View.GONE);
            }
        });


        backsearch = (ImageView)findViewById(R.id.backsearch);
        searchspace = (EditText)findViewById(R.id.searchspace);
        searchbutton = (ImageView)findViewById(R.id.searchbutton);

        backsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });



        searchspace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchWord = String.valueOf(v.getText());
                arrayList = new ArrayList<>();

                for(int i=0;i<listOfBook.size();i++){
                    if(listOfBook.get(i).toLowerCase().contains(searchWord.toLowerCase())){
                        arrayList.add(listOfBook.get(i));
                    }
                }

                SearchAdapter searchAdapter = new SearchAdapter(SearchClass.this,arrayList);
                gridView.setAdapter(searchAdapter);

                if(gridView.getCount()>0){
                    oppslayout.setVisibility(View.GONE);
                    searchresultlayout.setVisibility(View.VISIBLE);
                }else{
                    searchresultlayout.setVisibility(View.GONE);
                    oppslayout.setVisibility(View.VISIBLE);
                }






                return true;
            }
        });

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String searchWord = String.valueOf(searchspace.getText());
                arrayList = new ArrayList<>();

                for(int i=0;i<listOfBook.size();i++){
                    if(listOfBook.get(i).toLowerCase().contains(searchWord.toLowerCase())){
                        arrayList.add(listOfBook.get(i));
                    }
                }

                SearchAdapter searchAdapter = new SearchAdapter(SearchClass.this,arrayList);
                gridView.setAdapter(searchAdapter);


                if(gridView.getCount()>0){
                    oppslayout.setVisibility(View.GONE);
                    searchresultlayout.setVisibility(View.VISIBLE);
                }else{
                    searchresultlayout.setVisibility(View.GONE);
                    oppslayout.setVisibility(View.VISIBLE);
                }

            }
        });



        /*MobileAds.initialize(this,"ca-app-pub-2742716340205774~3983344135");

        adView = (AdView)findViewById(R.id.adView);
        bannerRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2742716340205774/8727462404");
        interRequest = new AdRequest.Builder().build();*/


        downTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished == 14000) {
                    Toast.makeText(SearchClass.this, "Sorry, Ads will be shown in less than 10 secs", Toast.LENGTH_LONG).show();
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


    public void findPDFBook(File file){
        String pattern = ".pdf";
        File[] filelist = file.listFiles();

        if(file!=null){
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


            String state = Environment.getExternalStorageState();

            // if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)){
            File[] file = null;

          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                file = getExternalFilesDirs(null);

            }
            if(file!=null) {
                if(file.length>1) {
                    if(Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
                        findPDFBook(file[1]);}
                }
                findPDFBook(file[0]);
            }*/

            String storageState = Environment.getExternalStorageState();

            File[] duofile =getExternalFilesDirs(null);

            String internal=duofile[0].getAbsolutePath();

            int index=0;
            int indexx = 0;
            int count =0;
            int counting =0;

            for(int i=0;i<internal.length();i++){

                char a = internal.charAt(i);

                if(a=='/'){
                    count++;

                    if(count==4) {

                        index = i;

                        break;
                    }
                }

            }

            String interefine = internal.substring(0,index);

            File interFile = new File(interefine);

            findPDFBook(interFile);


            if(storageState.equalsIgnoreCase(Environment.MEDIA_MOUNTED)){

                try {


                    if(duofile.length==2) {

                        String external = duofile[1].getAbsolutePath();

                        for (int i = 0; i < external.length(); i++) {

                            char p = external.charAt(i);

                            if (p == '/') {
                                counting++;

                                if (counting == 3) {

                                    indexx = i;

                                    break;
                                }
                            }

                        }

                        String exterefine = external.substring(0, indexx);

                        File exterFile = new File(exterefine);

                        findPDFBook(exterFile);
                    }

                }catch (ArrayIndexOutOfBoundsException e){
                    duofile.toString();
                }catch (NullPointerException e){
                    duofile.toString();
                }catch (Exception e){
                    duofile.toString();
                }

            }
            // Toast.makeText(context, ""+interefine+" ,,,,"+exterefine, Toast.LENGTH_LONG).show();

            //Toast.makeText(context, ""+interefine+" ,,,,"+exterefine, Toast.LENGTH_LONG).show();



            //}

            //String interefine = internal.substring(0,internal.indexOf("/",9)+2);
            //String exterefine = external.substring(0,external.indexOf("/",9));




            Collections.sort(listOfBook, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            //}



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




    private class SearchAdapter extends BaseAdapter{

        private final SharePref sharePref;
        private final Set<String> stringSet;
        LayoutInflater inflater;
        Context context;
        ArrayList<String> searchbooks;
        Dialog dialog;

        //RelativeLayout showcaselayout;
        //ImageView showcasepdf;
        //ImageView cancelshowcase;


        public SearchAdapter(Context context,ArrayList<String> searchbooks){
            this.context = context;
            this.searchbooks = searchbooks;
            inflater = LayoutInflater.from(context);

            dialog = new Dialog(context);

            sharePref = new SharePref(context,"Recent");



            if(!sharePref.getStringSet().isEmpty()){
                stringSet = sharePref.getStringSet();
            }else{
                stringSet = new ArraySet<>();
            }


        }

        private class ViewHolder{
            TextView name;
            ImageView imageView;
            TextView size;


            public ViewHolder(View view , final int position){

                name = (TextView)view.findViewById(R.id.pdfname);
                imageView = (ImageView)view.findViewById(R.id.pdfmore);
                size = (TextView)view.findViewById(R.id.pdfsize);

                //Glide.with(context).load(R.mipmap.pdfblackbackground).asBitmap().into(imageView);

                name.setText(bookBase(position));
                size.setText(pdfSize(position));

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu = new PopupMenu(context,v);
                        popupMenu.inflate(R.menu.pdfmenu);
                        popupMenu.show();

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch(item.getItemId()){
                                    case R.id.openpdf:
                                        Bundle bundle = new Bundle();
                                        bundle.putString("selectedbook",searchbooks.get(position));
                                        Intent intent = new Intent(context,ReadingRoom.class);
                                        intent.putExtra("selectedbook",bundle);

                                        stringSet.add(searchbooks.get(position));
                                        sharePref.setStringSet(stringSet);

                                        context.startActivity(intent);


                                        break;
                                    case R.id.previewpdf:


                                        View preview = LayoutInflater.from(context).inflate(R.layout.pdfview,null,false);

                                        PDFView pdfView = (PDFView)preview.findViewById(R.id.previewpdf);
                                        pdfView.fromFile(new File(searchbooks.get(position))).pageFling(true).pageSnap(true).pageFitPolicy(FitPolicy.BOTH).swipeHorizontal(false).enableSwipe(false).load();
                                        dialog.setContentView(preview);
                                        dialog.show();

                                       /* Intent intenting = new Intent("showfilter");
                                        intenting.putExtra("showcase",searchbooks.get(position));

                                        context.sendBroadcast(intenting);*/

                                        break;
                                    case R.id.location:

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("PDF Location");
                                        builder.setMessage(""+searchbooks.get(position));
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder.create().show();

                                        break;

                                }

                                return true;
                            }
                        });


                    }
                });



                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("selectedbook",searchbooks.get(position));
                        Intent intent = new Intent(context,ReadingRoom.class);
                        intent.putExtra("selectedbook",bundle);

                        stringSet.add(searchbooks.get(position));
                        sharePref.setStringSet(stringSet);

                        context.startActivity(intent);

                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Intent intent = new Intent("showfilter");
                        intent.putExtra("showcase",searchbooks.get(position));

                        context.sendBroadcast(intent);





                        return true;
                    }
                });



            }
        }

        public String pdfSize(int index){
            String filepath;

            String bigsize;
            String maining;
            String sizing;
            String theSize;

            long size;
            long big;

            filepath = searchbooks.get(index);
            File file = new File(filepath);
            long length = file.length();

            long main = length/1000000;

            big = length/1000000000;


            if(big>0){

                main = (length%1000000000)/1000000;

                bigsize = ""+big;

                if(main<10){
                   maining = "0"+main;
                }else{
                    maining=""+main;
                }


                theSize = bigsize+"."+maining+" GB";
            }else {

                if (main > 0) {
                    size = (length % 1000000) / 10000;

                    maining = "" + main;

                    if (size < 10) {
                        sizing = "0" + size;
                    } else {
                        sizing = "" + size;
                    }

                    theSize = maining + "." + sizing + " MB";

                } else {

                    size = length / 1000;

                    sizing = "" + size;

                    theSize = sizing + " KB";
                }
            }



            return theSize;



        }

        public String bookBase(int index){
            String theText;
            String correct;

        /*Cursor curText = db.getBook((long)index+1);
        if(curText!=null){
            curText.moveToFirst();
            theText = curText.getString(curText.getColumnIndexOrThrow(LibraryDatabase.TITLE));
             correct = theText.substring(theText.lastIndexOf("/")+1,theText.lastIndexOf(".")-1);
        }*/
            theText = searchbooks.get(index);
            correct = theText.substring(theText.lastIndexOf("/")+1,theText.lastIndexOf("."));


            return  correct;
        }





        @Override
        public int getCount() {
            return searchbooks.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {

                if(convertView==null){
                    convertView = inflater.inflate(R.layout.pdflist,parent,false);
                }

                new ViewHolder(convertView,position);

            }catch (InflateException e){
                if(convertView==null){
                    convertView = inflater.inflate(R.layout.pdflist,parent,false);
                }

                new ViewHolder(convertView,position);

            }catch (Exception e){
                if(convertView==null){
                    convertView = inflater.inflate(R.layout.pdflist,parent,false);
                }

                new ViewHolder(convertView,position);

            }
            return convertView;
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
        getMenuInflater().inflate(R.menu.search_class, menu);
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
    public void onDestroy(){
        super.onDestroy();
        downTimer.cancel();
       // unregisterReceiver(receiver);
    }

    public class SearchAsync extends AsyncTask<Void,String, String>{

        @Override
        protected String doInBackground(Void... voids) {
            getPDFBook();

            return null;
        }
    }
}
