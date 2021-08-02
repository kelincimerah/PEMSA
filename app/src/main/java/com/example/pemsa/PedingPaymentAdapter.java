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

public class PedingPaymentAdapter extends RecyclerView.Adapter<PedingPaymentAdapter.PedingPaymentViewHolder>{

    private PendingPaymentListActivity activity;
    private List<Bill> bList;

    public PedingPaymentAdapter(PendingPaymentListActivity activity, List<Bill> bList) {
        this.activity = activity;
        this.bList = bList;
    }

    @NonNull
    @NotNull
    @Override
    public PedingPaymentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_single_pending_payment, parent, false);
        return new PedingPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PedingPaymentAdapter.PedingPaymentViewHolder holder, int position) {

        holder.fullName.setText(bList.get(position).getName());
        holder.address.setText(bList.get(position).getAddress());
        holder.amounBill.setText(String.valueOf(bList.get(position).getBill()));
        holder.dateOfBill.setText(bList.get(position).getDateOfBill());

        if (bList.get(position).getStatus() == 1){
            holder.status.setText("Pending");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ApprovementPaymentActivity.class);
                intent.putExtra("pendingData", bList.get(position));
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public static class PedingPaymentViewHolder extends RecyclerView.ViewHolder{

        TextView fullName, address, amounBill, dateOfBill, status;
        public PedingPaymentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.fullNamePending);
            address = itemView.findViewById(R.id.addresstPending);
            amounBill = itemView.findViewById(R.id.amountBillPending);
            dateOfBill = itemView.findViewById(R.id.dateOfBIllPending);
            status = itemView.findViewById(R.id.statusPending);
        }
    }
}
