package du.joandlab.firebaselab;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by gson73 on 2016-10-28.
 */

public class ChatHolder extends RecyclerView.ViewHolder {

    private TextView mSenderMessageTextView;
    private TextView mRecipientMessageTextView;

    public ChatHolder(View itemView) {
        super(itemView);

        mSenderMessageTextView = (TextView) itemView.findViewById(R.id.senderMessage);
        mRecipientMessageTextView = (TextView) itemView.findViewById(R.id.recipientMessage);

    }

    public TextView getmSenderMessageTextView() {
        return mSenderMessageTextView;
    }

    public void setmSenderMessageTextView(TextView mSenderMessageTextView) {
        this.mSenderMessageTextView = mSenderMessageTextView;
    }

    public TextView getmRecipientMessageTextView() {
        return mRecipientMessageTextView;
    }

    public void setmRecipientMessageTextView(TextView mRecipientMessageTextView) {
        this.mRecipientMessageTextView = mRecipientMessageTextView;
    }
}
