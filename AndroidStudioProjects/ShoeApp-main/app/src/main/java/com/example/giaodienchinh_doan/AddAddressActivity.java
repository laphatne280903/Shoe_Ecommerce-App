package com.example.giaodienchinh_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giaodienchinh_doan.AdapterView.CartReviewAdapter;
import com.example.giaodienchinh_doan.AdapterView.MyCartAdapter;
import com.example.giaodienchinh_doan.AnotherNav.CheckoutActivity;
import com.example.giaodienchinh_doan.Model.MyCartModel;
import com.example.giaodienchinh_doan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private EditText name, address, city, email, phoneNumber;
    TextView quantity, totalprice;
    Toolbar toolbar;
    Button gocheckout;
    RecyclerView recyclerView;
    List<MyCartModel> myCartModelList;
    MyCartAdapter myCartAdapter;
    CartReviewAdapter cartReviewAdapter;
    RadioGroup radiogroup;
    CheckBox policy_checkbox;
    FirebaseFirestore firestore;
    String value;
    int count;
    Float totalAmount= (float) 0;
    FirebaseAuth auth;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);


        toolbar =findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        name=findViewById(R.id.ad_name);
        address=findViewById(R.id.ad_address);
        city=findViewById(R.id.ad_city);
        email=findViewById(R.id.ad_email);
        phoneNumber=findViewById(R.id.ad_phone);

        quantity=findViewById(R.id.quantity_item);
        totalprice=findViewById(R.id.total_amount);


        //Cart Review
        recyclerView = findViewById(R.id.buy_now_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myCartModelList = new ArrayList<>();
        cartReviewAdapter = new CartReviewAdapter(this, myCartModelList);
        recyclerView.setAdapter(cartReviewAdapter);

        radiogroup=findViewById(R.id.radio_group);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = radiogroup.getCheckedRadioButtonId();

                // Lấy value của radio button
                RadioButton radioButton = findViewById(radioButtonId);
                value = radioButton.getText().toString();
            }
        });

        policy_checkbox=findViewById(R.id.policy_argree);


        gocheckout=findViewById(R.id.ad_go_checkout);
        gocheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=name.getText().toString();
                String userAddress=address.getText().toString();
                String userCity=city.getText().toString();
                String userEmail=email.getText().toString();
                String userPhone=phoneNumber.getText().toString();

                String saveCurrentTime, saveCurrentDate;
                Calendar calForDate = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd.MM.yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime=currentTime.format(calForDate.getTime());

                SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("city", city.getText().toString());
                editor.putString("address", address.getText().toString());
                editor.putString("phone", phoneNumber.getText().toString());
                editor.apply();

                String  final_name="";
                String  final_address="";
                String  final_city="";
                String  final_email="";
                String  final_phone="";

                if(!userName.isEmpty()){
                    final_name+=userName;
                }
                if(!userAddress.isEmpty()){
                    final_address+=userAddress;
                }
                if(!userCity.isEmpty()){
                    final_city+=userCity;
                }
                if(!userEmail.isEmpty()){
                    final_email+=userEmail;
                }
                if(!userPhone.isEmpty()){
                    final_phone+=userPhone;
                }
                if(!userName.isEmpty() && !userAddress.isEmpty() && !userCity.isEmpty() && !userEmail.isEmpty() &&
                        !userPhone.isEmpty() && radiogroup.getCheckedRadioButtonId()!= -1 && policy_checkbox.isChecked()){
                    Map<String,String> map = new HashMap<>();
                    map.put("userName",final_name);
                    map.put("userAddress",final_address);
                    map.put("userCity",final_city);
                    map.put("userEmail",final_email);
                    map.put("userPhone",final_phone);
                    map.put("methodPayment", value);
                    map.put("date",saveCurrentDate);
                    map.put("time",saveCurrentTime);

                    firestore.collection("CurentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddAddressActivity.this,"Address Added",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    Intent intent = new Intent(AddAddressActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                }
                else if(userName.isEmpty() && userAddress.isEmpty() && userCity.isEmpty() && userEmail.isEmpty() && userPhone.isEmpty())
                {Toast.makeText(AddAddressActivity.this,"Please fill all information field",Toast.LENGTH_SHORT).show();}
                else if (radiogroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(AddAddressActivity.this, "You have to choose the payment method to continute", Toast.LENGTH_SHORT).show();
                }
                else if (!policy_checkbox.isChecked()) {
                    Toast.makeText(AddAddressActivity.this, "You have to check the Policy Argreement box to continute", Toast.LENGTH_SHORT).show();
                }

            }
        });



        firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                myCartModelList.add(myCartModel);
                                cartReviewAdapter.notifyDataSetChanged();
                                Float price=doc.getDouble("totalprice").floatValue();
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