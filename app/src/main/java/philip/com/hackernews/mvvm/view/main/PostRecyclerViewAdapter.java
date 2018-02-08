package philip.com.hackernews.mvvm.view.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.local.StoryEntity;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {
    private List<StoryEntity> mStoryEntities = new ArrayList<>();
    private final PostRecyclerListener mListener;

    public PostRecyclerViewAdapter(RequestManager mRequestManager, PostRecyclerListener postRecyclerListener) {
        this.mListener = postRecyclerListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StoryEntity storyEntity = mStoryEntities.get(position);

        holder.mTextViewTitle.setText(storyEntity.getTitle());
        holder.mTextViewPoint.setText(storyEntity.getScore() + "points");
        holder.mTextViewBy.setText(storyEntity.getBy());
        holder.mTextViewDate.setText(storyEntity.getTime());
    }

    @Override
    public int getItemCount() {
        return mStoryEntities.size();
    }

    public void setmStoryEntities(List<StoryEntity> mStoryEntities) {
        if (this.mStoryEntities.size() == mStoryEntities.size() || this.mStoryEntities.size() > mStoryEntities.size())
            return;

        int origin = this.mStoryEntities.size();
        int diffCount = mStoryEntities.size() - this.mStoryEntities.size();
        Log.d("TEST", origin + "," + diffCount);
        for (int i = diffCount - 1; i >= 0; i--) {
            Log.d("TEST", i + "");
            this.mStoryEntities.add(mStoryEntities.get(i));
        }
        notifyItemRangeChanged(origin - 1, this.mStoryEntities.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextViewTitle;
        public final TextView mTextViewPoint;
        public final TextView mTextViewBy;
        public final TextView mTextViewDate;
        public final TextView mTextViewLink;
        public final TextView mTextViewComment;

        public ViewHolder(View view) {
            super(view);
            mTextViewTitle = view.findViewById(R.id.text_title);
            mTextViewPoint = view.findViewById(R.id.text_point);
            mTextViewBy = view.findViewById(R.id.text_by);
            mTextViewDate = view.findViewById(R.id.text_date);
            mTextViewLink = view.findViewById(R.id.text_link);
            mTextViewComment = view.findViewById(R.id.text_comment);

            mTextViewLink.setOnClickListener(this);
            mTextViewBy.setOnClickListener(this);
            mTextViewComment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mStoryEntities.isEmpty()) {
                return;
            }

            switch (v.getId()) {
                case R.id.text_link:
                    mListener.onClick(mStoryEntities.get(getAdapterPosition()).getUrl(), null, null);
                    break;
                case R.id.text_by:
                    mListener.onClick(null, mStoryEntities.get(getAdapterPosition()).getBy(), null);
                    break;
                case R.id.text_comment:
                    mListener.onClick(null, null, mStoryEntities.get(getAdapterPosition()).getKids());
                    break;
            }
        }
    }
}