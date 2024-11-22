package com.time_em.profile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.time_em.ImageLoader.ImageLoader;
import com.time_em.android.R;
import com.time_em.asynctasks.AsyncResponseTimeEm;
import com.time_em.dashboard.HomeActivity;
import com.time_em.model.User;
import com.time_em.parser.Time_emJsonParser;
import com.time_em.utils.PrefUtils;
import com.time_em.utils.Utils;
import com.time_em.utils.ValueSetUtils;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileActivity extends Activity implements AsyncResponseTimeEm {


    //todo classes
    private Time_emJsonParser parser;
    private User mUser=null;
    //todo widgets
    private ImageView editButton, back;
    private CircleImageView circularImageView;
    private TextView headerText,txt_Logout;
    private EditText edt_Name,edt_Email,edt_pass,edt_Phone,edt_company,edt_userType;
    private TextView txtName;
    //todo variables
    private String Image_path=null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        initScreen();
        setUpClickListeners();
        fetchUserData();
        disableAll();

    }



    private void initScreen() {
        editButton = (ImageView) findViewById(R.id.AddButton);
        editButton.setVisibility(View.GONE);
        editButton.setImageDrawable(getResources().getDrawable(R.drawable.edit));
        back = (ImageView) findViewById(R.id.back);
        headerText=(TextView) findViewById(R.id.headerText);
        headerText.setText("Profile");
        txtName=(TextView) findViewById(R.id.txtName);
        txt_Logout=(TextView) findViewById(R.id.txt_Logout);

        edt_Name=(EditText)findViewById(R.id.edt_Name);
        edt_Email=(EditText)findViewById(R.id.edt_Email);
        edt_company=(EditText)findViewById(R.id.edt_company);
        edt_Phone=(EditText)findViewById(R.id.edt_Phone);
        edt_userType=(EditText)findViewById(R.id.edt_userType);
        circularImageView=(CircleImageView)findViewById(R.id.profile_image);

    }
    private void setUpClickListeners() {
        back.setOnClickListener(listener);
        editButton.setOnClickListener(listener);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == back){
                finish();
            }else if(v == editButton){
                EnableAll();
            }
        }
    };
    private void fetchUserData() {
        //todo get all user info
        String json = Utils.getSharedPrefs(MyProfileActivity.this, PrefUtils.KEY_USER);
        Gson gson = new Gson();
        if(json != "") {
            mUser = gson.fromJson(json, User.class);
        }

        if(mUser!=null) {
            ValueSetUtils.setValueTextView(txtName,  mUser.getFirstName() + " " + mUser.getLastName());
            String fullName=mUser.getFullName();
            ValueSetUtils.setValueEditText(edt_Name, fullName);
            String Email=mUser.getEmail();
            ValueSetUtils.setValueEditText(edt_Email,Email);
            String PhoneNumber=mUser.getPhoneNumber();
            ValueSetUtils.setValueEditText(edt_Phone,PhoneNumber);

            String company=PrefUtils.getStringPreference(getApplicationContext(),PrefUtils.KEY_COMPANY_NAME);
            ValueSetUtils.setValueEditText(edt_company,company);

            String userType=mUser.getUserType();
            ValueSetUtils.setValueEditText(edt_userType,userType);

        }


        // edt_Name.setText(mUser.getFullName());
        // txtName.setText(mUser.getFirstName()+" "+mUser.getLastName());
          /* if(mUser.getEmail()!=null && !mUser.getEmail().equalsIgnoreCase("null")) {
            edt_Email.setText(mUser.getEmail());
        }
        else {
            edt_Email.setText("");
        }*/
        /*if(mUser.getPhoneNumber()!=null && !mUser.getPhoneNumber().equalsIgnoreCase("null")) {
            edt_Phone.setText(mUser.getPhoneNumber());
        }
        else {
            edt_Phone.setText("");
        }*/

        Image_path=null;

        int loader = R.drawable.user_profile;
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(Image_path, loader, circularImageView);


    }
    private void disableAll()
    {
        edt_Name.setEnabled(false);
        changeColor(edt_Name,false);

        edt_Email.setEnabled(false);
        changeColor(edt_Email,false);

        edt_company.setEnabled(false);
        changeColor(edt_company,false);

        edt_Phone.setEnabled(false);
        changeColor(edt_Phone,false);

        edt_userType.setEnabled(false);
        changeColor(edt_userType,false);
        }
    private void EnableAll()
    {
        edt_Name.setEnabled(true);
        changeColor(edt_Name,true);

        edt_Email.setEnabled(true);
        changeColor(edt_Email,true);

        edt_company.setEnabled(true);
        changeColor(edt_company,true);

        edt_Phone.setEnabled(true);
        changeColor(edt_Phone,true);

        edt_userType.setEnabled(true);
        changeColor(edt_userType,true);
        }
    private void changeColor(EditText edt,boolean color)
    {
        if(color){
            edt.setTextColor(getResources().getColor(R.color.black));
        }else {
            edt.setTextColor(getResources().getColor(R.color.dullTextColor));
        }
    }
    @Override
    public void processFinish(String output, String methodName) {

    }

}