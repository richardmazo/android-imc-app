package com.rilel.imc_app.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rilel.imc_app.model.Ubicacion;
import com.rilel.imc_app.utils.Callback;

import java.lang.reflect.Type;
import java.util.List;

public class LocationFirebaseImpl implements LocationFirebase {

    final static String COLLECTION = "ubicacion";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<DocumentSnapshot> todosLosDocuments;
    private final String TAG = "Logs Firebase";

    @Override
    public void crear(Ubicacion ubicacion, Callback callback) {
        db.collection(COLLECTION)
                .document(ubicacion.getReference())
                .set(ubicacion.getMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.OnSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFailure(e);
                    }
                });
    }

    @Override
    public List<DocumentSnapshot>[] /*void*/ leerTodos(Callback callback) {
        final List<DocumentSnapshot>[] resultado = new List[0];
        db.collection(COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"signInWithCredential:success");
                            callback.OnSuccess(task.getResult().getDocuments());
                            resultado[0] = task.getResult().getDocuments();
                            Log.d(TAG,resultado[0].toString());
                        }else{
                            Log.w(TAG,"signInWithCredential:failure",task.getException());
                            Log.d("Logs de leerTodos", "Respuesta con leerTodos, " + task.getException());
                            callback.OnFailure(null);
                        }
                    }
                });
        return resultado;
    }

    @Override
    public List<Type> getListItems() {
        List<Type> mArrayList = null;
        db.collection(COLLECTION).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            List<Type> types = documentSnapshots.toObjects(Type.class);
                            mArrayList.addAll(types);
                            Log.d(TAG, "onSuccess: " + mArrayList);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return mArrayList;
    }

    private void devolverResultado(List<DocumentSnapshot> documents) {
        this.todosLosDocuments = documents;
    }

    @Override
    public void leerPorId(String id, Callback callback) {
        db.collection(COLLECTION)
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            callback.OnSuccess(task.getResult());
                        }else{
                            callback.OnFailure(null);
                        }
                    }
                });
    }
}
