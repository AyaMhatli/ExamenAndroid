package com.example.project7;



import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RatesActivity extends AppCompatActivity {

    private ListView ratesListView;
    private Button backButton;
    private TextView textammont;
    private EditText ammont ;

    private String sourceCurrency;
    private ArrayList<String> ratesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate);

        ratesListView = findViewById(R.id.ratesListView);
        backButton = findViewById(R.id.backButton);
        textammont = findViewById(R.id.ammontdevise);
        ammont = findViewById(R.id.ammont);

        // Récupération de la devise source sélectionnée dans l'activité MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sourceCurrency = extras.getString("source_currency");
            textammont.setText(sourceCurrency);
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   finish();
                String texte = ammont.getText().toString();
                // Appel à l'API pour obtenir les taux de change
                String apiUrl = "https://m1mpdam-exam.azurewebsites.net/CurrencyExchange/ExchangeRates/" + sourceCurrency+"/"+texte+"/";
                RequestQueue queue = Volley.newRequestQueue(RatesActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    JSONObject ratesObject = jsonResponse.getJSONObject("rates");
                                    List<String> rateList = new ArrayList<String>();
                                    Iterator<String> keys = ratesObject.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        double rate = ratesObject.getDouble(key);
                                        String rateString = key + ": " + Double.toString(rate);
                                        rateList.add(rateString);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RatesActivity.this, android.R.layout.simple_list_item_1, rateList);

                                    ratesListView.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                    }
                });
                queue.add(stringRequest);

            }


        });


    }

}