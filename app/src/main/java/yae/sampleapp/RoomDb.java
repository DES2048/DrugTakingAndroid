package yae.sampleapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yae.sampleapp.dao.DrugDao;
import yae.sampleapp.dao.DrugTakingDao;
import yae.sampleapp.dao.PeopleDao;
import yae.sampleapp.models.Drug;
import yae.sampleapp.models.DrugTaking;

@Database(
        entities = {PeopleViewModel.People.class, Drug.class, DrugTaking.class},
        version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RoomDb  extends RoomDatabase {

    public abstract PeopleDao peopleDao();
    public abstract DrugDao drugDao();
    public abstract DrugTakingDao drugTakingDao();
    private static volatile RoomDb INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RoomDb.class, "people_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
