package com.example.cresu.projectr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.modeles.Paquet;
import com.example.cresu.projectr.utils.VariablesGlobales;

import java.util.ArrayList;

/**
 * Created by cresu on 29/03/2018.
 */

public class PanierAdapter extends RecyclerView.Adapter<PanierAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Paquet> paquetArrayList;

    private TextView prix_tot;

    private int pos;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nom;
        public ImageView icon;
        public ImageButton btnDelete;

        public TextView prix_choisi;
        public Button btn_up;
        public Button btn_down;

        private View parentView;

        public ViewHolder(View view) {
            super(view);
            nom = (TextView) view.findViewById(R.id.panier_aliment_name);
            icon = (ImageView) view.findViewById(R.id.panier_aliment_icon);
            btnDelete = (ImageButton)view.findViewById(R.id.panier_aliment_delete_btn);
            prix_choisi = (TextView)view.findViewById(R.id.prix_choisi);
            btn_up = (Button)view.findViewById(R.id.btn_up);
            btn_down = (Button)view.findViewById(R.id.btn_down);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int reste = Integer.valueOf(prix_tot.getText().toString()) - paquetArrayList.get(getAdapterPosition()).getAliment().getPrix();

                    paquetArrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();

                    VariablesGlobales.getInstance().setPrix_total(reste);
                    prix_tot.setText(""+reste);
                }
            });

            btn_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    paquetArrayList.get(getAdapterPosition()).getAliment().setPrix(paquetArrayList.get(getAdapterPosition()).getAliment().getPrix() + paquetArrayList.get(getAdapterPosition()).getAliment().getPas_de_prix());
                    prix_choisi.setText(""+paquetArrayList.get(getAdapterPosition()).getAliment().getPrix());

                    VariablesGlobales.getInstance().setPrix_total(VariablesGlobales.getInstance().getPrix_total() + paquetArrayList.get(getAdapterPosition()).getAliment().getPas_de_prix());
                    prix_tot.setText(""+VariablesGlobales.getInstance().getPrix_total());
                }
            });

            btn_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(paquetArrayList.get(getAdapterPosition()).getAliment().getPrix() > 100) {
                        paquetArrayList.get(getAdapterPosition()).getAliment().setPrix(paquetArrayList.get(getAdapterPosition()).getAliment().getPrix() - paquetArrayList.get(getAdapterPosition()).getAliment().getPas_de_prix());
                        prix_choisi.setText(""+paquetArrayList.get(getAdapterPosition()).getAliment().getPrix());
                        VariablesGlobales.getInstance().setPrix_total(VariablesGlobales.getInstance().getPrix_total() - paquetArrayList.get(getAdapterPosition()).getAliment().getPas_de_prix());
                        prix_tot.setText(""+VariablesGlobales.getInstance().getPrix_total());
                    }
                }
            });
            parentView = view;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.getId() == parentView.getId()){

            }
        }
    }

    public PanierAdapter(Context mContext, ArrayList<Paquet> paquetArrayList, TextView prix_tot) {
        this.mContext = mContext;
        this.paquetArrayList = paquetArrayList;
        this.prix_tot = prix_tot;
    }

    @Override
    public PanierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.panier_list_ui, parent, false);

        return new PanierAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PanierAdapter.ViewHolder holder, int position) {
        final Paquet paquet = paquetArrayList.get(position);

        pos = position;
        holder.nom.setText(paquet.getAliment().getNom());

        holder.icon.setBackgroundResource(paquet.getAliment().getImage_id());

        holder.prix_choisi.setText(""+paquet.getAliment().getPrix());

    }

    @Override
    public int getItemCount() {
        return paquetArrayList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PanierAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PanierAdapter.ClickListener clickListener) {
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
