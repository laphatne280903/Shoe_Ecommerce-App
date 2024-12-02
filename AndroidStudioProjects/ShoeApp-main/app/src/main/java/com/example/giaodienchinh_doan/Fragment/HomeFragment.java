package com.example.giaodienchinh_doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.giaodienchinh_doan.R;
import com.example.giaodienchinh_doan.sampledata.NewsModel;
import com.example.giaodienchinh_doan.sampledata.PhotoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    ArrayList<NewsModel> newsModelModellist;
    PhotoAdapter photoAdapter;
    FirebaseFirestore fb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        recyclerView = view.findViewById(R.id.list_view_news);
        newsModelModellist = new ArrayList<NewsModel>();
        photoAdapter = new PhotoAdapter(getContext(), newsModelModellist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(photoAdapter);
        fb = FirebaseFirestore.getInstance();
        fb.collection("News")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                NewsModel newsModelModel =document.toObject(NewsModel.class);
                                newsModelModellist.add(newsModelModel);
                                photoAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });
        return view;
    }

}