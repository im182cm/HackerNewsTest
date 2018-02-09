package philip.com.hackernews.mvvm.view.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.local.StoryEntity;

/**
 * Adapter for PostRecyclerView.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {
    private List<StoryEntity> mStoryEntities = new ArrayList<>();
    private final PostRecyclerListener mListener;

    public PostRecyclerViewAdapter(PostRecyclerListener postRecyclerListener) {
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
        this.mStoryEntities = mStoryEntities;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextViewTitle;
        public final TextView mTextViewPoint;
        public final TextView mTextViewBy;
        public final TextView mTextViewDate;

        public ViewHolder(View view) {
            super(view);
            mTextViewTitle = view.findViewById(R.id.text_title);
            mTextViewPoint = view.findViewById(R.id.text_point);
            mTextViewBy = view.findViewById(R.id.text_by);
            mTextViewDate = view.findViewById(R.id.text_date);

            view.setOnClickListener(this);
            mTextViewBy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mStoryEntities.isEmpty()) {
                return;
            }

            switch (v.getId()) {
                case R.id.layout_post:
                    mListener.onClick(mStoryEntities.get(getAdapterPosition()), true);
                    break;
                case R.id.text_by:
                    mListener.onClick(mStoryEntities.get(getAdapterPosition()), false);
                    break;
            }
        }
    }
}