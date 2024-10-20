package yae.sampleapp.activites;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import yae.sampleapp.DrugsViewModel;
import yae.sampleapp.R;
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
        FloatingActionButton fabAddPeople = findViewById(R.id.fabAddPeople);
        fabAddPeople.setOnClickListener((v)->{
            showAddEditDrugDialog(null);
        });

        // set view model
        drugsViewModel = new ViewModelProvider(this).get(DrugsViewModel.class);

        initDrugsListView();

    }

}