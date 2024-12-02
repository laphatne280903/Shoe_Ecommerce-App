package com.example.giaodienchinh_doan.AnotherNav;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaodienchinh_doan.AdapterView.ChatAdapter;
import com.example.giaodienchinh_doan.ImageUploadCallback;
import com.example.giaodienchinh_doan.Model.ChatMessage;
import com.example.giaodienchinh_doan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private static int PICK_IMAGE_REQUEST=1;
    ProgressBar progressBar;
    RecyclerView ChatRec;
    String Id;
    String Email;
    String Name;
    TextView NameView;
    String Avatar;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;
    private List<ChatMessage> chatMessages;
    FirebaseFirestore firestore;
    String sender;
    EditText MessInput;
    ImageView imageView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        imageView = findViewById(R.id.ImageSentButton);
        imageView.setOnClickListener(v -> openFileChooser());

        ImageView chatAva = findViewById(R.id.avaImgChat);
        toolbar = findViewById(R.id.chat_back_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ChatRec = findViewById(R.id.Chat_Recycler);
        sender =user.getUid().toString();
        Email = getIntent().getStringExtra("user_email");
        Avatar = getIntent().getStringExtra("user_Img");
        Name = getIntent().getStringExtra("user_name");
        Id = getIntent().getStringExtra("user_Id");
        NameView = findViewById(R.id.nameUserChat);
        NameView.setText(Name);
        Glide.with(this)
                .load(Avatar)
                .override(30, 30) // Điều chỉnh kích thước ở đây theo nhu cầu của bạn
                .centerCrop()
                .into(chatAva);



        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        init(sender);
        loadMessages();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Hiển thị ảnh được chọn lên ImageView
            uploadImage(imageUri);
        }
    }


    private void uploadImage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();



        if (imageUri != null) {
            StorageReference imagesRef = storageRef.child("images/" + imageUri.getLastPathSegment());
            imagesRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle success
                        imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Lấy URL từ ảnh tải lên
                            String imageUrl = uri.toString();
                            Log.d("Link has been uploaded : ", imageUrl);
                            sendMessagePic(imageUrl);
                            Log.d(TAG, "Upload successful");

                        }).addOnFailureListener(e -> Log.d(TAG, "Failed to get download URL"));
                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors
                        Log.d(TAG, "Upload failed");
                    });
        }
    }



    private void init(String SenderId) {
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, SenderId,Id);
        RecyclerView recyclerView = findViewById(R.id.Chat_Recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
        listenForMessages();
    }


    public void sendMessage() {
        MessInput = findViewById(R.id.message_input);
        String mess = MessInput.getText().toString();



        HashMap<String, Object> message = new HashMap<>();
        message.put("senderId", sender);
        message.put("receiverId", Id);
        message.put("message", mess);
        message.put("dateTime", new Date());
        message.put("img", null);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Chats")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    MessInput.setText("");
                    // Tải lại tin nhắn sau khi gửi thành công
                    loadMessages();

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }


    public void sendMessagePic(String url) {
        MessInput = findViewById(R.id.message_input);
        String mess = MessInput.getText().toString();



        HashMap<String, Object> message = new HashMap<>();
        message.put("senderId", sender);
        message.put("receiverId", Id);
        message.put("message", null);
        message.put("dateTime", new Date());
        message.put("img", url);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Chats")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    MessInput.setText("");
                    // Tải lại tin nhắn sau khi gửi thành công
                    loadMessages();

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
    private void loadMessages() {
        database.collection("Chats")
                .whereEqualTo("senderId", sender)
                .whereEqualTo("receiverId", Id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        chatMessages.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                ChatMessage message = document.toObject(ChatMessage.class);
                                chatMessages.add(message);
                                Log.d("Message Loaded", message.message);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Load Messages", "Error converting document to object: " + e.getMessage());
                            }
                        }
                        // Sắp xếp danh sách tin nhắn theo thời gian
                        Collections.sort(chatMessages, (o1, o2) -> o1.dateTime.compareTo(o2.dateTime));
                        // In ra logcat
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        if (!sender.equals(Id)) {
            database.collection("Chats")
                    .whereEqualTo("senderId", Id)
                    .whereEqualTo("receiverId", sender)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    ChatMessage message = document.toObject(ChatMessage.class);
                                    chatMessages.add(message);
                                    Log.d("Message Loaded", message.message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("Errr", "Error converting document to object: " + e.getMessage());
                                }
                            }

                            Collections.sort(chatMessages, (o1, o2) -> o1.dateTime.compareTo(o2.dateTime));

                            chatAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Error getting documents", "Error getting documents: ", task.getException());
                        }
                    });
        }
    }



    private void listenForMessages() {
        database.collection("Chats")
                .whereEqualTo("senderId", sender)
                .whereEqualTo("receiverId", Id)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (value != null) {
                        chatMessages.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            ChatMessage message = doc.toObject(ChatMessage.class);
                            chatMessages.add(message);

                        }

                        chatAdapter.notifyDataSetChanged();
                    }
                });

    }






}