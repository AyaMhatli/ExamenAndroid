package com.example.project7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner sourceSpinner;
    private Button nextButton;

    //  private List<String> currencies ;
    private String sourceCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceSpinner = findViewById(R.id.sourceSpinner);
        nextButton = findViewById(R.id.nextButton);
        // Appel à l'API pour obtenir les taux de change
        String apiUrl = "https://m1mpdam-exam.azurewebsites.net//CurrencyExchange/Currencies/";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> currencies = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                String currency = response.getString(i);
                                currencies.add(currency);
                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, currencies);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sourceSpinner.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(request);





        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceCurrency = sourceSpinner.getSelectedItem().toString();
                Intent intent = new Intent(MainActivity.this, RatesActivity.class);
                intent.putExtra("source_currency", sourceCurrency);
                startActivity(intent);
            }
        });
    }
}