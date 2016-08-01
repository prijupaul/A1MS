package uk.com.a1ms.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.MessageAdapter;
import uk.com.a1ms.dto.LongMessage;
import uk.com.a1ms.dto.Message;
import uk.com.a1ms.dto.ShortMessage;

/**
 * Created by priju.jacobpaul on 28/07/16.
 */
public class MessagingFragment extends BaseFragment implements View.OnClickListener {

    private MessagingFragmentListener mMessagingFragmentListener;
    private ListView msgListView;
    private EditText msg_edittext;
    private ArrayList<Message> mMessagesArrayList = new ArrayList<>();
    public static MessageAdapter messageAdapter;

    public interface MessagingFragmentListener{

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static MessagingFragment newInstance(MessagingFragmentListener createGroupsFragmentListener){

        MessagingFragment fragment = new MessagingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.mMessagingFragmentListener = createGroupsFragmentListener;
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.messaging_fragment,container,false);
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
        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                return  true;
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

//            final Message chatMessage = new ChatMessage(user1, user2,
//                    message, "" + random.nextInt(1000), true);
//            chatMessage.setMsgID();
//            chatMessage.body = message;
//            chatMessage.Date = CommonMethods.getCurrentDate();
//            chatMessage.Time = CommonMethods.getCurrentTime();

            // TODO: Parse the messages here.

            Message messageObj = new Message();
            ShortMessage shortMessage = new ShortMessage();

            LongMessage longMessage = new LongMessage();
            longMessage.setLongMessage(new SpannableString(message));
            SpannableString spannableString = new SpannableString(message);
            spannableString.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.bg_msg_custom_acronym)),10,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.bg_msg_existing_acronym)),20,25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            shortMessage.setShortMessage(spannableString);

            messageObj.setShortMessage(shortMessage);
            messageObj.setSelf(true);

            msg_edittext.setText("");
            messageAdapter.add(messageObj);
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return super.onBackPressed();
    }
}
