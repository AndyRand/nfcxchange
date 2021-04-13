package mg.mbds.nfcx_change.Adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONException;

import java.util.List;

import mg.mbds.nfcx_change.Fragment.MyServicesListFragment;
import mg.mbds.nfcx_change.Fragment.ProductDetailFragment;
import mg.mbds.nfcx_change.Fragment.ViewPagerFragment;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;


public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.ViewHolder> {

    private List<ClassMapTable> serviceList;
    private FragmentActivity fragmentActivity;
    private boolean isMenuClicked;
    private Dialog dialog;
    private  MyServicesListFragment fragment;

    private ProductDetailFragment fragmentDetail;

    public MyServiceAdapter(FragmentActivity fragmentActivity,MyServicesListFragment fragment, List<ClassMapTable> serviceList, boolean isMenuClicked, Dialog dialog, ProductDetailFragment fragmentDetail){
        this.serviceList = serviceList;
        setFragmentActivity(fragmentActivity);
        this.isMenuClicked = isMenuClicked;
        this.dialog = dialog;
        this.fragment = fragment;
        this.fragmentDetail = fragmentDetail;
    }
    @NonNull
    @Override
    public MyServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_service_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceAdapter.ViewHolder viewHolder, int i) {
        final Service service = (Service) serviceList.get(i);
        if(service.getServiceImgUrl() != null && !service.getServiceImgUrl().isEmpty())
            //Utilitaire.downloadImage(getFragmentActivity(),viewHolder.product_img,service.getServiceImgUrl());
            Utilitaire.decodeBase64Image(getFragmentActivity(),viewHolder.product_img,service.getServiceImgUrl());
        viewHolder.product_name.setText(service.getServiceName());
        viewHolder.price_my_product.setText(String.valueOf(service.getPrice()));
        if(isMenuClicked){
            this.onClickCardView(viewHolder.cardView,service);
        }
        else{
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        fragmentDetail.getServiceRequestXChange().setServiceXchange(service);
                        fragmentDetail.addBuyAndXchange(fragmentDetail.getServiceRequestXChange(), 1,dialog);

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView product_name;
        ImageView product_img;
        TextView price_my_product;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_my_service);
            product_name = itemView.findViewById(R.id.product_name);
            product_img = itemView.findViewById(R.id.product_img);
            price_my_product = itemView.findViewById(R.id.price_my_product);
        }
    }

    public void onClickCardView(CardView cardView, final Service service){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showUpdate(service);

            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


}
