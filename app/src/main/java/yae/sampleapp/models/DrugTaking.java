package yae.sampleapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "drug_takings", foreignKeys = {
        @ForeignKey(entity = Drug.class, parentColumns = "id",
                childColumns = "drug_id", onDelete = ForeignKey.CASCADE)
})
public class DrugTaking {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "drug_id", index = true)
    private long drugId;
    @NonNull
    @ColumnInfo(name="taking_date")
    private Date takingDate;

    public DrugTaking(long drugId, @NonNull Date takingDate) {
        this.drugId = drugId;
        this.takingDate = takingDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDrugId() {
        return drugId;
    }

    public void setDrugId(long drugId) {
        this.drugId = drugId;
    }

    @NonNull
    public Date getTakingDate() {
        return takingDate;
    }

    public void setTakingDate(@NonNull Date takingDate) {
        this.takingDate = takingDate;
    }
}
