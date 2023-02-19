package DB_Testing.FireBaseDBTesting;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class FirebaseFirestoreJavaExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try {
            // Read service account credentials from a JSON file
            FileInputStream serviceAccount = new FileInputStream("C:\\Users\\Emre Duman\\Downloads\\fir-dbtesting-9e136-firebase-adminsdk-evahi-c695e74175.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            // Initialize the Firebase Admin SDK
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);

            // Get a reference to the Firestore database
            Firestore db = FirestoreClient.getFirestore();

            // Get a reference to the "students" collection
            Query query = db.collection("School_Sysytem");

            // Get the query results
            QuerySnapshot querySnapshot = query.get().get();

            // Iterate over the query results
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                String id = document.getId();
                String name = document.getString("isim");
                String okul = document.getString("okul");
               // double yas = document.getDouble("yas");

                System.out.println("Student ID: " + id + "- Name: " + name+ " - okul: "+okul);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
