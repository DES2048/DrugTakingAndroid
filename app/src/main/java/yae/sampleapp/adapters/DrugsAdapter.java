package yae.sampleapp.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import yae.sampleapp.models.Drug;

public class DrugsAdapter extends ListAdapter<Drug, KtDrugViewHolder> {
    private KtDrugViewHolder.OnClickListener onButtonTakeDrugClickListener;
    private KtDrugViewHolder.OnClickListener onButtonDrugMenuClickListener;
    public DrugsAdapter(@NonNull DiffUtil.ItemCallback<Drug> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public KtDrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drug_item, parent, false); */

        KtDrugViewHolder vh = KtDrugViewHolder.Companion.create(parent);
        if (onButtonTakeDrugClickListener != null) {
            vh.setOnTakeDrugClickListener(onButtonTakeDrugClickListener);
        }
        if (onButtonDrugMenuClickListener != null) {
            vh.setOnItemMenuClickListener(onButtonDrugMenuClickListener);
        }

        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull KtDrugViewHolder holder, int position) {
        holder.bind(getItem(position));

    }

    public void setOnButtonTakeDrugListener(KtDrugViewHolder.OnClickListener handler) {
        onButtonTakeDrugClickListener = handler;
    }
    public void setOnButtonDrugMenuClickListener(KtDrugViewHolder.OnClickListener onButtonDrugMenuClickListener) {
        this.onButtonDrugMenuClickListener = onButtonDrugMenuClickListener;
    }
}
