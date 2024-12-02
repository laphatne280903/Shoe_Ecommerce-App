package com.example.giaodienchinh_doan.AnotherNav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodienchinh_doan.AdapterView.CartReviewAdapter;
import com.example.giaodienchinh_doan.MainActivity;
import com.example.giaodienchinh_doan.Model.CheckoutModel;
import com.example.giaodienchinh_doan.Model.MyCartModel;
import com.example.giaodienchinh_doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MyCartModel> myCartModelList;
    List<CheckoutModel> checkoutModelList;
    List<String> documentID;
    List<MyCartModel> list;
    CartReviewAdapter cartReviewAdapter;
    TextView userName, userEmail, userPhone, userAddress, userCity;
    TextView quantity, totalprice;
    Button continuteshopping;
    Toolbar toolbar;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    int totalAmount, count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();


        userName=findViewById(R.id.checkout_name);
        userEmail=findViewById(R.id.checkout_email);
        userPhone=findViewById(R.id.checkout_phone);
        userAddress=findViewById(R.id.checkout_address);
        userCity=findViewById(R.id.checkout_city);
        continuteshopping=findViewById(R.id.continute_shop);
        continuteshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position;
                count= cartReviewAdapter.getItemCount();
                for(position=0;position<count;position++){

                DocumentReference documentReference = firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                        .collection("User").document(myCartModelList.get(position).getId());
                documentReference.delete();}
                startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
            }
        });

        quantity=findViewById(R.id.quantity_item);
        totalprice=findViewById(R.id.total_amount);

        recyclerView = findViewById(R.id.item_paied_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myCartModelList = new ArrayList<>();
        cartReviewAdapter = new CartReviewAdapter(this, myCartModelList);
        recyclerView.setAdapter(cartReviewAdapter);

        //Using SharePreferences to pass edit text data
        SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferences", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        String address = sharedPreferences.getString("address", null);
        String city = sharedPreferences.getString("city", null);
        String phone = sharedPreferences.getString("phone", null);

        userName.setText(name);
        userEmail.setText(email);
        userAddress.setText(address);
        userCity.setText(city);
        userPhone.setText(phone);

        
        firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                myCartModelList.add(myCartModel);
                                cartReviewAdapter.notifyDataSetChanged();
                                int price=doc.getDouble("totalprice").intValue();
                                totalAmount+=price;
                            }
                            totalprice.setText("$"+String.valueOf(totalAmount));
                            count =cartReviewAdapter.getItemCount();
                            quantity.setText("("+ String.valueOf(count));
                        }
                    }
                });
    }

}