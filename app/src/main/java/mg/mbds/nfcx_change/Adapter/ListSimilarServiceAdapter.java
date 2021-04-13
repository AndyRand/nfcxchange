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
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import mg.mbds.nfcx_change.Fragment.ProductDetailFragment;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;


public class ListSimilarServiceAdapter extends RecyclerView.Adapter<ListSimilarServiceAdapter.ViewHolder> {

    private List<ClassMapTable> serviceList;
    private FragmentActivity fragmentActivity;

    public ListSimilarServiceAdapter(FragmentActivity fragmentActivity, List<ClassMapTable> serviceList){
        this.serviceList = serviceList;
        setFragmentActivity(fragmentActivity);
    }
    @NonNull
    @Override
    public ListSimilarServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.similar_product_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSimilarServiceAdapter.ViewHolder viewHolder, int i) {
        Service service = (Service) serviceList.get(i);
        viewHolder.similar_product_name.setText(service.getServiceName());
        if(service.getServiceImgUrl() != null && !service.getServiceImgUrl().isEmpty())
            //Utilitaire.downloadImage(getFragmentActivity(),viewHolder.img_similar_product,service.getServiceImgUrl());
            Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.img_similar_product,service.getServiceImgUrl());
        if(service.getUtilisateur().getAvatar() != null && !service.getUtilisateur().getAvatar().isEmpty())
            //Utilitaire.downloadImage(getFragmentActivity(),viewHolder.user_img,service.getServiceImgUrl());
            Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.user_img,service.getUtilisateur().getAvatar());

        onClickCardView(viewHolder.card_view_similar,service.getId(),service.getTypeservice().getId());
        viewHolder.user_price.setText(String.valueOf(service.getPrice()));

    }

    public FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_similar_product;
        TextView similar_product_name;
        RoundedImageView user_img;
        CardView card_view_similar;
        TextView user_price;
        public ViewHolder(View itemView) {
            super(itemView);
            img_similar_product = itemView.findViewById(R.id.img_similar_product);
            similar_product_name = itemView.findViewById(R.id.similar_product_name);
            user_img = itemView.findViewById(R.id.user_img);
            card_view_similar = itemView.findViewById(R.id.card_view_similar);
            user_price = itemView.findViewById(R.id.user_price);

        }
    }

    public void onClickCardView(CardView cardView, final String serviceId, final String  type_service_id){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Service_Id", serviceId);
                bundle.putString("Type_Service_Id", type_service_id);
                productDetailFragment.setArguments(bundle);

                getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,productDetailFragment,"").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

}
