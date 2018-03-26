package com.nikhil.dialogflowdemo.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikhil.dialogflowdemo.R;
import com.nikhil.dialogflowdemo.app.BaseActivity;
import com.nikhil.dialogflowdemo.db.ChatMessages;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikhil on 26/3/18.
 */

public class DialogFlowActivity2 extends BaseActivity {

    @BindView(R.id.chatRecyclerView)
    RecyclerView chatRecyclerView;
    //
    ArrayList<ChatMessages> arrayList;
    ChatAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogflow2);
        ButterKnife.bind(this);

        arrayList = new ArrayList<>();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        chatRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList.add(new ChatMessages(true, null, "Hi"));
        arrayList.add(new ChatMessages(false, null, "Hello"));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int VIEW_TYPE_USER = 0;
        int VIEW_TYPE_AGENT = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == VIEW_TYPE_USER) {
                 view = LayoutInflater.from(DialogFlowActivity2.this)
                        .inflate(R.layout.layout_message_user, parent, false);
                return new UserViewHolder(view);
            } else if (viewType == VIEW_TYPE_AGENT) {
                view = LayoutInflater.from(DialogFlowActivity2.this)
                        .inflate(R.layout.layout_message_agent, parent, false);
                return new AgentViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof UserViewHolder) {
                ((UserViewHolder) holder).userMessage_TextView.setText(arrayList.get(position).getMessage());
            } else if (holder instanceof AgentViewHolder) {
                ((AgentViewHolder) holder).agentMessage_TextView.setText(arrayList.get(position).getMessage());
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (arrayList.get(position).isUser()){
                return VIEW_TYPE_USER;
            } else {
                return VIEW_TYPE_AGENT;
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userMessage_TextView)
        TextView userMessage_TextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AgentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.agentMessage_TextView)
        TextView agentMessage_TextView;

        public AgentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
