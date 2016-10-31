package du.joandlab.firebaselab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Objects;


/**
 * Created by jogus on 2016-10-27.
 */

class UserAdapter extends FirebaseRecyclerAdapter<UserObject, UserHolder> {

    private static final String TAG = UserAdapter.class.getSimpleName();
    private List<UserObject> mChatUsers;
    private Context context;

    UserAdapter(Class<UserObject> modelClass, int modelLayout, Class<UserHolder> viewHolderClass, DatabaseReference ref, List<UserObject> mChatUsers, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mChatUsers = mChatUsers;
        this.context = context;
    }

    public List<UserObject> getmChatUsers() {
        return mChatUsers;
    }

    @Override
    protected void populateViewHolder(UserHolder viewHolder, UserObject model, int position) {

    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new UserHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));

    }

    @Override
    public void onBindViewHolder(UserHolder viewHolder, int position) {

        UserObject userObject = mChatUsers.get(position);

        // Set avatar
        int userAvatarId = AvatarPicker.getDrawableAvatarId(userObject.getAvatarId());
        Drawable avatarDrawable = ContextCompat.getDrawable(context, userAvatarId);
        viewHolder.getVh_userAvatar().setImageDrawable(avatarDrawable);

        viewHolder.getVh_userName().setText(userObject.getUsername());
        viewHolder.getVh_connection().setText(userObject.getConnection());


        if (Objects.equals(userObject.getConnection(), Ref.KEY_ONLINE))
            viewHolder.getVh_connection().setTextColor(Color.parseColor("#00FF00"));
        else
            viewHolder.getVh_connection().setTextColor(Color.parseColor("#FF0000"));


    }

    @Override
    public int getItemCount() {
        return mChatUsers.size();
    }

    public void refill(UserObject users) {

        // Add each user and notify recyclerView about change
        mChatUsers.add(users);
        notifyDataSetChanged();
    }

    public void changeUser(int index, UserObject user) {

        // Handle change on each user and notify change
        mChatUsers.set(index, user);
        notifyDataSetChanged();
    }

}




