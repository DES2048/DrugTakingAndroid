package yae.sampleapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

public class PeopleViewModel extends AndroidViewModel {
    private PeopleRepository peopleRepo;
    private final LiveData<List<People>> peoples;
    public PeopleViewModel(@NonNull Application application) {
        super(application);
        peopleRepo = new PeopleRepository(application.getBaseContext());
        peoples = peopleRepo.getAll();
    }

    public LiveData<List<People>> getPeoples() {
        return peoples;
    }

    public void addPeople(People people) {
        peopleRepo.addPeople(people);
    }

    @Entity(tableName = "peoples")
    public static class People {
        @PrimaryKey(autoGenerate = true)
        private int id;
        @NonNull
        private String name;

        public People(@NonNull String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
