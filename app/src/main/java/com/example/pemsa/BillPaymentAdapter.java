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
import com.example.pemsa.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BillPaymentAdapter extends RecyclerView.Adapter<BillPaymentAdapter.BillPaymentViewHolder>{

    private Context context;
    private List<Bill> bList;

    public BillPaymentAdapter(Context context, List<Bill> bList) {
        this.context = context;
        this.bList = bList;
    }

    @NonNull
    @NotNull
    @Override
    public BillPaymentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_payment, parent, false);
        return new BillPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BillPaymentAdapter.BillPaymentViewHolder holder, int position) {

        holder.amounBill.setText(String.valueOf(bList.get(position).getBill()));
        holder.dateOfBill.setText(bList.get(position).getDateOfBill());

        if (bList.get(position).getStatus() == 0){
            holder.status.setText("Belum Dibayar");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, PaymentBillActivity.class);
                    intent.putExtra("billData", bList.get(position));
                    context.startActivity(intent);
                }
            });
        }else if (bList.get(position).getStatus() == 1){
            holder.status.setText("Pending");
        }



    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public static class BillPaymentViewHolder extends RecyclerView.ViewHolder{

        TextView amounBill, dateOfBill, status;

        public BillPaymentViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            amounBill = itemView.findViewById(R.id.amountbill);
            dateOfBill = itemView.findViewById(R.id.dateOfBIll);
            status = itemView.findViewById(R.id.statusPayment);
        }
    }
}
