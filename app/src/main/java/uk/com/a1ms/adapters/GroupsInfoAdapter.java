package uk.com.a1ms.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.com.a1ms.R;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;

import static uk.com.a1ms.R.layout.groups_info_fragment_card;

/**
 * Created by priju.jacobpaul on 17/09/16.
 */
public class GroupsInfoAdapter extends RecyclerView.Adapter<GroupsInfoAdapter.ViewHolder> {

    private A1MSGroup mGroups;
    private ArrayList<A1MSUser>mA1MSUsers;

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mMemberName;
        ImageView mMemberIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mMemberIcon = (ImageView)itemView.findViewById(R.id.imageview_icon);
            mMemberName = (TextView)itemView.findViewById(R.id.textview_contact_name);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public GroupsInfoAdapter(A1MSGroup group, ArrayList<A1MSUser> a1MSUsers){
        mGroups = group;
        mA1MSUsers = a1MSUsers;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mMemberName.setText(mA1MSUsers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mA1MSUsers.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(groups_info_fragment_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
}
