package philip.com.hackernews.mvvm.view.story;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.local.CommentEntity;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {
    private List<CommentEntity> mCommentEntities = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CommentEntity CommentEntity = mCommentEntities.get(position);

        if (!TextUtils.isEmpty(CommentEntity.getText())) {
            Spanned aboutHtml;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                aboutHtml = Html.fromHtml(CommentEntity.getText(), Html.FROM_HTML_MODE_LEGACY);
            } else {
                aboutHtml = Html.fromHtml(CommentEntity.getText());
            }
            holder.mTextViewText.setText(aboutHtml);
        }
        holder.mTextViewBy.setText(CommentEntity.getBy());
        holder.mTextViewDate.setText(CommentEntity.getTime());
    }

    @Override
    public int getItemCount() {
        return mCommentEntities.size();
    }

    public void setmCommentEntities(List<CommentEntity> mCommentEntities) {
        this.mCommentEntities = mCommentEntities;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextViewText;
        public final TextView mTextViewBy;
        public final TextView mTextViewDate;

        public ViewHolder(View view) {
            super(view);
            mTextViewText = view.findViewById(R.id.text_title);
            mTextViewBy = view.findViewById(R.id.text_by);
            mTextViewDate = view.findViewById(R.id.text_date);
        }
    }
}