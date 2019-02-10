package com.example.nvlnms.placesdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.MalformedJsonException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;


/**
 * Created by NVLNMS on 22-01-2018.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {

    private List<PlaceInfo> placeList;
    private Context cxt;
    CardView cv;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address;
        public ImageView img;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.pname);
            address = (TextView) view.findViewById(R.id.address);
            img=(ImageView)view.findViewById(R.id.img);
            cv=(CardView)view.findViewById(R.id.card);
        }
    }

    public PlacesAdapter(List<PlaceInfo> pi) {
        setHasStableIds(true);
        placeList=pi;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.place_info,parent,false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder,int pos)
    {
         PlaceInfo pi=placeList.get(pos);
         holder.name.setText(pi.getName());
         holder.address.setText(pi.getAddress());

         if(pi.getImgref()==null)
         holder.img.setImageResource(R.drawable.imgnot);

         else{
            String ref;
            ref=pi.getImgref();
            new DownImg(holder.img).execute(ref);
         }
    }

    private class DownImg extends AsyncTask<String,Void,Bitmap>{

         ImageView bmImage;
         protected Bitmap doInBackground(String... urls)
         {
             String ref=urls[0];
             String iUrl="https://maps.googleapis.com/maps/api/place/photo?maxwidth=4000&maxheight=4000&photoreference="+ref+"&key=AIzaSyDBB6GngTfK61rV_8ArGtMcHd-qAGQE1jU";
             Bitmap bit=null;

             try{
                 InputStream in =new java.net.URL(iUrl).openStream();
                 //bit= BitmapFactory.decodeStream(in);
                 BitmapFactory.Options opt=new BitmapFactory.Options();
                 opt.inScaled=true;
                 bit=BitmapFactory.decodeStream(in,new Rect(),opt);

                 //bit=BitmapFactory.decodeStream(in);
             }

             catch(Exception e)
             {}
             return Bitmap.createScaledBitmap(bit, 5000, 2000, true);
             //return bit;
         }

         //constructor
         public DownImg(ImageView i) {
             super();
             this.bmImage=i;
         }

         protected void onPostExecute(Bitmap result)
         {
             if(result!=null)
             bmImage.setImageBitmap(result);
         }
     }
    public int getItemCount() {
        return placeList.size();
    }
    public long getItemId(int pos){
            return pos;
    }
}