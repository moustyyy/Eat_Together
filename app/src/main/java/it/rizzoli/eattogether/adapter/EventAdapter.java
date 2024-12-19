package it.rizzoli.eattogether.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.rizzoli.eattogether.R;
import it.rizzoli.eattogether.Utility;
import it.rizzoli.eattogether.database.entity.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventsList;
    private OnItemClickListener onItemClickListener;

    public EventAdapter(List<Event> events, OnItemClickListener onItemClickListener) {
        this.eventsList = events;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventsList.get(position);
        holder.eventNameTextView.setText(event.getNome());
        holder.eventDateTextView.setText(event.getData());

        if(event.hasImg()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(event.getImg(), 0, event.getImg().length));
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;
        TextView eventDateTextView;
        ImageView imageView;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            eventNameTextView = itemView.findViewById(R.id.event_name);
            eventDateTextView = itemView.findViewById(R.id.event_date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }
}
