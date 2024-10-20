package yae.sampleapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.Date;

import yae.sampleapp.dao.DrugDao;
import yae.sampleapp.dao.DrugTakingDao;
import yae.sampleapp.models.Drug;
import yae.sampleapp.models.DrugTaking;

@RunWith(AndroidJUnit4.class)
public class DrugTakingDaoTest extends DaoTestBase {

    private DrugTakingDao drugTakingDao;
    private DrugDao drugDao;
    private Drug testDrug;
    @Before
    public void getDao() {
        drugDao = roomDb.drugDao();
        drugTakingDao = roomDb.drugTakingDao();
        long drugId = drugDao.addDrug(new Drug("test drug"));
        testDrug = drugDao.getDrug(drugId);
    }

    @Test
    public void testAddDrugTaking() {


        Date dateNow = new Date();
        DrugTaking dt = new DrugTaking(testDrug.getId(), dateNow);
        DrugTaking givenDt = drugTakingDao.addDrugTaking(dt);

        // ensure drug taking creating
        assertEquals(dt.getDrugId(), givenDt.getDrugId());
        assertEquals(dt.getTakingDate(), givenDt.getTakingDate());

        // ensure drug last taken date is set
        Drug givenDrug = drugDao.getDrug(testDrug.getId());
        assertEquals( dateNow, givenDrug.getLastTakingDate());

        // add taking in the past
        Date dateInThePast = new Date(dateNow.getTime() - 10000);
        DrugTaking pastDt = new DrugTaking(testDrug.getId(), dateInThePast);

        // check last taken date doesnt change
        assertEquals( dateNow, givenDrug.getLastTakingDate());
    }

    @Test
    public void testGetDrugTakingBelow() {
        DrugTaking[] dts = new DrugTaking[3];
        long now = new Date().getTime();

        for (int i=0; i< dts.length; i++) {
            DrugTaking dt = new DrugTaking(testDrug.getId(), new Date(now+(i*10000)));
            dts[i] = drugTakingDao.addDrugTaking(dt);

        }
        // test below first drug taking
        DrugTaking givenDt = drugTakingDao.getDrugTakingBelow(testDrug.getId(),
                dts[0].getTakingDate());
        // must be null
        assertNull(givenDt);

        // test below last drug taking
        givenDt = drugTakingDao.getDrugTakingBelow(testDrug.getId(),
                dts[dts.length -1].getTakingDate());

        // must be
        assertNotNull(givenDt);
        // given date must be under last taking date
        assertTrue(dts[dts.length-1].getTakingDate().compareTo(givenDt.getTakingDate()) > 0);
        // given date must be equal to prev element from last
        assertTrue(givenDt.getTakingDate().equals(dts[dts.length-2].getTakingDate()));
    }
    @Test
    public void getPrevDrugTaking() {

        // test no prev
        long drugId = testDrug.getId();
        DrugTaking firstDt = new DrugTaking(drugId, new Date());
        drugTakingDao.addDrugTaking(firstDt);

        DrugTaking givenDt = drugTakingDao.getPrevDrugTaking(firstDt);
        assertNull(givenDt);

        // test prev exists
        DrugTaking secDt = new DrugTaking(drugId, new Date());
        drugTakingDao.addDrugTaking(secDt);

        DrugTaking thirdDt = new DrugTaking(drugId, new Date());
        drugTakingDao.addDrugTaking(thirdDt);

        givenDt = drugTakingDao.getPrevDrugTaking(thirdDt);
        assertNotNull(givenDt);
        assertTrue(givenDt.getTakingDate().equals(secDt.getTakingDate()));
        assertTrue(givenDt.getTakingDate().compareTo(thirdDt.getTakingDate()) < 0);
    }

    @Test
    public void testDeleteDrugTaking() {

        // prepare
        long drugId = testDrug.getId();
        DrugTaking firstDt = new DrugTaking(drugId, new Date());
        drugTakingDao.addDrugTaking(firstDt);
        DrugTaking secDt = new DrugTaking(drugId, new Date());
        drugTakingDao.addDrugTaking(secDt);

        // delete last
        drugTakingDao.deleteDrugTaking(secDt);
        // test lastTaking date changed to first drug taking
        Drug givenDrug = drugDao.getDrug(drugId);
        assertEquals(firstDt.getTakingDate(), givenDrug.getLastTakingDate());

        // delete first
        drugTakingDao.deleteDrugTaking(firstDt);
        // last taking date must be set to null
        givenDrug = drugDao.getDrug(drugId);
        assertNull(givenDrug.getLastTakingDate());
    }
}
