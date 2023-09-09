package com.example.financas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enviar = findViewById(R.id.enviar);
        final Spinner categorias = findViewById(R.id.categorias);
        final Spinner forma_pagamentos = findViewById(R.id.forma_pagamentos);
        final EditText valor = findViewById(R.id.valor);
        final EditText obs = findViewById(R.id.obs);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object categoria = categorias.getSelectedItem();
                String str_categoria = String.valueOf(categoria);

                Object forma_pagamento = forma_pagamentos.getSelectedItem();
                String str_forma_pagamento = String.valueOf(forma_pagamento);

                Editable valor_pagamento = valor.getText();
                Editable valor_obs = obs.getText();

                sendRequest("https://profrodrigoaffonso.com.br/api/financas", str_categoria, str_forma_pagamento, valor_pagamento.toString(), valor_obs.toString());

                valor.setText("");
                obs.setText("");

            }
        });
    }

    protected String sendRequest(String strUrl, String categoria, String forma_pagamento, String valor_pagamento, String valor_obs) {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        JSONObject json = new JSONObject();


        try {
            url = new URL(strUrl);
            json.put("categoria",categoria);
            json.put("forma_pagamento",forma_pagamento);
            json.put("valor",valor_pagamento);
            json.put("obs",valor_obs);
            json.put("token","");
            String dados = json.toString();
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            //request.write(parameters);
            request.write((dados));
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            // You can perform UI operations here
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            isr.close();
            reader.close();


        } catch (IOException e) {
            // Error
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
