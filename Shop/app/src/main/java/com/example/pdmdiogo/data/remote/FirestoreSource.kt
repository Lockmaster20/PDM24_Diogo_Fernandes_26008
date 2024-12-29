package com.example.pdmdiogo.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FirestoreSource {
    val db: FirebaseFirestore = Firebase.firestore
}