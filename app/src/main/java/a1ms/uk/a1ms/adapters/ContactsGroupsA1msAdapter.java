package a1ms.uk.a1ms.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.SortedMap;

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.dto.Contacts;

import static a1ms.uk.a1ms.R.layout.contactsgroups_a1ms_card;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public class ContactsGroupsA1msAdapter extends RecyclerView.Adapter<ContactsGroupsA1msAdapter.ViewHolder>{

    private SortedMap<String,Contacts> mDataSet;
    private boolean mCheckboxStatus;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewEmail;
        TextView textViewPhone;
        CardView cardView;
        CheckBox checkBox;


        public ViewHolder(View view){
            super(view);
            this.cardView = (CardView)view.findViewById(R.id.card_view);
            this.textViewName = (TextView)view.findViewById(R.id.textview_contact_name);
            this.textViewEmail = (TextView)view.findViewById(R.id.textview_contact_email);
            this.textViewPhone = (TextView)view.findViewById(R.id.textview_contact_sms);
            this.checkBox = (CheckBox)view.findViewById(R.id.checkbox_imageview_icon);

            this.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

    }

    public ContactsGroupsA1msAdapter(SortedMap<String,Contacts> dataSet){
        mDataSet = dataSet;
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
        return mDataSet.size();
    }

    public void setGlobalCheckBoxStatusChange(boolean statusChange){
        mCheckboxStatus = statusChange;
    }
}
