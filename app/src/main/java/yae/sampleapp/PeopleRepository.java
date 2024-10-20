package yae.sampleapp;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import yae.sampleapp.dao.PeopleDao;

public class PeopleRepository {
    private PeopleDao peopleDao;
    private LiveData<List<PeopleViewModel.People>> peoples;

    public PeopleRepository(Context ctx) {
        RoomDb db = RoomDb.getDatabase(ctx);
        peopleDao = db.peopleDao();
        peoples = peopleDao.getAll();
    }

    public LiveData<List<PeopleViewModel.People>> getAll() {
        return peoples;
    }

    public void addPeople(PeopleViewModel.People people) {
        RoomDb.databaseWriteExecutor.execute(()->{peopleDao.add(people);});
    }
}
