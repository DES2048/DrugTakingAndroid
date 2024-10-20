package yae.sampleapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "drugs")
public class Drug {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String name;

    @ColumnInfo(name = "last_taking_date")
    private Date lastTakingDate;
    public Drug(@NonNull String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Date getLastTakingDate() {
        return lastTakingDate;
    }

    public void setLastTakingDate(Date lastTakingDate) {
        this.lastTakingDate = lastTakingDate;
    }
}
