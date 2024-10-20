package yae.sampleapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import yae.sampleapp.R;

public class AddDrugActivity extends AppCompatActivity {

    private EditText editPeopleName;
    private Button btnAddPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);

        editPeopleName = findViewById(R.id.editDrugName);
        btnAddPeople = findViewById(R.id.btnAddDrug);
        btnAddPeople.setOnClickListener((view)->{
            Intent reply = new Intent();
            if(TextUtils.isEmpty(editPeopleName.getText())) {
                setResult(RESULT_CANCELED, reply);
            } else {
                String name = editPeopleName.getText().toString();
                reply.putExtra("EXTRA_DRUG_NAME", name);
                setResult(RESULT_OK, reply);
            }
            finish();
        });


    }
}