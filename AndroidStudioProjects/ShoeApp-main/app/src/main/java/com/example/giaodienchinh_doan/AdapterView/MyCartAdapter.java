package com.example.giaodienchinh_doan.AdapterView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giaodienchinh_doan.AnotherNav.CartActivity;
import com.example.giaodienchinh_doan.DetailedActivity;
import com.example.giaodienchinh_doan.Model.MyCartModel;
import com.example.giaodienchinh_doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    public int totalAmount;
    private Context context;
    private List<MyCartModel> list;
    private List<DocumentSnapshot> documentSnapshots;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    Float newtotalprice = (float) 0;

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(list.get(position).getProductPrice());
        holder.totalprice.setText(String.valueOf(list.get(position).getTotalprice()));
        holder.date.setText(list.get(position).getCurrentDate());
        holder.quantity.setText(list.get(position).getQuantity());
        holder.removefromcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the delete button is clicked
                if (v.getId() == R.id.remove_from_cart) {
                    int position = holder.getAdapterPosition();
                    // Delete the item from firestore cloud
                    DocumentReference docRef = firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                            .collection("User").document(list.get(position).getId());
                    docRef.delete();
                    // Delete the item from recyclerview
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
        holder.minusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quan=holder.quantity.getText().toString();
                int totalquantity=Integer.parseInt(quan);
                String pprice=holder.price.getText().toString();
                Float price=Float.parseFloat(pprice);
                if(totalquantity>1) {
                    totalquantity--;
                    holder.quantity.setText(String.valueOf(totalquantity));
                    Float newtotalprice=totalquantity*price;
                    newtotalprice=Math.round(newtotalprice*100)/100f;
                    holder.totalprice.setText(String.valueOf(newtotalprice));
                    DocumentReference docRef = firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                            .collection("User").document(list.get(position).getId());
                    docRef.update("quantity", String.valueOf(totalquantity));
                    docRef.update("totalprice",newtotalprice);

                }
            }
        });
        holder.plusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quan=holder.quantity.getText().toString();
                int totalquantity=Integer.parseInt(quan);
                String pprice=holder.price.getText().toString();
                Float price=Float.parseFloat(pprice);
                if(totalquantity<10){
                    totalquantity++;
                    holder.quantity.setText(String.valueOf(totalquantity));
                    Float newtotalprice=totalquantity*price;
                    newtotalprice=Math.round(newtotalprice*100)/100f;
                    holder.totalprice.setText(String.valueOf(newtotalprice));
                    DocumentReference docRef = firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                            .collection("User").document(list.get(position).getId());
                    docRef.update("quantity", String.valueOf(totalquantity));
                    docRef.update("totalprice",newtotalprice);
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed", list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton removefromcart, plusquantity, minusquantity;
        ImageView imageView;
        TextView name, price, totalprice, date, quantity, id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.my_cart_img);
            name=itemView.findViewById(R.id.cart_product_name);
            price=itemView.findViewById(R.id.cart_product_price);
            totalprice=itemView.findViewById(R.id.total_price);
            date=itemView.findViewById(R.id.current_date);
            quantity=itemView.findViewById(R.id.total_quantity);
            removefromcart=itemView.findViewById(R.id.remove_from_cart);
            plusquantity=itemView.findViewById(R.id.plus_quantity);
            minusquantity=itemView.findViewById(R.id.minus_quantity);

        }
    }


}