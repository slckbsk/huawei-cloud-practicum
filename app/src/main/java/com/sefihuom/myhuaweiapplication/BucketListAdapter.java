package com.sefihuom.myhuaweiapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.obs.services.model.ObsBucket;
import com.sefihuom.myhuaweiapplication.utilities.AlertUtilities;
import com.sefihuom.myhuaweiapplication.utilities.BucketOperations;
import com.sefihuom.myhuaweiapplication.utilities.NetworkUtilities;

import java.util.List;


public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder>  {
    Context context;
    private final List<ObsBucket> itemList;
    ClickListener clickListener;
    AlertUtilities progress;

    BucketListAdapter(Context context, List<ObsBucket> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public BucketListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BucketListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String bucketNameCopy = itemList.get(position).getBucketName();

        holder.name.setText(bucketNameCopy);
        String location= itemList.get(position).getLocation();
        holder.loction.setText(location);


        holder.itemView.setOnClickListener(view -> {

            if (NetworkUtilities.isConnected(context)){

                Intent intent = new Intent(context, Bucket.class);
                intent.putExtra("bucketName", bucketNameCopy);
                context.startActivity(intent);


            } else {
                Toast.makeText(context, context.getString(R.string.you_are_not_connected_to_the_internet), Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progress = new AlertUtilities(context, "Being deleted...", true);

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        BucketOperations.deleteBucket(bucketNameCopy, new BucketOperations.OnWorkingListener(){
                            @Override
                            public void showProgress() {
                                progress.show();
                            }

                            @Override
                            public void onFailure(String e) {
                                progress.dismiss();
                            }

                            @Override
                            public void onSuccess() {
                                clickListener.onDelete();
                                progress.dismiss();
                            }

                            @Override
                            public void onAppend(String e) {
                                clickListener.onAppent(e);
                            }
                        });
                    }
                }).start();
            }
        });
    }


    @Override
    public int getItemCount() {
        return (itemList == null) ? 0 : itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder   {
        final TextView name;
        final TextView loction;
        ImageView delete;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            loction = itemView.findViewById(R.id.location);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onDelete();
        void onAppent(String e);
    }


}

