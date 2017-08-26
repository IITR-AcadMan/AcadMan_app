package net.acadman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;


public class Files extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    View v;
    RecyclerView filesrec;
    PostRequest filespostreq;
    List<String> filename = new ArrayList<String>();
    List<String> uploadedon = new ArrayList<String>();
    List<String> uploadedby = new ArrayList<String>();
    List<String> comment = new ArrayList<String>();
    List<String> filesize = new ArrayList<String>();
    List<String> Uploadedbyenld = new ArrayList<String>();
    List<String> id = new ArrayList<String>();
    String sendcourse;
    SwipeRefreshLayout refreshfiles;
    String per_enld;
    ProgressBar profiles;
    String tidsp,tokensp;





    public Files(String sendcourse) {
        this.sendcourse = sendcourse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_files, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Files");
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        setRetainInstance(true);
        filesrec = (RecyclerView)v.findViewById(R.id.filesrec);
        filesrec.setLayoutManager(new LinearLayoutManager(getActivity()));
        profiles = (ProgressBar)v.findViewById(R.id.profiles);
        SharedPreferences enldsp = getActivity().getSharedPreferences("Personal_Data", getActivity().MODE_PRIVATE);
        per_enld  = enldsp.getString("eid", "null");
        tidsp = enldsp.getString("tid","null");
        tokensp = enldsp.getString("token","null");
        SharedPreferences.Editor tempcou = getActivity().getSharedPreferences("temp", MODE_PRIVATE).edit();
        tempcou.putString("sendcourse", sendcourse);
        tempcou.apply();
        refreshfiles = (SwipeRefreshLayout) v.findViewById(R.id.refreshfiles);
        refreshfiles.setOnRefreshListener(this);
        refreshfiles.setColorSchemeColors(Color.parseColor("#669900"),
                Color.parseColor("#cc0000"),
                Color.parseColor("#0099cc"),
                Color.parseColor("#ff8800"));
        SharedPreferences fileslistsp = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE);
        String filesJSON  = fileslistsp.getString("files_list_"+sendcourse, "null");
        if(!filesJSON.equals("null")){
          JSON_parse_files(filesJSON);

        }
        else{no_history();}


        return v;}
    private class Filesholder extends RecyclerView.ViewHolder {
        public TextView filename;
        public TextView uploadedon;
        public TextView uploadedby;
        public TextView comment;
        public TextView filesize;
        public ImageView download_icon;
        public FrameLayout wholeitemview;
        public ProgressBar ps;
        public ImageView delete_icon;
        public Filesholder(View itemView) {
            super(itemView);
            filename = (TextView) itemView.findViewById(R.id.filename);
            uploadedon = (TextView) itemView.findViewById(R.id.uploadedon);
            uploadedby = (TextView) itemView.findViewById(R.id.uploadedby);
            comment = (TextView) itemView.findViewById(R.id.comment);
            filesize = (TextView) itemView.findViewById(R.id.filesize);
            download_icon = (ImageView) itemView.findViewById(R.id.download_icon);
            wholeitemview = (FrameLayout)itemView.findViewById(R.id.wholeitemview);
            ps = (ProgressBar)itemView.findViewById(R.id.progressBarfile);
            delete_icon = (ImageView)itemView.findViewById(R.id.deleteicon);

       }
    }



    private class Filesadapter extends RecyclerView.Adapter<Files.Filesholder> {

        List<String> filenamell;
        List<String> uploadedonll;
        List<String> uploadedbyll;
        List<String> commentll;
        List<String> filesizell;
        List<String> idl;
        List<String> Uploadedbyenldll;
        public Filesadapter(List<String> filenamell,List<String> uploadedonll,List<String> uploadedbyll,List<String> commentll,List<String> filesizell,List<String>idl,List<String>uploadedbyenld) {
            this.filenamell=filenamell;
            this.uploadedonll=uploadedonll;
            this.uploadedbyll=uploadedbyll;
            this.commentll=commentll;
            this.filesizell=filesizell;
            this.idl=idl;
            this.Uploadedbyenldll=Uploadedbyenld;
        }

        @Override
        public Files.Filesholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.file_element, parent, false);
            return new Files.Filesholder(view);
        }
        public void onBindViewHolder(final Files.Filesholder holder, final int position) {
            File iconico = new File(Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/"+idl.get(position));
            if(iconico.exists()){
                if(iconico.length()!=Long.parseLong(filesizell.get(position))){
                    holder.ps.setVisibility(View.VISIBLE);
                    holder.download_icon.setVisibility(View.INVISIBLE);
                }
                else{ holder.ps.setVisibility(View.INVISIBLE);
                    holder.download_icon.setVisibility(View.INVISIBLE);}
            }
            else{
                holder.ps.setVisibility(View.INVISIBLE);
                holder.download_icon.setVisibility(View.VISIBLE);
            }
            holder.filename.setText(filenamell.get(position));
            holder.uploadedon.setText(uploadedonll.get(position));
            holder.comment.setText(commentll.get(position));
            holder.uploadedby.setText(uploadedbyll.get(position));
            holder.filesize.setText(android.text.format.Formatter.formatFileSize(getActivity(), Long.parseLong(filesizell.get(position))));

            if(Uploadedbyenldll.get(position).equals(per_enld)){
                holder.delete_icon.setVisibility(View.VISIBLE);
                holder.delete_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isOnline()){

                            Dialog ss= new AlertDialog.Builder(getActivity())
                                    .setTitle("Are You Sure ?")
                                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            delete__file(idl.get(position));

                                        }
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .create();
                            ss.show();
                        }
                        else {
                            Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            final File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/",idl.get(position));
            if(file.exists()){holder.download_icon.setVisibility(View.INVISIBLE);}
            else {holder.download_icon.setVisibility(View.VISIBLE);}
            holder.wholeitemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(file.exists()){ File ddd = new File(Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/","temp");
                        if (!ddd.exists()) {
                            ddd.mkdir();
                        }
                    File source= new File (Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/",idl.get(position));
                        File target= new File (Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/temp/",filenamell.get(position));

                     try {
                         FileInputStream inStream = new FileInputStream(source);
                         FileOutputStream outStream = new FileOutputStream(target);
                         FileChannel inChannel = inStream.getChannel();
                         FileChannel outChannel = outStream.getChannel();
                         inChannel.transferTo(0, inChannel.size(), outChannel);
                         inStream.close();
                         outStream.close();
                   open_file(Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/temp/"+filenamell.get(position));


                     }
                     catch (Exception e){
                         Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                     }

                     }
                     else {
                        Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
                        Intent aa = new Intent (getContext(),Download_service.class);
                        aa.putExtra("fileid",idl.get(position));
                        aa.putExtra("filename",filenamell.get(position));
                        aa.putExtra("filesize",filesizell.get(position));
                        holder.ps.setVisibility(View.VISIBLE);
                        holder.download_icon.setVisibility((View.INVISIBLE));
                        getActivity().startService(aa);


                    }
                }
            });

            /*catche = new DatabaseDownload(getActivity()).getWritableDatabase();

            String adddata = "INSERT INTO Download_Files (id,course,name) VALUES ('"+idl.get(position)+"','"+sendcourse+"','"+filenamell.get(position)+"');";
           catche.execSQL(adddata);*/

        }
        @Override
        public int getItemCount() {
            return filenamell.size();
        }

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();}


     public void no_history (){
         if(isOnline()){
         filespostreq = new PostRequest(new String[]{"reqid=","&tid=","&token=","&course="},new String[]{"11",tidsp,tokensp,sendcourse});
         filespostreq.execute(0);

         filespostreq.bindListener(new JsonListener() {
             @Override
             public void jsonreceived(String string) {
               JSON_parse_files(string);
             }
         });}
         else { Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
             refreshfiles.setRefreshing(false);}

     }

    @Override
    public void onRefresh() {
        no_history();

    }
    public void JSON_parse_files (String JSON){
      try{ filename.clear();
        uploadedon.clear();
        uploadedby.clear();
        filesize.clear();
        comment.clear();
          id.clear();
          Uploadedbyenld.clear();
          refreshfiles.setRefreshing(false);
        JSONObject main = new JSONObject(JSON);
        String check = main.getString("err");
        if(check.equals("200")){
            JSONArray files = main.getJSONArray("files");
            for(int a=0;a<files.length();a++){
                JSONObject fileelement = files.getJSONObject(a);

                filename.add(a,fileelement.getString("filename"));

                id.add(a,fileelement.getString("id"));

                long timestamp =Long.parseLong(fileelement.getString("id"))*1000;

                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss aaa", Locale.US);
                String date1 = sdf1.format(timestamp);
                String date2 = sdf2.format(timestamp);
                uploadedon.add(a,date1.toString()+" at "+date2.toString());


                //  uploadedon.add(Integer.toString(hr)+":"+Integer.toString(min)+":"+Integer.toString(sec)+"  "+Integer.toString(mDay)+"-"+Integer.toString(mMonth)+"-"+Integer.toString(mYear));


                //  uploadedon.add(fileelement.getString("uploadedon"));
                uploadedby.add(a,"Uploaded By: "+fileelement.getString("fn"));
                comment.add(a,fileelement.getString("comment"));
                filesize.add(a,fileelement.getString("size"));
                Uploadedbyenld.add(a,fileelement.getString("eid"));

            }
            Filesadapter filesadapter= new Filesadapter( filename, uploadedon,uploadedby, comment,filesize,id,Uploadedbyenld);
            filesrec.setAdapter(filesadapter);

            SharedPreferences.Editor filinsert = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE).edit();
            filinsert.putString("files_list_"+sendcourse, JSON);
            filinsert.apply();
        }}
      catch (Exception e){
          Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
          refreshfiles.setRefreshing(false);
      }
    }
    public void open_file (String filepath){
        File temp_file=new File(filepath);
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(temp_file),getMimeType(temp_file.getAbsolutePath()));
        startActivity(intent);
    }
    private String getMimeType(String abcde)
    {
        String parts[]=abcde.split("\\.");
        String extension=parts[parts.length-1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
if(intent.getAction().equals("status")){
    Filesadapter filesadapiter= new Filesadapter( filename, uploadedon,uploadedby, comment,filesize,id,Uploadedbyenld);
    filesrec.setAdapter(filesadapiter);
}

        }
    };
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("status");
        getActivity().registerReceiver(receiver,new IntentFilter(filter));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);

    }
    public void delete__file(String fileid){
        refreshfiles.setAlpha(0.4f);
        profiles.setVisibility(View.VISIBLE);
        PostRequest delete= new PostRequest(new String[]{"reqid=","&tid=","&token=","&id="},new String[]{"14",tidsp,tokensp,fileid});
        delete.execute();
        delete.bindListener(new JsonListener() {
            @Override
            public void jsonreceived(String string) {
                try {
                    JSONObject main = new JSONObject(string);
                    if(main.getString("err").equals("200")){
                        Toast.makeText(getActivity(), "successfully Deleted", Toast.LENGTH_SHORT).show();
                        no_history();
                        profiles.setVisibility(View.INVISIBLE);
                        refreshfiles.setAlpha(1f);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();}
            }
        });
    }


}

