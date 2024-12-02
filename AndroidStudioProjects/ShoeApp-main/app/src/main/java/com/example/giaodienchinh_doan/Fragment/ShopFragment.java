package com.example.giaodienchinh_doan.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodienchinh_doan.AdapterView.ShowAllAdapter;
import com.example.giaodienchinh_doan.AnotherNav.ShowAllNewProductActivity;
import com.example.giaodienchinh_doan.Model.BrandsModel;
import com.example.giaodienchinh_doan.Model.PopularProductsModel;
import com.example.giaodienchinh_doan.Model.ShowAllModel;
import com.example.giaodienchinh_doan.R;
import com.example.giaodienchinh_doan.AdapterView.BrandsAdapter;
import com.example.giaodienchinh_doan.AdapterView.NewProductsAdapter;
import com.example.giaodienchinh_doan.Model.NewProductsModel;
import com.example.giaodienchinh_doan.ShowAllActivity;
import com.example.giaodienchinh_doan.AdapterView.PopularProductsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
    RecyclerView brandRecycleview, newProductRecycleview, showAllRecyclerView;
    TextView newProductsShowAll, showAll;
    BrandsAdapter brandsAdapter;
    BrandsModel brandsModel;
    List<BrandsModel> brandsModelList;
    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;
    PopularProductsAdapter popularProductsAdapter;
    List<PopularProductsModel> popularProductsModelList;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelsList;
    FirebaseFirestore db;
    ImageView searchIcon;

    public ShopFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        db = FirebaseFirestore.getInstance();

        brandRecycleview=root.findViewById(R.id.rec_brand);
        newProductRecycleview = root.findViewById(R.id.new_product_rec);

        showAllRecyclerView =root.findViewById(R.id.showall_rec);

        newProductsShowAll=root.findViewById(R.id.newProducts_see_all);
        showAll =root.findViewById(R.id.show_see_all);

        newProductsShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShowAllNewProductActivity.class);
                startActivity(intent);
            }
        });

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });
        //Brands
        brandRecycleview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        brandsModelList = new ArrayList<>();
        brandsAdapter = new BrandsAdapter(getContext(), brandsModelList);
        brandRecycleview.setAdapter(brandsAdapter);
        db.collection("Brands")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                BrandsModel brandsModel=document.toObject(BrandsModel.class);
                                brandsModelList.add(brandsModel);
                                brandsAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //New Products
        newProductRecycleview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext(),newProductsModelList);
        newProductRecycleview.setAdapter(newProductsAdapter);

        db.collection("ShowAll").whereEqualTo("status","new")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                NewProductsModel newProductsModel=document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Popular Products
        showAllRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        showAllModelsList = new ArrayList<>();
        showAllAdapter =new ShowAllAdapter(getContext(), showAllModelsList);
        showAllRecyclerView.setAdapter(showAllAdapter);


        db.collection("ShowAll")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                ShowAllModel showAllProductsModel=document.toObject(ShowAllModel.class);
                                showAllModelsList.add(showAllProductsModel);
                                showAllAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;

    }
}
