package com.dnd.Exercise.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileInputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public FirebaseApp initFireBase(){
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase/firebase_service_key.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            return FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(){
        FirebaseMessaging instance = FirebaseMessaging.getInstance();
        return instance;
    }

}
