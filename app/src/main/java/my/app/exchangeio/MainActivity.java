package my.app.exchangeio;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private RequestQueue requestQueue;

    String url2 = "https://api.apilayer.com/exchangerates_data/convert?to=&from=&amount=";

    TextView textView, from;
    Button button;
    EditText amount;
    Spinner convertToDropdown;
    Spinner convertFromDropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.resultID);
        button = findViewById(R.id.clickActionID);
        amount = findViewById(R.id.amountID);
        from = findViewById(R.id.FromID);


        convertToDropdown = (Spinner) findViewById(R.id.toID);
        convertFromDropdown = (Spinner) findViewById(R.id.fromID);

        String[] dropDownListFrom = {"USD", "GBP", "EUR", "AED", "KWD", "SAR", "INR"};
        String[] dropDownListTO = {"BDT", "INR", "EUR", "SAR", "AED", "USD", "KWD", "GBP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dropDownListFrom);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dropDownListTO);

        convertToDropdown.setAdapter(adapter2);
        convertFromDropdown.setAdapter(adapter);


        if (isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            textView.setText("No Internet");
            textView.setTextColor(Color.RED);

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");

                String input = amount.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    amount.setError("Please Enter Amount");

                } else {
                    String ff = convertFromDropdown.getSelectedItem().toString();
                    String tt = convertToDropdown.getSelectedItem().toString();

                    url2 = "https://api.apilayer.com/exchangerates_data/convert?to=" + tt + "&from=" + ff + "&amount=" + input;
                    getData();

                }


            }
        });


    }

    private void getData() {
        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String result = response.getString("result");
                    String from2 = response.getString("query");

                    from.setText(from2);
                    textView.setText(result);
                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            // You can send parameters as header with POST request also....
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("apiKey", "j1RHjFvBlCPQCvb12hUpOwaWRFXmp2mM");
                return headers;
            }
        };


        requestQueue.add(req);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}





