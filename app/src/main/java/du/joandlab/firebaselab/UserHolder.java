package du.joandlab.firebaselab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gson73 on 2016-10-28.
 */

class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = UserHolder.class.getSimpleName();

    private TextView vh_userName;
    private TextView vh_connection;
    private CircleImageView vh_userAvatar;
    private Context mContextViewHolder;

    public UserHolder(Context context, View itemView) {
        super(itemView);

        vh_userName = (TextView) itemView.findViewById(R.id.userFirstNameProfile);
        vh_connection = (TextView) itemView.findViewById(R.id.connectionStatus);
        vh_userAvatar = (CircleImageView) itemView.findViewById(R.id.userPhotoProfile);
        mContextViewHolder = context;

        itemView.setOnClickListener(this);

    }

    TextView getVh_userName() {
        return vh_userName;
    }

    TextView getVh_connection() {
        return vh_connection;
    }

    CircleImageView getVh_userAvatar() {
        return vh_userAvatar;
    }

    @Override
    public void onClick(View view) {

        Log.d(TAG, "onClick: ");

        UsersFragment usersFragment = new UsersFragment();
        usersFragment.startNewChat();

    }
}