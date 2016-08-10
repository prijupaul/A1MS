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
import uk.com.a1ms.listeners.OnSwipeTouchListener;

/**
 * Created by priju.jacobpaul on 29/07/16.
 */
public class MessageAdapter extends BaseAdapter{

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
        final Message message = chatMessageList.get(position);
        View vi = convertView;
        final ViewHolder viewHolder;

        if (vi == null) {
            vi = inflater.inflate(R.layout.chatbubble, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) vi.findViewById(R.id.message_text);
            viewHolder.linearLayout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
            viewHolder.linearLayoutParent = (LinearLayout) vi.findViewById(R.id.bubble_layout_parent);
            vi.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)vi.getTag();
        }

        viewHolder.textView.setText(message.getShortMessage().getShortMessage());
        viewHolder.indicatorView = addView(message,viewHolder);

        // if message is mine then align to right
        if (message.isSelf()) {
            viewHolder.linearLayout.setBackgroundResource(R.drawable.bubble2);
            viewHolder.linearLayoutParent.setGravity(Gravity.RIGHT);

        }
        // If not mine then align to left
        else {
            viewHolder.linearLayout.setBackgroundResource(R.drawable.bubble1);
            viewHolder.linearLayoutParent.setGravity(Gravity.LEFT);
        }
        viewHolder.textView.setTextColor(Color.BLACK);
        viewHolder.linearLayoutParent.setOnTouchListener(new OnSwipeTouchListener(viewHolder.textView.getContext()){
            @Override
            public void onSwipeRight() {
                viewHolder.textView.setText("Swiped right");
            }

            @Override
            public void onSwipeLeft() {
                viewHolder.textView.setText("Swiped left");
            }
        });
        return vi;
    }

    public void add(Message object) {
        chatMessageList.add(object);
    }

    private View addView(Message message,ViewHolder viewHolder){

        if(message.getShortMessage() == null || message.getShortMessage().getShortMessage() == null){
            return null;
        }

        View view = new View(viewHolder.linearLayoutParent.getContext());
        view.setBackgroundColor(viewHolder.linearLayoutParent.getResources().getColor(R.color.colorAccent));
        view.setLayoutParams(new LinearLayout.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT));

        viewHolder.linearLayoutParent.removeView(viewHolder.indicatorView);

        if(message.isSelf()){
            viewHolder.linearLayoutParent.addView(view,1);
        }
        else {
            viewHolder.linearLayoutParent.addView(view,0);
        }

        return view;

    }



    private class ViewHolder{
        private TextView textView;
        private View indicatorView;
        private LinearLayout linearLayoutParent;
        private LinearLayout linearLayout;

    }
}
