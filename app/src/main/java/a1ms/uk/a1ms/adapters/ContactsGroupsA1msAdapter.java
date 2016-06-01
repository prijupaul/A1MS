package a1ms.uk.a1ms.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.dto.Contacts;

import static a1ms.uk.a1ms.R.layout.contactsgroups_a1ms_card;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public class ContactsGroupsA1msAdapter extends RecyclerView.Adapter<ContactsGroupsA1msAdapter.ViewHolder>{

    private HashMap<String,Contacts> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(View view){
            super(view);
            this.textView = (TextView)view.findViewById(R.id.textview_contact_name);
        }

    }

    public ContactsGroupsA1msAdapter(HashMap<String,Contacts> dataSet){
        mDataSet = dataSet;
    }

    public void setDataSet(HashMap<String,Contacts> dataSet){
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

        holder.textView.setText((new ArrayList<>(mDataSet.values())).get(position).getContactName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
