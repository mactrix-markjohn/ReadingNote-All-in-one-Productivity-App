package com.mactrix.www.readingnotepro;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Mactrix on 8/31/2018.
 */

public class VoiceSearchFragment extends Fragment implements TextToSpeech.OnInitListener {


    ListView listView;
    Context context;
    ArrayList<String> pdfArray;
    VoicePDFAdapter pdfAdapter ;
    ArrayList<String> matchPdf;

    EditText searchField;
    ImageView searchmic;

    RelativeLayout noresultLayout;

    Dialog dialog;
    //Dialog tapdialog;

    View tabview;
    LayoutInflater inflater;
    ImageView tabimage;

    TextToSpeech textToSpeech;





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saveInstancepage){
        View view=inflater.inflate(R.layout.viocefragment,parent,false);

        listView = (ListView)view.findViewById(R.id.viocelist);
        searchField = (EditText)view.findViewById(R.id.searchfield);
        searchmic = (ImageView)view.findViewById(R.id.searchmic);
        noresultLayout = (RelativeLayout)view.findViewById(R.id.noresultLayout);



        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //registerReceiver();
        //registerEditReceive();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        pdfArray = new ArrayList<>();
        //getPDFBook();
        new LibraryAsync().execute();
        textToSpeech = new TextToSpeech(context,this);


        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //context.sendBroadcast(new Intent("editReceive"));

                String searchInput = String.valueOf(v.getText());
                matchPdf = new ArrayList<>();


                for(int i =0; i<pdfArray.size();i++) {

                    if (pdfArray.get(i).toLowerCase().contains(searchInput.toLowerCase())) {
                        matchPdf.add(pdfArray.get(i));
                    }
                }

                    pdfAdapter = new VoicePDFAdapter(context,matchPdf);
                    listView.setAdapter(pdfAdapter);

                    if(listView.getCount()>0){
                        noresultLayout.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        //speakout(""+listView.getCount()+" Result Found");
                    }else{
                        noresultLayout.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        speakout("Sorry, No Result Found. Please Try Again");

                    }



                return true;
            }
        });

        searchmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startListening();
            }
        });





    }

    public void startListening(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,context.getPackageName());

        dialog = new Dialog(context);
        //tapdialog = new Dialog(context);

        dialog.setContentView(R.layout.listeningdialog);
        dialog.show();

       /* inflater = LayoutInflater.from(context);
        tabview = inflater.inflate(R.layout.listeningtabagain,null,false);
        tabimage = (ImageView)tabview.findViewById(R.id.tabagain);

        tabimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListening();
            }
        });*/

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

                String searchInput="";
                ArrayList<String> voiceresult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(voiceresult == null){
                    // Call tap again dialog here
                    //tapdialog.setContentView(R.layout.listeningtabagain);
                    //tapdialog.show();

                }else {
                    // Send Broadcast with Arraylist or run the search here and also dismiss the first dialog


                    matchPdf = new ArrayList<>();

                    for (String match : voiceresult) {
                        searchInput = match;
                    }
                    searchField.setText(searchInput);

                    for (int i = 0; i < pdfArray.size(); i++) {

                        if (pdfArray.get(i).toLowerCase().contains(searchInput.toLowerCase())) {
                            matchPdf.add(pdfArray.get(i));
                        }
                    }

                        pdfAdapter = new VoicePDFAdapter(context, matchPdf);
                        listView.setAdapter(pdfAdapter);

                        if (listView.getCount() > 0) {
                            noresultLayout.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                          //  speakout(""+listView.getCount()+" Result Found");
                        } else {
                            noresultLayout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            speakout("Sorry, No Result Found. Please Try Again");
                        }


                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {


            }
        };

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(intent);

    }




    // BroadcastReceiver for Voice Search
    final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter("receiving");
        context.registerReceiver(receiver,intentFilter);
    }

    // BroadCastReceiver for Edittext
    final BroadcastReceiver editReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String searchInput = String.valueOf(searchField.getText());
            matchPdf = new ArrayList<>();


            for(int i =0; i<pdfArray.size();i++) {

                if (pdfArray.get(i).toLowerCase().contains(searchInput.toLowerCase())) {
                    matchPdf.add(pdfArray.get(i));
                }
            }

                pdfAdapter = new VoicePDFAdapter(context,matchPdf);
                listView.setAdapter(pdfAdapter);

                if(listView.getCount()>0){
                    noresultLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }else{
                    noresultLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }







        }
    };

    public void registerEditReceive(){
        IntentFilter intentFilter = new IntentFilter("editReceive");
        context.registerReceiver(editReceive,intentFilter);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        textToSpeech.shutdown();
        //context.unregisterReceiver(receiver);
       // context.unregisterReceiver(editReceive);
    }


    public void retrievePDF(File file){

        String pattern = ".pdf";
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.endsWith(".pdf")));
            }
        };

        File[] filelist = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pdf");
            }
        });

        for(int i=0;i<filelist.length;i++){
            pdfArray.add(filelist[i].getAbsolutePath());
        }

    }

    public void callFile(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            File file = Environment.getExternalStorageDirectory();
            retrievePDF(file);

        }else{
            ActivityCompat.requestPermissions(new ProNote(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},10);
        }
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
                        pdfArray.add(filelist[i].getAbsolutePath());
                        //}
                    }
                }
            }
        }
    }

    public void getPDFBook(){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){


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


           // Toast.makeText(context, ""+Environment.getExternalStorageDirectory().getAbsolutePath(), Toast.LENGTH_SHORT).show();

            String storageState = Environment.getExternalStorageState();

            File[] duofile =context.getExternalFilesDirs(null);

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

                    duofile[0].getAbsolutePath();

                }catch (NullPointerException e){
                    duofile[0].getAbsolutePath();
                }catch (Exception e){
                    duofile[0].getAbsolutePath();
                }

            }
            // Toast.makeText(context, ""+interefine+" ,,,,"+exterefine, Toast.LENGTH_LONG).show();

            //Toast.makeText(context, ""+interefine+" ,,,,"+exterefine, Toast.LENGTH_LONG).show();



            //}

            //String interefine = internal.substring(0,internal.indexOf("/",9)+2);
            //String exterefine = external.substring(0,external.indexOf("/",9));




            Collections.sort(pdfArray, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            //}



        }else{
            ActivityCompat.requestPermissions(new ProNote(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        }

    }

    @Override
    public void onInit(int status) {

        if(status==TextToSpeech.SUCCESS){
            int result = textToSpeech.setLanguage(Locale.US);


            if(result==TextToSpeech.LANG_NOT_SUPPORTED||result==TextToSpeech.LANG_MISSING_DATA){
                Toast.makeText(context, "This language is not Supported", Toast.LENGTH_LONG).show();
            }else{

            }

        }else{
            Toast.makeText(context, "Initialization Failed", Toast.LENGTH_LONG).show();
        }



    }

    public void speakout(String script){
        if(script==null||script.isEmpty()){
            return;
        }

        if(Build.VERSION.SDK_INT== Build.VERSION_CODES.LOLLIPOP){
            String utteranceID = context.hashCode()+"";
            textToSpeech.speak(script,TextToSpeech.QUEUE_FLUSH,null,utteranceID);


        }else{
            textToSpeech.speak(script,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    public class LibraryAsync extends AsyncTask<Void,String,String> {


        @Override
        protected String doInBackground(Void... voids) {

            getPDFBook();

            return null;
        }


    }
}
