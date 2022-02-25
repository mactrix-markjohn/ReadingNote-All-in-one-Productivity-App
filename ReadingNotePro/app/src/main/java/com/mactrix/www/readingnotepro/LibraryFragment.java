package com.mactrix.www.readingnotepro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mactrix on 8/31/2018.
 */

public class LibraryFragment extends Fragment {


    ListView listView;
    Context context;
    ArrayList<String> pdfArray;
    PDFAdapter pdfAdapter ;
    RelativeLayout waitinglayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle saveInstancepage){

        View view = inflater.inflate(R.layout.libraryfragment,parent,false);
        listView = (ListView)view.findViewById(R.id.librarylist);
        waitinglayout = (RelativeLayout)view.findViewById(R.id.waitinglayout);



        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        pdfArray = new ArrayList<>();

        //getPDFBook();
        //pdfAdapter = new PDFAdapter(context,pdfArray);
        //listView.setAdapter(pdfAdapter);
        waitinglayout.setVisibility(View.VISIBLE);

        new LibraryAsync().execute();




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
                return dir.getName().endsWith(".pdf");
            }
        });

        for(int i=0;i<filelist.length;i++){
            pdfArray.add(filelist[i].getAbsolutePath());
        }

    }

    public void callFile(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            retrievePDF(file);

        }else{
            ActivityCompat.requestPermissions(new ProNote(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},10);
        }
    }

    public void findPDFBook(File file){

        try {

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

        }catch (ArrayIndexOutOfBoundsException e){

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

        }catch (Exception e){

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



    }

    public void getPDFBook(){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            //  String state = Environment.getExternalStorageState();
            // if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)){
            //File[] file = null;

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
            //File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

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
                }catch (NullPointerException e){

                   duofile.toString();

                    // Toast.makeText(context, "Your External SD card is not mounted..", Toast.LENGTH_SHORT).show();

                }catch (ArrayIndexOutOfBoundsException e){
                    //Toast.makeText(context, "Unable to read from your External SD card", Toast.LENGTH_SHORT).show();
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




            Collections.sort(pdfArray, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });

        }else{
            ActivityCompat.requestPermissions(new ProNote(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        }

    }

    public class LibraryAsync extends AsyncTask<Void,String,String>{

        //AlertDialog.Builder builder;
        //ProgressBar progressBar;

        @Override
        protected String doInBackground(Void... voids) {

        //    builder = new AlertDialog.Builder(context);
      //      builder.setTitle("Please wait..");
    //        progressBar = new ProgressBar(context);
  //          builder.setView(progressBar);
//            builder.create().show();


            getPDFBook();

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            //super.onPostExecute(result);

          //  builder.create().dismiss();
            waitinglayout.setVisibility(View.GONE);

            pdfAdapter = new PDFAdapter(context,pdfArray);
            listView.setAdapter(pdfAdapter);


        }

    }






}
