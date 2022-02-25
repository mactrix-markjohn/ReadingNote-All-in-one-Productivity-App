package com.mactrix.www.readingnotepro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Mactrix on 9/3/2018.
 */

public class RecentFragment extends Fragment {

    Context context;
    View view;

    Button clearbutton;
    ListView recentlist;

    RecentAdapter recentAdapter;
    SharePref sharePref;

    RelativeLayout norecentholder;
    RecentDatabase recentDatabase;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstancestate){
        super.onCreateView(inflater,parent,savedInstancestate);

        view  = inflater.inflate(R.layout.recentfragment,parent,false);
        clearbutton = (Button)view.findViewById(R.id.clearbutton);
        recentlist = (ListView)view.findViewById(R.id.recentlist);
        norecentholder = (RelativeLayout)view.findViewById(R.id.norecentholder);


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);

        registerReceiver();
    }

    @Override
    public void onActivityCreated(Bundle savedInstancestate){
        super.onActivityCreated(savedInstancestate);

        sharePref = new SharePref(context,"Recent");
        recentDatabase = new RecentDatabase(context,null,null,1);
        //recentAdapter = new RecentAdapter(context,sharePref.getStringSet());
        recentAdapter = new RecentAdapter(context,recentDatabase);
        recentlist.setAdapter(recentAdapter);

        if(recentlist.getCount()==0){

            recentlist.setVisibility(View.GONE);
            norecentholder.setVisibility(View.VISIBLE);

        }else{

            norecentholder.setVisibility(View.GONE);
            recentlist.setVisibility(View.VISIBLE);

        }






        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // sharePref.getStringSet().clear();
                recentDatabase.deleteall();

                context.sendBroadcast(new Intent("recent"));
            }
        });




    }

    @Override
    public void onResume(){
        super.onResume();


        if(recentlist.getCount()==0){

            recentlist.setVisibility(View.GONE);
            norecentholder.setVisibility(View.VISIBLE);

        }else{

            norecentholder.setVisibility(View.GONE);
            recentlist.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        context.unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recentAdapter = new RecentAdapter(context,recentDatabase);
            recentlist.setAdapter(recentAdapter);

            onResume();
        }
    };

    public void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter("recent");
        context.registerReceiver(receiver,intentFilter);
    }


}
