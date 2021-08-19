package com.supriya.detectface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ResultActivity extends DialogFragment {

    Button btndialogclose;
    TextView textresult;
    String text;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle saveInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_result,viewGroup,false);
        btndialogclose = view.findViewById(R.id.btndialogclose);
        textresult = view.findViewById(R.id.textresult);
        Bundle bundle = getArguments();
        text=bundle.getString(Detection.result);
        textresult.setText(text);
        btndialogclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}
