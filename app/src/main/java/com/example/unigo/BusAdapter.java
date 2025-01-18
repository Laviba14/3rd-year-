package com.example.unigo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<String> busList = new ArrayList<>();
    private OnItemClickListener listener;

    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(itemView, listener); // Pass the listener to the ViewHolder
    }

    @Override
    public void onBindViewHolder(BusViewHolder holder, int position) {
        holder.busNumberTextView.setText(busList.get(position));
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public void addBusNumber(String busNumber) {
        busList.add(busNumber);
        notifyItemInserted(busList.size() - 1);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String busNumber);
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView busNumberTextView;

        public BusViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            busNumberTextView = itemView.findViewById(R.id.busNumberTextView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(busNumberTextView.getText().toString());
                }
            });
        }
    }
}
