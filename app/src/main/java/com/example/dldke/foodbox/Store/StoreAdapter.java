package com.example.dldke.foodbox.Store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ItemViewHolder> {

    private ArrayList<StoreData> Items;
    private Context context;

    public StoreAdapter(Context context, ArrayList<StoreData> items){
        this.context = context;
        this.Items = items;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView theme_image;
        private TextView theme_title, theme_point;
        private Button buy;

        public ItemViewHolder(View view){
            super(view);
            theme_image = (ImageView) view.findViewById(R.id.theme_picture);
            theme_title = (TextView) view.findViewById(R.id.theme_title);
            theme_point = (TextView) view.findViewById(R.id.theme_price);
            buy = (Button) view.findViewById(R.id.buy);
        }
    }


    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    public void onBindViewHolder(final ItemViewHolder holder, final int position){
        String theme_title = Items.get(position).getTheme_title();
        String theme_point = Integer.toString(Items.get(position).getTheme_point());

        holder.theme_title.setText(theme_title);
        holder.theme_point.setText(theme_point+"point");
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_theme = Mapper.searchUserInfo().getTheme();
                String user_point = Integer.toString(Mapper.searchUserInfo().getPoint());

                int userPoint = Integer.parseInt(user_point);
                int themePoint = Integer.parseInt(theme_point);
                int point = userPoint - themePoint;

                if((point > 0)){
                    Mapper.updateTheme(theme_title, point);
                }
                else{
                    Toast.makeText(context, "포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int getItemCount(){return Items.size();}
}
