package uk.com.a1ms.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dto.Message;

import static uk.com.a1ms.R.layout.contactsgroups_a1ms_card;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public class ContactsGroupsA1MSAdapter extends RecyclerView.Adapter<ContactsGroupsA1MSAdapter.ViewHolder> implements SectionTitleProvider {

    private List<A1MSUser> mDataSet = new ArrayList<>();
    private boolean mCheckboxStatus;
    private ContactsGroupsA1MSAdapterListener mListener;
    private boolean isAnItemSelected;
    private HashMap<String,Integer>mUnreadTextCounter = new HashMap<>();

    public interface ContactsGroupsA1MSAdapterListener {
        void onLongClick(View view,int position);
        void onPrepareSelection(View view, int position);
        void onItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewEmail;
        TextView textViewPhone;
        CardView cardView;
        CheckBox checkBox;
        TextView textViewUnreadMsgs;
        FrameLayout frameLayoutunReadMessageCounterHolder;
        TextView textViewUnreadCounter;

        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
            this.textViewName = (TextView) view.findViewById(R.id.textview_contact_name);
            this.textViewEmail = (TextView) view.findViewById(R.id.textview_contact_email);
            this.textViewPhone = (TextView) view.findViewById(R.id.textview_contact_sms);
            this.checkBox = (CheckBox) view.findViewById(R.id.checkbox_imageview_icon);
            this.textViewUnreadMsgs = (TextView) view.findViewById(R.id.tv_unread_text);
            this.frameLayoutunReadMessageCounterHolder = (FrameLayout) view.findViewById(R.id.framelayout_unreadtext_holder);
            this.textViewUnreadCounter = (TextView)view.findViewById(R.id.tv_unread_text);

            this.checkBox.setVisibility(View.GONE);
            this.checkBox.setChecked(false);

            this.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isSelectionAllowed(getAdapterPosition())) {
                        return;
                    }

                    if(isAnItemSelected){
                        mDataSet.get(getAdapterPosition()).setChecked(checkBox.isChecked());
                        if(checkBox.getVisibility() == View.VISIBLE) {
                            checkBox.setVisibility(View.GONE);
                        }
                        else {
                            checkBox.setVisibility(View.VISIBLE);
                        }
                        mListener.onPrepareSelection(cardView,getAdapterPosition());
                    }
                    else {
                        mDataSet.get(getAdapterPosition()).setChecked(false);
                    }

                }
            });

            this.cardView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {

                    if (mListener != null) {

                        CheckBox cb = (CheckBox)view.findViewById(R.id.checkbox_imageview_icon);
                        if(!isSelectionAllowed(getAdapterPosition())){
                            cb.setVisibility(View.GONE);
                            return false;
                        }

                        cb.setVisibility(View.VISIBLE);
                        cb.setChecked(true);
                        mDataSet.get(getAdapterPosition()).setChecked(cb.isChecked());
                        isAnItemSelected = true;
                        mListener.onLongClick(view,getAdapterPosition());
                    }
                    return true;
                }
            });

            this.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isAnItemSelected){
                        if(!isSelectionAllowed(getAdapterPosition())) {
                            return;
                        }

                        checkBox.setChecked(!checkBox.isChecked());
                        mDataSet.get(getAdapterPosition()).setChecked(checkBox.isChecked());
                        if(checkBox.getVisibility() == View.VISIBLE) {
                            checkBox.setVisibility(View.GONE);
                        }
                        else {
                            checkBox.setVisibility(View.VISIBLE);
                        }

                        mListener.onPrepareSelection(checkBox,getAdapterPosition());
                    }
                    else {
                        int position = getAdapterPosition();
                        A1MSUser a1MSUser = mDataSet.get(position);
                        clearUnreadCounter(a1MSUser);
                        mListener.onItemClick(view, position);
                    }
                }
            });
        }
    }

    public ContactsGroupsA1MSAdapter(List<A1MSUser> dataSet, ContactsGroupsA1MSAdapterListener listener) {
        mDataSet.addAll(dataSet);
        mListener = listener;
    }

    public void setDataSet(List<A1MSUser> dataSet) {
        mDataSet.clear();
        mDataSet.addAll(dataSet);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(contactsgroups_a1ms_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        A1MSUser user = mDataSet.get(position);

        holder.textViewName.setText(user.getName());
        holder.textViewEmail.setText(user.getEmail());
        holder.textViewPhone.setText(user.getMobile());

        if(isAnItemSelected && user.isChecked()){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
            user.setChecked(false);
        }

        holder.frameLayoutunReadMessageCounterHolder.setVisibility(View.GONE);

        if(mUnreadTextCounter.size() > 0){
            if(mUnreadTextCounter.containsKey(user.getUserId())){
                holder.frameLayoutunReadMessageCounterHolder.setVisibility(View.VISIBLE);
                holder.textViewUnreadCounter.setText(String.valueOf(mUnreadTextCounter.get(user.getUserId())));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setGlobalCheckBoxStatusChange(boolean statusChange) {
        mCheckboxStatus = statusChange;
    }

    public void resetContextMenu(){
        isAnItemSelected = false;
        notifyDataSetChanged();
    }

    private boolean isSelectionAllowed(int position){
        if(mDataSet != null){
            A1MSUser contacts = mDataSet.get(position);
            if(!contacts.isEditable()){
                return false;
            }
        }
        return true;
    }

    @Override
    public String getSectionTitle(int position) {
        return mDataSet.get(position).getName().substring(0,1);
    }

    public void addToUnreadTextCounter(String messageType,Message message){

        Integer count = mUnreadTextCounter.get(message.getIdUser().getUserId());
        if(count == null){
            count = new Integer(0);
        }
        mUnreadTextCounter.put(message.getIdUser().getUserId(),count+1);
        notifyDataSetChanged();
    }

    private void clearUnreadCounter(A1MSUser user){
        if(mUnreadTextCounter != null){
            mUnreadTextCounter.remove(user.getUserId());
            notifyDataSetChanged();
        }
    }


}
