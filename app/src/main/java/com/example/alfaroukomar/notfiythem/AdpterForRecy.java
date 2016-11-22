package com.example.alfaroukomar.notfiythem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Alfarouk omar on 11/21/2016.
 */
public class AdpterForRecy extends RecyclerView.Adapter<AdpterForRecy.Viewholder1> {

    private Newsobject[] News;
    Context context;
    LayoutInflater inflater;

    public AdpterForRecy(Context con, Newsobject[] k) {
        News = k;
        context = con;

    }

    @Override
    public AdpterForRecy.Viewholder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View v;
        v = inflater.inflate(R.layout.newdegien, parent, false);
        Viewholder1 v1 = new Viewholder1(v);
        return v1;
    }

    @Override
    public void onBindViewHolder(AdpterForRecy.Viewholder1 holder, int position) {

        holder.name.setText(News[position].getName());
        holder.other.setText(News[position].getPhone() + "/" + News[position].getOther());
        holder.type.setText(News[position].getType());
    }

    @Override
    public int getItemCount() {
       return News.length;
    }

    class Viewholder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView type;
        TextView other;

        public Viewholder1(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.namenews);
            type = (TextView) itemView.findViewById(R.id.Type);
            other = (TextView) itemView.findViewById(R.id.other);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int postion = getAdapterPosition();
            String phone = News[postion].phone;
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phone));
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

             return;
            }
            context.startActivity(callIntent);

        }
    }
}
