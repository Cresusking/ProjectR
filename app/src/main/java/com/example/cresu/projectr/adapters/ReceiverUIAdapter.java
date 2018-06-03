package com.example.cresu.projectr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.modeles.Receiver;

import java.util.ArrayList;

/**
 * Created by cresu on 08/05/2018.
 */

public class ReceiverUIAdapter extends RecyclerView.Adapter<ReceiverUIAdapter.ViewHolder>{

    private Context context;

    private ArrayList<Receiver> receiverArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView receiver_id_txt, receiver_nom_txt;

        public ViewHolder(View view) {
            super(view);
            receiver_id_txt = (TextView) view.findViewById(R.id.receiver_id_txt);
            receiver_nom_txt = (TextView) view.findViewById(R.id.receiver_nom_txt);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public ReceiverUIAdapter(Context context, ArrayList<Receiver> receiverArrayList){
        this.context = context;
        this.receiverArrayList = receiverArrayList;
    }

    @Override
    public ReceiverUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receiver_ui, parent, false);

        return new ReceiverUIAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReceiverUIAdapter.ViewHolder holder, int position) {

        final Receiver receiver = receiverArrayList.get(position);

        holder.receiver_id_txt.setText(receiver.getId_receiver());

        holder.receiver_nom_txt.setText(receiver.getNom());
    }

    @Override
    public int getItemCount() {
        return receiverArrayList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ReceiverUIAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ReceiverUIAdapter.ClickListener clickListener) {
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
