package com.example.pemsa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pemsa.models.Bill;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryPaymentAdapter extends RecyclerView.Adapter<HistoryPaymentAdapter.HistoryPaymentViewHolder>{

    private Context context;
    private List<Bill> bList;

    public HistoryPaymentAdapter(Context context, List<Bill> bList) {
        this.context = context;
        this.bList = bList;
    }

    @NonNull
    @NotNull
    @Override
    public HistoryPaymentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_payment, parent, false);
        return new HistoryPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryPaymentAdapter.HistoryPaymentViewHolder holder, int position) {

        holder.amounBill.setText(String.valueOf(bList.get(position).getBill()));
        holder.dateOfBill.setText(bList.get(position).getDateOfBill());

        if (bList.get(position).getStatus() == 2){
            holder.status.setText("Lunas");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailHistoryPaymentActivity.class);
                intent.putExtra("historyData", bList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public static class HistoryPaymentViewHolder extends RecyclerView.ViewHolder{

        TextView amounBill, dateOfBill, status;
        public HistoryPaymentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            amounBill = itemView.findViewById(R.id.amountbill);
            dateOfBill = itemView.findViewById(R.id.dateOfBIll);
            status = itemView.findViewById(R.id.statusPayment);
        }
    }
}
