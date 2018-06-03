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
import com.example.cresu.projectr.modeles.PanierWallet;

import java.util.ArrayList;

/**
 * Created by cresu on 06/05/2018.
 */

public class PanierWalletAdapter extends RecyclerView.Adapter<PanierWalletAdapter.ViewHolder>{

    private Context context;

    private ArrayList<PanierWallet> panierWalletArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView cout_panier_txt, liste_des_produits_txt, liste_des_prix_txt, date_operation_txt;

        public ViewHolder(View view) {
            super(view);
            cout_panier_txt = (TextView) view.findViewById(R.id.cout_panier_txt);
            liste_des_produits_txt = (TextView) view.findViewById(R.id.liste_des_produits_txt);
            liste_des_prix_txt = (TextView) view.findViewById(R.id.liste_des_prix_txt);
            date_operation_txt = (TextView) view.findViewById(R.id.date_operation_txt);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public PanierWalletAdapter(Context context, ArrayList<PanierWallet> panierWalletArrayList){
        this.context = context;
        this.panierWalletArrayList = panierWalletArrayList;
    }

    @Override
    public PanierWalletAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.panier_wallet_ui, parent, false);

        return new PanierWalletAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PanierWalletAdapter.ViewHolder holder, int position) {

        final PanierWallet panierWallet = panierWalletArrayList.get(position);

        holder.cout_panier_txt.setText(""+panierWallet.getCoutTotal());

        holder.liste_des_produits_txt.setText(panierWallet.getListeDesProduits());

        holder.liste_des_prix_txt.setText(panierWallet.getListeDesPrix());

        holder.date_operation_txt.setText(panierWallet.getDateOperation());
    }

    @Override
    public int getItemCount() {
        return panierWalletArrayList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PanierWalletAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PanierWalletAdapter.ClickListener clickListener) {
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
