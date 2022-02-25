package com.mactrix.www.readingnotepro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Mactrix on 4/23/2018.
 */

public class Studylist extends BaseAdapter {

    Context context;
    StudyDatabase database;
    LayoutInflater inflater;

    public Studylist(Context context,StudyDatabase database){
        this.context = context;
        this.database = database;
        inflater = LayoutInflater.from(context);
    }


    public class ViewHolder{

        ImageView motionthumbnail;
        TextView motionTitle;
        ImageView moremotion;


        public ViewHolder(final View view, final int position){


            motionthumbnail = (ImageView)view.findViewById(R.id.motionthumbnail);
            motionTitle = (TextView) view.findViewById(R.id.motionTitle);
            moremotion = (ImageView)view.findViewById(R.id.moremotion);



            Cursor cursor = database.getAllNote();

            if(cursor!=null){
                if(cursor.getCount()>0){

                    cursor.moveToPosition(position);

                    String path = cursor.getString(cursor.getColumnIndex(StudyDatabase.MEDIAFILE));



                    if(path!=null){

                        if(path.endsWith(".3gpp")||path.endsWith(".mp3")){
                            motionthumbnail.setImageResource(R.drawable.microphoneline);

                        }else if(path.endsWith(".mp4")){

                            motionthumbnail.setImageResource(R.drawable.videocaming);

                        }else if(path.endsWith(".pdf")){

                            motionthumbnail.setImageResource(R.drawable.books);

                        }else if(path.endsWith("writtennote")){

                            motionthumbnail.setImageResource(R.drawable.notebook);

                        }

                    }




                    motionTitle.setText(cursor.getString(cursor.getColumnIndex(StudyDatabase.TITLE)));

                }
            }


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor cursor = database.getAllNote();

                    if(cursor!=null){
                        if(cursor.getCount()>0){

                            cursor.moveToPosition(position);

                            String path = cursor.getString(cursor.getColumnIndex(StudyDatabase.MEDIAFILE));

                            if(path!=null){

                                if(path.endsWith(".mp3")||path.endsWith(".3gpp")){

                                    Bundle bundle  = new Bundle();
                                    bundle.putInt("position",position);

                                    Intent intent = new Intent(context,AudioStudy.class);
                                    intent.putExtra("position",bundle);


                                    context.startActivity(intent);


                                }else if(path.endsWith(".mp4")||path.endsWith(".3gp")){

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("position",position);

                                    Intent intent = new Intent(context,VideoStudy.class);
                                    intent.putExtra("position",bundle);

                                    context.startActivity(intent);



                                }else if(path.endsWith(".pdf")){

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("position",position);

                                    Intent intent  = new Intent(context,ReadingRoom.class);
                                    intent.putExtra("position",bundle);

                                    context.startActivity(intent);



                                }else if(path.endsWith("writtennote")){

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("position",position);

                                    Intent intent = new Intent(context,Notepad.class);
                                    intent.putExtra("position",bundle);

                                    context.startActivity(intent);

                                }

                            }



                        }
                    }

                }
            });

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


                                    Cursor cursor = database.getAllNote();

                                    if(cursor!=null){
                                        if(cursor.getCount()>0){

                                            cursor.moveToPosition(position);

                                            String path = cursor.getString(cursor.getColumnIndex(StudyDatabase.MEDIAFILE));

                                            if(path.endsWith(".mp3")){

                                                Bundle bundle  = new Bundle();
                                                bundle.putInt("position",position);

                                                Intent intent = new Intent(context,AudioStudy.class);
                                                intent.putExtra("position",bundle);


                                                context.startActivity(intent);


                                            }else if(path.endsWith(".mp4")){

                                                Bundle bundle = new Bundle();
                                                bundle.putInt("position",position);

                                                Intent intent = new Intent(context,VideoStudy.class);
                                                intent.putExtra("position",bundle);

                                                context.startActivity(intent);



                                            }else if(path.endsWith(".pdf")){

                                                Bundle bundle = new Bundle();
                                                bundle.putInt("position",position);

                                                Intent intent  = new Intent(context,ReadingRoom.class);
                                                intent.putExtra("position",bundle);

                                                context.startActivity(intent);



                                            }else if(path.endsWith("writtennote")){

                                                Bundle bundle = new Bundle();
                                                bundle.putInt("position",position);

                                                Intent intent = new Intent(context,Notepad.class);
                                                intent.putExtra("position",bundle);

                                                context.startActivity(intent);

                                            }

                                        }
                                    }



                                    break;
                                case R.id.delete:

                                    Cursor curso  =  database.getAllNote();
                                    if(curso!=null){
                                        if(curso.getCount()>0){

                                            curso.moveToPosition(position);

                                            long id = curso.getLong(curso.getColumnIndex(StudyDatabase.ID));


                                            database.deleteNote(id);

                                            Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent("delete");
                                            context.sendBroadcast(intent);






                                        }
                                    }

                                    break;
                            }





                            return true;
                        }
                    });

                }
            });

        }
    }


    @Override
    public int getCount() {
        int count=0;

        Cursor cursor = database.getAllNote();
        if(cursor!=null){
            if(cursor.getCount()>0){
                count= cursor.getCount();
            }
        }

        return count;

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
                convertView = inflater.inflate(R.layout.studystorelayout,parent,false);

            }

            new ViewHolder(convertView,position);




        }catch (InflateException e){

            if(convertView==null){
                convertView = inflater.inflate(R.layout.studystorelayout,parent,false);

            }

            new ViewHolder(convertView,position);

        }catch (Exception e){

            if(convertView==null){
                convertView = inflater.inflate(R.layout.studystorelayout,parent,false);

            }

            new ViewHolder(convertView,position);

        }



        return convertView;
    }
}
