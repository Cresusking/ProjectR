package com.example.cresu.projectr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.modeles.CategorieAliment;

import java.util.ArrayList;

/**
 * Created by cresu on 16/03/2018.
 */

public class CategorieAlimentAdapter extends RecyclerView.Adapter<CategorieAlimentAdapter.ViewHolder>  {

    private Context mContext;

    private ArrayList<CategorieAliment> categoriesArrayListAliment;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titre;
        public ImageView icon;

        public ViewHolder(View view) {
            super(view);
            titre = (TextView) view.findViewById(R.id.titre);
            icon = (ImageView) view.findViewById(R.id.icon_subject);

        }
    }

    public CategorieAlimentAdapter(Context mContext, ArrayList<CategorieAliment> categoriesArrayListAliment) {
        this.mContext = mContext;
        this.categoriesArrayListAliment = categoriesArrayListAliment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.aliment_categorie_ui, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategorieAliment categorieAliment = categoriesArrayListAliment.get(position);

        holder.titre.setText(categorieAliment.getNom());

        //holder.icon.setImageResource(chatRoom.getIcon());
        holder.icon.setBackgroundResource(categorieAliment.getImage_id());
    }

    @Override
    public int getItemCount() {
        return categoriesArrayListAliment.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
