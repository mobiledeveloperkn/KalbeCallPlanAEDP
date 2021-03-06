package com.kalbe.kalbecallplanaedp.Fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalbe.kalbecallplanaedp.BL.clsActivity;
import com.kalbe.kalbecallplanaedp.BL.clsHelperBL;
import com.kalbe.kalbecallplanaedp.BL.clsMainBL;
import com.kalbe.kalbecallplanaedp.Common.VMDownloadFile;
import com.kalbe.kalbecallplanaedp.Common.clsSpinner;
import com.kalbe.kalbecallplanaedp.Common.clsToken;
import com.kalbe.kalbecallplanaedp.Common.mActivity;
import com.kalbe.kalbecallplanaedp.Common.mApotek;
import com.kalbe.kalbecallplanaedp.Common.mDokter;
import com.kalbe.kalbecallplanaedp.Common.mUserLogin;
import com.kalbe.kalbecallplanaedp.Common.mUserMappingArea;
import com.kalbe.kalbecallplanaedp.Common.tAkuisisiDetail;
import com.kalbe.kalbecallplanaedp.Common.tAkuisisiHeader;
import com.kalbe.kalbecallplanaedp.Common.tMaintenanceDetail;
import com.kalbe.kalbecallplanaedp.Common.tMaintenanceHeader;
import com.kalbe.kalbecallplanaedp.Common.tNotification;
import com.kalbe.kalbecallplanaedp.Common.tProgramVisit;
import com.kalbe.kalbecallplanaedp.Common.tProgramVisitSubActivity;
import com.kalbe.kalbecallplanaedp.Common.tRealisasiVisitPlan;
import com.kalbe.kalbecallplanaedp.Data.VolleyResponseListener;
import com.kalbe.kalbecallplanaedp.Data.clsHardCode;
import com.kalbe.kalbecallplanaedp.MainMenu;
import com.kalbe.kalbecallplanaedp.R;
import com.kalbe.kalbecallplanaedp.Repo.clsTokenRepo;
import com.kalbe.kalbecallplanaedp.Repo.mActivityRepo;
import com.kalbe.kalbecallplanaedp.Repo.mApotekRepo;
import com.kalbe.kalbecallplanaedp.Repo.mDokterRepo;
import com.kalbe.kalbecallplanaedp.Repo.mUserMappingAreaRepo;
import com.kalbe.kalbecallplanaedp.Repo.tAkuisisiDetailRepo;
import com.kalbe.kalbecallplanaedp.Repo.tAkuisisiHeaderRepo;
import com.kalbe.kalbecallplanaedp.Repo.tMaintenanceDetailRepo;
import com.kalbe.kalbecallplanaedp.Repo.tMaintenanceHeaderRepo;
import com.kalbe.kalbecallplanaedp.Repo.tNotificationRepo;
import com.kalbe.kalbecallplanaedp.Repo.tProgramVisitRepo;
import com.kalbe.kalbecallplanaedp.Repo.tProgramVisitSubActivityRepo;
import com.kalbe.kalbecallplanaedp.Repo.tRealisasiVisitPlanRepo;
import com.kalbe.kalbecallplanaedp.ResponseDataJson.createUnplan.CreateUnplan;
import com.kalbe.kalbecallplanaedp.ResponseDataJson.downloadtMappingArea.DownloadtMappingArea;
import com.kalbe.kalbecallplanaedp.Utils.IOBackPressed;
import com.kalbe.kalbecallplanaedp.Utils.LongThread;
import com.kalbe.kalbecallplanaedp.Utils.Tools;
import com.kalbe.mobiledevknlibs.ToastAndSnackBar.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dewi Oktaviani on 10/5/2018.
 */

public class FragmentAddUnplan extends Fragment implements IOBackPressed, Handler.Callback{
    View v;
    AppCompatSpinner spnArea, spnActivity, spnOutlet;
    TextView tvOultet;
    EditText etDesc, etOutlet;
    CheckBox cbOutlet;
    Button btnCreate;
    LinearLayout lnOutlet;
    public List<String> listArea = new ArrayList<>();
    public HashMap<String, String> mapArea = new HashMap<>();
    public List<String> listActivity = new ArrayList<>();
    public HashMap<String, Integer> mapActivity = new HashMap<>();
    public List<String> listOutlet = new ArrayList<>();
    public HashMap<String, String> mapOutlet = new HashMap<>();
    mActivityRepo activityRepo;
    List<mUserMappingArea> listDtArea = new ArrayList<>();
    mUserMappingAreaRepo areaRepo;
    List<mActivity> listdtActivity = new ArrayList<>();
    List<clsSpinner> listSpnArea = new ArrayList<>();
    List<clsSpinner> listSpnActivity = new ArrayList<>();
    List<clsSpinner> listSpnOutlet = new ArrayList<>();
    mDokterRepo dokterRepo;
    mApotekRepo apotekRepo;
    ArrayAdapter<String> spinnerAdapterOutlet;
    tProgramVisitSubActivityRepo visitSubActivityRepo;
    tProgramVisit visitHeader;
    tProgramVisitRepo visitRepo;
    tRealisasiVisitPlanRepo realisasiVisitPlanRepo;
    mUserLogin dtUserLogin;
    private Gson gson;
    List<clsToken> dataToken;
    clsTokenRepo tokenRepo;
    int curCount = 0;
    float totalCount;
    ThreadPoolExecutor executor;
    ProgressDialog progress;
    private String FRAG_VIEW = "Fragment view";
    private String i_View ="Fragment";
    boolean isCreateNotification = false;

    tProgramVisitSubActivity dataPlan;
    tRealisasiVisitPlan data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_unplan, container, false);
        spnArea = (AppCompatSpinner)v.findViewById(R.id.spnArea_unplan);
        spnActivity = (AppCompatSpinner)v.findViewById(R.id.spnActivity_add_unplan);
        spnOutlet = (AppCompatSpinner)v.findViewById(R.id.spn_outlet_unplan);
        tvOultet = (TextView)v.findViewById(R.id.tv_outlet_unplan);
        etDesc = (EditText)v.findViewById(R.id.et_desc_unplan);
        etOutlet = (EditText)v.findViewById(R.id.etOutlet_unplan);
        cbOutlet = (CheckBox)v.findViewById(R.id.cb_outlet_unplan);
        btnCreate = (Button)v.findViewById(R.id.button_add_unplan);
        lnOutlet = (LinearLayout)v.findViewById(R.id.ln_cb_unplan);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        areaRepo = new mUserMappingAreaRepo(getContext());
        activityRepo = new mActivityRepo(getContext());
        apotekRepo = new mApotekRepo(getContext());
        dokterRepo = new mDokterRepo(getContext());
        visitSubActivityRepo = new tProgramVisitSubActivityRepo(getContext());
        visitRepo = new tProgramVisitRepo(getContext());
        realisasiVisitPlanRepo = new tRealisasiVisitPlanRepo(getContext());
        dtUserLogin = new clsMainBL().getUserLogin(getContext());



        listArea.clear();
        listActivity.clear();
        listOutlet.clear();

        listSpnArea.clear();
        listSpnActivity.clear();
        listSpnOutlet.clear();

        listArea.add("Select One");
        listActivity.add("Select One");
        listOutlet.add("Select One");

        mapActivity.put("Select One", 0);
        mapOutlet.put("Select One", "-");

        spnActivity.setEnabled(false);
        spnOutlet.setEnabled(false);
        cbOutlet.setEnabled(false);

        lnOutlet.setVisibility(View.GONE);
        spnOutlet.setVisibility(View.GONE);

        try {
            listDtArea = (List<mUserMappingArea>) areaRepo.findAll();
            if (listDtArea!=null&&listDtArea.size()>0){
                for (int i = 0; i <listDtArea.size(); i++){
//                    clsSpinner data = new clsSpinner(listDtArea.get(i).getTxtKecamatanID(), listDtArea.get(i).getTxtKecamatanID());
//                    data.setTxtKey(listDtArea.get(i).getTxtKecamatanID());
//                    data.setTxtValue(listDtArea.get(i).getTxtKecamatanID());
//                    listSpnArea.add(data);
                    listArea.add(listDtArea.get(i).getTxtKecamatanID());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Initializing an ArrayAdapter with initial text like select one
        final ArrayAdapter<String> spinnerAdapterArea = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_dropdown_item, listArea){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.green_200));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.green_400));
                }
                return view;
            }
        };
        spnArea.setAdapter(spinnerAdapterArea);

        // attaching data adapter to spinner
        spnArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                if (tv!=null){
                    if(i == 0){
                        // Set the hint text color gray
                        tv.setTextColor(getResources().getColor(R.color.green_200));
                    }
                    else {
                        tv.setTextColor(getResources().getColor(R.color.green_400));
                    }
                }

//                tv.setTextColor(getResources().getColor(R.color.green_300));
                if (i!=0){
                    onAreaSelected();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                new ToastCustom().showToasty(getActivity(),"Please select Area",4);
                // put code here
            }
        });

        // Initializing an ArrayAdapter with initial text like select one
        final ArrayAdapter<String> spinnerAdapterActivity = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_dropdown_item, listActivity){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.green_200));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.green_400));
                }
                return view;
            }
        };
        spnActivity.setAdapter(spinnerAdapterActivity);

        // attaching data adapter to spinner
        spnActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                if (tv!=null){
                    if(i == 0){
                        // Set the hint text color gray
                        tv.setTextColor(getResources().getColor(R.color.green_200));
                    }
                    else {
                        tv.setTextColor(getResources().getColor(R.color.green_400));
                    }
                }
//                tv.setTextColor(getResources().getColor(R.color.green_300));
                if (i!=0){
                    onActivitySelected(mapActivity.get(spnActivity.getSelectedItem().toString()), spnArea.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                new ToastCustom().showToasty(getActivity(),"Please select Activity",4);
                // put code here
            }
        });

        // Initializing an ArrayAdapter with initial text like select one
         spinnerAdapterOutlet = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_dropdown_item, listOutlet){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.green_200));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.green_400));
                }
                return view;
            }
        };
        spnOutlet.setAdapter(spinnerAdapterOutlet);

        // attaching data adapter to spinner
        spnOutlet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tv = (TextView)view;
                if (tv!=null){
                    if(i == 0){
                        // Set the hint text color gray
                        tv.setTextColor(getResources().getColor(R.color.green_200));
                    }
                    else {
                        tv.setTextColor(getResources().getColor(R.color.green_400));
                    }
                }
//                if (tv!=null)
//                    tv.setTextColor(getResources().getColor(R.color.green_300));
//                    onActivitySelected(mapActivity.get(spnActivity.getSelectedItem().toString()), spnArea.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                new ToastCustom().showToasty(getActivity(),"Please select Activity",4);
                // put code here
            }
        });
        cbOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbOutlet.isChecked()==true){
                    spnOutlet.setVisibility(View.GONE);
                    etOutlet.setVisibility(View.VISIBLE);
                }else {
                    etOutlet.setVisibility(View.GONE);
                    spnOutlet.setVisibility(View.VISIBLE);
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are You sure?");

                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
//                        long id = spnArea.getSelectedItemId();
//                        int position = spnArea.getSelectedItemPosition();
//                        String obj = spnArea.getSelectedItem().toString();
//                        String obj2 = spnArea.getItemAtPosition(0).toString();

                        saveData();
//                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
        return v;
    }

    private void onAreaSelected(){
        try {
            listdtActivity = (List<mActivity>) activityRepo.findAll();
            if (listdtActivity!=null&&listdtActivity.size()>0){
                for (int i = 0; i <listdtActivity.size(); i++){
                    listActivity.add(listdtActivity.get(i).getTxtName());
                    mapActivity.put(listdtActivity.get(i).getTxtName(), listdtActivity.get(i).getIntActivityId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spnActivity.setEnabled(true);
    }

    private void onActivitySelected(int intActivityId, String txtAreaId){
        if (intActivityId==1){
            listOutlet.clear();
            mapOutlet.clear();
            listOutlet.add("Select One");
            mapOutlet.put("Select One", "-");
            tvOultet.setText("DOCTOR NAME");
            try {
                List<mDokter> dokterList = ( List<mDokter>)dokterRepo.findAll();
                if (dokterList!=null&&dokterList.size()>0){
                    for (int i = 0; i < dokterList.size(); i++){
                        if (dokterList.get(i).getTxtLastName()!=null){
                            listOutlet.add(dokterList.get(i).getTxtFirstName() + " " + dokterList.get(i).getTxtLastName());
                            mapOutlet.put(dokterList.get(i).getTxtFirstName() + " " + dokterList.get(i).getTxtLastName(), dokterList.get(i).getTxtId());
                        }else {
                            listOutlet.add(dokterList.get(i).getTxtFirstName());
                            mapOutlet.put(dokterList.get(i).getTxtFirstName(), dokterList.get(i).getTxtId());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lnOutlet.setVisibility(View.VISIBLE);
            cbOutlet.setEnabled(true);
            spnOutlet.setVisibility(View.VISIBLE);
            spnOutlet.setEnabled(true);
            spinnerAdapterOutlet.notifyDataSetChanged();
            spnOutlet.setAdapter(spinnerAdapterOutlet);
        }else if (intActivityId==2){
            tvOultet.setText("PHARMACY NAME");
            listOutlet.clear();
            mapOutlet.clear();
            listOutlet.add("Select One");
            mapOutlet.put("Select One", "-");
            try {
                List<mApotek> apotekList = (List<mApotek>) apotekRepo.findAll();
                if (apotekList!=null&&apotekList.size()>0){
                    for (mApotek data : apotekList){
                        listOutlet.add(data.getTxtName());
                        mapOutlet.put(data.getTxtName(), data.getTxtCode());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lnOutlet.setVisibility(View.VISIBLE);
            cbOutlet.setEnabled(true);
            spnOutlet.setVisibility(View.VISIBLE);
            spnOutlet.setEnabled(true);
            spinnerAdapterOutlet.notifyDataSetChanged();
            spnOutlet.setAdapter(spinnerAdapterOutlet);
        }else {
            lnOutlet.setVisibility(View.GONE);
            cbOutlet.setEnabled(false);
            spnOutlet.setVisibility(View.GONE);
            spnOutlet.setEnabled(false);
            etOutlet.setVisibility(View.GONE);
        }
        spnArea.setEnabled(false);
    }

    private void saveData(){
        boolean valid = true;
        String msg = "";
        if (spnArea.getSelectedItem().toString().equals("Select One")){
            valid = false;
            msg = "Please select Area";
        }else if (spnActivity.getSelectedItem().toString().equals("Select One")){
            valid = false;
            msg = "Please select Activity";
        }else if (mapActivity.get(spnActivity.getSelectedItem())==1){
            if (cbOutlet.isChecked()){
                if (etOutlet.getText().toString().equals("")){
                    valid = false;
                    msg = "Please fill name of Doctor";
                }
            }else {
                if (spnOutlet.getSelectedItem().toString().equals("Select One")){
                    valid = false;
                    msg = "Please select Doctor";
                }
            }
        }else if (mapActivity.get(spnActivity.getSelectedItem())==2){
            if (cbOutlet.isChecked()){
                if (etOutlet.getText().toString().equals("")){
                    valid = false;
                    msg = "Please fill name of Pharmacy";
                }
            }else {
                if (spnOutlet.getSelectedItem().toString().equals("Select One")){
                    valid = false;
                    msg = "Please select Pharmacy";
                }
            }
        }

        if (valid){
            try {

                DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dateLongFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
//            visitHeader = (tProgramVisit) visitRepo.findAll().get(0);
                    if (visitRepo.isExistProgramVisit(getContext())){
                        visitHeader = (tProgramVisit) visitRepo.getProgramVisitActive(getContext());
                    }else {
                        tProgramVisit dt = new tProgramVisit();
                        dt = new tProgramVisit();
                        dt.setTxtProgramVisitId(new clsActivity().GenerateGuid());
                        dt.setIntUserId(dtUserLogin.getIntUserID());
                        dt.setIntRoleId(dtUserLogin.getIntRoleID());
                        dt.setTxtNotes("(" + dateLongFormat.format(dateTimeFormat.parse(dtUserLogin.getDtLogIn())) + " - " + dtUserLogin.getTxtNick() + ") Create");
                        dt.setIntType(new clsHardCode().UnPlan);
                        dt.setIntStatus(0);
                        dt.setDtStart(dtUserLogin.getDtLogIn());
                        dt.setDtEnd(dtUserLogin.getDtLogIn());
                        dt.setIntFlagPush(new clsHardCode().Sync);
//                        visitRepo.createOrUpdate(dt);
                        visitHeader = dt;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


//                tProgramVisitSubActivity dataPlan = new tProgramVisitSubActivity();
                dataPlan = new tProgramVisitSubActivity();
                dataPlan.setTxtProgramVisitSubActivityId(new clsActivity().GenerateGuid());
                if (mapActivity.get(spnActivity.getSelectedItem()) == 1) {
                    if (cbOutlet.isChecked()){
                        dataPlan.setTxtDokterName(etOutlet.getText().toString());
                        dataPlan.setTxtDokterId(etOutlet.getText().toString());
                    }else {
                        dataPlan.setTxtDokterId(mapOutlet.get(spnOutlet.getSelectedItem()));
                        dataPlan.setTxtDokterName(spnOutlet.getSelectedItem().toString());
                    }

                }else if (mapActivity.get(spnActivity.getSelectedItem())==2){
                    if (cbOutlet.isChecked()){
                        dataPlan.setTxtApotekId(etOutlet.getText().toString());
                        dataPlan.setTxtApotekName(etOutlet.getText().toString());
                    }else {
                        dataPlan.setTxtApotekId(mapOutlet.get(spnOutlet.getSelectedItem()));
                        dataPlan.setTxtApotekName(spnOutlet.getSelectedItem().toString());
                    }
                }
                dataPlan.setIntType(new clsHardCode().UnPlan);
                dataPlan.setTxtAreaName(spnArea.getSelectedItem().toString());
                dataPlan.setTxtNotes(etDesc.getText().toString());
                dataPlan.setTxtProgramVisitId(visitHeader.getTxtProgramVisitId());
                dataPlan.setIntActivityId(mapActivity.get(spnActivity.getSelectedItem()));
                dataPlan.setTxtAreaId(spnArea.getSelectedItem().toString());
                dataPlan.setDtStart(dtUserLogin.getDtLogIn());
                dataPlan.setDtEnd(dtUserLogin.getDtLogIn());
                dataPlan.setIntFlagPush(new clsHardCode().Sync);
//                visitSubActivityRepo.createOrUpdate(dataPlan);

//                tRealisasiVisitPlan data = new tRealisasiVisitPlan();
                data = new tRealisasiVisitPlan();
                data.setTxtRealisasiVisitId(new clsActivity().GenerateGuid());
                data.setTxtProgramVisitSubActivityId(dataPlan.getTxtProgramVisitSubActivityId());
                data.setIntUserId(dtUserLogin.getIntUserID());
                data.setIntRoleID(dtUserLogin.getIntRoleID());
                if (mapActivity.get(spnActivity.getSelectedItem()) == 1) {
                    if (cbOutlet.isChecked()){
                        data.setTxtDokterName(etOutlet.getText().toString());
                        data.setTxtDokterId(etOutlet.getText().toString());
                    }else {
                        data.setTxtDokterId(mapOutlet.get(spnOutlet.getSelectedItem()));
                        data.setTxtDokterName(spnOutlet.getSelectedItem().toString());
                    }

                }else if (mapActivity.get(spnActivity.getSelectedItem())==2){
                    if (cbOutlet.isChecked()){
                        data.setTxtApotekId(etOutlet.getText().toString());
                        data.setTxtApotekName(etOutlet.getText().toString());
                    }else {
                        data.setTxtApotekId(mapOutlet.get(spnOutlet.getSelectedItem()));
                        data.setTxtApotekName(spnOutlet.getSelectedItem().toString());
                    }
                }
                data.setDtCheckIn("");
                data.setDtCheckOut("");
                data.setDtDateRealisasi("");
                data.setDtDatePlan(dateFormat.format(dateTimeFormat.parse(dtUserLogin.getDtLogIn())));///tanggal login
                data.setIntNumberRealisasi(null); //generate number
                data.setTxtAcc("");
                data.setTxtLat("");
                data.setTxtLong("");
                data.setTxtImgName1("");
                data.setBlobImg1(null);
                data.setTxtImgName2("");
                data.setBlobImg2(null);
                data.setIntStatusRealisasi(new clsHardCode().VisitPlan);
                data.setIntFlagPush(new clsHardCode().Sync);
//                realisasiVisitPlanRepo.createOrUpdate(data);

                createUnplan(visitHeader, dataPlan, data);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            new ToastCustom().showToasty(getContext(), msg, 4);
        }
    }

    private JSONObject ParamCreateUnplan(tProgramVisit header, tProgramVisitSubActivity visitPlan, tRealisasiVisitPlan dtRealisasi){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonHeader = new JSONObject();
        JSONObject jsonVisit = new JSONObject();
        JSONObject jsonRealisasi = new JSONObject();

        try {
            tProgramVisit dataHeader = new tProgramVisit();
            jsonHeader.put(dataHeader.Property_txtProgramVisitId, String.valueOf(header.getTxtProgramVisitId()));
            jsonHeader.put(dataHeader.Property_intUserId, String.valueOf(header.getIntUserId()));
            jsonHeader.put(dataHeader.Property_intRoleId, String.valueOf(header.getIntRoleId()));
            jsonHeader.put(dataHeader.Property_txtNotes, String.valueOf(header.getTxtNotes()));
            jsonHeader.put(dataHeader.Property_intType, String.valueOf(header.getIntType()));
            jsonHeader.put(dataHeader.Property_intStatus, String.valueOf(header.getIntStatus()));
            jsonHeader.put(dataHeader.Property_dtLogin, dtUserLogin.getDtLogIn());
            jsonHeader.put(dataHeader.Property_intFlagPush, String.valueOf(header.getIntFlagPush()));

            tProgramVisitSubActivity dataVisit = new tProgramVisitSubActivity();
            jsonVisit.put(dataVisit.Property_txtProgramVisitSubActivityId, String.valueOf(visitPlan.getTxtProgramVisitSubActivityId()));
            jsonVisit.put(dataVisit.Property_txtApotekName, String.valueOf(visitPlan.getTxtApotekName()));
            jsonVisit.put(dataVisit.Property_intType, String.valueOf(visitPlan.getIntType()));
            jsonVisit.put(dataVisit.Property_txtAreaName, String.valueOf(visitPlan.getTxtAreaName()));
            jsonVisit.put(dataVisit.Property_txtDokterId, String.valueOf(visitPlan.getTxtDokterId()));
            jsonVisit.put(dataVisit.Property_txtNotes, String.valueOf(visitPlan.getTxtNotes()));
            jsonVisit.put(dataVisit.Property_txtDokterName, String.valueOf(visitPlan.getTxtDokterName()));
            jsonVisit.put(dataVisit.Property_txtProgramVisitId, String.valueOf(visitPlan.getTxtProgramVisitId()));
            jsonVisit.put(dataVisit.Property_txtApotekId, String.valueOf(visitPlan.getTxtApotekId()));
            jsonVisit.put(dataVisit.Property_intActivityId, String.valueOf(visitPlan.getIntActivityId()));
            jsonVisit.put(dataVisit.Property_intSubActivityId, String.valueOf(visitPlan.getIntSubActivityId()));
            jsonVisit.put(dataVisit.Property_txtAreaId, String.valueOf(visitPlan.getTxtAreaId()));
            jsonVisit.put(dataVisit.Property_dtStart, String.valueOf(visitPlan.getDtStart()));
            jsonVisit.put(dataVisit.Property_dtEnd, String.valueOf(visitPlan.getDtEnd()));


            tRealisasiVisitPlan dataRealisasi = new tRealisasiVisitPlan();
            jsonRealisasi.put(dataRealisasi.Property_txtRealisasiVisitId, String.valueOf(dtRealisasi.getTxtRealisasiVisitId()));
            jsonRealisasi.put(dataRealisasi.Property_txtProgramVisitSubActivityId, String.valueOf(dtRealisasi.getTxtProgramVisitSubActivityId()));
            jsonRealisasi.put(dataRealisasi.Property_intUserId, String.valueOf(dtRealisasi.getIntUserId()));
            jsonRealisasi.put(dataRealisasi.Property_intRoleID, String.valueOf(dtRealisasi.getIntRoleID()));
            jsonRealisasi.put(dataRealisasi.Property_txtDokterId, String.valueOf(dtRealisasi.getTxtDokterId()));
            jsonRealisasi.put(dataRealisasi.Property_txtDokterName, String.valueOf(dtRealisasi.getTxtDokterName()));
            jsonRealisasi.put(dataRealisasi.Property_txtApotekId, String.valueOf(dtRealisasi.getTxtApotekId()));
            jsonRealisasi.put(dataRealisasi.Property_txtApotekName, String.valueOf(dtRealisasi.getTxtApotekName()));
            jsonRealisasi.put(dataRealisasi.Property_dtCheckIn, String.valueOf(dtRealisasi.getDtCheckIn()));
            jsonRealisasi.put(dataRealisasi.Property_dtCheckOut, String.valueOf(dtRealisasi.getDtCheckOut()));
            jsonRealisasi.put(dataRealisasi.Property_dtDateRealisasi, String.valueOf(dtRealisasi.getDtDateRealisasi()));
            jsonRealisasi.put(dataRealisasi.Property_dtDatePlan, String.valueOf(dtRealisasi.getDtDatePlan()));
            jsonRealisasi.put(dataRealisasi.Property_intNumberRealisasi, String.valueOf(dtRealisasi.getIntNumberRealisasi()));
            jsonRealisasi.put(dataRealisasi.Property_txtAcc, String.valueOf(dtRealisasi.getTxtAcc()));
            jsonRealisasi.put(dataRealisasi.Property_txtLong, String.valueOf(dtRealisasi.getTxtLong()));
            jsonRealisasi.put(dataRealisasi.Property_txtLat, String.valueOf(dtRealisasi.getTxtLat()));
            jsonRealisasi.put(dataRealisasi.Property_txtImgName1, String.valueOf(dtRealisasi.getTxtImgName1()));
            jsonRealisasi.put(dataRealisasi.Property_txtImgName2, String.valueOf(dtRealisasi.getTxtImgName2()));
            jsonRealisasi.put(dataRealisasi.Property_intStatusRealisasi, String.valueOf(dtRealisasi.getIntStatusRealisasi()));
            jsonRealisasi.put(dataRealisasi.Property_blobImg1, String.valueOf(dtRealisasi.getBlobImg1()));
            jsonRealisasi.put(dataRealisasi.Property_blobImg2, String.valueOf(dtRealisasi.getBlobImg2()));

            jsonObject.put("tProgramVisit",jsonHeader);
            jsonObject.put("tProgramVisitSubActivity", jsonVisit);
            jsonObject.put("tRealisasiVisitPlan", jsonRealisasi);
            jsonObject.put("userId", dtUserLogin.getIntUserID());
            jsonObject.put("intRoleId", dtUserLogin.getIntRoleID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    List<VMDownloadFile> vmList = new ArrayList<>();
    private void  createUnplan(tProgramVisit header, tProgramVisitSubActivity visitPlan, tRealisasiVisitPlan dataRealisasi) {
        String strLinkAPI = new clsHardCode().linkCreateUnplan;
        JSONObject resJson = new JSONObject();
        try {
            tokenRepo = new clsTokenRepo(getContext());
            dataToken = (List<clsToken>) tokenRepo.findAll();

            resJson.put("data", ParamCreateUnplan(header, visitPlan, dataRealisasi));
            resJson.put("device_info", new clsHardCode().pDeviceInfo());
            resJson.put("txtRefreshToken", dataToken.get(0).txtRefreshToken.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String mRequestBody = resJson.toString();
        new clsHelperBL().volleyDownloadData(getActivity(), strLinkAPI, mRequestBody, "Please Wait....", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                Intent res = null;
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        CreateUnplan model = gson.fromJson(jsonObject.toString(), CreateUnplan.class);
                        boolean txtStatus = model.getResult().isStatus();
                        String txtMessage = model.getResult().getMessage();
                        String txtMethode_name = model.getResult().getMethodName();
                        if (txtStatus == true){
                            vmList.clear();

//                            visitRepo.createOrUpdate(dt);

                            tProgramVisit dataHeader = visitHeader;
                            if (model.getData().getDataHeader()!=null){
//                                dataHeader.setIntStatus(model.getData().getDataHeader().getTxtProgramVisitId());
                                dataHeader.setDtStart(new clsActivity().parseDate(model.getData().getDataHeader().getDtStart()));
                                dataHeader.setDtEnd(new clsActivity().parseDate(model.getData().getDataHeader().getDtEnd()));
//                                dataHeader.setIntFlagPush(new clsHardCode().Sync);
                            }
                            visitRepo.createOrUpdate(dataHeader);
                            visitSubActivityRepo.createOrUpdate(dataPlan);
                            realisasiVisitPlanRepo.createOrUpdate(data);

                            if (model.getData().getDtAkuisisi().getAkuisisiHeader()!=null){
                                if (model.getData().getDtAkuisisi().getAkuisisiHeader().size()>0){
                                    for (int i = 0; i < model.getData().getDtAkuisisi().getAkuisisiHeader().size(); i++){
                                        tAkuisisiHeader data = new tAkuisisiHeader();
                                        data.setTxtHeaderId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getTxtAkuisisiHeaderId());
                                        data.setIntSubSubActivityId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getIntSubDetailActivityId());
                                        data.setIntSubSubActivityTypeId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getIntFlag());
                                        data.setTxtNoDoc(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getTxtNoDoc());
                                        data.setDtExpiredDate(new clsActivity().parseDate(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getDtExpiredDate()));
                                        data.setIntUserId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getIntUserId());
                                        data.setIntRoleId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getIntRoleId());
                                        data.setIntDokterId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getTxtDokterId());
                                        data.setIntApotekID(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getTxtApotekId());
                                        data.setIntAreaId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getIntAreaId());
                                        data.setTxtUserName(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getTxtUserName());
                                        data.setTxtRealisasiVisitId(model.getData().getDtAkuisisi().getAkuisisiHeader().get(i).getTxtRealisasiVisitId());
                                        data.setIntFlagPush(new clsHardCode().Sync);
                                        data.setIntFlagShow(new clsHardCode().Save);
                                        new tAkuisisiHeaderRepo(getContext()).createOrUpdate(data);
                                    }
                                }
                            }

                            if (model.getData().getDtAkuisisi().getAkuisisiDetail()!=null){
                                if (model.getData().getDtAkuisisi().getAkuisisiDetail().size()>0){
                                    for (int i = 0; i < model.getData().getDtAkuisisi().getAkuisisiDetail().size(); i++){
                                        tAkuisisiDetail data = new tAkuisisiDetail();
                                        data.setTxtHeaderId(model.getData().getDtAkuisisi().getAkuisisiDetail().get(i).getTxtAkuisisiHeaderId());
                                        data.setTxtDetailId(model.getData().getDtAkuisisi().getAkuisisiDetail().get(i).getTxtAkuisisiDetailId());
                                        data.setTxtImgName(model.getData().getDtAkuisisi().getAkuisisiDetail().get(i).getTxtImageName());

                                        if (model.getData().getDtAkuisisi().getAkuisisiDetail().get(i).getTxtLink()!=null){
                                            if (model.getData().getDtAkuisisi().getAkuisisiDetail().get(i).getTxtLink().length()>0){
                                                VMDownloadFile file = new VMDownloadFile();
                                                file.setLink(model.getData().getDtAkuisisi().getAkuisisiDetail().get(i).getTxtLink());
                                                file.setGroupDownload(new clsHardCode().AKUISISI);
                                                file.setIndex(i+1);
                                                file.setTxtId(model.getData().getDtAkuisisi().getAkuisisiDetail().get(i).getTxtAkuisisiDetailId());
                                                vmList.add(file);
                                            }
                                        }
                                        new tAkuisisiDetailRepo(getContext()).createOrUpdate(data);
                                    }
                                }
                            }

                            if (model.getData().getDtNotification()!=null){
                                if (model.getData().getDtNotification().size()>0){
                                    for (int i=0; i < model.getData().getDtNotification().size(); i++){
                                        tNotification data = new tNotification();
                                        data.setIntHeaderAkuisisiId(model.getData().getDtNotification().get(i).getTxtAkuisisiHeaderId());
                                        data.setIntActivityId(model.getData().getDtNotification().get(i).getIntActivityId());
                                        data.setIntDokterId(model.getData().getDtNotification().get(i).getTxtDokterId());
                                        data.setIntApotekId(model.getData().getDtNotification().get(i).getTxtApotekId());
                                        data.setDtExpired(new clsActivity().parseDate(model.getData().getDtNotification().get(i).getDtExpiredDate()));
                                        data.setIntSubDetailActivityId(model.getData().getDtNotification().get(i).getIntSubDetailActivityId());
                                        data.setTxtNoDoc(model.getData().getDtNotification().get(i).getTxtNoDoc());
                                        new tNotificationRepo(getContext()).createOrUpdate(data);
                                    }
                                    isCreateNotification = true;
                                }else {
                                    isCreateNotification =false;
                                }
                            }

                            if (model.getData().getDtMaintenance().getLtMaintenanceHeader()!=null){
                                if (model.getData().getDtMaintenance().getLtMaintenanceHeader().size()>0){
                                    for (int i = 0; i < model.getData().getDtMaintenance().getLtMaintenanceHeader().size(); i++){
                                        tMaintenanceHeader data = new tMaintenanceHeader();
                                        data.setTxtHeaderId(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getTxtMaintenanceHeaderId());
                                        data.setTxtRealisasiVisitId(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getTxtRealisasiVisitId());
                                        data.setIntActivityId(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getIntActivityId());
                                        data.setIntUserId(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getIntUserId());
                                        data.setIntRoleId(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getIntRoleId());
                                        data.setIntDokterId(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getIntDokterId());
                                        data.setIntApotekID(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getIntApotekId());
                                        data.setIntAreaId(model.getData().getDtMaintenance().getLtMaintenanceHeader().get(i).getIntAreaId());
                                        data.setIntFlagPush(new clsHardCode().Sync);
                                        new tMaintenanceHeaderRepo(getContext()).createOrUpdate(data);
                                    }
                                }
                            }

                            if (model.getData().getDtMaintenance().getLtMaintenanceDetail()!=null){
                                if (model.getData().getDtMaintenance().getLtMaintenanceDetail().size()>0){
                                    for (int i = 0; i < model.getData().getDtMaintenance().getLtMaintenanceDetail().size(); i ++){
                                        tMaintenanceDetail data = new tMaintenanceDetail();
                                        data.setTxtDetailId(model.getData().getDtMaintenance().getLtMaintenanceDetail().get(i).getTxtMaintenanceDetailId());
                                        data.setTxtHeaderId(model.getData().getDtMaintenance().getLtMaintenanceDetail().get(i).getTxtMaintenanceHeaderId());
                                        data.setIntSubDetailActivityId(model.getData().getDtMaintenance().getLtMaintenanceDetail().get(i).getIntSubDetailActivityId());
                                        data.setTxtNoDoc(model.getData().getDtMaintenance().getLtMaintenanceDetail().get(i).getTxtNoDocument());
                                        data.setIntFlagPush(new clsHardCode().Sync);
                                        new tMaintenanceDetailRepo(getContext()).createOrUpdate(data);
                                    }
                                }
                            }

                            Log.d("Data info", "Success Download");


                            if (vmList.size()>0){
                                downlaodFileNew(vmList, getActivity().getApplicationContext());
                            }else {
                                List<tNotification> notificationList = null;
                                if (isCreateNotification){
                                    try {
                                        notificationList = (List<tNotification>)  new tNotificationRepo(getContext()).findOutletId();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                new ToastCustom().showToasty(getContext(),"Save",1);
                                new Tools().intentFragment(FragmentListCallPlan.class, "Call Plan", getContext());
                                if (notificationList!=null){
                                    if (notificationList.size()>0){
                                        createNotification(notificationList.size());
                                    }
                                }
                            }

                        } else {
                            new ToastCustom().showToasty(getContext(),txtMessage,4);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void downlaodFileNew(List<VMDownloadFile> listVm, Context context){
        curCount = 0;
        totalCount = listVm.size();
        if (listVm.size()>0){
            progress=new ProgressDialog(getContext());
            progress.setMessage("Downloading file....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.setMax(100);
            progress.setProgress(0);
            progress.show();
            int index = 0;
            for (VMDownloadFile data : listVm){
                executor.execute(new LongThread(context, index, data, new Handler(this)));
                index++;
            }
        }
    }
    @Override
    public boolean onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(FRAG_VIEW, "Plan");
        new Tools().intentFragmentSetArgument(FragmentHeaderCallPlan.class, "Call Plan", getContext(), bundle);
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        curCount++;
        float per = (curCount / totalCount) * 100;
        progress.setIndeterminate(false);
        progress.setMax(100);
        progress.setProgress((int) per);
//        Toast.makeText(getContext(), "hahaha", Toast.LENGTH_SHORT).show();
        if (curCount == (int)totalCount){
            progress.dismiss();

            List<tNotification> notificationList = null;
            try {
                notificationList = (List<tNotification>)  new tNotificationRepo(getContext()).findOutletId();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            new ToastCustom().showToasty(getContext(),"Save",1);
            new Tools().intentFragment(FragmentListCallPlan.class, "Call Plan", getContext());
            if (notificationList!=null){
                if (notificationList.size()>0){
                  createNotification(notificationList.size());
                }
            }
        }
        return true;
    }
    public void createNotification(int size){
        Intent i = new Intent(getContext(), MainMenu.class);
        i.putExtra(i_View, "FragmentNotification");

        try {
            me.leolin.shortcutbadger.ShortcutBadger.applyCountOrThrow(getActivity(), size);
        } catch (me.leolin.shortcutbadger.ShortcutBadgeException e) {
            e.printStackTrace();
        }


        String CHANNEL_ID = "kalbenutritionals_channel";
        CharSequence name = "kalbenutritionals_channel";
        String Description = "kalbenutritionals channel";

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(MainMenu.class);
        stackBuilder.addNextIntent(i);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setContentTitle("Document Expired")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("There are some documents will expire!"))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setNumber(size)
                .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(),
                        R.mipmap.ic_launcher))
                .setColor(getContext().getResources().getColor(R.color.green_300));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }

        }else {
            Notification note = builder.build();
            note.defaults |= Notification.DEFAULT_VIBRATE;
            note.defaults |= Notification.DEFAULT_SOUND;
            if (notificationManager != null) {

                notificationManager.notify(NOTIFICATION_ID, note);
            }
        }

    }
}
