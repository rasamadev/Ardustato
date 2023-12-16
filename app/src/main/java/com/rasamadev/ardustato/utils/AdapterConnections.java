package com.rasamadev.ardustato.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.Connection;

import java.util.List;

public class AdapterConnections extends ArrayAdapter<Connection> {

    Context ctx;
    int layoutTemplate;
    List<Connection> connectionList;

    public AdapterConnections(@NonNull Context context, int resource, @NonNull List<Connection> connectionList) {
        super(context, resource, connectionList);
        this.ctx = context;
        this.layoutTemplate = resource;
        this.connectionList = connectionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate,parent,false);

        // Obtener la informacion del elemento de la lista que estoy iterando en este momento
        Connection elementoActual = connectionList.get(position);

        // Rescatar los elementos de la UI de la template
        TextView txtConnectionName_Adapter = (TextView) v.findViewById(R.id.tvConnectionName_Adapter);
        TextView txtConnectionIp_Adapter = (TextView) v.findViewById(R.id.tvConnectionIp_Adapter);

        // Hacer un set de la info del elemento Actual en los elementos de la UI
        txtConnectionName_Adapter.setText(elementoActual.getConnectionname());
        txtConnectionIp_Adapter.setText(elementoActual.getIp());

        return v;
    }
}
