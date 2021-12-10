package com.rilel.imc_app.repository;

import com.google.firebase.firestore.DocumentSnapshot;
import com.rilel.imc_app.model.Ubicacion;
import com.rilel.imc_app.utils.Callback;

import java.lang.reflect.Type;
import java.util.List;

public interface LocationFirebase {

    void crear(Ubicacion ubicacion, Callback callback);
    List<DocumentSnapshot>[] leerTodos(Callback callback);
    void leerPorId(String id, Callback callback);
    List<Type> getListItems();

}
