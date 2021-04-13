package mg.mbds.nfcx_change.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import mg.mbds.nfcx_change.Fragment.ProductDetailFragment;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;


public class ListServiceAdapter  extends RecyclerView.Adapter<ListServiceAdapter.ViewHolder> {

    private List<ClassMapTable> serviceList;
    private FragmentActivity fragmentActivity;

    public  ListServiceAdapter(FragmentActivity fragmentActivity,List<ClassMapTable> serviceList){
        this.serviceList = serviceList;
        setFragmentActivity(fragmentActivity);
    }
    @NonNull
    @Override
    public ListServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.liste_default_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListServiceAdapter.ViewHolder viewHolder, int i) {
        Service service = (Service)serviceList.get(i);

        viewHolder.titleService.setText(service.getServiceName());
        viewHolder.dateService.setText(Utilitaire.formatDate(service.getPublicationDate()));
        viewHolder.serviceOwner.setText(service.getUtilisateur().getName());
        viewHolder.descriptionService.setText(service.getServiceDescription());
        viewHolder.priceService.setText(String.valueOf(service.getPrice()));

        if(service.getUtilisateur().getAvatar() != null && !service.getUtilisateur().getAvatar().isEmpty()) {
            //Utilitaire.downloadImage(getFragmentActivity(), viewHolder.buyerAvatar, service.getUtilisateur().getAvatar());
            Utilitaire.decodeBase64Image(getFragmentActivity(), viewHolder.buyerAvatar, service.getUtilisateur().getAvatar());
        }
        if(service.getServiceImgUrl() != null && !service.getServiceImgUrl().isEmpty()) {
            //Utilitaire.downloadImage(getFragmentActivity(), viewHolder.productImage, service.getServiceImgUrl());
            Utilitaire.decodeBase64Image(getFragmentActivity(), viewHolder.productImage, service.getServiceImgUrl());
        }

        changeFragmentOnclickDetail(viewHolder.detailButton, String.valueOf(service.getId()),service.getTypeservice().getId());

    }

    public FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Button detailButton ;
        ImageView buyerAvatar;
        ImageView productImage;
        TextView titleService;
        TextView dateService;
        TextView serviceOwner;
        TextView descriptionService;
        TextView priceService;

        public ViewHolder(View itemView) {
            super(itemView);
            detailButton = itemView.findViewById(R.id.product_detail);
            buyerAvatar = itemView.findViewById(R.id.buyer_avatar);
            productImage = itemView.findViewById(R.id.product_img);
            titleService = itemView.findViewById(R.id.titleService);
            dateService = itemView.findViewById(R.id.date_service);
            serviceOwner = itemView.findViewById(R.id.service_owner);
            descriptionService = itemView.findViewById(R.id.description_service);
            priceService = itemView.findViewById(R.id.price_service);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void changeFragmentOnclickDetail(Button detailButton, final String id, final String type_service_id){
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Service_Id", id);
                bundle.putString("Type_Service_Id", type_service_id);
                productDetailFragment.setArguments(bundle);
                getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,productDetailFragment,"Product Detail").commit();
            }
        });
    }
}
