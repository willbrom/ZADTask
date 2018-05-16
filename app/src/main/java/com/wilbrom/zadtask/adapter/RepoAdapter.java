package com.wilbrom.zadtask.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilbrom.zadtask.R;
import com.wilbrom.zadtask.model.Repo;

import java.util.ArrayList;
import java.util.Timer;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ItemViewHolder> {

    public interface OnRepoItemInteractionListener {
        void onLongClick(Repo repo);
        boolean onLoadMoreData();
    }

    private OnRepoItemInteractionListener mListener;

    private Context mContext;
    private ArrayList<Repo> mRepoList;

    public RepoAdapter(Context context) {
        mListener = (OnRepoItemInteractionListener) context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TAG", "viewType " + viewType);
        if (viewType == 1)
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_layout, parent, false));

        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            holder.mRepoName.setText(mRepoList.get(position).getName());
            holder.mRepoDescription.setText(mRepoList.get(position).getDescription());
            holder.mRepoOwner.setText(mRepoList.get(position).getOwner());

            if (!mRepoList.get(position).isForked())
                holder.mRepoRoot.setBackgroundColor(Color.parseColor("#FFA1EA91"));
        } else {
            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    mListener.onLoadMoreData();
                }

            }.start();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (mRepoList.size() - 1))
            return 1;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mRepoList == null) return 0;
        return mRepoList.size();
    }

    public void setmRepoList(ArrayList<Repo> repoList) {
        this.mRepoList = repoList;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private LinearLayout mRepoRoot;
        private LinearLayout mRepoParent;
        private TextView mRepoName;
        private TextView mRepoDescription;
        private TextView mRepoOwner;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mRepoParent = (LinearLayout) itemView.findViewById(R.id.repo_parent);
            mRepoRoot = (LinearLayout) itemView.findViewById(R.id.repo_root);
            mRepoName = (TextView) itemView.findViewById(R.id.repo_name);
            mRepoDescription = (TextView) itemView.findViewById(R.id.repo_description);
            mRepoOwner = (TextView) itemView.findViewById(R.id.repo_owner);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onLongClick(mRepoList.get(getAdapterPosition()));
            return true;
        }
    }
}
