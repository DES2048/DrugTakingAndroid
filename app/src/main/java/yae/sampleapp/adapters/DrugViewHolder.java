package yae.sampleapp.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import yae.sampleapp.R;
import yae.sampleapp.models.Drug;
import yae.sampleapp.util.DateHelper;

public class DrugViewHolder extends RecyclerView.ViewHolder {
    private final TextView textDrugName;
    private final TextView textLastTaking;
    private final Button btnTakeDrug;
    private final Button btnDrugItemMenu;

    private boolean takingDateIsPretty = true;
    @FunctionalInterface
    public interface onButtonClickListener {
        void click(View v, Drug drug);
    }

    private onButtonClickListener onTakeDrugClickListener;

    private onButtonClickListener onItemMenuClickListener;

    protected void handleOnTakeDrugClick() {
        if (onTakeDrugClickListener != null) {
            onTakeDrugClickListener.click(btnTakeDrug, drug);
        }
    }
    protected void handleOnItemMenuClick() {
        if (onItemMenuClickListener != null) {
            onItemMenuClickListener.click(btnDrugItemMenu, drug);
        }
    }
    private Drug drug;
    public DrugViewHolder(@NonNull View itemView) {
        super(itemView);
        textDrugName = itemView.findViewById(R.id.textDrugName);
        textLastTaking = itemView.findViewById(R.id.textLastTaking);
        btnTakeDrug = itemView.findViewById(R.id.btnTakeDrug);
        btnDrugItemMenu = itemView.findViewById(R.id.btnDrugItemMenu);

        btnTakeDrug.setOnClickListener(view -> handleOnTakeDrugClick());

        btnDrugItemMenu.setOnClickListener(view -> handleOnItemMenuClick());

        textLastTaking.setOnClickListener(view -> toggleTakingDateFormat());
    }
    private void toggleTakingDateFormat() {
        Date lastTakingDate = drug.getLastTakingDate();
        if (lastTakingDate == null) {
            return;
        }
        String lastTakingText = takingDateIsPretty ? DateHelper.formatDate(drug.getLastTakingDate())
                : DateHelper.prettyDate(lastTakingDate);

        textLastTaking.setText(lastTakingText);
        takingDateIsPretty = !takingDateIsPretty;
    }

    public void bind(@NonNull Drug drug) {
        this.drug = drug;
        textDrugName.setText(drug.getName());
        textLastTaking.setText(
                takingDateIsPretty ? DateHelper.prettyDate(drug.getLastTakingDate())
                        : DateHelper.formatDate(drug.getLastTakingDate())
        );
    }

    public void setOnButtonTakeDrugClick(onButtonClickListener handler) {
        onTakeDrugClickListener = handler;
    }
    public void setOnItemMenuClickListener(onButtonClickListener onItemMenuClickListener) {
        this.onItemMenuClickListener = onItemMenuClickListener;
    }
}
