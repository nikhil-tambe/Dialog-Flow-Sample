package com.nikhil.dialogflowdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nikhil.dialogflowdemo.Constants.API_KEY;
import static com.nikhil.dialogflowdemo.Constants.APPLICATION_JSON;
import static com.nikhil.dialogflowdemo.Constants.AUTHORIZATION;
import static com.nikhil.dialogflowdemo.Constants.BASE_URL;
import static com.nikhil.dialogflowdemo.Constants.BEARER;
import static com.nikhil.dialogflowdemo.Constants.CONTENT_TYPE;
import static com.nikhil.dialogflowdemo.Constants.J_KEY_LANG;
import static com.nikhil.dialogflowdemo.Constants.J_KEY_QUERY;
import static com.nikhil.dialogflowdemo.Constants.J_KEY_RESULT;
import static com.nikhil.dialogflowdemo.Constants.J_KEY_SESSION_ID;
import static com.nikhil.dialogflowdemo.Constants.J_KEY_SPEECH;
import static com.nikhil.dialogflowdemo.Constants.K_KEY_FULFILLMENT;
import static com.nikhil.dialogflowdemo.Constants.VALUE_LANG_EN;
import static com.nikhil.dialogflowdemo.Constants.VALUE_SESSION_ID;

/**
 * Created by nikhil on 7/1/18.
 */

public class DialogFlowActivity extends AppCompatActivity {

    public static final int AUDIO_PERMISSION = 123;
    private static final String TAG = "nikhil DialogFlow";

    private final int REQ_CODE_SPEECH_INPUT = 100;
    //
    @BindView(R.id.mainActivity_RL)
    RelativeLayout mainActivity_RL;
    @BindView(R.id.messages_ScrollView)
    ScrollView scrollView;
    @BindView(R.id.messages_LinerLayout)
    LinearLayout messages_LinerLayout;
    @BindView(R.id.queryInput_EditText)
    EditText queryInput_EditText;
    //
    Context context;
    int userColor, aiColor, whiteColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogflow);
        ButterKnife.bind(this);
        context = DialogFlowActivity.this;

        userColor = ContextCompat.getColor(context, R.color.colorUserRequest);
        aiColor = ContextCompat.getColor(context, R.color.colorAIResponse);
        whiteColor = ContextCompat.getColor(context, R.color.colorWhite);

        /*// Trial
        String userQuery = "weight loss";
        addMessage(false, "You: \n" + userQuery);
        queryInput_EditText.setText(userQuery);
        new GetResponseAsync().execute(userQuery);*/
    }

    /**
     * Showing google speech input dialog
     */
    @OnClick(R.id.mic_Button)
    public void promptSpeechInput() {
        if (checkAudioPermission()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Your Query Now");
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } else {
            showShortSnackBar("Record Permission Required");
        }
    }

    @OnClick(R.id.sendMessage_Button)
    public void sendMessage() {
        String userQuery = queryInput_EditText.getText().toString();
        if (userQuery.trim().equals("")) {
            showShortSnackBar("Query is blank");
            return;
        }
        addMessage(false, "You: \n" + userQuery);
        //txtSpeechInput.setText(userQuery);
        new GetResponseAsync().execute(userQuery);
        queryInput_EditText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String userQuery = result.get(0);
                    addMessage(false, "You: \n" + userQuery);
                    //txtSpeechInput.setText(userQuery);
                    new GetResponseAsync().execute(userQuery);
                }
                break;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUDIO_PERMISSION) {
            if (grantResults.length == 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Permissions Not Granted
                Log.e(TAG, "onRequestPermissionsResult: Record Audio Permissions Not Granted");
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Record Audio Permissions Granted");
            }
        }
    }

    private boolean checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION);
                return false;
            }
        }
        return false;
    }

    private void addMessage(boolean onLeft, String s) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        tv.setTextSize(18);
        tv.setPadding(15,18,15,18);
        tv.setText(s);
        lp.bottomMargin = lp.topMargin = 10;
        //lp.weight = 1.0f;
        if (onLeft) {
            lp.gravity = Gravity.START;
            lp.setMarginStart(20);
            lp.setMarginEnd(150);
            tv.setBackgroundColor(aiColor);
        } else {
            lp.gravity = Gravity.END;
            lp.setMarginStart(150);
            lp.setMarginEnd(20);
            tv.setTextColor(whiteColor);
            tv.setBackgroundColor(userColor);
        }
        tv.setLayoutParams(lp);

        messages_LinerLayout.addView(tv);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void showShortSnackBar(String message) {
        Snackbar.make(mainActivity_RL, message, Snackbar.LENGTH_LONG).show();
    }

    private class GetResponseAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            return getResponse(voids[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            addMessage(true, "Response: \n" + s);
        }

        private String getResponse(String query) {
            String text = "Error";
            BufferedReader reader = null;
            try {
                // Defined Base URL
                URL url = new URL(BASE_URL);

                // Open Connection
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // Set request properties
                conn.setRequestProperty(AUTHORIZATION, BEARER + " " + API_KEY);
                conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);

                //Create request JSONObject
                JSONObject jsonParam = new JSONObject();
                JSONArray queryArray = new JSONArray();
                queryArray.put(query);
                jsonParam.put(J_KEY_QUERY, queryArray);
                jsonParam.put(J_KEY_LANG, VALUE_LANG_EN);
                jsonParam.put(J_KEY_SESSION_ID, VALUE_SESSION_ID);

                // UTF-8 encoding
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(jsonParam.toString());
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                // Read & Save Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line);
                    sb.append("\n");
                }
                text = sb.toString();

                // Convert text to JSONObject and extract value stored in 'speech' keyword.
                JSONObject object1 = new JSONObject(text);
                JSONObject object = object1.getJSONObject(J_KEY_RESULT);
                JSONObject fulfillment = object.getJSONObject(K_KEY_FULFILLMENT);

                // return value stored in 'speech' key
                return fulfillment.optString(J_KEY_SPEECH);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
            return text;
        }

    }
}
