package com.nikhil.dialogflowdemo.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.nikhil.dialogflowdemo.R;
import com.nikhil.dialogflowdemo.chat.DialogFlowActivity;
import com.nikhil.dialogflowdemo.utils.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nikhil on 26/1/18.
 */

public class AgentsActivity extends AppCompatActivity {


    @BindView(R.id.agentsActivity_MainLayout)
    ConstraintLayout agentsActivity_MainLayout;
    @BindView(R.id.agentName_EditText)
    EditText agentName_EditText;
    @BindView(R.id.accessToken_EditText)
    EditText accessToken_EditText;
    @BindView(R.id.addAgent_Button)
    Button addAgentButton;
    @BindView(R.id.agentList_RecyclerView)
    RecyclerView agentList_RecyclerView;
    //
    PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        if (!prefManager.getAccessToken().trim().equals("")){
            startDialogFlowActivity();
        }

        setContentView(R.layout.activity_agents);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.addAgent_Button)
    public void addAgent(){
        String agentName = agentName_EditText.getText().toString().trim();
        String accessToken = accessToken_EditText.getText().toString().trim();

        if (agentName.equals("")){
            showSnackBar("Agent Name Cannot Be Blank");
            agentName_EditText.setError("Please Enter Name!");
        } else if (accessToken.equals("")){
            showSnackBar("Access Token Cannot Be Blank");
            accessToken_EditText.setError("Please Enter Access Token!");
        } else {
            prefManager.setAgentDetailsToken(agentName, accessToken);
            startDialogFlowActivity();
        }
    }

    private void startDialogFlowActivity() {
        startActivity(new Intent(this, DialogFlowActivity.class));
        finish();
    }

    private void showSnackBar(String message) {
        Snackbar.make(agentsActivity_MainLayout, message, Snackbar.LENGTH_LONG).show();
    }

}
