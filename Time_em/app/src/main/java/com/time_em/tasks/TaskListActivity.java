package com.time_em.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.time_em.android.R;
import com.time_em.asynctasks.AsyncResponseTimeEm;
import com.time_em.asynctasks.AsyncTaskTimeEm;
import com.time_em.dashboard.HomeActivity;
import com.time_em.db.TimeEmDbHandler;
import com.time_em.model.ColorSiteId;
import com.time_em.model.TaskEntry;
import com.time_em.model.UserWorkSite;
import com.time_em.model.UserWorkSiteData;
import com.time_em.model.WorkSiteList;
import com.time_em.parser.Time_emJsonParser;
import com.time_em.utils.PrefUtils;
import com.time_em.utils.Utils;

public class TaskListActivity extends Activity implements AsyncResponseTimeEm {

    //todo widgets
    private ListView taskListview;
    private ImageView addTaskButton, back;
    private ImageButton calbutton;
    private TextView headerText, currentDate,caldate,noTaskMsg,left_side;
    private LinearLayout footer;
    private RecyclerView recyclerView;
    private ImageView addButton;
    private LinearLayout lay_upperGraph, lay_colorIndicator;
    private TimeEmDbHandler dbHandler;
    private TextView headerInfo;

    //todo variables
    private int UserId;
    private int selectedPos = 14;
    private String selectedDate;
    private int first_time = 1, totalWidth = 0;
    private float oneMin, stratPoint, endPoint;
    private boolean refresh = true;

    //todo classes
    private Intent intent;
    private SimpleDateFormat apiDateFormater, dateFormatter, dayFormatter;
    private Time_emJsonParser parser;
    private Context context;

    //todo array list
    private ArrayList<TaskEntry> tasks;
    private ArrayList<Calendar> arrayList;
    private ArrayList<String> backGroundColor_array = new ArrayList<>();
    private ArrayList<ColorSiteId> array_colorSiteId = new ArrayList<>();

    //todo for graphs
    private LinearLayout mainLinearLayout, lay_date, lay_hours;
    private DateSliderAdapter adapter;
    private DatePickerDialog datePickerDialog;
    private TextView bottom_side;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        context = getApplicationContext();
        // currentDate.setText(Utils.formatDateChange(selectedDate,"MM-dd-yyyy","EEE dd MMM, yyyy"));
        // GetUserWorkSiteApi();

        // showTaskList();

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                caldate.setVisibility(View.VISIBLE);
                caldate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                apiDateFormater = new SimpleDateFormat("MM-dd-yyyy");
                selectedDate = apiDateFormater.format(newDate.getTime());
                getTaskList(selectedDate);

                for(int i=0;i<arrayList.size()-1;i++){
                    Calendar cal1 = arrayList.get(i);
                    if(cal1.get(Calendar.YEAR) == newDate.get(Calendar.YEAR) &&
                            cal1.get(Calendar.DAY_OF_YEAR) == newDate.get(Calendar.DAY_OF_YEAR))
                    {
                        selectedPos = i;
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(i);

                    }
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void showTaskList() {
        // todo for user tasks layout
        populatRecyclerView();
        initScreen();
        setUpClickListeners();
        getTaskList(selectedDate);
    }

    private void showGraphs() {
    // todo for user graphs layout
        setContentView(R.layout.activity_graph);
        setColorArray();
        lay_colorIndicator = (LinearLayout) findViewById(R.id.lay_colorIndicator);
        lay_upperGraph = (LinearLayout) findViewById(R.id.lay_upperGraph);
        lay_upperGraph.setVisibility(View.GONE);
        lay_hours = (LinearLayout) findViewById(R.id.lay_hours);
        lay_hours.setVisibility(View.GONE);
        headerInfo = (TextView) findViewById(R.id.headerText);
        if(getIntent().getStringExtra("UserName")!=null) {
            String username = getIntent().getStringExtra("UserName");
            headerInfo.setText(username + "'s Graphs");
            }
        back = (ImageView) findViewById(R.id.back);
        addButton = (ImageView) findViewById(R.id.AddButton);
        addButton.setVisibility(View.GONE);

        try{
            UserId =  Integer.parseInt(getIntent().getStringExtra("UserId"));
        }catch (Exception e){}

        // todo fetch from data base
        dbHandler = new TimeEmDbHandler(TaskListActivity.this);
        UserWorkSiteData allData = dbHandler.getGeoGraphData1(""+UserId);

        //ArrayList<UserWorkSite> array_workSite=  dbHandler.getGeoGraph(""+UserId);
        //fetchDataGraphs(array_workSite);
        settingGraph();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initScreen() {
        dbHandler = new TimeEmDbHandler(TaskListActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        addTaskButton = (ImageView) findViewById(R.id.AddButton);
        back = (ImageView) findViewById(R.id.back);
        taskListview = (ListView) findViewById(R.id.taskList);
        parser = new Time_emJsonParser(TaskListActivity.this);
        headerText = (TextView) findViewById(R.id.headerText);
        noTaskMsg = (TextView) findViewById(R.id.noTaskMsg);

        currentDate = (TextView) findViewById(R.id.currentDate);
        currentDate.setVisibility(View.VISIBLE);
        caldate=(TextView) findViewById(R.id.caldate);
        calbutton=(ImageButton) findViewById(R.id.calbutton);


          if(getIntent().getStringExtra("UserName")!=null) {
            String username = getIntent().getStringExtra("UserName");
            headerText.setText(username + "'s Tasks");
            addTaskButton.setVisibility(View.INVISIBLE);
            try{
                UserId =  Integer.parseInt(getIntent().getStringExtra("UserId"));
            }catch (Exception e){
            }
            calbutton.setVisibility(View.GONE);
              if(PrefUtils.getStringPreference(TaskListActivity.this,PrefUtils.KEY_TPCheck).equalsIgnoreCase("true")) {
                  showMessageTrackingPlot();
                  PrefUtils.setStringPreference(TaskListActivity.this,PrefUtils.KEY_TPCheck,"false");

              }
        }
        else{
            headerText.setText("My Tasks");
            try{
            UserId =   Integer.parseInt(Utils.getSharedPrefs(getApplicationContext(), PrefUtils.KEY_USER_ID));
            }catch (Exception e){
            }
        }
        footer = (LinearLayout) findViewById(R.id.footer);
        footer.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(TaskListActivity.this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.scrollToPositionWithOffset(selectedPos - 2, 20);
        recyclerView.setLayoutManager(layoutManager);
        apiDateFormater = new SimpleDateFormat("MM-dd-yyyy");
        selectedDate = apiDateFormater.format(arrayList.get(selectedPos).getTime());

        dateFormatter = new SimpleDateFormat("dd");
        dayFormatter = new SimpleDateFormat("E", Locale.US);


         adapter = new DateSliderAdapter(arrayList, new OnItemClickListener() {
            @Override
            public void onItemClick(Calendar item, int position) {
                String weekDay;
                // SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.US);
                // weekDay = dayFormat.format(item.getTime());
                // Utils.showToast(TaskListActivity.this, item.get(Calendar.DAY_OF_MONTH)+" "+weekDay+" Clicked");

                selectedDate = apiDateFormater.format(item.getTime());
                showAdd(item.getTime());
                caldate.setVisibility(View.GONE);
                getTaskList(selectedDate);


            }
        });
        recyclerView.setAdapter(adapter);// // todo set adapter on recyclerview
        adapter.notifyDataSetChanged();// // todo Notify the adapter
    }

    public void showAdd(Date selectedDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            //strDate = sdf.parse(selectedDate);
            if (new Date().after(selectedDate)) {
                addTaskButton.setVisibility(View.VISIBLE);

            }else if(new Date().equals(selectedDate)){
                addTaskButton.setVisibility(View.VISIBLE);
            }
            else{
                addTaskButton.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showMessageTrackingPlot() {
      //  Utils.alertMessageWithoutBack(TaskListActivity.this,"To rotate the device to view tracking plot should be displayed.");
      Intent in = new Intent(getApplicationContext(),RotateDialogActivity.class);
      startActivity(in);

    }


    private void setUpClickListeners() {
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh = false;
                intent = new Intent(TaskListActivity.this, AddEditTaskEntry.class);
                intent.putExtra("selectDate", selectedDate);
                intent.putExtra("UserId", ""+UserId);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        taskListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refresh = false;
                Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
                intent.putExtra("taskEntry", tasks.get(position));
                startActivity(intent);
            }
        });

        calbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void populatRecyclerView() {

        arrayList = new ArrayList<>();
        Date myDate = new Date();

        for (int i = 0; i < selectedPos; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, i - selectedPos);
            arrayList.add(calendar);
        }
        for (int i = 0; i <= selectedPos; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, i);
            arrayList.add(calendar);
        }

    }

    private void getTaskList(String createdDate) {

        currentDate.setText(Utils.formatDateChange(selectedDate, "MM-dd-yyyy", "EEE dd MMM, yyyy"));

        String timeStamp = Utils.getSharedPrefs(TaskListActivity.this, UserId + "-" + selectedDate + "-" + getResources().getString(R.string.taskTimeStampStr));
        if (timeStamp == null || timeStamp.equals(null) || timeStamp.equals("null"))
            timeStamp = "";

        HashMap<String, String> postDataParameters = new HashMap<String, String>();
        postDataParameters.put("userId", String.valueOf(UserId));
        postDataParameters.put("createdDate",createdDate);
        postDataParameters.put("TimeStamp", "");
        postDataParameters.put("CompanyId", PrefUtils.getStringPreference(TaskListActivity.this,PrefUtils.KEY_COMPANY));
        //Log.e("values"+Utils.GetUserActivityTask, "userid: " + String.valueOf(UserId) + ", createdDate: " + createdDate + ", TimeStamp: " + timeStamp);
        //Log.e(""+Utils.GetUserActivityTask, "" + postDataParameters.toString());

        AsyncTaskTimeEm mWebPageTask = new AsyncTaskTimeEm(
                TaskListActivity.this, "post", Utils.GetUserActivityTask,
                postDataParameters, true, "Please wait...");
        mWebPageTask.delegate = (AsyncResponseTimeEm) TaskListActivity.this;
        mWebPageTask.execute();

    }

    private void deleteTask(TaskEntry taskEntry) {

        if (Utils.isNetworkAvailable(TaskListActivity.this)) {

            if (taskEntry.getId() == 0) {
                //  BaseActivity.deleteIds.add(""+taskEntry.getId());
                taskEntry.setIsActive(false);
                ArrayList<TaskEntry> taskEntries = new ArrayList<>();
                taskEntries.add(taskEntry);
                dbHandler.updateDeleteOffline(taskEntries, selectedDate);
                getTaskList(selectedDate);
            } else {
                HashMap<String, String> postDataParameters = new HashMap<String, String>();
                postDataParameters.put("Id", String.valueOf(taskEntry.getId()));

                Log.e(Utils.deleteTaskAPI, "" + postDataParameters.toString());
                AsyncTaskTimeEm mWebPageTask = new AsyncTaskTimeEm(
                        TaskListActivity.this, "post", Utils.deleteTaskAPI,
                        postDataParameters, true, "Please wait...");
                mWebPageTask.delegate = (AsyncResponseTimeEm) TaskListActivity.this;
                mWebPageTask.execute();

                dbHandler.deleteTask(taskEntry.getId());

            }

        } else {
            if (taskEntry.getId() == 0) {
                taskEntry.setIsActive(false);
                ArrayList<TaskEntry> taskEntries = new ArrayList<>();
                taskEntries.add(taskEntry);
                dbHandler.updateDeleteOffline(taskEntries, selectedDate);
                getTaskList(selectedDate);
            } else {
                taskEntry.setIsActive(false);
                ArrayList<TaskEntry> taskEntries = new ArrayList<>();
                taskEntries.add(taskEntry);
                HomeActivity.deleteIds.add("" + taskEntry.getId());
                dbHandler.updateTask(taskEntries, selectedDate, false);
            }
            // Utils.alertMessage(TaskListActivity.this, "Task Updated Successfully.!");
            //Utils.alertMessage(TaskListActivity.this, Utils.network_error);
        }

    }

    public class TaskAdapter extends BaseSwipeAdapter {
        private Context context;
        private TextView taskName, hours, taskComments;
        private LinearLayout edit, delete;

        public TaskAdapter(Context ctx) {
            context = ctx;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tasks.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public void fillValues(final int position, View convertView) {
            // TODO Auto-generated method stub

            final TaskEntry selectedTask = tasks.get(position);

            taskName = (TextView) convertView.findViewById(R.id.taskName);
            hours = (TextView) convertView.findViewById(R.id.hours);
            delete = (LinearLayout) convertView.findViewById(R.id.delete);
            edit = (LinearLayout) convertView.findViewById(R.id.edit);
            taskComments = (TextView) convertView.findViewById(R.id.taskComments);

            taskName.setText(selectedTask.getTaskName());
            taskComments.setText(selectedTask.getComments());
           // hours.setText("(" + String.valueOf(selectedTask.getTimeSpent()) + ") Hours");
            hours.setText( String.valueOf(selectedTask.getTimeSpent()) + " hours");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            TaskListActivity.this);
                    alert.setTitle("Delete this task?");
                    alert.setMessage("Are you sure?");
                    alert.setPositiveButton("No", null);
                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteTask(tasks.get(position));
                        }
                    });

                    alert.show();
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh = false;
                    intent = new Intent(TaskListActivity.this, AddEditTaskEntry.class);
                    intent.putExtra("selectDate", selectedDate);
                    intent.putExtra("taskEntry", selectedTask);
                    intent.putExtra("UserId", ""+UserId);
                    startActivity(intent);
                }
            });
        }

        @Override
        public View generateView(int arg0, ViewGroup arg1) {
            return LayoutInflater.from(TaskListActivity.this).inflate(
                    R.layout.task_row, null);

        }

        @Override
        public int getSwipeLayoutResourceId(int arg0) {
            // TODO Auto-generated method stub
            return R.id.swipe;
        }
    }

    @Override
    public void processFinish(String output, String methodName) {
        // TODO Auto-generated method stub
        Log.e("output", ":: " + output);
        if (methodName.equals(Utils.GetUserActivityTask)) {
            ArrayList<TaskEntry> taskEntries = parser.parseTaskList(output, UserId, selectedDate);
            dbHandler.updateTask(taskEntries, selectedDate, false);
            String companyId=  PrefUtils.getStringPreference(TaskListActivity.this,PrefUtils.KEY_COMPANY);
            tasks = dbHandler.getTaskEnteries(UserId, selectedDate, false,companyId);
            taskListview.setAdapter(new TaskAdapter(TaskListActivity.this));
            if(tasks.size()==0)
                noTaskMsg.setVisibility(View.VISIBLE);
            else
                noTaskMsg.setVisibility(View.GONE);
               // Utils.alertMessageWithoutBack(TaskListActivity.this, "No Task Available");

        } else if (methodName.equals(Utils.deleteTaskAPI)) {
            boolean error = parser.parseDeleteTaskResponse(output);
            if (!error) {
                Utils.showToast(getApplicationContext(),"Task deleted Successfully.");
                getTaskList(selectedDate);
            }else{
                Utils.showToast(TaskListActivity.this,Utils.Api_error);
            }
        }
    }

    public static <WorkSiteList> List<com.time_em.model.WorkSiteList> stringToArray(String s, Class<com.time_em.model.WorkSiteList[]> clazz) {
        com.time_em.model.WorkSiteList[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    private void fetchDataGraphs(ArrayList<UserWorkSite> arrayList) {
        //Gson gson = new Gson();
        ArrayList<UserWorkSite> array_UserWorkSite= new ArrayList<UserWorkSite>();
        for(int i=0;i<arrayList.size();i++) {
           String allData= arrayList.get(i).getAllData();
           String date= arrayList.get(i).getDate();
           List<WorkSiteList> arrayList_WorkSiteList= stringToArray(allData, WorkSiteList[].class);


            Log.e("size=",""+arrayList_WorkSiteList.size());
            UserWorkSite userWorkSite=new UserWorkSite();
            userWorkSite.setDate(date);
            ArrayList<WorkSiteList> array_WorkSiteList= new ArrayList<WorkSiteList>();

            for(int j=0;j<arrayList_WorkSiteList.size();j++) {
                WorkSiteList workSiteList=new WorkSiteList();
                workSiteList.setWorkSiteId(stringToArray(allData, WorkSiteList[].class).get(j).getWorkSiteId());
                workSiteList.setWorkSiteName(stringToArray(allData, WorkSiteList[].class).get(j).getWorkSiteName());
                workSiteList.setWorkingHour(stringToArray(allData, WorkSiteList[].class).get(j).getWorkingHour());
                workSiteList.setTimeIn(stringToArray(allData, WorkSiteList[].class).get(j).getTimeIn());
                workSiteList.setTimeOut(stringToArray(allData, WorkSiteList[].class).get(j).getTimeOut());
                array_WorkSiteList.add(workSiteList);
            }

            userWorkSite.setArraylist_WorkSiteList(array_WorkSiteList);
            array_UserWorkSite.add(userWorkSite);
        }
        array_UserWorkSite.size();
        Log.e("total","ss="+array_UserWorkSite.size());
        //setColorWithModel(array_UserWorkSite);
        //settingGraph(array_UserWorkSite);
    }

    private void setColorWithModel(ArrayList<UserWorkSite> array_worksite) {
        if (array_worksite != null && array_worksite.size() > 0) {
            array_colorSiteId = new ArrayList<>();
            for (int i = 0; i < array_worksite.size(); i++) {

                for (int j = 0; j < array_worksite.get(i).getArraylist_WorkSiteList().size(); j++) {
                    String siteId = array_worksite.get(i).getArraylist_WorkSiteList().get(j).getWorkSiteName();

                    ColorSiteId colorSiteId = new ColorSiteId();
                    if(j>10) {
                        String position=i+""+j;
                        int pos=Integer.parseInt(position);
                        colorSiteId.setColor(backGroundColor_array.get(pos));
                        Log.e("pos", "" + pos);
                    }else{
                        colorSiteId.setColor(backGroundColor_array.get(i + j));
                        Log.e("pos", "" + i + j+backGroundColor_array.get(i + j));
                    }
                    colorSiteId.setSietId(siteId);
                    boolean value=true;
                    for(int k=0;k<array_colorSiteId.size();k++) {
                       if(siteId.equalsIgnoreCase(array_colorSiteId.get(k).getSietId())) {
                           value=false;
                       }

                    }
                    if(value){
                        array_colorSiteId.add(colorSiteId);
                    }
                }
                Log.e("color size", "" + array_colorSiteId.size());
            }
           /* array_colorSiteId = new ArrayList<>();
            ColorSiteId colorSiteId = new ColorSiteId();
            colorSiteId.setSietId("OSBORNE PARK");
            colorSiteId.setColor(backGroundColor_array.get(1));
            array_colorSiteId.add(colorSiteId);

            colorSiteId = new ColorSiteId();
            colorSiteId.setSietId("LCPL - GCSB");
            colorSiteId.setColor(backGroundColor_array.get(2));
            array_colorSiteId.add(colorSiteId);*/

            setColor(array_colorSiteId);
        }
    }


    public class DateSliderAdapter extends RecyclerView.Adapter<DateSliderAdapter.ViewHolder> {

        private final ArrayList<Calendar> items;
        private final OnItemClickListener listener;

        public DateSliderAdapter(ArrayList<Calendar> items, OnItemClickListener listener) {
            this.items = items;

            this.listener = listener;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_slider_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(items.get(position), listener, position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView day, date;

            public ViewHolder(View itemView) {
                super(itemView);
                day = (TextView) itemView.findViewById(R.id.day);
                date = (TextView) itemView.findViewById(R.id.date);
            }

            public void bind(final Calendar item, final OnItemClickListener listener, final int pos) {
                if (selectedPos == pos) {
                    date.setBackgroundResource(R.drawable.date_bg);
                    date.setTextColor(Color.WHITE);
                } else {
                    date.setBackgroundResource(R.drawable.date_bg_grey);
                    date.setTextColor(Color.BLACK);
                }

                date.setText(dateFormatter.format(item.getTime()));
                day.setText(dayFormatter.format(item.getTime()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyItemChanged(selectedPos);
                        selectedPos = pos;
                        notifyItemChanged(selectedPos);

                        listener.onItemClick(item, pos);
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Calendar item, int position);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getStringExtra("UserName") != null) {
            int value = TaskListActivity.this.getResources().getConfiguration().orientation;
            String orientation = "";
            if (value == Configuration.ORIENTATION_PORTRAIT) {
                orientation = "Portrait";
                //  Toast.makeText(this, "PORTRAIT", Toast.LENGTH_SHORT).show();
                showTaskList();
            }

            if (value == Configuration.ORIENTATION_LANDSCAPE) {
                orientation = "Landscape";
                //  Toast.makeText(this, "LANDSCAPE", Toast.LENGTH_SHORT).show();
                UserId =  Integer.parseInt(getIntent().getStringExtra("UserId"));
                dbHandler = new TimeEmDbHandler(TaskListActivity.this);
                UserWorkSiteData userdata = dbHandler.getGeoGraphData1(""+UserId);
                if(userdata == null || userdata.ListSites.size() == 0){
                   showTaskList();
                   Utils.alertMessageWithoutBack(TaskListActivity.this,"Tracking Plot not available for this user.");
                }
                else {
                    showGraphs();
                }

            }
        } else {

             if(refresh)
                 showTaskList();
             else
                 getTaskList(selectedDate);
        }
    }

    private void settingGraph() {
        UserWorkSiteData userdata = dbHandler.getGeoGraphData1(""+UserId);

        lay_upperGraph = (LinearLayout) findViewById(R.id.lay_upperGraph);
        lay_upperGraph.setVisibility(View.VISIBLE);

        lay_hours = (LinearLayout) findViewById(R.id.lay_hours);
        lay_hours.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams hrs_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        hrs_param.setMargins(80,0,0,0);
        lay_hours.setLayoutParams(hrs_param);

        bottom_side=(TextView)findViewById(R.id.bottom_side);
        LinearLayout.LayoutParams btm_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        btm_param.setMargins(118,0,0,0);
        bottom_side.setLayoutParams(btm_param);

        left_side = (TextView) findViewById(R.id.left_side);
        LinearLayout.LayoutParams left_param = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        left_param.setMargins(18,0,0,0);
        left_side.setLayoutParams(left_param);

        mainLinearLayout = (LinearLayout) findViewById(R.id.graphLayout);
        lay_date = (LinearLayout) findViewById(R.id.lay_date);
        LinearLayout.LayoutParams date_param = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay_date.setLayoutParams(date_param);

        totalWidth = getScreenWidth(TaskListActivity.this);
        mainLinearLayout.removeAllViews();
        lay_date.removeAllViews();

        for(int a=0; a < userdata.ListSites.size(); a++) {

            //for horizontal separator.
            TextView hori_separator = new TextView(this);
            hori_separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4));
            hori_separator.setBackgroundResource(R.drawable.dotted_line);
            mainLinearLayout.addView(hori_separator);


            // Create LinearLayout( particular row)
            LinearLayout outerlayout = new LinearLayout(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 65);
            param.setMargins(0,0,0,0);   //71
            outerlayout.setLayoutParams(param);
            outerlayout.setOrientation(LinearLayout.HORIZONTAL);
            outerlayout.setPadding(0, 0, 0, 0);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            for (int i = 0; i < userdata.ListSites.get(a).WerksiteDates.size(); i++) {

                // Create LinearLayout (particular date)
                LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65, 1.0f);
                para.setMargins(2,0,2,0);
                linearLayout.setLayoutParams(para);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(0, 0, 0, 0);

                // float totalWidth= lay_hours.getWidth();
                float oneHour = totalWidth / 24;
                float totalMins = 24 * 60;
                int onepart = totalWidth / userdata.ListSites.get(a).WerksiteDates.size();
                oneMin = onepart / totalMins;

                // Add text view
                if (userdata.ListSites.get(a).WerksiteDates.get(i).workSiteList != null && userdata.ListSites.get(a).WerksiteDates.get(i).workSiteList.size() > 0) {
                    for (int j = 0; j < userdata.ListSites.get(a).WerksiteDates.get(i).workSiteList.size(); j++) {
                        String value = null, valueIn = null, valueOut = null;
                        value = userdata.ListSites.get(a).WerksiteDates.get(i).workSiteList.get(j).getWorkingHour();

                        valueIn = userdata.ListSites.get(a).WerksiteDates.get(i).workSiteList.get(j).getTimeIn();
                        valueOut =userdata.ListSites.get(a).WerksiteDates.get(i).workSiteList.get(j).getTimeOut();
                        Log.e("hours", "hours=" + value + " valueIn=" + valueIn + " valueOut=" + valueOut);


                        if (first_time == 1) {
                            first_time = 0;
                            endPoint = getStartTime(valueIn);
                            float width = getDifferenceTwoMins(0, endPoint);
                            Log.e("width", "" + width);
                            float oneWidth = width * oneMin;
                            int int_width = (int) oneWidth;
                            View view = new TextView(this);
                            view.setPadding(0, 0, 0, 0);// in pixels (left, top, right, bottom)
                            view.setLayoutParams(new LinearLayout.LayoutParams(int_width, 65));
                            view.setBackgroundColor(getResources().getColor(R.color.editBg));
                            linearLayout.addView(view);
                        }
                        if (endPoint != getStartTime(valueIn) && endPoint != 0) {
                            // endPoint = getStartTime(valueIn);
                            if (value != null
                                    && !value.equalsIgnoreCase("0.0")
                                    && valueIn != null
                                    && !valueIn.equalsIgnoreCase("null")
                                    && valueOut != null
                                    && !valueOut.equalsIgnoreCase("null")
                                    ) {

                                float width = getDifferenceTwoMins(endPoint, getStartTime(valueIn));
                                Log.e("width", "" + width);
                                float oneWidth = width * oneMin;
                                int int_width = (int) oneWidth;
                                View view = new TextView(this);
                                view.setPadding(0, 0, 0, 0);// in pixels (left, top, right, bottom)
                                view.setLayoutParams(new LinearLayout.LayoutParams(int_width, 65));
                                view.setBackgroundColor(getResources().getColor(R.color.editBg));
                                linearLayout.addView(view);
                            }
                        }
                        stratPoint = getStartTime(valueIn);
                        endPoint = getStartTime(valueOut);
                        float width = getDifferenceTwoMins(stratPoint, endPoint);

                        Log.e("difference width", "" + width);
                        float one_width = width * oneMin;
                        //float widtha= width+Float.parseFloat(value);
                        int int_width = (int) one_width;
                        Log.e("total_width", "" + int_width);
                        if (int_width > 0) {
                            View view = new TextView(this);
                            view.setPadding(0, 0, 0, 0);// in pixels (left, top, right, bottom)
                            view.setLayoutParams(new LinearLayout.LayoutParams(int_width, 65));
                            view.setBackgroundColor(color);
                            //view.setBackgroundColor(getResources().getColor(R.color.grey));

                            /*String id = userdata.ListSites.get(a).WerksiteDates.get(i).workSiteList.get(j).getWorkSiteName();
                            if (array_colorSiteId != null && array_colorSiteId.size() > 0) {
                                for (int k = 0; k < array_colorSiteId.size(); k++) {
                                    if (id.equalsIgnoreCase(array_colorSiteId.get(k).getSietId())) {
                                        view.setBackgroundColor(Color.parseColor(array_colorSiteId.get(k).getColor()));
                                    }
                                }
                            }*/
                            linearLayout.addView(view);

                        }
                    }
                } else {
                    //todo for not data for work site show empty
                    TextView textView = new TextView(this);
                    textView.setPadding((int) 0, 0, 0, 0);  // in pixels (left, top, right, bottom)
                    textView.setLayoutParams(new LinearLayout.LayoutParams(0, 65));
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    textView.setBackgroundColor(getResources().getColor(R.color.deleteBg));
                    linearLayout.addView(textView);
                }
                first_time = 1;
                outerlayout.addView(linearLayout);

                //for dates
                if(a == userdata.ListSites.size()-1){

                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    textView.setLayoutParams(param1);
                    //textView.setLayoutParams(new LinearLayout.LayoutParams(60, 66));
                    textView.setGravity(Gravity.CENTER);
                    // textView.setTextColor(getResources().getColor(R.color.black));
                    //textView.setText(array_worksite.get(i).getDate().substring(0, 5));
                    SimpleDateFormat sdfSource  = new SimpleDateFormat("MM-dd-yyyy");
                    SimpleDateFormat sdfDestination  = new SimpleDateFormat("dd-MM-yyyy");

                    String createdDate = userdata.ListSites.get(a).WerksiteDates.get(i).CreatedDate;
                    Date date = null;
                    try {
                        date = sdfSource.parse(createdDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    createdDate = sdfDestination.format(date);

                    textView.setText(createdDate);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setTextSize(12);
                    textView.setPadding(0, 10, 0, 10);
                    lay_hours.addView(textView);
                }

            }

            mainLinearLayout.addView(outerlayout);

            //for sitename
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT);
            param2.setMargins(0,4,0,0);
            textView.setLayoutParams(param2);
            textView.setGravity(Gravity.CENTER);
            // textView.setTextColor(getResources().getColor(R.color.black));
            // textView.setText(array_worksite.get(i).getDate().substring(0, 5));
            textView.setText(userdata.ListSites.get(a).SiteName);
            //textView.setMaxLines(1);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTextSize(12);
            textView.setPadding(0, 0, 0, 0);
            lay_date.addView(textView);

        }

    }

    private float getStartTime(String startTime) {
        try {
            String[] parts = startTime.split(":");
            String part1 = parts[0]; //
            String part2 = parts[1]; //
            float fPart1 = Float.parseFloat(part1);
            float fPart2 = Float.parseFloat(part2);
            float hoursInMins = fPart1 * 60;
            float totalMins = hoursInMins + fPart2;
            return totalMins;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private float getDifferenceTwoMins(float startTime, float endTime) {
        try {
            float totalWidth = endTime - startTime;
            return totalWidth;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }


    private void setColor(ArrayList<ColorSiteId> arrayList) {
         lay_colorIndicator.removeAllViews();
         for(int i=0;i<arrayList.size();i++) {
             LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             View rowView = inflater.inflate(R.layout.color_indicator, null);
             TextView txt_color = (TextView) rowView.findViewById(R.id.txt_color);
             TextView txt_name = (TextView) rowView.findViewById(R.id.txt_name);
             txt_name.setText(arrayList.get(i).getSietId());
             txt_color.setBackgroundColor(Color.parseColor(arrayList.get(i).getColor()));
             lay_colorIndicator.addView(rowView);
         }
    }

    private int getScreenWidth(Activity activity)
    {
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
// since SDK_INT = 1;
        int  widthPixels = metrics.widthPixels;
        long  heightPixels = metrics.heightPixels;

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = metrics.heightPixels / density;
        float dpWidth  = metrics.widthPixels / density;
        Log.e("widthPixel","dpWidth="+widthPixels+" dpHeight="+heightPixels);

// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d)-140;  //200
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                Log.e("widthPixels","widthPixels="+widthPixels+" heightPixels="+heightPixels);
                return widthPixels;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x-100;  //200
                heightPixels = realSize.y;

                Log.e("widthPixels","widthPixels="+widthPixels+" heightPixels="+heightPixels);
                return widthPixels;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        return 650;
    }

    private void setColorArray() {

        backGroundColor_array.clear();
        backGroundColor_array = new ArrayList<>();
        backGroundColor_array.add("#63C070");
        backGroundColor_array.add("#B83F3A");
        backGroundColor_array.add("#51B3CE");
        backGroundColor_array.add("#FFFAF0");
        backGroundColor_array.add("#FF55F0");
        backGroundColor_array.add("#FFFF00");
        backGroundColor_array.add("#B0171F");
        backGroundColor_array.add("#FFB6C1");
        backGroundColor_array.add("#EEA2AD");
        backGroundColor_array.add("#8B5F65");
        backGroundColor_array.add("#DA70D6");
        backGroundColor_array.add("#FF00FF");
        backGroundColor_array.add("#912CEE");
        backGroundColor_array.add("#551A8B");
        backGroundColor_array.add("#9F79EE");
        backGroundColor_array.add("#0000FF");
        backGroundColor_array.add("#00008B");
        backGroundColor_array.add("#3D59AB");
        backGroundColor_array.add("#27408B");
        backGroundColor_array.add("#B0C4DE");
        backGroundColor_array.add("#6E7B8B");
        backGroundColor_array.add("#00B2EE");
        backGroundColor_array.add("#00FA9A");
        backGroundColor_array.add("#98FB98");
        backGroundColor_array.add("#FFFF00");
        backGroundColor_array.add("#BDB76B");
        backGroundColor_array.add("#FCE6C9");
        backGroundColor_array.add("#CDAA7D");
        backGroundColor_array.add("#00008b");
        backGroundColor_array.add("#cd3333");
        backGroundColor_array.add("#5f9ea0");
        backGroundColor_array.add("#8ee5ee");
        backGroundColor_array.add("#7fff00");


        backGroundColor_array.add("#51B3CE");
        backGroundColor_array.add("#63C070");
        backGroundColor_array.add("#B83F3A");
        backGroundColor_array.add("#FFFAF0");
        backGroundColor_array.add("#FFFFF0");
        backGroundColor_array.add("#FFFAFA");
        backGroundColor_array.add("#FFFF00");
        backGroundColor_array.add("#B0171F");
        backGroundColor_array.add("#FFB6C1");
        backGroundColor_array.add("#EEA2AD");
        backGroundColor_array.add("#8B5F65");
        backGroundColor_array.add("#DA70D6");
        backGroundColor_array.add("#FF00FF");
        backGroundColor_array.add("#912CEE");
        backGroundColor_array.add("#551A8B");
        backGroundColor_array.add("#9F79EE");
        backGroundColor_array.add("#0000FF");
        backGroundColor_array.add("#00008B");
        backGroundColor_array.add("#3D59AB");
        backGroundColor_array.add("#27408B");
        backGroundColor_array.add("#B0C4DE");
        backGroundColor_array.add("#6E7B8B");
        backGroundColor_array.add("#00B2EE");
        backGroundColor_array.add("#00FA9A");
        backGroundColor_array.add("#98FB98");
        backGroundColor_array.add("#FFFF00");
        backGroundColor_array.add("#BDB76B");
        backGroundColor_array.add("#FCE6C9");
        backGroundColor_array.add("#CDAA7D");
        backGroundColor_array.add("#00008b");
        backGroundColor_array.add("#cd3333");
        backGroundColor_array.add("#5f9ea0");
        backGroundColor_array.add("#8ee5ee");
        backGroundColor_array.add("#7fff00");

    }


}