package com.example.sqlite_todolist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.viewHolder> {

    private ArrayList<TodoItem> mtodoItems;
    private Context mContext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<TodoItem> mtodoItems, Context mContext) {
        this.mtodoItems = mtodoItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public CustomAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new viewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.viewHolder holder, int position) {
        holder.tv_title.setText(mtodoItems.get(position).getTitle());
        holder.tv_content.setText(mtodoItems.get(position).getContent());
        holder.tv_writeDate.setText(mtodoItems.get(position).getWriteDate());

    }

    @Override
    public int getItemCount() {
        return mtodoItems.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writeDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition(); //?????? ????????? ????????? ????????? ??????
                    TodoItem todoItem = mtodoItems.get(curPos); //

                    //???????????? ????????????
                    String[] strChoiceItems = {"????????????", "????????????"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("????????? ????????? ????????? ?????????");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0) {
                                //????????????
                                //????????? ?????????
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);
                                EditText et_title = dialog.findViewById(R.id.et_title);
                                EditText et_content = dialog.findViewById(R.id.et_content);
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                                et_title.setText(todoItem.getTitle());
                                et_content.setText(todoItem.getContent());
                                //?????? ??????
                                et_title.setSelection(et_title.getText().length());

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //???????????? ?????? update database
                                        String title = et_title.getText().toString();
                                        String content = et_content.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); //???????????? ?????????????????? ????????????
                                        String beforeTime = todoItem.getWriteDate();

                                        mDBHelper.UpdateTodo(title, content, currentTime, beforeTime);

                                        //Insert UI
                                         todoItem.setTitle(title);
                                         todoItem.setContent(content);
                                         todoItem.setWriteDate(currentTime);
                                         notifyItemChanged(curPos, todoItem);
                                         dialog.dismiss();
                                        Toast.makeText(mContext, "?????? ????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.show();
                            }
                            else if (position == 1) {
                                //???????????? delete table
                                String beforeTime = todoItem.getWriteDate();
                                mDBHelper.deleteTodo(beforeTime);
                                // delete UI ??????????????? ??????
                                mtodoItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    //?????????????????? ???????????? ??????, ?????? ???????????? ????????? ????????? ???????????? ???????????? ???????????? ????????????.
    public void addItem(TodoItem _item) {
        mtodoItems.add(0, _item);
        notifyItemInserted(0);
    }

}
