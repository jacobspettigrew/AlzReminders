package com.back4app.patient_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.back4app.patient_app.R;
import com.back4app.patient_app.models.Family;

import java.util.List;

public class FamilyListAdapter extends RecyclerView.Adapter<FamilyListAdapter.FamilyViewHolder>{

    private final LayoutInflater mInflater;
    private List<Family> mFamilies; // Cached copy of Familys
    private OnItemClickListener mListener;

    public FamilyListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public FamilyListAdapter.FamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new FamilyListAdapter.FamilyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(FamilyListAdapter.FamilyViewHolder holder, int position) {
        if (mFamilies != null) {
            Family current = mFamilies.get(position);
            holder.FamilyItemView.setText(current.getName());



        } else {
            // Covers the case of data not being ready yet.
            holder.FamilyItemView.setText("No Family");
        }
    }

    public void setmFamilies(List<Family> Families){
        mFamilies = Families;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mFamilys has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFamilies != null)
            return mFamilies.size();
        else return 0;
    }

    public Family getFamilyAtPosition (int position) {
        return mFamilies.get(position);
    }

    class FamilyViewHolder extends RecyclerView.ViewHolder {
        private final TextView FamilyItemView;

        private FamilyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            FamilyItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener  != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
