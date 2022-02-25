package com.mactrix.www.readingnotepro;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mactrix on 4/10/2018.
 */

public class AudioStore extends Fragment {



    Context context;
    ListView listView;
    ArrayList<String> arrayList;
    AudioAdapter audioAdapter;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance){

        View view = inflater.inflate(R.layout.app_bar_audio_store,parent,false);
        listView = (ListView)view.findViewById(R.id.audiostorelist);





        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerFile();
        //regiterResuming();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            new AudioAsync().execute();

            //getAllAudio();

            //getAudioRecording(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File/AudioRecording"));

            //audioAdapter = new AudioAdapter(context,arrayList);
            //listView.setAdapter(audioAdapter);


        }
    }

    @Override
    public void onResume(){
        super.onResume();


    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                new AudioAsync().execute();

                //getAllAudio();
               // getAudioRecording(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File/AudioRecording"));
               // audioAdapter = new AudioAdapter(context,arrayList);
               // listView.setAdapter(audioAdapter);


            }

            onResume();

        }
    };

    public void registerFile(){
        IntentFilter intentFilter = new IntentFilter("update");
        context.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        context.unregisterReceiver(broadcastReceiver);
    }


    public void getAudioRecording(File file){
        arrayList = new ArrayList<>();

        String pattern = ".3gpp";
        String patht=".mp3";
        File[] filing = file.listFiles();


        if(filing!=null){
            for (int i =0;i<filing.length;i++){

                if(filing[i].isDirectory()){
                    getAudioRecording(filing[i]);
                }else{

                    if(filing[i].getName().endsWith(pattern)||filing[i].getName().endsWith(patht)){

                        arrayList.add(filing[i].getAbsolutePath());


                    }





                }

            }

            }
        }

        public class  AudioAsync extends AsyncTask<Void, String, String>{

            @Override
            protected String doInBackground(Void... voids) {

                getAudioRecording(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReadingNote Pro File/AudioRecording"));

                //getAllAudio();


                return null;
            }

            @Override
            protected void onPostExecute(String result){

                audioAdapter = new AudioAdapter(context,arrayList);
                listView.setAdapter(audioAdapter);

            }
        }

        public void getAllAudio(){

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

            getAudioRecording(interFile);


            if(storageState.equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
                String external=duofile[1].getAbsolutePath();

                for(int i=0;i<external.length();i++){

                    char p = external.charAt(i);

                    if(p=='/'){
                        counting++;

                        if(counting==3) {

                            indexx = i;

                            break;
                        }
                    }

                }

                String exterefine = external.substring(0,indexx);

                File exterFile = new File(exterefine);

                getAudioRecording(exterFile);

            }

            Collections.sort(arrayList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });


        }










}
