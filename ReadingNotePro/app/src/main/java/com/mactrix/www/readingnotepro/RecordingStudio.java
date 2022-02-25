package com.mactrix.www.readingnotepro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Mactrix on 4/18/2018.
 */

public class RecordingStudio {

    MediaRecorder mediaRecorder;
    boolean isRecording=false;
    boolean pause=false;
    boolean resume = false;
    Context context;
    public RecordingStudio(Context context){
        this.context = context;
        mediaRecorder = new MediaRecorder();

    }

    public boolean hasMicrophone(){
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    public void record(final String path){
        try {




            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(path);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            try {


                mediaRecorder.start();
            }catch (IllegalStateException e){
                noticeDialog();
                Intent intent = new Intent("recordstop");
                context.sendBroadcast(intent);
                Toast.makeText(context, "Start IllegalStateException", Toast.LENGTH_SHORT).show();
            }
            isRecording=true;
        } catch (IOException e) {
            mediaRecorder = new MediaRecorder();
            Toast.makeText(context, "IOException", Toast.LENGTH_SHORT).show();
        }catch (IllegalStateException e){

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(path);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.start();
            isRecording=true;
            Toast.makeText(context, "General IllegalStateException", Toast.LENGTH_SHORT).show();
        }/*catch (Exception e){
            mediaRecorder = new MediaRecorder();
            Toast.makeText(context, "General Exception", Toast.LENGTH_SHORT).show();
        }*/





    }

    public void noticeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setIcon(R.drawable.cancel);
        builder.setMessage("Hello, Sorry for the Sudden notice. This Song has cause an Error in the Recording Engine of this App, and So therefore this app can not record your voice.\n\nSorry for the Inconvenience...  ");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("recordnotice");
                context.sendBroadcast(intent);
            }
        });
        builder.create().show();
    }
    public boolean checkVersion(){
        return Build.VERSION.SDK_INT>= Build.VERSION_CODES.N;
    }

    public void pause(){
        try{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.pause();
                isRecording = false;
            }else{
                Toast.makeText(context, "This Feature is not supported by your Device, Please Stop the recording Instead", Toast.LENGTH_LONG).show();
            }

        }catch (IllegalStateException e){
            stop();
        }

    }

    public void resume(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder.resume();
            isRecording=true;

        }else{
        Toast.makeText(context, "This Feature is not supported by your Device, Please Stop the recording Instead", Toast.LENGTH_LONG).show();
    }
    }

    public void stop(){
        try {

            mediaRecorder.stop();
            isRecording=false;
        }catch (IllegalStateException e) {

            mediaRecorder = new MediaRecorder();
        }
    }

    public boolean isRecording(){

        return isRecording;
    }

    public void setOutput(String path){


    }

    public void setMaxDuration(int max){
        mediaRecorder.setMaxDuration(max);
    }

    public void reset(){
        mediaRecorder.reset();
    }



}
