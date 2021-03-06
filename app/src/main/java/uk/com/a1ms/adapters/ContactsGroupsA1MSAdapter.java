package uk.com.a1ms.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.db.dto.A1MSUser;

import static uk.com.a1ms.R.layout.contactsgroups_a1ms_card;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public class ContactsGroupsA1MSAdapter extends RecyclerView.Adapter<ContactsGroupsA1MSAdapter.ViewHolder> implements SectionTitleProvider {

    private List<A1MSUser> mDataSet;
    private boolean mCheckboxStatus;
    private ContactsGroupsA1MSAdapterListener mListener;
    private boolean isAnItemSelected;


    public interface ContactsGroupsA1MSAdapterListener {
        void onLongClick(View view,int position);
        void onPrepareSelection(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewEmail;
        TextView textViewPhone;
        CardView cardView;
        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
            this.textViewName = (TextView) view.findViewById(R.id.textview_contact_name);
            this.textViewEmail = (TextView) view.findViewById(R.id.textview_contact_email);
            this.textViewPhone = (TextView) view.findViewById(R.id.textview_contact_sms);
            this.checkBox = (CheckBox) view.findViewById(R.id.checkbox_imageview_icon);
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
                }
            });
        }
    }

    public ContactsGroupsA1MSAdapter(List<A1MSUser> dataSet, ContactsGroupsA1MSAdapterListener listener) {
        mDataSet = dataSet;
        mListener = listener;
    }

    public void setDataSet(List<A1MSUser> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(contactsgroups_a1ms_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textViewName.setText(mDataSet.get(position).getName());
        holder.textViewEmail.setText(mDataSet.get(position).getEmail());
        holder.textViewPhone.setText(mDataSet.get(position).getMobile());

        if(isAnItemSelected && mDataSet.get(position).isChecked()){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
            mDataSet.get(position).setChecked(false);
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
}
