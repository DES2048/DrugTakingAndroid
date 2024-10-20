package yae.sampleapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import yae.sampleapp.PeopleViewModel;

@Dao
public interface PeopleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void add(PeopleViewModel.People people);

    @Query("select * from peoples;")
    public LiveData<List<PeopleViewModel.People>> getAll();

    @Query("DELETE FROM peoples;")
    public void deleteAll();
}
