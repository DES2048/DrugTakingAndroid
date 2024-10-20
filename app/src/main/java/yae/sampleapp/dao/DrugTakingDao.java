package yae.sampleapp.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

import yae.sampleapp.RoomDb;
import yae.sampleapp.models.Drug;
import yae.sampleapp.models.DrugTaking;

@Dao
public abstract class DrugTakingDao {

    private final DrugDao drugDao;

    public DrugTakingDao(@NonNull RoomDb db) {
        drugDao = db.drugDao();
    }

    @Insert
    protected abstract long insertDrugTaking(DrugTaking drugTaking);

    @Query("SELECT * FROM drug_takings WHERE id =:id")
    public abstract DrugTaking getDrugTaking(long id);
    @Transaction
    public DrugTaking addDrugTaking(DrugTaking drugTaking) {
        long drugTakingId = insertDrugTaking(drugTaking);

        Drug drug = drugDao.getDrug(drugTaking.getDrugId());
        Date lastTakingDate = drug.getLastTakingDate();
        if (lastTakingDate == null ||
                lastTakingDate.compareTo(drugTaking.getTakingDate()) < 0) {
            drug.setLastTakingDate(drugTaking.getTakingDate());
            drugDao.updateDrug(drug);
        }
        return getDrugTaking(drugTakingId);

    }
    @Query("SELECT * FROM drug_takings ORDER BY taking_date DESC")
    public abstract List<DrugTaking> listDrugTakings();

    @Delete
    protected abstract void innerDeleteDrugTaking(DrugTaking drugTaking);

    @Query("SELECT * FROM drug_takings WHERE drug_id = :drugId AND taking_date = :takingDate")
    public abstract DrugTaking getDrugTakingByDate(long drugId, Date takingDate);
    @Query("SELECT * FROM drug_takings WHERE drug_id =:drugId AND taking_date<:takingDate " +
            "ORDER BY taking_date DESC LIMIT 1")
    public abstract DrugTaking getDrugTakingBelow(long drugId, Date takingDate);

    public DrugTaking getPrevDrugTaking(@NonNull DrugTaking from) {
        return getDrugTakingBelow(from.getDrugId(), from.getTakingDate());
    }
    @Transaction
    public void deleteDrugTaking(@NonNull DrugTaking drugTaking) {
        Date takingDate = drugTaking.getTakingDate();
        // delete drug taking
        innerDeleteDrugTaking(drugTaking);
        // get associated drug by drug id
        Drug drug = drugDao.getDrug(drugTaking.getDrugId());
        // if this drug have same taking date as deleted drug taking
        if(takingDate.equals(drug.getLastTakingDate())) {
            // get previous drug taking by closest date
            DrugTaking prevDt = getDrugTakingBelow(drugTaking.getDrugId(), drugTaking.getTakingDate());
            // when no prev drug taking set last taking date to nullS
            Date newLastTaking = prevDt != null ? prevDt.getTakingDate() : null;
            drug.setLastTakingDate(newLastTaking);
            drugDao.updateDrug(drug);
        }
    }
    @Transaction
    public void deleteLastDrugTaking(long drugId) {
        Drug drug = drugDao.getDrug(drugId);
        Date lastTakingDate = drug.getLastTakingDate();
        if(lastTakingDate == null) {
            return;
        }
        DrugTaking dt = getDrugTakingByDate(drug.getId(), lastTakingDate);
        deleteDrugTaking(dt);
    }
}
