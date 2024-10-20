package yae.sampleapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import yae.sampleapp.models.Drug;
import yae.sampleapp.models.DrugTaking;
import yae.sampleapp.repos.DrugsRepository;

public class DrugsViewModel extends AndroidViewModel {

    private DrugsRepository drugRepo;
    private LiveData<List<Drug>> drugs;

    public DrugsViewModel(@NonNull Application application) {
        super(application);

        drugRepo = new DrugsRepository(application);
        drugs = drugRepo.getDrugs();
    }

    public void addDrug(Drug drug) {
        drugRepo.addDrug(drug);
    }

    public void updateDrug(Drug drug) { drugRepo.updateDrug(drug);}
    public void deleteDrug(Drug drug) { drugRepo.deleteDrug(drug);}
    public void deleteLastDrugTaking(long drugId) { drugRepo.deleteLastDrugTaking(drugId);}
    public void addDrugTaking(DrugTaking drugTaking) {
        drugRepo.addDrugTaking(drugTaking);
    }
    public LiveData<List<Drug>> getDrugs() {
        return drugs;
    }
}
