package mg.mbds.nfcx_change.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;


public class ShopXChangeAdapter extends RecyclerView.Adapter<ShopXChangeAdapter.ViewHolder> {

    private List<ClassMapTable> serviceList;
    private FragmentActivity fragmentActivity;
    private boolean isDemande;
    private SessionManager sessionManager ;
    public ShopXChangeAdapter(FragmentActivity fragmentActivity, List<ClassMapTable> serviceList, boolean isDemande){
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
    public ShopXChangeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.liste_default_layout_x_change, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopXChangeAdapter.ViewHolder viewHolder, int i) {
        ServiceRequest serviceRequest = (ServiceRequest) serviceList.get(i);
        if(serviceRequest.getServiceXchange().getServiceImgUrl() != null || !serviceRequest.getServiceXchange().getServiceImgUrl().isEmpty())
            Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.product_img_user1,serviceRequest.getServiceXchange().getServiceImgUrl());
        if(serviceRequest.getService().getServiceImgUrl() != null || !serviceRequest.getService().getServiceImgUrl().isEmpty())
            Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.img_product_user2,serviceRequest.getService().getServiceImgUrl());
        if(serviceRequest.getService().getUtilisateur().getAvatar() != null || !serviceRequest.getService().getUtilisateur().getAvatar().isEmpty())
            Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.user2_img,serviceRequest.getService().getUtilisateur().getAvatar());

        viewHolder.title_buy.setText(serviceRequest.getServiceXchange().getServiceName());
        viewHolder.price_buy.setText(String.valueOf(serviceRequest.getServiceXchange().getPrice()));

        if(serviceRequest.getServiceXchange().getPrice() > serviceRequest.getService().getPrice()) {
            viewHolder.difference_p1.setText(String.valueOf(Utilitaire.difference(serviceRequest.getServiceXchange().getPrice(), serviceRequest.getService().getPrice())));
            viewHolder.reste_p2.setVisibility(View.GONE);
        }
        else if(serviceRequest.getServiceXchange().getPrice() < serviceRequest.getService().getPrice()) {
            viewHolder.difference_p2.setText(String.valueOf(Utilitaire.difference(serviceRequest.getServiceXchange().getPrice(), serviceRequest.getService().getPrice())));
            viewHolder.reste_p1.setVisibility(View.GONE);
        }
        else {
            viewHolder.reste_p1.setVisibility(View.GONE);
            viewHolder.reste_p2.setVisibility(View.GONE);
        }

        viewHolder.title_buy_user2.setText(serviceRequest.getService().getServiceName());
        viewHolder.price_user2.setText(String.valueOf(serviceRequest.getService().getPrice()));
        viewHolder.name_user2.setText(serviceRequest.getService().getUtilisateur().getName());

        if(serviceRequest.isStatusAccept() || isDemande){
            viewHolder.pending.setVisibility(View.GONE);
        }
        if(!serviceRequest.isStatusAccept() && !isDemande){
            viewHolder.checkboxChange.setVisibility(View.GONE);
        }

        Utilitaire.setOnCheckBox(getFragmentActivity(),viewHolder.checkboxChange,serviceRequest.getId(),serviceRequest.getService().getUtilisateur());

        onClickDelete(i,viewHolder.delete,viewHolder);
    }

    public FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView product_img_user1;
        ImageView img_product_user2;
        ImageView user2_img;
        TextView title_buy;
        TextView price_buy;
        TextView price_user2;
        TextView title_buy_user2;
        TextView name_user2;
        TextView pending;
        TextView difference_p1;
        TextView difference_p2;
        LinearLayout reste_p1;
        LinearLayout reste_p2;
        CheckBox checkboxChange;
        ImageView delete;
        CardView card_view;
        LinearLayout supprimer_layout;
        Button delete_final;
        TextView cancel;
        public ViewHolder(View itemView) {
            super(itemView);
            product_img_user1 = itemView.findViewById(R.id.product_img_user1);
            img_product_user2 = itemView.findViewById(R.id.img_product_user2);
            user2_img = itemView.findViewById(R.id.user2_img);
            title_buy = itemView.findViewById(R.id.title_buy);
            price_buy = itemView.findViewById(R.id.price_buy);
            price_user2 = itemView.findViewById(R.id.price_user2);
            title_buy_user2 = itemView.findViewById(R.id.title_buy_user2);
            name_user2 = itemView.findViewById(R.id.name_user2);
            difference_p1 = itemView.findViewById(R.id.difference_p1);
            difference_p2 = itemView.findViewById(R.id.difference_p2);
            reste_p1 = itemView.findViewById(R.id.reste_p1);
            reste_p2 = itemView.findViewById(R.id.reste_p2);
            pending = itemView.findViewById(R.id.pending);
            checkboxChange = itemView.findViewById(R.id.checkboxChange);
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
