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
public class CreateGroupsAdapter extends RecyclerView.Adapter<CreateGroupsAdapter.ViewHolder> implements SectionTitleProvider {

    private List<A1MSUser> mDataSet;
    private CreateGroupsAdapterListener mListener;
    private boolean isSelectable;


    public interface CreateGroupsAdapterListener {
        void onClick(View view, boolean isChecked, int position);
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

            if(isSelectable) {
                this.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mListener != null) {

                            CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox_imageview_icon);
                            if (cb.getVisibility() == View.GONE) {
                                cb.setVisibility(View.VISIBLE);
                                cb.setChecked(true);
                            } else {
                                cb.setVisibility(View.GONE);
                                cb.setChecked(false);
                            }

                            mDataSet.get(getAdapterPosition()).setChecked(cb.isChecked());
                            mListener.onClick(view, cb.isChecked(), getAdapterPosition());
                        }

                    }
                });
            }

            if (isSelectable) {
                this.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mListener != null) {

                            CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox_imageview_icon);
                            if (cb.getVisibility() == View.GONE) {
                                cb.setVisibility(View.VISIBLE);
                                cb.setChecked(true);
                            } else {
                                cb.setVisibility(View.GONE);
                                cb.setChecked(false);
                            }

                            mDataSet.get(getAdapterPosition()).setChecked(cb.isChecked());
                            mListener.onClick(view, cb.isChecked(), getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

    public CreateGroupsAdapter(List<A1MSUser> dataSet, boolean isSelectable, CreateGroupsAdapterListener listener) {
        mDataSet = dataSet;
        this.isSelectable = isSelectable;
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

        if (mDataSet.get(position).isChecked() && isSelectable) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
            mDataSet.get(position).setChecked(true);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public String getSectionTitle(int position) {
        return mDataSet.get(position).getName().substring(0, 1);
    }

    public void setCheckBoxStatusChange(A1MSUser a1MSUser, boolean isChecked) {
        int index = mDataSet.indexOf(a1MSUser);
        mDataSet.get(index).setChecked(isChecked);
        notifyDataSetChanged();
    }
}
