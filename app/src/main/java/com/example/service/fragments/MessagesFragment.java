package com.example.service.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.service.R;
import com.example.service.activities.ChatActivity;

public class MessagesFragment extends Fragment {

    // ui views
    Button btnChat;

    public MessagesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        // bind views
        btnChat = view.findViewById(R.id.messages_chat_btn);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goChatActivity();
            }
        });

    }

    // go to chat activity
    private void goChatActivity() {
        Intent i = new Intent(getContext(), ChatActivity.class);
        startActivity(i);
    }


}