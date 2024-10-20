package yae.sampleapp;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DaoTestBase {

    protected RoomDb roomDb;

    @Before
    public void getDb() {
        Context ctx = ApplicationProvider.getApplicationContext();
        roomDb = Room.inMemoryDatabaseBuilder(ctx, RoomDb.class).build();
    }

    @After
    public void cleanDb() {
        roomDb.clearAllTables();
        roomDb.close();
    }

}
