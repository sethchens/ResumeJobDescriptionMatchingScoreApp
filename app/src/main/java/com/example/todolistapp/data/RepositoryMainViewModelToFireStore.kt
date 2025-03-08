package com.example.todolistapp.data
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore



// THIS IS HOW FIRESTORE DATABASE STORE DATA (NoSQL)
// Firestore
//   └── Collection
//       └── Document
//           └── Fields (Key-Value pairs)
//           └── Subcollections (Nested Collections)
//
// Where collection is like a table, and document is like a row
// Each document contains fields with a key-value pair. The key is the
// field name (string), and the value can be any Firestore-supported data type:
// strings, numbers, booleans, maps, arrays, etc.
class RepositoryMainViewModelToFireStore(private val db: FirebaseFirestore) {

    private val _resumeJob = MutableLiveData<List<DocumentSnapshot>>()
    val resumeJob: LiveData<List<DocumentSnapshot>> get() = _resumeJob

    fun fetchResumeJob() {
        db.collection("resume_job_match")
            .get()
            .addOnSuccessListener { result ->
                // Create a list of documents
                val documents = result.documents
                Log.d("Firestore", "Fetched documents: $documents")

                // Update _resumeJob (LiveData)
                _resumeJob.postValue(documents)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }

    fun createResumeJobScore(resumeText: String, jobText: String, score: Float) {
        val newPair = hashMapOf(
            "resume_text" to resumeText,
            "job_description_text" to jobText,
            "score" to score
        )
        db.collection("resume_job_match")
            .add(newPair)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Document added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error adding document", exception)
            }
    }

}