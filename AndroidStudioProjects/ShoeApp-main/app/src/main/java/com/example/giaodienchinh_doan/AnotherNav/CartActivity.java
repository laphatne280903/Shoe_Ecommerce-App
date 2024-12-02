package com.example.giaodienchinh_doan.AnotherNav;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giaodienchinh_doan.AdapterView.MyCartAdapter;
import com.example.giaodienchinh_doan.AddAddressActivity;
import com.example.giaodienchinh_doan.Model.MyCartModel;
import com.example.giaodienchinh_doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartActivity extends AppCompatActivity {

    TextView overAllAmount;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    List<MyCartModel> myCartModelList;
    private MyCartAdapter mycartAdapter;
    private  List<String> data;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    Button addToCart, buyNow, removefromcart;
    public Float totalAmount= (float) 0;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        String brand=getIntent().getStringExtra("brand");
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar=findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextAppearance(this, R.style.TitleTextAppearance_Bold);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buyNow=findViewById(R.id.buy_now);

        //Button Buy now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mycartAdapter.getItemCount()>0) {
                    startActivity(new Intent(CartActivity.this, AddAddressActivity.class));
                }
                else Toast.makeText(CartActivity.this,"You have no items to purchase",Toast.LENGTH_SHORT).show();
            }
        });


        overAllAmount = findViewById(R.id.overall_amount);
        recyclerView = findViewById(R.id.cart_item_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myCartModelList = new ArrayList<>();
        mycartAdapter = new MyCartAdapter(this, myCartModelList);
        recyclerView.setAdapter(mycartAdapter);


        firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                //myCartModelList.add(myCartModel);
                                Float totalprice=doc.getDouble("totalprice").floatValue();
                                totalAmount+=totalprice;
                                mycartAdapter.notifyDataSetChanged();
                                mycartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                    @Override
                                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                                        super.onItemRangeRemoved(positionStart, itemCount);
                                        // Lấy position của phần tử đã bị xóa
                                        int position = positionStart + itemCount - 1;
                                        // Nếu position của phần tử đã bị xóa không phải là phần tử đầu tiên
                                        if (position != 0) {
                                            // Cập nhật lại position của các phần tử còn lại
                                            mycartAdapter.notifyItemRangeChanged(position, myCartModelList.size() - position - 1);
                                        }
                                        // Tính tổng giá trị mới
                                        Float totalAmount = (float) 0;
                                        for (MyCartModel myCartModel : myCartModelList) {
                                            totalAmount += myCartModel.totalprice;
                                            totalAmount=Math.round(totalAmount*100)/100f;
                                        }
                                        // Cập nhật textview tổng giá trị
                                        overAllAmount.setText("TOTAL: $"+String.valueOf(totalAmount));
                                    }
                                });
                                mycartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                    @Override
                                    public void onItemRangeChanged(int positionStart, int itemCount) {
                                        super.onItemRangeChanged(positionStart, itemCount);

                                        // Nếu position của phân tử đã bị xóa là phân tử đầu tiên
                                        if (positionStart == 0) {
                                            // Cập nhật lại danh sách
                                            myCartModelList.remove(positionStart);
                                            // Thông báo cho adapter về những thay đổi
                                            mycartAdapter.notifyItemRangeRemoved(positionStart, itemCount);
                                        }
                                    }
                                });
                            }
                        }
                        overAllAmount.setText("TOTAL: $"+String.valueOf(totalAmount));
                    }
                });

        firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                .collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle error
                            return;
                        }

                        // Update cart data
                        myCartModelList.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            myCartModelList.add(documentSnapshot.toObject(MyCartModel.class));
//                            myCartModelList.add(documentSnapshot.toObject(MyCartModel.class));
                        }

                        // Update adapter
                        mycartAdapter.notifyDataSetChanged();

                        // Calculate total amount
                        Float totalAmount = (float) 0;
                        for (MyCartModel myCartModel : myCartModelList) {
                            totalAmount += myCartModel.totalprice;
                            totalAmount=Math.round(totalAmount*100)/100f;
                        }
                        overAllAmount.setText("TOTAL: $" + String.valueOf(totalAmount));
                    }
                });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }
}