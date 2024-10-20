package yae.sampleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PeopleAdapter extends ListAdapter<PeopleViewModel.People, PeopleAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewLarge;
        private TextView textViewSmall;
        private Button btnMakeBig;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLarge = itemView.findViewById(R.id.textViewLarge);
            textViewSmall = itemView.findViewById(R.id.textViewSmall);
            btnMakeBig = itemView.findViewById(R.id.btnMakeBig);
            btnMakeBig.setOnClickListener(view -> {
                textViewLarge.setText(textViewSmall.getText().toString().toUpperCase());
            });
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.textViewLarge.setText();
        PeopleViewModel.People people = getItem(position);

        holder.textViewSmall.setText(people.getName());
    }


    public PeopleAdapter(@NonNull DiffUtil.ItemCallback<PeopleViewModel.People> diffCallback) {
        super(diffCallback);
    }

}
