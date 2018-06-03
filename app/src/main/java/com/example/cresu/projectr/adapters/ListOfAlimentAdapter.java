package com.example.cresu.projectr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cresu.projectr.R;
import com.example.cresu.projectr.modeles.Aliment;
import com.example.cresu.projectr.modeles.Paquet;
import com.example.cresu.projectr.utils.VariablesGlobales;

import java.util.ArrayList;

/**
 * Created by cresu on 17/03/2018.
 */

public class ListOfAlimentAdapter extends RecyclerView.Adapter<ListOfAlimentAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Aliment> alimentsArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nom;
        public ImageView icon;
        public RadioButton radBtn;
        public View parentView;

        public ViewHolder(View view) {
            super(view);
            nom = (TextView) view.findViewById(R.id.aliment_name);
            icon = (ImageView) view.findViewById(R.id.aliment_icon);
            radBtn = (RadioButton) view.findViewById(R.id.radio_deja_ajoute);

            parentView = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.getId() == parentView.getId() || view.getId() == icon.getId() || view.getId() == radBtn.getId()){
                radBtn.setChecked(true);

                if(radBtn.isChecked()){

                    if(isInPanier(alimentsArrayList.get(getAdapterPosition()))){
                        Toast.makeText(mContext, "Cet aliment est déja dans le panier", Toast.LENGTH_SHORT).show();
                    }else{
                        ajouterAlimentAuPanier(alimentsArrayList.get(getAdapterPosition()));
                        Toast.makeText(mContext, "Aliment Ajouté au panier", Toast.LENGTH_SHORT).show();
                        VariablesGlobales.getInstance().setPrix_total(VariablesGlobales.getInstance().getPrix_total() + alimentsArrayList.get(getAdapterPosition()).getPrix());
                    }
                }
            }

        }

        private void ajouterAlimentAuPanier(Aliment aliment){

            ArrayList<Paquet> panier = new ArrayList<>();

            panier = VariablesGlobales.getInstance().getPaniers();

            Paquet paquet = new Paquet();
            paquet.setAliment(aliment);
            paquet.setQuantite(0);

            panier.add(paquet);

            VariablesGlobales.getInstance().setPaniers(panier);
        }
    }

    public ListOfAlimentAdapter(Context mContext, ArrayList<Aliment> alimentsArrayList) {
        this.mContext = mContext;
        this.alimentsArrayList = alimentsArrayList;
    }

    @Override
    public ListOfAlimentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.aliment_on_list_ui, parent, false);

        return new ListOfAlimentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListOfAlimentAdapter.ViewHolder holder, int position) {
        Aliment aliment = alimentsArrayList.get(position);

        holder.nom.setText(aliment.getNom());

        //holder.icon.setImageResource(chatRoom.getIcon());
        holder.icon.setBackgroundResource(aliment.getImage_id());

        if(isInPanier(aliment)){
            holder.radBtn.setChecked(true);
        }

        holder.radBtn.setClickable(false);

    }

    @Override
    public int getItemCount() {
        return alimentsArrayList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     *  Other Functions
     */

    private boolean isInPanier(Aliment aliment){

        ArrayList<Paquet> panier = new ArrayList<>();

        panier = VariablesGlobales.getInstance().getPaniers();

        for(int i = 0; i < panier.size(); i++){
            if(aliment.getNom().equals(panier.get(i).getAliment().getNom())){
                return true;
            }
        }

        return false;
    }

    /*************************************************************************************************************************/

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ListOfAlimentAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ListOfAlimentAdapter.ClickListener clickListener) {
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
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
