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

    @Value("${firebase.file.path}")
    private String filePath;

    @PostConstruct
    public FirebaseApp initFireBase(){
        try {
            FileInputStream serviceAccount = new FileInputStream(filePath);
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
