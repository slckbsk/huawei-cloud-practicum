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

import com.obs.services.model.ObsObject;
import com.sefihuom.myhuaweiapplication.utilities.AlertUtilities;
import com.sefihuom.myhuaweiapplication.utilities.NetworkUtilities;
import com.sefihuom.myhuaweiapplication.utilities.ObjectOperations;

import java.util.List;


public class ObjectListAdapter extends RecyclerView.Adapter<ObjectListAdapter.ViewHolder>  {
    Context context;
    private final List<ObsObject> itemList;
    ClickListener clickListener;
    AlertUtilities progress;

    ObjectListAdapter(Context context, List<ObsObject> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public ObjectListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_list_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ObjectListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String bucketNameCopy = itemList.get(position).getBucketName();
        String name= itemList.get(position).getObjectKey();
        holder.name.setText(name);


        holder.itemView.setOnClickListener(view -> {

            if (NetworkUtilities.isConnected(context)){

                Intent intent = new Intent(context, Object.class);
                intent.putExtra("bucketName", bucketNameCopy);
                intent.putExtra("objectName", name);
                context.startActivity(intent);




            } else {
                Toast.makeText(context, context.getString(R.string.you_are_not_connected_to_the_internet), Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progress = new AlertUtilities(context, "Being Deleted...", true);

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        ObjectOperations.deleteObject(bucketNameCopy, name, new ObjectOperations.OnWorkingListener(){
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
        ImageView delete;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
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

