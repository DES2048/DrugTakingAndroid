package yae.sampleapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import yae.sampleapp.dao.DrugDao;
import yae.sampleapp.models.Drug;

@RunWith(AndroidJUnit4.class)
public class DrugDaoTest extends DaoTestBase{

    private DrugDao drugDao;

    @Before
    public void getDao() {
        drugDao = roomDb.drugDao();
    }

    @Test
    public void InsertDrug() {
        Drug drug = new Drug("cocaine");
        long id = drugDao.addDrug(drug);
        assertTrue(id > 0);
        Drug givenDrug = drugDao.getDrug((int)id);
        assertEquals(givenDrug.getName(),  drug.getName());
    }

}
