package com.example.giaodienchinh_doan;



import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MessagingService extends FirebaseMessagingService {
 @Override
 public void onNewToken(@NonNull String token){
     super.onNewToken(token);

 }

 @Override
 public void onMessageReceived(@NonNull RemoteMessage remoteMessage){
     super.onMessageReceived(remoteMessage);

 }
}
