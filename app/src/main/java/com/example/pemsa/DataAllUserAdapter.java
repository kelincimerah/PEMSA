package com.example.pemsa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pemsa.models.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DataAllUserAdapter extends RecyclerView.Adapter<DataAllUserAdapter.DataAllUserViewHolder>{

    private DataUserActivity activity;
    private List<User> uList;

    public DataAllUserAdapter(DataUserActivity activity, List<User> uList) {
        this.activity = activity;
        this.uList = uList;
    }

    @NonNull
    @NotNull
    @Override
    public DataAllUserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_single_user_item, parent, false);
        return new DataAllUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DataAllUserAdapter.DataAllUserViewHolder holder, int position) {

        holder.fullName.setText(uList.get(position).getFullName());
        holder.email.setText(String.valueOf(uList.get(position).getEmail()));
        holder.alamat.setText(String.valueOf(uList.get(position).getAddress()));
        holder.phoneNum.setText(String.valueOf(uList.get(position).getPhone()));

        int type = uList.get(position).getType();

        if (type == 0){
            holder.type.setText("Warga");
        }else {
            holder.type.setText("RT");
        }

        User user = uList.get(position);
        String imageUri = null;
        imageUri = user.getUrl();
        Picasso.get().load(imageUri).into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return uList.size();
    }

    public static class DataAllUserViewHolder extends RecyclerView.ViewHolder{

        TextView fullName, email, alamat, phoneNum, type;
        ImageView userImage;

        public DataAllUserViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.fullNameAlluser);
            email = itemView.findViewById(R.id.emailAllUser);
            alamat = itemView.findViewById(R.id.addressAllUser);
            phoneNum = itemView.findViewById(R.id.phoneNumAllUser);
            userImage = itemView.findViewById(R.id.imageAllUser);
            type = itemView.findViewById(R.id.typeAllUser);
        }
    }
}
