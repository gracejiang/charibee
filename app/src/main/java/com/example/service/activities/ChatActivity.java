package com.example.service.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.R;
import com.example.service.data.Data;
import com.example.service.adapters.ChatAdapter;
import com.example.service.models.Message;
import com.example.service.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = "ChatActivity";

    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    // ui views
    EditText etMessage;
    ImageButton btnSend;

    // data
    RecyclerView rvChat;
    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;
    boolean mFirstLoad; // Keep track of initial load to scroll to the bottom of the ListView

    // users
    private ParseUser pCurrentUser;
    private User currentUser;
    private User toUser;

    // boolean new msg
    private boolean newMsg = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        pCurrentUser = ParseUser.getCurrentUser();
        currentUser = new User(pCurrentUser);
        toUser = Data.getToUser();

        setupMessagePosting();
        refreshMessages();
    }

    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // bind ui views
        etMessage = (EditText) findViewById(R.id.chat_message_et);
        btnSend = (ImageButton) findViewById(R.id.chat_send_et);
        rvChat = (RecyclerView) findViewById(R.id.chat_msg_rv);

        // set data values
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = pCurrentUser.getObjectId();
        mAdapter = new ChatAdapter(ChatActivity.this, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true); // orders msgs from oldest to newest
        rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object on Parse
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageStr = etMessage.getText().toString();

                if (messageStr != null & messageStr.length() > 0 && toUser != null) {
                    // Using new `Message` Parse-backed model now
                    Message message = new Message();
                    message.setBody(messageStr);
                    message.setUserId(pCurrentUser.getObjectId());
                    message.setToUser(toUser.getParseUser());
                    message.setFromUser(pCurrentUser);

                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                refreshMessages();
                            } else {
                                Log.e(TAG, "Failed to save message", e);
                            }
                        }
                    });
                    etMessage.setText(null);

                    if (newMsg) {
                        Log.i(TAG, "made it into new msg!");
                        // toUser.addUserToMsgsWith(currentUser);
                        currentUser.addUserToMsgsWith(toUser);
                        newMsg = false;
                    }
                }
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {

        // query msgs
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW); // limits 50 msgs

        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(filterMsgs(messages));
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    // manual filtering
    private List<Message> filterMsgs(List<Message> messages) {
        List<Message> msgs = new ArrayList<>();
        for (Message msg : messages) {
            if (validMessage(msg)) {
                msgs.add(0, msg);
            }
        }
        return msgs;
    }

    private boolean validMessage(Message message) {
        String currUserId = pCurrentUser.getObjectId();
        String toUserId = toUser.getId();

        String msgToUserId = message.getToUser().getObjectId();
        String msgFromUserId = message.getFromUser().getObjectId();
        return ((msgToUserId.equals(toUserId) && msgFromUserId.equals(currUserId))
                || (msgToUserId.equals(currUserId) && msgFromUserId.equals(toUserId)));
    }

    // MORE EFFICIENT QUERYING (every 3 seconds instead of constant)

    // create handler which runs code periodically
    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(20);
    Handler myHandler = new android.os.Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // only start checking for new messages when the app becomes active in foreground
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    @Override
    protected void onPause() {
        // Stop background task from refreshing messages, to avoid unnecessary traffic & battery drain
        myHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}
