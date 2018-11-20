package com.example.dldke.foodbox.HalfRecipe;

<<<<<<< Updated upstream
=======
import android.net.Uri;
>>>>>>> Stashed changes
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

<<<<<<< Updated upstream
public class HalfRecipeRecipeAdapter extends RecyclerView.Adapter<HalfRecipeRecipeAdapter.ItemViewHolder> {

    private ArrayList<HalfRecipeRecipeItem> mItems;
    private Double editCount;
    private String strEditCount;

    public HalfRecipeRecipeAdapter(ArrayList<HalfRecipeRecipeItem> mItems) {
=======
public class HalfRecipeDueDateAdapter extends RecyclerView.Adapter<HalfRecipeDueDateAdapter.ItemViewHolder> {

    private ArrayList<HalfRecipeDueDateItem> mItems;

    public HalfRecipeDueDateAdapter(ArrayList<HalfRecipeDueDateItem> mItems) {
>>>>>>> Stashed changes
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
<<<<<<< Updated upstream
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_recipe_item, parent, false);
=======
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_duedate_item, parent, false);
>>>>>>> Stashed changes
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
<<<<<<< Updated upstream
        //재료의 이름 세팅
        holder.txtName.setText(mItems.get(position).getName());
        //재료 보유 개수 세팅
        final Double iCount = mItems.get(position).getCount();
        String strCount = Double.toString(iCount);
        holder.txtCount.setText(strCount);

        //기본으로 사용할 개수 1.0으로 세팅
        editCount = 1.0;
        mItems.get(position).setEditCount(editCount);

        holder.imgFood.setImageURI(mItems.get(position).getImage());

        //plus, minus 버튼 클릭으로 사용할 재료 개수 정하기
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItems.get(position).getEditCount() < mItems.get(position).getCount()) {
                    double plus = mItems.get(position).getEditCount() + 0.5;
                    strEditCount = Double.toString(plus);
                    holder.txtCountEdit.setText(strEditCount);
                    mItems.get(position).setEditCount(plus);
                }
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItems.get(position).getEditCount() > 0.5) {
                    double minus = mItems.get(position).getEditCount() - 0.5;
                    strEditCount = Double.toString(minus);
                    holder.txtCountEdit.setText(strEditCount);
                    mItems.get(position).setEditCount(minus);
                }
            }
        });
=======
        String foodName = mItems.get(position).getName();
        String foodImgUri = "file:///storage/emulated/0/Download/"+foodName+".jpg";
        holder.txtName.setText(foodName);
        holder.imgFood.setImageURI(Uri.parse(foodImgUri));
>>>>>>> Stashed changes
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
<<<<<<< Updated upstream
        private TextView txtName, txtCount, txtCountEdit;
        private Button btnPlus, btnMinus;
=======
        private TextView txtName;
>>>>>>> Stashed changes
        private ImageView imgFood;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
<<<<<<< Updated upstream
            txtCount = (TextView) itemView.findViewById(R.id.txt_count);
            txtCountEdit = (TextView) itemView.findViewById(R.id.txt_count_edit);
            btnPlus = (Button) itemView.findViewById(R.id.btn_plus);
            btnMinus = (Button) itemView.findViewById(R.id.btn_minus);
=======
>>>>>>> Stashed changes
            imgFood = (ImageView) itemView.findViewById(R.id.img_food);
        }
    }
}


