package yae.sampleapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import yae.sampleapp.models.Drug;

@Dao
public abstract class DrugDao {
    @Insert
    public abstract long addDrug(Drug drug);

    @Query("SELECT * FROM drugs;")
    public abstract LiveData<List<Drug>> getDrugs();

    @Query("SELECT * FROM drugs WHERE id=:drugId;")
    public abstract Drug getDrug(long drugId);

    @Update()
    public abstract void updateDrug(Drug drug);

    @Delete
    public abstract void deleteDrug(Drug drug);

}
