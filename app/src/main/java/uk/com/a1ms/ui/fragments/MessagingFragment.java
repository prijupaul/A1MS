package uk.com.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONObject;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;
import uk.com.a1ms.adapters.MessageAdapter;
import uk.com.a1ms.db.A1MSMessageFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dto.LongMessage;
import uk.com.a1ms.dto.Message;
import uk.com.a1ms.dto.ShortMessage;
import uk.com.a1ms.messages.MessageImpl;
import uk.com.a1ms.messages.MessageListerner;
import uk.com.a1ms.messages.MessageNotificationHandler;
import uk.com.a1ms.messages.MessageParser;
import uk.com.a1ms.network.handlers.UserMessageIOSocketHandler;
import uk.com.a1ms.network.handlers.UserMessageWebSocketHandler;
import uk.com.a1ms.ui.MessagingActivity;
import uk.com.a1ms.util.DateTime;
import uk.com.a1ms.util.ExecutorUtils;
import uk.com.a1ms.util.SharedPreferenceManager;

import java.util.ArrayList;

/**
 * Created by priju.jacobpaul on 28/07/16.
 */
public class MessagingFragment extends BaseFragment implements View.OnClickListener,
        UserMessageWebSocketHandler.UserMessageWebSocketListener,
        UserMessageIOSocketHandler.UserMessageIOSocketListener,
        MessageNotificationHandler.MessageNotificationHandlerListener
{

    private MessagingFragmentListener mMessagingFragmentListener;
    private ListView msgListView;
    private EditText msg_edittext;
    private ImageButton mSendButton;
    private ArrayList<Message> mMessagesArrayList = new ArrayList<>();
    public static MessageAdapter messageAdapter;
    //    private UserMessageWebSocketHandler webSocketHandler;
//    private UserMessageIOSocketHandler webIOSocketHandler;
    private MessageParser messageParser;
    private A1MSUser mCurrentUser;
    private A1MSGroup mCurrentGroup;
    private MenuItem mShowGroupInfo;
    private MessageImpl mMessageSender;
    private boolean isMemberOfGroup = true;


    public interface MessagingFragmentListener {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MessagingActivity messagingActivity = (MessagingActivity)getActivity();
        mCurrentUser = messagingActivity.getCurrentUser();
        mCurrentGroup = messagingActivity.getCurrentGroup();
        mMessageSender = new MessageImpl();

        A1MSApplication context = (A1MSApplication) getActivity().getApplicationContext();
        context.setCurrentActiveUser(mCurrentUser);

        if(!isMemberOfGroups()){
            isMemberOfGroup = false;
        }

        if(!isMemberOfGroup){
            mSendButton.setVisibility(View.GONE);
            msg_edittext.setEnabled(false);
            msg_edittext.setFocusable(false);
            msg_edittext.setText(getString(R.string.group_not_member));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        webSocketHandler = new UserMessageWebSocketHandler(this);
//        webIOSocketHandler = new UserMessageIOSocketHandler(this);
        messageParser = new MessageParser();

//        ExecutorUtils.runInBackgroundThread(new Runnable() {
//            @Override
//            public void run() {
////                webSocketHandler.connect();
//                webIOSocketHandler.connect();
//            }
//        });
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

        mSendButton = (ImageButton) view.findViewById(R.id.sendMessageButton);
        mSendButton.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mCurrentUser.getName());

        MessageNotificationHandler.getInstance().registerForEvents(MessageNotificationHandler.PRIORITY_HIGH,this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message,menu);
        mShowGroupInfo = menu.findItem(R.id.action_group_info);

        if(mCurrentUser != null && mCurrentUser.isGroup() && isMemberOfGroup){
            mShowGroupInfo.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_group_info:{

                GroupInfoFragment groupInfoFragment = GroupInfoFragment.getInstance(mCurrentGroup,(GroupInfoFragment.GroupInfoFragmentListener) getActivity());
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.message_framelayout_holder,groupInfoFragment,
                        groupInfoFragment.getClass().getSimpleName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        sendTextMessage(view);
    }

    private boolean isMemberOfGroups(){
        if(mCurrentGroup != null){
            ArrayList<String> members = mCurrentGroup.getMembersList();
            if(!members.contains(SharedPreferenceManager.getUserId(getActivity()))){
                return false;
            }
        }
        return true;
    }

    public void sendTextMessage(View v) {

        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {

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

            messageObj.setIdToUser(mCurrentUser);

            msg_edittext.setText("");
            messageAdapter.add(messageObj);
            messageAdapter.notifyDataSetChanged();

            String token = SharedPreferenceManager.getUserToken(getContext());
            final JSONObject jsonObject = messageObj.convertToJson(token,mCurrentUser,mCurrentGroup);

            A1MSMessageFieldsDataSource dataSource = new A1MSMessageFieldsDataSource(getContext());

            if(!mCurrentUser.isEditable()) {
                mMessageSender.sendMessage(MessageListerner.MESSAGETYPE.MESSAGE_ECHO,jsonObject);
                dataSource.insertMessage(A1MSApplication.getMessagesSqLiteDb(),messageObj,false,false,true);
            }
            else if(mCurrentUser.isGroup()){
                mMessageSender.sendMessage(MessageListerner.MESSAGETYPE.MESSAGE_GROUP,jsonObject);
                dataSource.insertMessage(A1MSApplication.getMessagesSqLiteDb(),messageObj,false,true,true);
            }
            else {
                mMessageSender.sendMessage(MessageListerner.MESSAGETYPE.MESSAGE_PRIVATE,jsonObject);
                dataSource.insertMessage(A1MSApplication.getMessagesSqLiteDb(),messageObj,false,false,true);
            }


        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MessageNotificationHandler.getInstance().unregisterForEvents(MessageNotificationHandler.PRIORITY_HIGH);
        A1MSApplication.setCurrentActiveUser(null);
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return super.onBackPressed();
    }

    @Override
    public void onMessageReceived(String message,String shortMsg) {

    }


    public void onMessageReceived(SpannableString message,SpannableString shortMsg) {

//        Gson gson = new GsonBuilder().create();


//        try {
//            MessageResponseDetails response = gson.fromJson(message, MessageResponseDetails.class);
//            if ((response != null) && response.canIgnore()) {
//                return;
//            }
//
////            msg = response.getMsg().getData();
//            msg = message;
//
//        } catch (JsonSyntaxException e) {
//
//        }


        final Message messageObj = new Message();
        ShortMessage shortMessage = new ShortMessage();
        LongMessage longMessage = new LongMessage();

        longMessage.setLongMessage(message);
        shortMessage.setShortMessage(shortMsg);

        messageObj.setMessage(longMessage);
        messageObj.setShortMessage(shortMessage);
        messageObj.setSelf(false);
        messageObj.setTime(DateTime.getTimeInAmPm());


        ExecutorUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.add(messageObj);
                messageAdapter.notifyDataSetChanged();
            }
        }, 0);
    }

    @Override
    public boolean onNewMessageReceived(String messageType, Message message) {
        if(message != null){

            A1MSMessageFieldsDataSource dataSource = new A1MSMessageFieldsDataSource(getContext());


            switch (messageType){
                case "echoMessage":
                    if(mCurrentUser.getUserId().compareTo(message.getIdToUser().getUserId()) == 0){
                        onMessageReceived(message.getMessage().getLongMessage(),message.getShortMessage().getShortMessage());
                        dataSource.insertMessage(A1MSApplication.getMessagesSqLiteDb(),message,true,false,true);
                        return true;
                    }
                    break;
                case "privateMessage":{
                    if(mCurrentUser.getUserId().compareTo(message.getIdUser().getUserId()) == 0){
                        onMessageReceived(message.getMessage().getLongMessage(),message.getShortMessage().getShortMessage());
                        dataSource.insertMessage(A1MSApplication.getMessagesSqLiteDb(),message,true,false,true);
                        return true;
                    }
                    break;
                }
                case "groupMessage":{
                    if(mCurrentGroup.getGroupId().compareTo(message.getIdUser().getUserId()) == 0){
                        onMessageReceived(message.getMessage().getLongMessage(),message.getShortMessage().getShortMessage());
                        dataSource.insertMessage(A1MSApplication.getMessagesSqLiteDb(),message,true,true,true);
                        return true;
                    }
                    break;
                }

            }

        }

        return false;
    }

}
