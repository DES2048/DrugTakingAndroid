package yae.sampleapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class PeopleDiff extends DiffUtil.ItemCallback<PeopleViewModel.People> {
    @Override
    public boolean areItemsTheSame(@NonNull PeopleViewModel.People oldItem, @NonNull PeopleViewModel.People newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull PeopleViewModel.People oldItem, @NonNull PeopleViewModel.People newItem) {
        return oldItem.getName().equals(newItem.getName());
    }
}
