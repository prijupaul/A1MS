package uk.com.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.MessageAdapter;
import uk.com.a1ms.dto.LongMessage;
import uk.com.a1ms.dto.Message;
import uk.com.a1ms.dto.ShortMessage;
import uk.com.a1ms.messages.MessageParser;
import uk.com.a1ms.network.dto.MessageResponseDetails;
import uk.com.a1ms.network.handlers.UserMessageIOSocketHandler;
import uk.com.a1ms.network.handlers.UserMessageWebSocketHandler;
import uk.com.a1ms.util.DateTime;
import uk.com.a1ms.util.ExecutorUtils;
import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 28/07/16.
 */
public class MessagingFragment extends BaseFragment implements View.OnClickListener,
        UserMessageWebSocketHandler.UserMessageWebSocketListener,
        UserMessageIOSocketHandler.UserMessageIOSocketListener {

    private MessagingFragmentListener mMessagingFragmentListener;
    private ListView msgListView;
    private EditText msg_edittext;
    private ArrayList<Message> mMessagesArrayList = new ArrayList<>();
    public static MessageAdapter messageAdapter;
    //    private UserMessageWebSocketHandler webSocketHandler;
    private UserMessageIOSocketHandler webIOSocketHandler;
    private MessageParser messageParser;


    public interface MessagingFragmentListener {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        webSocketHandler = new UserMessageWebSocketHandler(this);
        webIOSocketHandler = new UserMessageIOSocketHandler(this);
        messageParser = new MessageParser();
        ExecutorUtils.runInBackgroundThread(new Runnable() {
            @Override
            public void run() {
//                webSocketHandler.connect();
                webIOSocketHandler.connect();
            }
        });
    }

    public static MessagingFragment newInstance(MessagingFragmentListener createGroupsFragmentListener) {

        MessagingFragment fragment = new MessagingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.mMessagingFragmentListener = createGroupsFragmentListener;
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.messaging_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        messageAdapter = new MessageAdapter(getActivity(), mMessagesArrayList);
        msgListView.setAdapter(messageAdapter);

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        sendTextMessage(view);
    }

    public void sendTextMessage(View v) {
            String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {


            // TODO: Parse the messages here.

            SpannableString spannableString = new SpannableString(messageParser.Parse(message));

            Message messageObj = new Message();

            LongMessage longMessage = new LongMessage();
            longMessage.setLongMessage(new SpannableString(messageParser.getLongSpannableStringBuilder()));

            ShortMessage shortMessage = new ShortMessage();
            shortMessage.setShortMessage(spannableString);

            messageObj.setShortMessage(shortMessage);
            messageObj.setMessage(longMessage);
            messageObj.setSelf(true);
            messageObj.setTime(DateTime.getTimeInAmPm());

            msg_edittext.setText("");
            messageAdapter.add(messageObj);
            messageAdapter.notifyDataSetChanged();

            String token = SharedPreferenceManager.getUserToken(getContext());
            final JSONObject jsonObject = messageObj.convertToJson(token);

//            webSocketHandler.sendMessage(jsonObject.toString());
            webIOSocketHandler.sendMessage(jsonObject.toString());

        }
    }


    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        if (webIOSocketHandler != null) {
            webIOSocketHandler.disconnect();
        }

        return super.onBackPressed();
    }

    @Override
    public void onMessageReceived(String message) {

        Gson gson = new GsonBuilder().create();
        String msg = message;


        try {
            MessageResponseDetails response = gson.fromJson(message, MessageResponseDetails.class);
            if ((response != null) && response.canIgnore()) {
                return;
            }

//            msg = response.getMsg().getData();
            msg = message;

        } catch (JsonSyntaxException e) {

        }


        final Message messageObj = new Message();
        ShortMessage shortMessage = new ShortMessage();

        LongMessage longMessage = new LongMessage();
        longMessage.setLongMessage(new SpannableString(msg));
        shortMessage.setShortMessage(new SpannableString(msg));

        messageObj.setShortMessage(shortMessage);
        messageObj.setSelf(false);

        ExecutorUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.add(messageObj);
                messageAdapter.notifyDataSetChanged();
            }
        }, 0);
    }

}
