package uk.com.a1ms.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.db.dto.A1MSUser;

import static uk.com.a1ms.R.layout.groups_adduser_fragment_card;

/**
 * Created by priju.jacobpaul on 17/09/16.
 */
public class GroupsAddUserAdapter extends RecyclerView.Adapter<GroupsAddUserAdapter.ViewHolder> {

    private List<A1MSUser> mA1MSUsers;
    private List<String> mA1MSCurrentUsers;
    private GroupsAddUserListener listener;

    public interface GroupsAddUserListener{
        public void onItemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mMemberName;
        ImageView mMemberIcon;
        CheckBox mUserSelectionCheckbox;
        CardView cardView;
        TextView mAlreadyMember;

        public ViewHolder(View itemView) {

            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            mMemberIcon = (ImageView)itemView.findViewById(R.id.imageview_icon);
            mMemberName = (TextView)itemView.findViewById(R.id.textview_contact_name);
            mUserSelectionCheckbox = (CheckBox)itemView.findViewById(R.id.checkbox_imageview_icon);
            mAlreadyMember = (TextView)itemView.findViewById(R.id.textview_already_member);

            mUserSelectionCheckbox.setVisibility(View.GONE);
            mUserSelectionCheckbox.setChecked(false);

            mUserSelectionCheckbox.setOnClickListener(this);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if(mUserSelectionCheckbox.getVisibility() == View.VISIBLE) {
                mUserSelectionCheckbox.setVisibility(View.GONE);
            }
            else {
                mUserSelectionCheckbox.setVisibility(View.VISIBLE);
            }

            mUserSelectionCheckbox.setChecked(!mUserSelectionCheckbox.isChecked());
            listener.onItemClicked(mUserSelectionCheckbox, getAdapterPosition());
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public GroupsAddUserAdapter(List<String> currentUsers, List<A1MSUser> a1MSUsers, GroupsAddUserListener listener){
        mA1MSCurrentUsers = currentUsers;
        mA1MSUsers = a1MSUsers;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mMemberName.setText(mA1MSUsers.get(position).getName());
        String userId = mA1MSUsers.get(position).getUserId();

        if(mA1MSCurrentUsers.contains(userId)){
            holder.cardView.setEnabled(false);
            holder.mUserSelectionCheckbox.setEnabled(false);
            holder.mMemberName.setTextColor(holder.mMemberName.getResources().getColor(R.color.light_gray));
            holder.mAlreadyMember.setVisibility(View.VISIBLE);
        }
        else {
            holder.mAlreadyMember.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mA1MSUsers.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(groups_adduser_fragment_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


}
