package yae.sampleapp.activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import yae.sampleapp.DrugsViewModel;
import yae.sampleapp.R;
import yae.sampleapp.RoomDb;
import yae.sampleapp.adapters.DrugDiff;
import yae.sampleapp.adapters.DrugsAdapter;
import yae.sampleapp.models.Drug;
import yae.sampleapp.models.DrugTaking;

public class MainActivity extends AppCompatActivity {

    private RecyclerView drugsRC;
    private DrugsViewModel drugsViewModel;
    private DrugsAdapter drugsAdapter;

    private void showAddEditDrugDialog(Drug drug) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View addDrugView = getLayoutInflater().inflate(R.layout.add_drug, null);
        EditText editDrugName = addDrugView.findViewById(R.id.editDrugName);
        if (drug !=null) {
            editDrugName.setText(drug.getName());
        }
        int resButtonText = drug != null ? R.string.save_text : R.string.add_text;
        builder.setView(addDrugView)
                .setPositiveButton(resButtonText, (dialogInterface, i) -> {
                    String editedName = editDrugName.getText().toString().trim();
                    // validate
                    if (editedName.isEmpty()) {
                        return;
                    }
                    // edit
                    if (drug != null) {

                        if (!editedName.equals(drug.getName())) {
                            Drug editedDrug = new Drug(editedName);
                            editedDrug.setId(drug.getId());
                            drugsViewModel.updateDrug(editedDrug);
                        }
                    } else {
                        drugsViewModel.addDrug(new Drug(editedName));
                    }
                });
        builder.show();
    }

    private void doConfirm(String title, String message, View.OnClickListener posListener){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Да", (dialogInterface, i) -> posListener.onClick(null))
                .setNegativeButton("Нет", (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }
    protected void initDrugsListView() {
        // Drugs recycler view set
        // get recycler view
        drugsRC = findViewById(R.id.catsList);
        // set layout
        drugsRC.setLayoutManager(new LinearLayoutManager(this));
        // set adapter
        drugsAdapter = new DrugsAdapter(new DrugDiff());
        drugsAdapter.setOnButtonTakeDrugListener((v, drug) -> {
            DrugTaking dt = new DrugTaking(drug.getId(), new Date());
            drugsViewModel.addDrugTaking(dt);
        });
        //set menu
        drugsAdapter.setOnButtonDrugMenuClickListener((v, drug) -> {
            PopupMenu menu = new PopupMenu(this, v);
            menu.inflate(R.menu.drug_item_menu);
            menu.setGravity(Gravity.END);
            menu.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.drugItemMenuDelete) {
                    doConfirm("Вы уверены?",
                            "Это действие удалит лекарство и всю историю приемов. Продолжить?",
                            view -> drugsViewModel.deleteDrug(drug));

                    /*Toast.makeText(this, "DELETE for name " + drug.getName(), Toast.LENGTH_LONG)
                            .show(); */
                } else if (itemId == R.id.drugItemMenuEdit) {
                    showAddEditDrugDialog(drug);
                } else if (itemId == R.id.drugItemMenuDeleteLastTaking) {
                    doConfirm("Вы уверены",
                            "Удалить последний прием?",
                            view -> drugsViewModel.deleteLastDrugTaking(drug.getId()));
                }
                return true;
            });
            menu.show();
        });
        // start observation
        drugsViewModel.getDrugs().observe(this, drugs -> {
            // Update the cached copy of the words in the adapter.
            drugsAdapter.submitList(drugs);
        });
        drugsRC.setAdapter(drugsAdapter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup app bar
        var appBar = getSupportActionBar();

        FloatingActionButton fabAddPeople = findViewById(R.id.fabAddPeople);
        fabAddPeople.setOnClickListener((v)->{
            showAddEditDrugDialog(null);
        });

        // set view model
        drugsViewModel = new ViewModelProvider(this).get(DrugsViewModel.class);

        initDrugsListView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return switch (item.getItemId()) {
            case R.id.appMenuBackup -> {
                Toast.makeText(getApplicationContext(), "Backup...", Toast.LENGTH_LONG).show();
                backupDb();
                yield true;
            }
            case R.id.appMenuRestore -> {
                Toast.makeText(getApplicationContext(), "Restore...", Toast.LENGTH_LONG).show();
                restoreDb();
                yield true;
            }
            default -> super.onOptionsItemSelected(item);
        };
    }

    private void backupDb() {
        // get database
        var appContext = getApplicationContext();
        var dbFile = appContext.getDatabasePath("people_db").getAbsoluteFile();
        // close db in order to flush wal
        RoomDb.getDatabase(getApplicationContext()).getOpenHelper().close();

        var backupFile = new File(getExternalFilesDir(null), "db_backup");

        try (var inputStream = new FileInputStream(dbFile)) {
            try (var os = new FileOutputStream(backupFile, false)) {
                byte[] buffer = new byte[1024];
                int lengthRead;
                while ((lengthRead = inputStream.read(buffer)) > 0) {
                    os.write(buffer, 0, lengthRead);
                    os.flush();
                }
                Toast.makeText(appContext, "Backup completed", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);

            // write to external app dir
        }
    }private void restoreDb() {
        // get database
        var appContext = getApplicationContext();
        var dbFile = appContext.getDatabasePath("people_db").getAbsoluteFile();
        // close db in order to flush wal
        RoomDb.getDatabase(getApplicationContext()).getOpenHelper().close();

        var backupFile = new File(getExternalFilesDir(null), "db_backup");

        try (var inputStream = new FileInputStream(backupFile)) {
            try (var os = new FileOutputStream(dbFile, false)) {
                byte[] buffer = new byte[1024];
                int lengthRead;
                while ((lengthRead = inputStream.read(buffer)) > 0) {
                    os.write(buffer, 0, lengthRead);
                    os.flush();
                }
                Toast.makeText(appContext, "Restore completed", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}