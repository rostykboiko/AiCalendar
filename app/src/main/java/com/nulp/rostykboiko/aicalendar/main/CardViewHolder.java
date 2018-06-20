package com.nulp.rostykboiko.aicalendar.main;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nulp.rostykboiko.aicalendar.R;
import com.nulp.rostykboiko.aicalendar.utils.firebase.dao.Event;


class CardViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private TextView title;
    private ImageView deleteBtn;

    private QuestionCardCallback callback;

    CardViewHolder(final View view, @NonNull Context context, @NonNull QuestionCardCallback callback) {
        super(view);
        this.context = context;
        this.callback = callback;

        title = view.findViewById(R.id.title);

        deleteBtn = view.findViewById(R.id.close_icon);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(deleteBtn, getAdapterPosition());
            }
        });

    }

    public void setEvent(@NonNull Event event){
    }

    void setSurveyName(String eventTitle) {
        title.setText(eventTitle);
    }

    interface QuestionCardCallback {
        void onDeleteCard(int position);

        void onEditClick(int position);

    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.getMenu().findItem(R.id.action_edit).setVisible(true);
        popup.setOnMenuItemClickListener(new MenuItemClickListener(position, callback));
        popup.show();
    }
}