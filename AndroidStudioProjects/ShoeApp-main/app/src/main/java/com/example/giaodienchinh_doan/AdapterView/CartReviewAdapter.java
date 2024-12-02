package com.example.giaodienchinh_doan.AdapterView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giaodienchinh_doan.Model.MyCartModel;
import com.example.giaodienchinh_doan.R;

import java.util.List;

public class CartReviewAdapter extends RecyclerView.Adapter<CartReviewAdapter.ViewHolder> {

    private Context context;
    private List<MyCartModel> list;

    public CartReviewAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartReviewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartReviewAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into((holder.imageView));
        holder.name.setText(list.get(position).getProductName());
        holder.totalprice.setText(String.valueOf(list.get(position).getTotalprice()));
        holder.quantity.setText(list.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, totalprice, quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.image_review);
            name=itemView.findViewById(R.id.review_name);
            totalprice=itemView.findViewById(R.id.review_price);
            quantity=itemView.findViewById(R.id.review_quantity);
        }
    }
}
