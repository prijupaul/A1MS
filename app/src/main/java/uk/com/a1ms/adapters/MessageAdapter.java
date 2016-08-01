package uk.com.a1ms.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import uk.com.a1ms.R;
import uk.com.a1ms.dto.Message;

/**
 * Created by priju.jacobpaul on 29/07/16.
 */
public class MessageAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<Message> chatMessageList;

    public MessageAdapter(Activity activity, ArrayList<Message> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = (Message) chatMessageList.get(position);
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.chatbubble, null);
        }

        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        msg.setText(message.getShortMessage().getShortMessage());
        LinearLayout layout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) vi.findViewById(R.id.bubble_layout_parent);

        // if message is mine then align to right
        if (message.isSelf()) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }

    public void add(Message object) {
        chatMessageList.add(object);
    }
}
