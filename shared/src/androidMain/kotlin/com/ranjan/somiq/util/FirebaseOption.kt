package com.ranjan.somiq.util

import com.google.firebase.FirebaseOptions

val firebaseOptions = FirebaseOptions.Builder()
    .setProjectId("we4rule-1f526")
    .setApplicationId("1:477460285859:android:65c6e012419365c238f216") // From google-services.json -> client -> client_info -> mobilesdk_app_id
    .setApiKey("AIzaSyB4q-Tg24TamJBsI6VQLBDuEn5x8jD4WLA") // From google-services.json -> client -> api_key -> current_key
    .build()