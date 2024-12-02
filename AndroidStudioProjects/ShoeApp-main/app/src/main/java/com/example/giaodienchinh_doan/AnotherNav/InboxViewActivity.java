package com.example.giaodienchinh_doan.AnotherNav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.giaodienchinh_doan.AdapterView.InboxAdapter;
import com.example.giaodienchinh_doan.UserListener;
import com.example.giaodienchinh_doan.User;
import com.example.giaodienchinh_doan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InboxViewActivity extends AppCompatActivity implements UserListener {
    RecyclerView recyclerView;
    String CurrentUser;

    InboxAdapter adapter;
    ArrayList<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_view);

        recyclerView = findViewById(R.id.inbox_list);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new InboxAdapter(this,list,this);
        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        list.add(user);


                        adapter.notifyDataSetChanged();


                    }

                }
                // Ở đây, bạn có thể sử dụng danh sách userList để hiển thị danh sách người dùng
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }
    @Override
    public void onUserClicked(User user){
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("user_email", user.email);
        intent.putExtra("user_name", user.displayName);
        intent.putExtra("user_Id", user.id);
        intent.putExtra("user_Img", user.getI());
        startActivity(intent);
        finish();
    }
}