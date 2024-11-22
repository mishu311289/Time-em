package com.time_em.team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.gson.Gson;
import com.time_em.android.R;
import com.time_em.asynctasks.AsyncResponseTimeEm;
import com.time_em.asynctasks.AsyncTaskTimeEm;
import com.time_em.db.TimeEmDbHandler;
import com.time_em.model.User;
import com.time_em.model.UserWorkSiteData;
import com.time_em.parser.Time_emJsonParser;
import com.time_em.tasks.TaskListActivity;
import com.time_em.utils.PrefUtils;
import com.time_em.utils.Utils;

public class UserListActivity extends Activity implements AsyncResponseTimeEm {

	//todo widgets
	private ListView taskListview;
	private ArrayList<User> team;
	private Time_emJsonParser parser;
	private ImageButton callButton;
	//	private HorizontalScrollView sView;
	private TextView swipeInfo, headerText;
	private ImageView back, addTask;
	private RecyclerView recyclerView;
	private TimeEmDbHandler dbHandler;
	//todo variables
	private int array_position=0;
	private String SelectedUserId="";
	private int userId=0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);

		initScreen();
		SimpleDateFormat postFormater = new SimpleDateFormat("dd/MM/yyyy");

		String currentDate = postFormater.format(new Date());

		try {
			 userId = Integer.parseInt(Utils.getSharedPrefs(getApplicationContext(), PrefUtils.KEY_USER_ID));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		getUserList(userId);
	}
	
	private void initScreen(){

		dbHandler = new TimeEmDbHandler(UserListActivity.this);
		taskListview = (ListView)findViewById(R.id.taskList);
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		parser = new Time_emJsonParser(UserListActivity.this);
//		sView = (HorizontalScrollView)findViewById(R.id.dateSlider);
		swipeInfo = (TextView)findViewById(R.id.swipeInfo);
		back = (ImageView)findViewById(R.id.back);
		addTask = (ImageView) findViewById(R.id.AddButton);
		headerText = (TextView)findViewById(R.id.headerText);
		callButton = (ImageButton) findViewById(R.id.calbutton);
		callButton.setVisibility(View.GONE);
		recyclerView.setVisibility(View.GONE);
		swipeInfo.setText("Swipe left to Sign In/ Out some user");
		addTask.setVisibility(View.GONE);
		headerText.setText("My Team");
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		taskListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				SelectedUserId="" + team.get(position).getId();
				array_position = position;
				GetUserWorkSiteApi("" + team.get(position).getId());

			}

		});
	}
	private void GetUserWorkSiteApi(String UserId) {

		//int UserId = HomeActivity.user.getId();
		HashMap<String, String> postDataParameters = new HashMap<String, String>();

		postDataParameters.put("userid", String.valueOf(UserId));

		Log.e("" + Utils.GetUserWorksiteActivity, "" + postDataParameters.toString());

		AsyncTaskTimeEm mWebPageTask = new AsyncTaskTimeEm(
				UserListActivity.this, "get", Utils.GetUserWorksiteActivity,
				postDataParameters, true, "Please wait...");
		mWebPageTask.delegate = (AsyncResponseTimeEm) UserListActivity.this;
		mWebPageTask.execute();
	}
	private void getUserList(int userId){
			String getCompanyId=PrefUtils.getStringPreference(getApplicationContext(),PrefUtils.KEY_COMPANY);
			String timeStamp = Utils.getSharedPrefs(UserListActivity.this, userId+getCompanyId+getResources().getString(R.string.teamTimeStampStr));

			if(timeStamp==null || timeStamp.equals(null) || timeStamp.equals("null"))
				timeStamp="";
			
			HashMap<String, String> postDataParameters = new HashMap<String, String>();

			postDataParameters.put("TimeStamp","");
			postDataParameters.put("UserId", String.valueOf(userId));
			postDataParameters.put("CompanyId", PrefUtils.getStringPreference(UserListActivity.this,PrefUtils.KEY_COMPANY));
			Log.e("values","userid: "+String.valueOf(userId)+", TimeStamp: "+timeStamp);
			
			AsyncTaskTimeEm mWebPageTask = new AsyncTaskTimeEm(
					UserListActivity.this, "post", Utils.getTeamAPI,
					postDataParameters, true, "Please wait...");
			mWebPageTask.delegate = (AsyncResponseTimeEm) UserListActivity.this;
			mWebPageTask.execute();

	}

	public class TeamAdapter extends BaseSwipeAdapter {
		private Context context;
		private TextView userName, signInInfo, txtStatus, signOutInfo;
		private ImageView status, shift;
		private User user;

		public TeamAdapter(Context ctx) {
			context = ctx;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return team.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return team.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public void fillValues(final int position, View convertView) {
			// TODO Auto-generated method stub
			user = team.get(position);

			userName = (TextView)convertView.findViewById(R.id.userName);
			shift = (ImageView)convertView.findViewById(R.id.shiftInfo);
			status = (ImageView)convertView.findViewById(R.id.status);
			signInInfo = (TextView)convertView.findViewById(R.id.signInInfo);
			txtStatus = (TextView)convertView.findViewById(R.id.txtUserStatus);
			signOutInfo = (TextView)convertView.findViewById(R.id.signOutInfo);

			userName.setText(user.getFullName());

			if(user.isSignedIn()) {
				signOutInfo.setVisibility(View.GONE);
				status.setImageResource(R.drawable.online);
				signInInfo.setText("In: "+user.getSignInAt());
				txtStatus.setText("Sign Out");
			}else {
				status.setImageResource(R.drawable.offline);

				if(user.getSignInAt()==null || user.getSignInAt().equals("")) {
					signInInfo.setVisibility(View.GONE);
					signOutInfo.setVisibility(View.GONE);
				}else {
					signInInfo.setVisibility(View.VISIBLE);
					signOutInfo.setVisibility(View.VISIBLE);
					signInInfo.setText("In: " + user.getSignInAt());
					signOutInfo.setText("Out: " + user.getSignOutAt());
				}

				txtStatus.setText("Sign In");
			}

			if(user.isNightShift())
				shift.setImageResource(R.drawable.night);
			else
				shift.setImageResource(R.drawable.day);

			txtStatus.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					user = team.get(position);

					if(user.isSignedIn()) {
						Utils.ChangeStatus(UserListActivity.this, "" + user.getId(), "signOut");
					}else{
						Utils.ChangeStatus(UserListActivity.this, "" + user.getId(), "signIn");
					}
				}
			});
		}

		@Override
		public View generateView(int arg0, ViewGroup arg1) {
			return LayoutInflater.from(UserListActivity.this).inflate(
					R.layout.user_row, null);

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
		if(methodName.equals(Utils.getTeamAPI)) {
			Log.e("output", ",,, ::: " + output);
			ArrayList<User> teamMembers = parser.getTeamList(output, methodName);
			TimeEmDbHandler dbHandler = new TimeEmDbHandler(UserListActivity.this);
			dbHandler.updateTeam(teamMembers);

			String companyId=PrefUtils.getStringPreference(UserListActivity.this,PrefUtils.KEY_COMPANY);
			///////////
			User user=null;
			String json = Utils.getSharedPrefs(UserListActivity.this, PrefUtils.KEY_USER);
			Gson gson = new Gson();
			if(json != "") {
				user = gson.fromJson(json, User.class);
			}
			/////////////
			if(user!=null) {

				if (user.getUserType().equalsIgnoreCase("admin")) {
					team = dbHandler.getTeam("Admin", userId, companyId); // *****
				} else {
					team = dbHandler.getTeam("", userId,companyId);
				}
			}
			else{
				team = dbHandler.getTeam("", userId,companyId);
			}

			taskListview.setAdapter(new TeamAdapter(UserListActivity.this));
		}
		else if (methodName.contains(Utils.GetUserWorksiteActivity)) {
			parser = new Time_emJsonParser(UserListActivity.this);
			UserWorkSiteData userdata = parser.getListSites(output);
			//ArrayList<ListSites> array_worksite = parser.getListSites(output);
			//ArrayList<UserWorkSite> array_worksite = parser.getUserWorkSite(output);

			Gson gson = new Gson();
			dbHandler.updateGeoGraphData1(SelectedUserId,gson.toJson(userdata));
			UserWorkSiteData data = dbHandler.getGeoGraphData1(SelectedUserId);
			/*for(int i=0;i<array_worksite.size();i++) {
				Gson gson = new Gson();
				// This can be any object. Does not have to be an arraylist.
				String allData = gson.toJson(array_worksite.get(i).WerksiteDates);
				dbHandler.updateGeoGraphData(SelectedUserId,allData, array_worksite.get(i).SiteName);

			}*/
			goToNext();
			//array_worksite=  dbHandler.getGeoGraphData();
			//fetchDataGraphs(array_worksite);


			// array_worksite.clear();
			// array_worksite= dbHandler.getGeoGraphData("10");
			//Log.e("UserWorkSite", "" + array_worksite.size());
			//setColorWithModel(array_worksite);
			//settingGraph(array_worksite); // setting graphs with bar
		}
		else{
//			Utils.showToast(UserListActivity.this, output);
			getUserList(userId);
		}
	}
	private void goToNext() {
		Intent intent = new Intent(UserListActivity.this, TaskListActivity.class);
		intent.putExtra("UserId", ""+team.get(array_position).getId());
		intent.putExtra("UserName", team.get(array_position).getFirstName());
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PrefUtils.setStringPreference(UserListActivity.this,PrefUtils.KEY_TPCheck,"true");
	}

}