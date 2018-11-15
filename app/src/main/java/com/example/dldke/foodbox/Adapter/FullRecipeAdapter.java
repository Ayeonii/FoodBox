package com.example.dldke.foodbox.Adapter;

import android.content.Context;
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

import com.example.dldke.foodbox.FullRecipeData;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class FullRecipeAdapter extends RecyclerView.Adapter<FullRecipeAdapter.FullRecipeViewHolder> {

    private ArrayList<FullRecipeData> mList;
    private Context mContext;

    private final String TAG = "FullRecipeAdapter";

    public class FullRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 1. 리스너 추가

        protected TextView Method;
        protected TextView Mintue;
        protected TextView Fire;
        protected ImageView StepImage;


        public FullRecipeViewHolder(View view) {
            super(view);
            this.Method = (TextView) view.findViewById(R.id.textview_recyclerview_method);
            this.Mintue = (TextView) view.findViewById(R.id.textview_recyclerview_mintue);
            this.Fire = (TextView) view.findViewById(R.id.textview_recyclerview_fire);
            this.StepImage = (ImageView) view.findViewById(R.id.imgview_foodimg);

            view.setOnCreateContextMenuListener(this); //2. 리스너 등록
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    //수정 눌렀을 때
                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.fullrecipe_popup, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.done_btn);
                        final Spinner MethodSpinner = (Spinner) view.findViewById(R.id.method_spinner);
                        final Spinner MinuteSpinner = (Spinner) view.findViewById(R.id.minute_spinner);
                        final Spinner FireSpinner = (Spinner) view.findViewById(R.id.fire_spinner);

                        String[] methodstr = view.getResources().getStringArray(R.array.MethodSpinner);
                        ArrayAdapter<String> madapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, methodstr);
                        MethodSpinner.setAdapter(madapter);

                        Log.d(TAG, "Method 스피너이다아아아"+methodstr);

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

                                String method = MethodSpinner.getSelectedItem().toString();
                                String minute = MinuteSpinner.getSelectedItem().toString();
                                String fire = FireSpinner.getSelectedItem().toString();


                                Log.d(TAG, "방법:"+method+"시간: "+minute+"불세기: "+fire);

                                //String strID = method_str.getText().toString();
                                //String strEnglish = minute_str.getText().toString();
                                //String strKorean = fire_str.getText().toString();

                                FullRecipeData dict = new FullRecipeData(method, minute, fire);

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
                .inflate(R.layout.fullrecipe_item_list, viewGroup, false);

        FullRecipeViewHolder viewHolder = new FullRecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FullRecipeViewHolder viewholder, int position) {

        viewholder.Method.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.Mintue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.Fire.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.Method.setGravity(Gravity.CENTER);
        viewholder.Mintue.setGravity(Gravity.CENTER);
        viewholder.Fire.setGravity(Gravity.CENTER);

        viewholder.Method.setText(mList.get(position).getMethod());
        viewholder.Mintue.setText(mList.get(position).getMinute());
        viewholder.Fire.setText(mList.get(position).getFire());

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}