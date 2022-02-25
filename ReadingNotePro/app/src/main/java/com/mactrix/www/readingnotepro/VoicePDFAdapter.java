package com.mactrix.www.readingnotepro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Mactrix on 9/4/2018.
 */

public class VoicePDFAdapter extends BaseAdapter {

    ImageView cancelshowcase;
    ImageView showcasepdf;
    RelativeLayout showcaselayout;
    ArrayList<String> listOfBook;
    LayoutInflater inflater;
    Context context;
    private File file;
    private ParcelFileDescriptor pfd;
    private PdfRenderer renderer;
    private PdfRenderer.Page page;
    private Bitmap map;

  //  SharePref sharePref;

    //Set<String> stringSet;
    Dialog dialog;
    //Object[] object;

    public VoicePDFAdapter(Context context, ArrayList<String> listOfBook){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listOfBook = listOfBook;
        dialog = new Dialog(context);

        /*sharePref = new SharePref(context,"Recent");


        stringSet = new ArraySet<>();

        if(!sharePref.getStringSet().isEmpty()) {
            object = sharePref.getStringSet().toArray();

            for(int i=0;i<sharePref.getStringSet().size();i++){

                stringSet.add((String)object[i]);


            }

        }*/






        showcaselayout = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.app_bar_pro_note,null,false).findViewById(R.id.showcaselayout);
        showcasepdf = (ImageView)LayoutInflater.from(context).inflate(R.layout.app_bar_pro_note,null,false).findViewById(R.id.pdfshowcase);
        cancelshowcase = (ImageView)LayoutInflater.from(context).inflate(R.layout.app_bar_pro_note,null,false).findViewById(R.id.cancelshowcase);

        cancelshowcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaselayout.setVisibility(View.GONE);
            }
        });

    }


    class ViewHolder{
        TextView name;
        TextView size;
        ImageView imageView;
        public ViewHolder(View view , final int position){

            name = (TextView)view.findViewById(R.id.pdfname);
            size = (TextView)view.findViewById(R.id.pdfsize);
            imageView = (ImageView)view.findViewById(R.id.pdfmore);

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
                                    bundle.putString("selectedbook",listOfBook.get(position));
                                    Intent intent = new Intent(context,ReadingRoom.class);
                                    intent.putExtra("selectedbook",bundle);

                                    //stringSet.add(listOfBook.get(position));
                                    //sharePref.setStringSet(stringSet);

                                    context.startActivity(intent);


                                    break;
                                case R.id.previewpdf:

                                    View preview = LayoutInflater.from(context).inflate(R.layout.pdfview,null,false);

                                    PDFView pdfView = (PDFView)preview.findViewById(R.id.previewpdf);
                                    pdfView.fromFile(new File(listOfBook.get(position))).pageFling(true).pageSnap(true).pageFitPolicy(FitPolicy.BOTH).swipeHorizontal(false).enableSwipe(false).load();
                                    dialog.setContentView(preview);
                                    dialog.show();

                                    /*Intent intenting = new Intent("showfilter");
                                    intenting.putExtra("showcase",listOfBook.get(position));

                                    context.sendBroadcast(intenting);*/

                                    break;
                                case R.id.location:

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("PDF Location");
                                    builder.setMessage(""+listOfBook.get(position));
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
                    bundle.putString("selectedbook",listOfBook.get(position));
                    Intent intent = new Intent(context,ReadingRoom.class);
                    intent.putExtra("selectedbook",bundle);

                    //stringSet.add(listOfBook.get(position));
                   // sharePref.setStringSet(stringSet);



                    context.startActivity(intent);


                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Intent intent = new Intent("showfilter");
                    intent.putExtra("showcase",listOfBook.get(position));

                    context.sendBroadcast(intent);



                    return true;
                }
            });

        }
    }

    public Bitmap pdfImage(final int i){

        Bitmap bit=null;




        if(listOfBook!=null){
            file = new File(listOfBook.get(i));

            //context.startActivity(new Intent(context,MyLibrary.class));
        }

        if(file==null){
            context.startActivity(new Intent(context,ProNote.class));
        }



        try {
            pfd = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                renderer = new PdfRenderer(pfd);

                page = renderer.openPage(0);
                map = Bitmap.createBitmap(100,150, Bitmap.Config.ARGB_8888);
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


    public String pdfSize(int index){
        String filepath;

        String bigsize;
        String maining;
        String sizing;
        String theSize;

        long size;
        long big;

        filepath = listOfBook.get(index);
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
        theText = listOfBook.get(index);
        correct = theText.substring(theText.lastIndexOf("/")+1,theText.lastIndexOf("."));


        return  correct;
    }



    @Override
    public int getCount() {
        return listOfBook.size();
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
