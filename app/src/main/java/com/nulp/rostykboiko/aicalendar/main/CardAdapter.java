package com.nulp.rostykboiko.aicalendar.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nulp.rostykboiko.aicalendar.R;
import com.nulp.rostykboiko.aicalendar.utils.firebase.dao.Event;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private QuestionsCardCallback callback;

    private ArrayList<Event> eventList = new ArrayList<>();

    CardAdapter(@NonNull QuestionsCardCallback callback) {
        this.callback = callback;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new CardViewHolder(itemView, parent.getContext(), new CardViewHolder.QuestionCardCallback() {
            @Override
            public void onDeleteCard(int position) {
                callback.onCardDeleted(eventList.get(position));
                eventList.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onEditClick(int position) {
                callback.onEditClick(eventList.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Event event = eventList.get(position);
        CardViewHolder viewHolder = (CardViewHolder) holder;
        viewHolder.setSurveyName(event.getEventTitle());
        viewHolder.setEvent(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    void setEventList(@NonNull List<Event> eventList) {
        if (!this.eventList.containsAll(eventList)) {
            this.eventList.addAll(eventList);
        }
    }

    interface QuestionsCardCallback {
        void onCardDeleted(@NonNull Event event);

        void onEditClick(@NonNull Event event);
    }
}