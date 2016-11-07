package du.joandlab.firebaselab;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by jogus on 2016-10-31.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<UserObject> mFireChatUsers;
    private NewChatInterface mCallback;
    private Context mContext;
    private String mCurrentUserName;
    private int mCurrentAvatarId;

    private static final String PREFERENCES_FILE = "My_settings";

    public UserAdapter(Context context, List<UserObject> fireChatUsers) {
        mFireChatUsers = fireChatUsers;
        mContext = context;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try {
            mCallback = (NewChatInterface) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString()
                    + " must implement NewChatInterface");
        }
        // Inflate layout for each row
        return new UserHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {

        UserObject userObject = mFireChatUsers.get(position);

        // Set avatar
        int userAvatarId = AvatarPicker.getDrawableAvatarId(userObject.getAvatarId());
        Drawable avatarDrawable = ContextCompat.getDrawable(mContext, userAvatarId);
        holder.getVh_userAvatar().setImageDrawable(avatarDrawable);

        // Set username
        holder.getVh_userName().setText(userObject.getUsername());

        // Set presence status
        holder.getVh_connection().setText(userObject.getConnection());

        // Set presence text color
        if (Objects.equals(userObject.getConnection(), Ref.KEY_ONLINE))
            holder.getVh_connection().setTextColor(Color.parseColor("#00FF00"));
        else
            holder.getVh_connection().setTextColor(Color.parseColor("#FF0000"));


    }

    @Override
    public int getItemCount() {
        return mFireChatUsers.size();
    }

    public void refill(UserObject users) {

        // Add each user and notify recyclerView about change
        mFireChatUsers.add(users);
        notifyDataSetChanged();
    }

    public void setNameAndCreatedAt(String username, int avatarid) {

        // Set current user name and time account created at
        mCurrentUserName = username;
        mCurrentAvatarId = avatarid;

        saveSharedSetting(mContext, "currentUserAvatarId", String.valueOf(avatarid));

        Log.d(TAG, "setNameAndCreatedAt: " + username);
        Log.d(TAG, "setNameAndCreatedAt: " + avatarid);
    }

    public void changeUser(int index, UserObject user) {

        // Handle change on each user and notify change
        mFireChatUsers.set(index, user);
        notifyDataSetChanged();
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }


    /* ViewHolder for RecyclerView */
    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        public TextView getVh_userName() {
            return vh_userName;
        }

        public TextView getVh_connection() {
            return vh_connection;
        }

        public CircleImageView getVh_userAvatar() {
            return vh_userAvatar;
        }

        @Override
        public void onClick(View view) {

            // Handle click on each row

            int position = getLayoutPosition(); // Get row position

            UserObject user = mFireChatUsers.get(position); // Get use object

            saveSharedSetting(mContext, "recipientAvatarId", String.valueOf(user.getAvatarId()));

            try {
                mCallback.userToNewChat(user);
            } catch (Exception ex) {
                Toast.makeText(mContextViewHolder, ex.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mCallback = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

}
