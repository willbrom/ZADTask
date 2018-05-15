package com.wilbrom.zadtask.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wilbrom.zadtask.R;
import com.wilbrom.zadtask.model.Repo;

import java.util.ArrayList;

/**
 * Created by user on 5/15/2018.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ItemViewHolder> {

    public interface OnLongClickListener {
        void onLongClick(Repo repo);
    }

    private OnLongClickListener mListener;

    private Context mContext;
    private ArrayList<Repo> mRepoList;

    public RepoAdapter(Context context) {
        mListener = (OnLongClickListener) context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.mRepoName.setText(mRepoList.get(position).getName());
        holder.mRepoDescription.setText(mRepoList.get(position).getDescription());
        holder.mRepoOwner.setText(mRepoList.get(position).getOwner());
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

        private TextView mRepoName;
        private TextView mRepoDescription;
        private TextView mRepoOwner;

        public ItemViewHolder(View itemView) {
            super(itemView);

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
