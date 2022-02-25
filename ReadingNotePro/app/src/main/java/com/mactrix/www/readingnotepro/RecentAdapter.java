package com.mactrix.www.readingnotepro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.ArraySet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Mactrix on 9/3/2018.
 */

public class RecentAdapter extends BaseAdapter {

   // Set<String> stringset;
    Context context;
    LayoutInflater inflater;
   // Object[] arrayString;
   // SharePref sharePref;

    Dialog dialog;

    RecentDatabase database;
    Cursor cursor;

    public RecentAdapter(Context context, Set<String> strings){

        //stringset = strings;
        this.context = context;
        inflater = LayoutInflater.from(context);
        //arrayString = stringset.toArray();
        //sharePref = new SharePref(context,"Recent");
        dialog = new Dialog(context);

    }

    public RecentAdapter(Context context,RecentDatabase recentDatabase){
        this.context = context;
        inflater = LayoutInflater.from(context);
        dialog = new Dialog(context);

        database = recentDatabase;
    }


    public class ViewHolder{

        TextView name;
        TextView size;
        ImageView imageView;


        public ViewHolder(View view, final int position){

            name = (TextView)view.findViewById(R.id.pdfname);
            size = (TextView)view.findViewById(R.id.pdfsize);
            imageView = (ImageView)view.findViewById(R.id.pdfmore);

            cursor = database.getAllNote();

            name.setText(bookBase(position));
            size.setText(pdfSize(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popupMenu = new PopupMenu(context,v);
                    popupMenu.inflate(R.menu.recentmenu);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            switch(item.getItemId()){
                                case R.id.openpdf:

                                    if(cursor != null){
                                        if(cursor.getCount()>0){

                                            cursor.moveToPosition(position);

                                            Bundle bundle = new Bundle();
                                            bundle.putString("selectedbook",cursor.getString(cursor.getColumnIndex(RecentDatabase.MEDIAFILE)));
                                            Intent intent = new Intent(context,ReadingRoom.class);
                                            intent.putExtra("selectedbook",bundle);
                                            context.startActivity(intent);


                                        }


                                    }


                                   /*( Bundle bundle = new Bundle();
                                    bundle.putString("selectedbook",(String)arrayString[position]);
                                    Intent intent = new Intent(context,ReadingRoom.class);
                                    intent.putExtra("selectedbook",bundle);
                                    context.startActivity(intent);*/


                                    break;
                                case R.id.previewpdf:

                                    if(cursor !=null){
                                        if(cursor.getCount()>0){

                                            cursor.moveToPosition(position);


                                            View preview = LayoutInflater.from(context).inflate(R.layout.pdfview,null,false);

                                            PDFView pdfView = (PDFView)preview.findViewById(R.id.previewpdf);
                                            pdfView.fromFile(new File(cursor.getString(cursor.getColumnIndex(RecentDatabase.MEDIAFILE)))).pageFling(true).pageSnap(true).pageFitPolicy(FitPolicy.BOTH).swipeHorizontal(false).enableSwipe(false).load();
                                            dialog.setContentView(preview);
                                            dialog.show();




                                            /*View preview = LayoutInflater.from(context).inflate(R.layout.pdfview,null,false);

                                            PDFView pdfView = (PDFView)preview.findViewById(R.id.previewpdf);
                                            pdfView.fromFile(new File((String) arrayString[position])).pageFling(true).pageSnap(true).pageFitPolicy(FitPolicy.BOTH).swipeHorizontal(false).enableSwipe(false).load();
                                            dialog.setContentView(preview);
                                            dialog.show();*/

                                        }
                                    }




                                    break;
                                case R.id.location:

                                    if(cursor != null){
                                        if(cursor.getCount()>0){


                                            cursor.moveToPosition(position);


                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("PDF Location");
                                            builder.setMessage(""+cursor.getString(cursor.getColumnIndex(RecentDatabase.MEDIAFILE)));
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.create().show();

                                        }
                                    }

                                   /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("PDF Location");
                                    builder.setMessage(""+arrayString[position]);
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.create().show();*/

                                    break;
                                case R.id.delete:


                                    if(cursor !=null){
                                        if(cursor.getCount()>0){
                                            cursor.moveToPosition(position);

                                            long id = cursor.getLong(cursor.getColumnIndex(RecentDatabase.ID));
                                            database.deleteNote(id);
                                        }
                                    }

                                   /* Object removing = arrayString[position];
                                    stringset.remove(removing);
                                    sharePref.setStringSet(stringset);*/

                                    context.sendBroadcast(new Intent("recent"));

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

                   /* Bundle bundle = new Bundle();
                    bundle.putString("selectedbook",(String) arrayString[position]);
                    Intent intent = new Intent(context,ReadingRoom.class);
                    intent.putExtra("selectedbook",bundle);
                    context.startActivity(intent);*/


                    cursor = database.getAllNote();

                    if(cursor != null){
                        if(cursor.getCount()>0){

                            cursor.moveToPosition(position);

                            Bundle bundle = new Bundle();
                            bundle.putString("selectedbook",cursor.getString(cursor.getColumnIndex(RecentDatabase.MEDIAFILE)));
                            Intent intent = new Intent(context,ReadingRoom.class);
                            intent.putExtra("selectedbook",bundle);
                            context.startActivity(intent);


                        }


                    }



                }
            });


        }

    }


    public String pdfSize(int index){
        String filepath="";

        String bigsize;
        String maining;
        String sizing;
        String theSize = "";

        long size;
        long big;


        if(cursor != null){
            if(cursor.getCount()>0){

                cursor.moveToPosition(index);

                filepath = cursor.getString(cursor.getColumnIndex(RecentDatabase.MEDIAFILE)) ;   //(String) arrayString[index];
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

            }
        }





        return theSize;



    }


    public String bookBase(int index){
        String theText;
        String correct="";

        /*Cursor curText = db.getBook((long)index+1);
        if(curText!=null){
            curText.moveToFirst();
            theText = curText.getString(curText.getColumnIndexOrThrow(LibraryDatabase.TITLE));
             correct = theText.substring(theText.lastIndexOf("/")+1,theText.lastIndexOf(".")-1);
        }*/

        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToPosition(index);

                theText = cursor.getString(cursor.getColumnIndex(RecentDatabase.MEDIAFILE));  //(String) arrayString[index] ;
                correct = theText.substring(theText.lastIndexOf("/")+1,theText.lastIndexOf("."));

            }
        }






        return  correct;
    }




    @Override
    public int getCount() {
        cursor = database.getAllNote();

        return cursor !=null ?cursor.getCount():0;
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


        try{

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
