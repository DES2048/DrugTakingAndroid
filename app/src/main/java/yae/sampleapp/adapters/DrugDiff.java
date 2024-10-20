package yae.sampleapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Date;

import yae.sampleapp.models.Drug;

public class DrugDiff extends DiffUtil.ItemCallback<Drug> {
    @Override
    public boolean areItemsTheSame(@NonNull Drug oldItem, @NonNull Drug newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Drug oldItem, @NonNull Drug newItem) {

         Boolean nameSame = oldItem.getName().equals(newItem.getName());
         Date oldTaking = oldItem.getLastTakingDate();
         Boolean takingSame = false;
         if (oldTaking == null) {
             takingSame = newItem.getLastTakingDate() == null;
         } else {
             takingSame = oldTaking.equals(newItem.getLastTakingDate());
         }
         /*
         Boolean takingSame = oldTaking == null ? newItem.getLastTakingDate() == null
                 : oldTaking.compareTo(newItem.getLastTakingDate()) == 0; */
         return nameSame && takingSame;
    }
}
