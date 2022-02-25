package com.mactrix.www.readingnotepro;

import android.content.Context;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.*;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mactrix on 4/27/2018.
 */

public class VideoAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<String> arrayList;
    Context context;


    public VideoAdapter(Context context, ArrayList arrayList){
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.context = context;



    }

    public class ViewHolder{

        ImageView motionthumbnail;
        TextView motionTitle;
        ImageView moremotion;


        public ViewHolder(View view, final int position){

            motionthumbnail = (ImageView)view.findViewById(R.id.motionthumbnail);
            motionTitle = (TextView) view.findViewById(R.id.motionTitle);
            moremotion = (ImageView)view.findViewById(R.id.moremotion);


            Glide.with(context).load(arrayList.get(position)).asBitmap().into(motionthumbnail);


            String title = arrayList.get(position);
            String titling = title.substring(title.lastIndexOf("/")+1,title.length());
            motionTitle.setText(titling);


            moremotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,v);
                    popupMenu.inflate(R.menu.studystoremenu);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()){

                                case R.id.open:

                                    String path = arrayList.get(position);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("videostore",path);
                                    Intent intent = new Intent(context,VideoStudy.class);
                                    intent.putExtra("videostore",bundle);
                                    context.startActivity(intent);



                                    break;
                                case R.id.delete:
                                    String pathy = arrayList.get(position);

                                    File file = new File(pathy);
                                    file.delete();

                                    Toast.makeText(context, "Recording Deleted", Toast.LENGTH_LONG).show();
                                    context.sendBroadcast(new Intent("updating"));



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


                    String path = arrayList.get(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("videostore",path);
                    Intent intent = new Intent(context,VideoStudy.class);
                    intent.putExtra("videostore",bundle);
                    context.startActivity(intent);

                }
            });


        }
    }


    @Override
    public int getCount() {
        return arrayList.size();
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
                convertView = inflater.inflate(R.layout.videolist,parent,false);
            }

            new ViewHolder(convertView,position);



        }catch (InflateException e){

            if(convertView==null){
                convertView = inflater.inflate(R.layout.videolist,parent,false);
            }

            new ViewHolder(convertView,position);

        }catch (Exception e){

            if(convertView==null){
                convertView = inflater.inflate(R.layout.videolist,parent,false);
            }

            new ViewHolder(convertView,position);

        }

        return convertView;
    }
}
