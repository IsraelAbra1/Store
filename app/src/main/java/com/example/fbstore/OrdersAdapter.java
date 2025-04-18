package com.example.fbstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.UserViewHolder> {

    //this context we will use to inflate the layout
    private Context context;

    //storing all the records in a list
    private List<Order> ordersList;

    public OrdersAdapter(Context context, List<Order> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_layout, null);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // read eachtime from arraylist object and write to listview Item
        //getting the record of the specified position
        Order order = ordersList.get(position);

        // TODO: update according to your Class
        holder.tvProductName.setText("ProductName: " + order.getProductName());
        holder.tvQuantity.setText("Quantity: "+order.getQuantity());
        holder.tvPrice.setText("Price: "+order.getPrice());
        holder.boolSwitch.setChecked(order.isDelivered());

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{
        //TextView tvName, tvRecord;
        TextView tvProductName, tvQuantity, tvPrice;
        Switch boolSwitch;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            // TODO:
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            boolSwitch = itemView.findViewById(R.id.switchDelivered);
        }
    }
}
