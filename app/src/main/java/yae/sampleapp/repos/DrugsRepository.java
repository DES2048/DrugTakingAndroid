package yae.sampleapp.repos;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import yae.sampleapp.RoomDb;
import yae.sampleapp.dao.DrugDao;
import yae.sampleapp.dao.DrugTakingDao;
import yae.sampleapp.models.Drug;
import yae.sampleapp.models.DrugTaking;

public class DrugsRepository {

    private DrugDao drugDao;
    private DrugTakingDao drugTakingDao;

    private LiveData<List<Drug>> drugs;
    public DrugsRepository(Context ctx) {
        RoomDb db = RoomDb.getDatabase(ctx);
        drugDao = db.drugDao();
        drugTakingDao = db.drugTakingDao();
        drugs = drugDao.getDrugs();
    }

    public void addDrug(Drug drug) {
        RoomDb.databaseWriteExecutor.execute(()->drugDao.addDrug(drug));
    }
    public void addDrugTaking(DrugTaking drugTaking) {
        RoomDb.databaseWriteExecutor.execute(()->drugTakingDao.addDrugTaking(drugTaking));
    }

    public void updateDrug(Drug drug) {
        RoomDb.databaseWriteExecutor.execute(()->drugDao.updateDrug(drug));
    }
    public void deleteDrug(Drug drug) {
        RoomDb.databaseWriteExecutor.execute(()->drugDao.deleteDrug(drug));
    }
    public void deleteLastDrugTaking(long drugId) {
        RoomDb.databaseWriteExecutor.execute(() -> drugTakingDao.deleteLastDrugTaking(drugId));
    }
    public LiveData<List<Drug>> getDrugs() {
        return drugs;
    }
}
