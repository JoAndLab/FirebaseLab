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
    private TextView mSenderTimeStamp;
    private TextView mRecipientTimeStamp;

    public ChatHolder(View itemView) {
        super(itemView);

        mSenderMessageTextView = (TextView) itemView.findViewById(R.id.message_text_view_sent);
        mRecipientMessageTextView = (TextView) itemView.findViewById(R.id.message_text_view_rcv);

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

    public TextView getmSenderTimeStamp() {
        return mSenderTimeStamp;
    }

    public void setmSenderTimeStamp(TextView mSenderTimeStamp) {
        this.mSenderTimeStamp = mSenderTimeStamp;
    }

    public TextView getmRecipientTimeStamp() {
        return mRecipientTimeStamp;
    }

    public void setmRecipientTimeStamp(TextView mRecipientTimeStamp) {
        this.mRecipientTimeStamp = mRecipientTimeStamp;
    }
}
