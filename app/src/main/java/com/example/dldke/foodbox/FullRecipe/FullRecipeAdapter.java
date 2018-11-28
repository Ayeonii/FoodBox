package com.example.dldke.foodbox.FullRecipe;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxHalfRecipeDetailActivity;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeAdapter extends RecyclerView.Adapter<FullRecipeAdapter.FullRecipeViewHolder> {

    private ArrayList<FullRecipeData> mList;
    private Context mContext;
    private List<RecipeDO.Ingredient> data;
    private RecipeBoxHalfRecipeDetailActivity recipeBoxHalfRecipeDetailActivity = new RecipeBoxHalfRecipeDetailActivity();
    private String recipeId = recipeBoxHalfRecipeDetailActivity.getRecipeId();
    String testStr = "";
    private static List<RecipeDO.Ingredient> specIngredientList = new ArrayList<>();

    private final String TAG = "FullRecipeAdapter";

    public class FullRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 1. 리스너 추가

        protected TextView StepDetail;
        protected ImageView StepImage;


        public FullRecipeViewHolder(View view) {
            super(view);
            this.StepDetail = (TextView) view.findViewById(R.id.full_recipe_step_detail);
            this.StepImage = (ImageView) view.findViewById(R.id.imgview_foodimg);
            view.setOnCreateContextMenuListener(this); //2. 리스너 등록
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
            data = Mapper.searchRecipe(recipeId).getIngredient();

        }

        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    //수정 눌렀을 때
                    case 1001:

                        //레시피 다이얼로그 build
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("레시피를 작성하세요");

                        final List<String> specItems = new ArrayList<>();
                        final List<String> tempItems = new ArrayList<>();
                        //다이얼로그 안에 체크박스 만들기
                        for(int i = 0; i<data.size(); i++){
                            specItems.add(data.get(i).getIngredientName());
                        }
                        final CharSequence[] items =  specItems.toArray(new String[specItems.size()]);  //체크박스 만들기
                        final List SelectedItems  = new ArrayList();

                        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                                //선택된 재료 저장

                                if(isChecked) {
                                    SelectedItems.add(i);
                                    tempItems.add(specItems.get(i));//레시피 단계 작성후에 text로 보여지는 재료 이걸로 쓰자

                                    Log.e(TAG, "재료 이름 가져와 "+tempItems);
                                }

                            }
                        });

                        View view = LayoutInflater.from(mContext).inflate(R.layout.fullrecipe_step_dialog, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.done_btn);
                        final Spinner MethodSpinner = (Spinner) view.findViewById(R.id.method_spinner);
                        final Spinner MinuteSpinner = (Spinner) view.findViewById(R.id.minute_spinner);
                        final Spinner FireSpinner = (Spinner) view.findViewById(R.id.fire_spinner);

                        String[] methodstr = view.getResources().getStringArray(R.array.MethodSpinner);
                        ArrayAdapter<String> madapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, methodstr);
                        MethodSpinner.setAdapter(madapter);


                        String[] minutestr = view.getResources().getStringArray(R.array.MinuteSpinner);
                        final ArrayAdapter<String> minadapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, minutestr);
                        MinuteSpinner.setAdapter(minadapter);

                        String[] firestr = view.getResources().getStringArray(R.array.FireSpinner);
                        final ArrayAdapter<String> fireadapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, firestr);
                        FireSpinner.setAdapter(fireadapter);


                        //수정완료 후 진행되는 이벤트
                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                String step_descriptoin="";

                                for(int num = 0; num<SelectedItems.size(); num++){
                                    specIngredientList.add(Mapper.searchRecipe(recipeId).getIngredient().get(num));
                                    Log.e(TAG, ""+Mapper.searchRecipe(recipeId).getIngredient().get(num));
                                }

                                for(int num = 0; num < SelectedItems.size(); num++){
                                    int index = (int)SelectedItems.get(num);
                                    Log.e(TAG, "SelectedItems이다다"+SelectedItems.get(num).toString());
                                    testStr = testStr.concat(tempItems.get(num));
                                    Log.e(TAG, "하나씩 붙어라"+testStr);
                                }


                                String method = MethodSpinner.getSelectedItem().toString();
                                String minute = MinuteSpinner.getSelectedItem().toString();
                                String fire = FireSpinner.getSelectedItem().toString();

                                if(minute.equals("0") || fire.equals("없음")){
                                    step_descriptoin = testStr+" 을/를 \r\n"+method;
                                }else{
                                    step_descriptoin = testStr+" 을/를 \r\n"+minute+" 분 동안 \r\n"+method+" (불 세기: "+fire+" )";
                                }

                                FullRecipeData dict = new FullRecipeData(step_descriptoin);

                                //FullRecipeData dict = new FullRecipeData(method, minute, fire);

                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        break;


                        //삭제 눌렀을 때
                    case 1002:
                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());

                        break;
                }
                return true;
            }
        };
    }

    public FullRecipeAdapter(Context context, ArrayList<FullRecipeData> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public FullRecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fullrecipe_step_list_item, viewGroup, false);

        FullRecipeViewHolder viewHolder = new FullRecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FullRecipeViewHolder viewholder, int position) {
        viewholder.StepDetail.setText(mList.get(position).getStepDescription());
        viewholder.StepDetail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        viewholder.StepDetail.setGravity(Gravity.CENTER);
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}