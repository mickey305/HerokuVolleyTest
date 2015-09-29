package com.example.herokuvolleytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements HerokuUri, View.OnClickListener{
    private static final String hostUri = TEST_SERVER_URI;

    private final String testStr = "{\"name\":[" +
            "\n{\"last\":\"kohdai\",\"first\":\"taro\"}," +
            "\n{\"last\":\"suzuki\",\"first\":\"hanako\"}," +
            "\n{\"last\":\"tanaka\",\"first\":\"syota\"}" +
            "\n]}";
    private RequestQueue requestQueue;
    private EditText postEditText;
    private Button sendButton;
    private TextView resTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.postEditText = (EditText) findViewById(R.id.post_edt);
        this.postEditText.setText(this.testStr);
        this.resTextView = (TextView) findViewById(R.id.res_txt);
        this.sendButton = (Button) findViewById(R.id.send_btn);
        this.sendButton.setOnClickListener(this);
        this.requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.send_btn) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(this.postEditText.getText().toString());
            } catch (JSONException e) {
                this.resTextView.setText(e.toString());
            }

            if(jsonObject != null) {
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, hostUri, jsonObject,
                        createReqSuccessListener(), createReqErrorListener());

                this.requestQueue.add(req);
            }
        }
    }

    private Response.Listener<JSONObject> createReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                resTextView.setText(response.toString());
            }
        };
    }

    private Response.ErrorListener createReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resTextView.setText(error.toString());
            }
        };
    }
}
