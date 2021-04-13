package mg.mbds.nfcx_change.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mg.mbds.nfcx_change.Fragment.ProductDetailFragment;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;


public class ShopBuyAdapter extends RecyclerView.Adapter<ShopBuyAdapter.ViewHolder> {

    private List<ClassMapTable> serviceList;
    private FragmentActivity fragmentActivity;
    private boolean isDemande;
    private String idServiceChecked;
    private SessionManager sessionManager ;
    public ShopBuyAdapter(FragmentActivity fragmentActivity, List<ClassMapTable> serviceList, boolean isDemande){
        this.serviceList = serviceList;
        setFragmentActivity(fragmentActivity);
        this.isDemande = isDemande;
        sessionManager = new SessionManager();
        if(!isDemande) {
            if(sessionManager.getPreferences(getFragmentActivity(),"checkbox") != null || !sessionManager.getPreferences(getFragmentActivity(),"checkbox").isEmpty())
                sessionManager.remove(getFragmentActivity(), "checkbox");
        }
    }
    @NonNull
    @Override
    public ShopBuyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.liste_default_layout_buy, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopBuyAdapter.ViewHolder viewHolder, int i) {
        ServiceRequest serviceRequest = (ServiceRequest) serviceList.get(i);
        viewHolder.title_buy.setText(serviceRequest.getService().getServiceName());
        viewHolder.price_buy.setText(String.valueOf(serviceRequest.getService().getPrice()));
        viewHolder.user_name_buy.setText(serviceRequest.getService().getUtilisateur().getName());
        Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.product_img_buy,serviceRequest.getService().getServiceImgUrl());
        Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.image_user_buy,serviceRequest.getService().getUtilisateur().getAvatar());

        if(serviceRequest.isStatusAccept() || isDemande){
            viewHolder.pending.setVisibility(View.GONE);
        }
        if(!serviceRequest.isStatusAccept() && !isDemande){
            viewHolder.checkboxBuy.setVisibility(View.GONE);
        }

        Utilitaire.setOnCheckBox(getFragmentActivity(),viewHolder.checkboxBuy,serviceRequest.getId(),serviceRequest.getService().getUtilisateur());
        onClickDelete(i,viewHolder.delete,viewHolder);
    }

    public FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public String getIdServiceChecked() {
        return idServiceChecked;
    }

    public void setIdServiceChecked(String idServiceChecked) {
        this.idServiceChecked = idServiceChecked;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title_buy;
        TextView price_buy;
        TextView user_name_buy;
        ImageView image_user_buy;
        ImageView product_img_buy;
        TextView pending;
        CheckBox checkboxBuy;
        ImageView delete;
        CardView card_view;
        LinearLayout supprimer_layout;
        Button delete_final;
        TextView cancel;
        public ViewHolder(View itemView) {
            super(itemView);
            title_buy = itemView.findViewById(R.id.title_buy);
            price_buy = itemView.findViewById(R.id.price_buy);
            user_name_buy = itemView.findViewById(R.id.user_name_buy);
            image_user_buy = itemView.findViewById(R.id.image_user_buy);
            product_img_buy = itemView.findViewById(R.id.product_img_buy);
            pending = itemView.findViewById(R.id.pending);
            checkboxBuy = itemView.findViewById(R.id.checkboxBuy);
            delete = itemView.findViewById(R.id.delete);
            card_view = itemView.findViewById(R.id.card_view);
            supprimer_layout = itemView.findViewById(R.id.supprimer_layout);
            delete_final = itemView.findViewById(R.id.delete_final);
            cancel = itemView.findViewById(R.id.cancel);

        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void onClickDelete(final int position, final ImageView delete, final ViewHolder viewHolder){
        viewHolder.supprimer_layout.animate().translationY(viewHolder.supprimer_layout.getHeight());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.supprimer_layout.animate().translationY(0);
                viewHolder.supprimer_layout.setVisibility(View.VISIBLE);
                viewHolder.delete_final.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.supprimer_layout.animate().translationY(viewHolder.supprimer_layout.getHeight());
                        viewHolder.supprimer_layout.setVisibility(View.GONE);
                    }
                });

            }
        });
    }


}
