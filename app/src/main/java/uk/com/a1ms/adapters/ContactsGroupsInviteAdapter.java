package uk.com.a1ms.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.ArrayList;
import java.util.SortedMap;

import uk.com.a1ms.R;
import uk.com.a1ms.dto.Contacts;

import static uk.com.a1ms.R.layout.contactsgroups_a1ms_card;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public class ContactsGroupsInviteAdapter extends RecyclerView.Adapter<ContactsGroupsInviteAdapter.ViewHolder> implements SectionTitleProvider {

    private SortedMap<String,Contacts> mDataSet;
    private boolean mCheckboxStatus;
    private ContactsGroupsInviteAdapterListener mInviteAdapterListener;

    public interface ContactsGroupsInviteAdapterListener {
        void onInviteClick(String email, String mobileNumber, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewEmail;
        TextView textViewPhone;
        CardView cardView;
        CheckBox checkBox;
        Button invite;


        public ViewHolder(View view){
            super(view);
            this.cardView = (CardView)view.findViewById(R.id.card_view);
            this.textViewName = (TextView)view.findViewById(R.id.textview_contact_name);
            this.textViewEmail = (TextView)view.findViewById(R.id.textview_contact_email);
            this.textViewPhone = (TextView)view.findViewById(R.id.textview_contact_sms);
            this.checkBox = (CheckBox)view.findViewById(R.id.checkbox_imageview_icon);
            this.invite = (Button)view.findViewById(R.id.button_invite);
            this.invite.setVisibility(View.VISIBLE);

            this.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ContactsGroupsInviteAdapter.this.mInviteAdapterListener != null){
                        mInviteAdapterListener.onInviteClick(
                                textViewEmail.getText().toString(),
                               textViewPhone.getText().toString(),
                                getAdapterPosition() );
                    }
                }
            });

        }

    }

    public ContactsGroupsInviteAdapter(SortedMap<String,Contacts> dataSet,ContactsGroupsInviteAdapterListener listener){
        mDataSet = dataSet;
        mInviteAdapterListener = listener;
    }

    public void setDataSet(SortedMap<String,Contacts> dataSet){
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(contactsgroups_a1ms_card,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textViewName.setText((new ArrayList<>(mDataSet.values())).get(position).getContactName());
        holder.textViewEmail.setText((new ArrayList<>(mDataSet.values())).get(position).getContactEmailMobile());
        holder.textViewPhone.setText((new ArrayList<>(mDataSet.values())).get(position).getContactPhoneMobile());
        holder.checkBox.setChecked(mCheckboxStatus);
    }

    @Override
    public int getItemCount() {
        return mDataSet != null ? mDataSet.size() : 0;
    }

    public void setGlobalCheckBoxStatusChange(boolean statusChange){
        mCheckboxStatus = statusChange;
    }

    @Override
    public String getSectionTitle(int position) {
        return ((String)mDataSet.keySet().toArray()[position]).substring(0,1);
    }
}
