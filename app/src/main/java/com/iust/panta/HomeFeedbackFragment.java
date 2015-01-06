package com.iust.panta;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HomeFeedbackFragment extends Fragment {

    private Button buttonSend;
    private EditText textTo;
    private EditText editTextSubject;
    private EditText Message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home_feedback, container, false);


        textTo = (EditText) rootView.findViewById(R.id.editTextTo);
        editTextSubject = (EditText) rootView.findViewById(R.id.editTextSubject);
        Message = (EditText) rootView.findViewById(R.id.messageInfo);
        buttonSend = (Button) rootView.findViewById(R.id.btnSubmit);
        textTo.setText("panta_app@yahoo.com");


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set null error
                textTo.setError(null);
                editTextSubject.setError(null);
                Message.setError(null);

                boolean has_error = false;
                View focus_view;

                String to = textTo.getText().toString();
                String subject = editTextSubject.getText().toString();
                String message = Message.getText().toString();


                if (TextUtils.isEmpty(subject)) {
                    editTextSubject.setError("لطفا موضوع نظر و انتقاد خود را مشخص کنید");
                    focus_view = editTextSubject;
                    focus_view.requestFocus();
                    has_error = true;
                }


                if (TextUtils.isEmpty(message)) {
                    Message.setError("لطفا توضیحاتی ارائه دهید");
                    focus_view = Message;
                    focus_view.requestFocus();
                    has_error = true;
                }
                write(has_error);


                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));


            }


        });


        return rootView;
    }

    public void write(boolean has_error) {
        if (!has_error) {
            Intent intent = new Intent(getActivity(), Home.class);
            getActivity().finish();
            startActivity(intent);
        }

    }
}