package com.windwarriors.appetite;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.windwarriors.appetite.service.SharedPreferencesService;

import org.w3c.dom.Text;

import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_SEARCH_RANGE;

/**
 * Created by Owner on 3/9/2018.
 */

public class BusinessListRangeDialog extends AppCompatDialogFragment {
    private SeekBar rangeSeekBar;
    private TextView tvRangeValue;
    private int selectedRange = 1;
    private SharedPreferencesService spService;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        spService = new SharedPreferencesService(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.range_dialog, null);

        builder.setView(view)
                .setTitle("Search range (km)")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        spService.saveToSharedPreferences(SHARED_PREFERENCES_SEARCH_RANGE, String.valueOf(selectedRange));
                    }
                });

        tvRangeValue = view.findViewById(R.id.tvRangeValue);
        rangeSeekBar = view.findViewById(R.id.range_seekbar);

        rangeSeekBar.setMax(40);

        String spSearchRange = spService.getFromSharedPreferences(SHARED_PREFERENCES_SEARCH_RANGE);
        rangeSeekBar.setProgress(Integer.parseInt(spSearchRange.equals("") ? "1" : spSearchRange));
        setValueLabel(Integer.parseInt(spSearchRange.equals("") ? "1" : spSearchRange));

        rangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i == 0 ? 1 : i;

                setValueLabel(i);
                selectedRange = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return builder.create();
    }

    protected void setValueLabel(int valueRange){
        tvRangeValue.setText(valueRange + " km");
    }
}