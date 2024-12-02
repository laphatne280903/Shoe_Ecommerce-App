package com.example.giaodienchinh_doan.AdapterView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.giaodienchinh_doan.Model.BrandsModel;
import com.example.giaodienchinh_doan.R;

import java.util.ConcurrentModificationException;
import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {
    private Context context;
    private List<BrandsModel> list;

    public BrandsAdapter(Context context, List<BrandsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BrandsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.brands_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BrandsAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.brandImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.example.giaodienchinh_doan.ShowAllActivity.class);
                intent.putExtra("brand",list.get(position).getBrand());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView brandImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            brandImg=itemView.findViewById(R.id.brand_img);
        }
    }
}
