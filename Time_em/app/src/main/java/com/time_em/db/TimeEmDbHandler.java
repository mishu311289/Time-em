package com.time_em.db;

import java.sql.Time;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.time_em.model.Company;
import com.time_em.model.ListSites;
import com.time_em.model.UserWorkSite;
import com.time_em.model.Notification;
import com.time_em.model.SpinnerData;
import com.time_em.model.TaskEntry;
import com.time_em.model.User;
import com.time_em.model.UserWorkSiteData;
import com.time_em.model.WorkSiteList;
import com.time_em.model.mutiUserworkSiteList;
import com.time_em.utils.PrefUtils;
import com.time_em.utils.Utils;

public class TimeEmDbHandler extends SQLiteOpenHelper {

	//todo The Android's default system path of your application database.
	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/com.time_em.android/databases/";
	private static String DB_NAME = "TimeEm_db.sqlite";
	private SQLiteDatabase myDataBase;

	private final Context myContext;
	//todo tables names
	private String TABLE_TASK = "Task";
	private String TABLE_TEAM = "Team";
	private String TABLE_ACTIVE_USERS = "ActiveUsers";
	private String TABLE_NOTIFICATIONS = "Notifications";
	private String TABLE_NOTIFICATIONS_TYPE = "NotificationsType";
	private String TABLE_PROJECT_TASK = "ProjectTask";
	private String TABLE_GEOGRAPH = "GeoGraph";
	private String TABLE_GEOGRAPH_DATA = "GeoGraphData";
	private String TABLE_USERTASK = "UserTask";
	private String TABLE_USER_SIGN_INOUT = "UserSignInOut";
	private String TABLE_COMPANY = "company";


	//todo fields for task table
	private String Id = "Id";
	private String ActivityId = "ActivityId";
	private String TaskId = "TaskId";
	private String UserId = "UserId";
	private String TaskName = "TaskName";
	private String Comments = "Comments";
	private String StartTime = "StartTime";
	private String CreatedDate = "CreatedDate";
	private String EndTime = "EndTime";
	private String SelectedDate = "SelectedDate";
	private String Token = "Token";
	private String AttachmentImageFile = "AttachmentImageFile";
	private String TimeSpent = "TimeSpent";
	private String SignedInHours = "SignedInHours";
	private String TaskDate = "TaskDate";
	private String UniqueNumber = "UniqueNumber";
	//todo fields for user table
	// private String UserId = "UserId";
	private String SupervisorId = "SupervisorId";
	private String UserTypeId = "UserTypeId";
	private String DepartmentId = "DepartmentId";
	private String CompanyId = "CompanyId";
	private String WorkSiteId = "WorkSiteId";
	private String ProjectId = "ProjectId";
	// private String ActivityId = "ActivityId";
	private String TaskActivityId = "TaskActivityId";
	private String LoginID = "LoginID";
	private String SignOutAt = "SignOutAt";
	private String SignInAt = "SignInAt";
	private String FirstName = "FirstName";
	private String LastName = "LastName";
	private String FullName = "FullName";
	private String LoginCode = "LoginCode";
	private String Supervisor = "Supervisor";
	private String UserType = "UserType";
	private String Department = "Department";
	private String Company = "Company";
	private String Worksite = "Worksite";
	private String Project = "Project";
	private String IsSecurityPin = "IsSecurityPin";
	private String NfcTagId = "NfcTagId";
	// private String Token="Token";
	private String ReferenceCount = "ReferenceCount";
	private String IsSignedIn = "IsSignedIn";
	private String IsNightShift = "IsNightShift";
	private String SignedHours = "SignedHours";

	//todo fields for notification table
	private String NotificationId = "NotificationId";
	private String SenderId = "SenderId";
	private String NotificationType = "NotificationType";
	private String AttachmentPath = "AttachmentPath";
	private String Subject = "Subject";
	private String Msg = "Msg";
	private String notificationTypeId = "notificationTypeId";
	private String SenderFullName = "SenderFullName";
	private String TimeZone = "TimeZone";
	private String IsOffline = "IsOffline";


	//todo fields for message type table
	private String MessageId = "MessageId";
	private String MessageType = "MessageType";

	//todo fields for GeoGraphs type table
	private String hoursCount = "hoursCount";
	private String workSiteName = "workSiteName";
	//private String workSiteId = "workSiteId";
	private String InTime = "InTime";
	private String OutTime = "OutTime";
	private String DateData = "DateTime";
	private String SiteName = "SiteName";
	private String allData = "allData";

	//todo fields for user Sign in Out type table
	private String SignInTimeSpent = "SignInTimeSpent";
	private String SignOutTimeSpent = "SignOutTimeSpent";

	//todo fields for COMPANY table
	private String Key = "key";
	private String CompanyName = "companyName";


	SQLiteCursor cursor;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */

	public TimeEmDbHandler(Context context) {

		super(context, DB_NAME, null, 2);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TASK_TABLE = "CREATE TABLE if NOT Exists " + TABLE_TASK
				+ "(" + Id + " TEXT," + ActivityId + " TEXT," + TaskId
				+ " TEXT," + UserId + " TEXT," + TaskName + " TEXT," + Comments
				+ " TEXT," + StartTime + " TEXT," + CreatedDate + " TEXT,"
				+ EndTime + " TEXT," + SelectedDate + " TEXT," + Token
				+ " TEXT," + TimeSpent + " TEXT," + SignedInHours + " TEXT," + AttachmentImageFile + " TEXT," + TaskDate
				+ " TEXT," + IsOffline + " TEXT,"+ CompanyId + " TEXT,"+UniqueNumber + " TEXT)";


		String CREATE_USER_TABLE = "CREATE TABLE if NOT Exists " + TABLE_TEAM
				+ "(" + UserId + " TEXT," + SupervisorId + " TEXT,"
				+ UserTypeId + " TEXT," + DepartmentId + " TEXT," + CompanyId
				+ " TEXT," + WorkSiteId + " TEXT," + ProjectId + " TEXT,"
				+ ActivityId + " TEXT," + TaskActivityId + " TEXT," + LoginID
				+ " TEXT," + SignOutAt + " TEXT," + SignInAt + " TEXT,"
				+ FirstName + " TEXT," + LastName + " TEXT," + FullName
				+ " TEXT," + LoginCode + " TEXT," + Supervisor + " TEXT,"
				+ UserType + " TEXT," + Department + " TEXT," + Company
				+ " TEXT," + Worksite + " TEXT," + Project + " TEXT,"
				+ IsSecurityPin + " TEXT," + NfcTagId + " TEXT," + Token
				+ " TEXT," + ReferenceCount + " TEXT," + IsSignedIn + " TEXT,"
				+ IsNightShift + " TEXT," + SignedHours + " TEXT)";

		String CREATE_ACTIVE_USERS_TABLE = "CREATE TABLE if NOT Exists " + TABLE_ACTIVE_USERS
				+ "(" + Id + " TEXT," + FullName + " TEXT,"+ CompanyId + " TEXT)";

		String CREATE_NOTIFICATION_TABLE = "CREATE TABLE if NOT Exists " + TABLE_NOTIFICATIONS
				+ "(" + NotificationId + " TEXT," + SenderId + " TEXT," + NotificationType + " TEXT,"
				+ AttachmentPath + " TEXT," + Subject + " TEXT," + Msg + " TEXT," + CreatedDate + " TEXT,"
				+ SenderFullName + " TEXT,"+ TimeZone + " TEXT,"+ notificationTypeId + " TEXT,"
				+ IsOffline + " TEXT," + UserId + " TEXT,"+ CompanyId + " TEXT,"+UniqueNumber + " TEXT)";


		String CREATE_NOTIFICATION_TYPE_TABLE = "CREATE TABLE if NOT Exists " + TABLE_NOTIFICATIONS_TYPE
				+ "(" + MessageId + " TEXT," + MessageType + " TEXT)";

		String CREATE_PROJECT_TASKS = "CREATE TABLE if NOT Exists " + TABLE_PROJECT_TASK
				+ "(" + MessageId + " TEXT," + MessageType + " TEXT,"+ CompanyId + " TEXT)";

		String CREATE_GEO_GRAPHS = "CREATE TABLE if NOT Exists " + TABLE_GEOGRAPH
				+ "(" + UserId + " TEXT,"+ allData + " TEXT," + DateData + " TEXT)";

		String CREATE_GEO_GRAPHS_DATA = "CREATE TABLE if NOT Exists " + TABLE_GEOGRAPH_DATA
				+ "(" + UserId + " TEXT,"+ allData + " TEXT," + SiteName + " TEXT)";

		String CREATE_USERTASK = "CREATE TABLE if NOT Exists " + TABLE_USERTASK
				+ "(" + CreatedDate + " TEXT," + TimeSpent + " TEXT," +CompanyId + " TEXT)";;

		String CREATE_USER_SIGN_INOUT = "CREATE TABLE if NOT Exists " + TABLE_USER_SIGN_INOUT
				+ "(" + CreatedDate + " TEXT," + SignInTimeSpent + " TEXT," + SignOutTimeSpent + " TEXT,"+CompanyId + " TEXT)";

		String CREATE_COMPANY = "CREATE TABLE if NOT Exists " + TABLE_COMPANY
				+ "(" + CompanyId + " TEXT," + UserId + " TEXT," + CompanyName + " TEXT)";


		db.execSQL(CREATE_TASK_TABLE);
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_ACTIVE_USERS_TABLE);
		db.execSQL(CREATE_NOTIFICATION_TABLE);
		db.execSQL(CREATE_NOTIFICATION_TYPE_TABLE);
		db.execSQL(CREATE_PROJECT_TASKS);
		db.execSQL(CREATE_GEO_GRAPHS);
		db.execSQL(CREATE_GEO_GRAPHS_DATA);
		db.execSQL(CREATE_USERTASK);
		db.execSQL(CREATE_USER_SIGN_INOUT);
		db.execSQL(CREATE_COMPANY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	//todo delete all tables
	public void deleteTaskTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASK, null, null);
		db.close();
	}

	public void deleteTeamTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASK, null, null);
		db.close();
	}
	public void deleteNotificationsTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTIFICATIONS, null, null);
		db.close();
	}

	public void deleteActiveUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ACTIVE_USERS, null, null);
		db.close();
	}

	public void deleteTABLE_NOTIFICATIONS_TYPE() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTIFICATIONS_TYPE, null, null);
		db.close();
	}
	public void deleteProjectTask() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROJECT_TASK, null, null);
		db.close();
	}
	public void deleteGeoGraphs() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GEOGRAPH_DATA, null, null);
		db.close();
	}
	public void deleteUSERTASK() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERTASK, null, null);
		db.close();
	}
	public void deleteUSER_SIGNINOUT() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER_SIGN_INOUT, null, null);
		db.close();
	}
	public void deleteCOMPANY() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COMPANY, null, null);
		db.close();
	}

	//todo update and get from all users
	public void updateActiveUsers(ArrayList<User> activeUsers) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < activeUsers.size(); i++) {
			User user = activeUsers.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_ACTIVE_USERS + " where "
					+ Id + "=" + user.getId() +" AND "+ CompanyId + "=" + user.getCompanyId();
			try {
				ContentValues values = new ContentValues();
				values.put(Id, String.valueOf(user.getId()));
				values.put(FullName, user.getFullName());
				values.put(CompanyId, String.valueOf(user.getCompanyId()));

				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
//db.update(TABLE_TEAM, values, UserId + " = ? AND " + CompanyId + " = ?", new String[] { String.valueOf(user.getId()),companyId });
						db.update(TABLE_ACTIVE_USERS, values, Id + " = ? AND " + CompanyId + " = ?",new String[] { String.valueOf(user.getId()),String.valueOf(user.getCompanyId())});
				} else {
						db.insert(TABLE_ACTIVE_USERS, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	public void updateTask(ArrayList<TaskEntry> taskList, String taskDate,boolean insert) {
		// Fetch only records with selected Date

		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntry taskEntry = taskList.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_TASK + " where "
					+ Id + "=" + taskEntry.getId();
			try {
				ContentValues values = new ContentValues();
				values.put(Id, String.valueOf(taskEntry.getId()));
				values.put(ActivityId,String.valueOf(taskEntry.getActivityId()));
				values.put(TaskId, String.valueOf(taskEntry.getTaskId()));
				values.put(UserId, String.valueOf(taskEntry.getUserId()));
				values.put(TaskName, taskEntry.getTaskName());
				values.put(Comments, taskEntry.getComments());
				values.put(StartTime, taskEntry.getStartTime());
				values.put(CreatedDate,taskEntry.getCreatedDate());
			    values.put(EndTime, taskEntry.getEndTime());
				values.put(SelectedDate, taskEntry.getSelectedDate());
				values.put(Token, taskEntry.getToken());
				values.put(AttachmentImageFile, taskEntry.getAttachmentImageFile());
				values.put(TimeSpent, String.valueOf(taskEntry.getTimeSpent()));
				values.put(SignedInHours,String.valueOf(taskEntry.getSignedInHours()));
				String taskDate1="";
				//CreateDate//27/06/2016 00:00:00
				try {
					taskDate1 = Utils.formatDateChange(taskEntry.getCreatedDate(), "dd/MM/yyyy hh:mm:ss", "MM-dd-yyyy");
				}catch (Exception e)
				{e.printStackTrace();}

				values.put(TaskDate,taskDate1);
				values.put(IsOffline,String.valueOf(taskEntry.getIsoffline()));
				values.put(UniqueNumber,String.valueOf(taskEntry.getUniqueNumber()));
				values.put(CompanyId,String.valueOf(taskEntry.getCompanyId()));
				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if(insert)
				{
					db.insert(TABLE_TASK, null, values);
					}
				else {
					if (cursor.moveToFirst()) {
						// updating row
						if (!taskEntry.getIsActive())
							db.delete(TABLE_TASK, Id + " = ?", new String[]{String.valueOf(taskEntry.getId())});
						else
							db.update(TABLE_TASK, values, Id + " = ?", new String[]{String.valueOf(taskEntry.getId())});
					} else {
						if (taskEntry.getIsActive())
							db.insert(TABLE_TASK, null, values);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}




	public void updateDeleteOffline(ArrayList<TaskEntry> taskList, String taskDate) {
		// Fetch only records with selected Date

		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < taskList.size(); i++) {
			TaskEntry taskEntry = taskList.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_TASK + " where "
					+ UniqueNumber + "=\"" + taskEntry.getUniqueNumber()+"\"";
			try {
				ContentValues values = new ContentValues();
				values.put(Id, String.valueOf(taskEntry.getId()));
				values.put(ActivityId,String.valueOf(taskEntry.getActivityId()));
				values.put(TaskId, String.valueOf(taskEntry.getTaskId()));
				values.put(UserId, String.valueOf(taskEntry.getUserId()));
				values.put(TaskName, taskEntry.getTaskName());
				values.put(Comments, taskEntry.getComments());
				values.put(StartTime, taskEntry.getStartTime());
				values.put(CreatedDate, taskEntry.getCreatedDate());
				values.put(EndTime, taskEntry.getEndTime());
				values.put(SelectedDate, taskEntry.getSelectedDate());
				values.put(Token, taskEntry.getToken());
				values.put(AttachmentImageFile, taskEntry.getAttachmentImageFile());
				values.put(TimeSpent, String.valueOf(taskEntry.getTimeSpent()));
				values.put(SignedInHours,String.valueOf(taskEntry.getSignedInHours()));
				values.put(TaskDate,taskDate);
				values.put(IsOffline,String.valueOf(taskEntry.getIsoffline()));
				values.put(UniqueNumber,String.valueOf(taskEntry.getUniqueNumber()));
				values.put(CompanyId,String.valueOf(taskEntry.getCompanyId()));
				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);

				if (cursor.moveToFirst()) {
					// updating row
					if (!taskEntry.getIsActive())
						db.delete(TABLE_TASK, UniqueNumber + " = ?", new String[]{taskEntry.getUniqueNumber()});
					else
						db.update(TABLE_TASK, values, UniqueNumber + " = ?", new String[]{taskEntry.getUniqueNumber()});
				}
				/*else {
					if (taskEntry.getIsActive())
						db.insert(TABLE_TASK, null, values);
				}*/

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}
	public void updateNotifications(ArrayList<Notification> notificationList) {

		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < notificationList.size(); i++) {
			Notification notification = notificationList.get(i);


			String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS + " where "
					+ NotificationId + "=\"" + notification.getNotificationId()+"\""+
					" AND "+ CompanyId + "=\"" + notification.getCompanyId()+"\"";
			try {

				ContentValues values = new ContentValues();
				values.put(NotificationId, String.valueOf(notification.getNotificationId()));
				values.put(SenderId, String.valueOf(notification.getSenderId()));
				values.put(NotificationType, notification.getNotificationType());
				values.put(AttachmentPath, notification.getAttachmentPath());
				values.put(Subject, notification.getSubject());
				values.put(Msg, notification.getMessage());
				values.put(CreatedDate, notification.getCreatedDate());
				values.put(SenderFullName, notification.getSenderFullName());

				values.put(TimeZone, notification.getTimeZone());
				values.put(notificationTypeId, notification.getNotificationTypeId());
				values.put(IsOffline, notification.getIsOffline());

				values.put(UserId, notification.getUserId());
				values.put(UniqueNumber, notification.getUniqueNumber());
				values.put(CompanyId, notification.getCompanyId());

				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					// updating row
					//db.update(TABLE_TEAM, values, UserId + " = ? AND " + CompanyId + " = ?", new String[] { String.valueOf(user.getId()),companyId })
					db.update(TABLE_NOTIFICATIONS, values, NotificationId + " = ? AND "+CompanyId + " = ?",
							new String[]{String.valueOf(notification.getNotificationId()),String.valueOf(notification.getCompanyId())});
				} else {
					db.insert(TABLE_NOTIFICATIONS, null, values);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	public ArrayList<Notification> getNotificationsByType(String notificationType,
														  boolean all,String str_IsOffline,String companyId) {
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		String selectQuery=null;
		if(all){
			selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS+ " where "
					+ IsOffline + "=\"" + str_IsOffline + "\""+" AND " + CompanyId + "=\"" + companyId + "\"";
		}
		else {
			 selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS +
					 " where " + IsOffline + "=\"" + str_IsOffline + "\"" +" AND " + CompanyId + "=\"" + companyId + "\"";
					//+ NotificationType + "=\"" + notificationType + "\"" +" AND " + IsOffline + "=\"" + str_IsOffline + "\"";
		}
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {

					Notification notification = new Notification();
					notification.setNotificationId(Integer.valueOf(cursor.getString(cursor
							.getColumnIndex(NotificationId))));
					notification.setSenderId(cursor
							.getString(cursor.getColumnIndex(SenderId)));
					notification.setNotificationType(cursor.getString(cursor
							.getColumnIndex(NotificationType)));
					notification.setAttachmentPath(cursor.getString(cursor
							.getColumnIndex(AttachmentPath)));
					notification.setSubject(cursor.getString(cursor
							.getColumnIndex(Subject)));
					notification.setMessage(cursor.getString(cursor
							.getColumnIndex(Msg)));
					notification.setCreatedDate(cursor.getString(cursor
							.getColumnIndex(CreatedDate)));
					notification.setSenderFullName(cursor.getString(cursor
							.getColumnIndex(SenderFullName)));

					notification.setTimeZone(cursor.getString(cursor
							.getColumnIndex(TimeZone)));
					notification.setNotificationTypeId(cursor.getInt(cursor
							.getColumnIndex(notificationTypeId)));
					notification.setIsOffline(cursor.getString(cursor
							.getColumnIndex(IsOffline)));
					notification.setUserId(cursor.getInt(cursor
							.getColumnIndex(UserId)));
					notification.setUniqueNumber(cursor.getString(cursor
							.getColumnIndex(UniqueNumber)));
					notification.setCompanyId(cursor.getString(cursor
							.getColumnIndex(CompanyId)));
					notifications.add(notification);
				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return notifications;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return notifications;
		}
	}

	public void deleteTask(int id){

		SQLiteDatabase db = this.getWritableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_TASK + " where "
				+ Id + "=" + id;
		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				db.delete(TABLE_TASK, Id + " = ?", new String[] { String.valueOf(id) });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();

	}

	public void deleteNotification(int notificationId) {
		SQLiteDatabase db = this.getWritableDatabase();


		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS + " where "
				+ NotificationId + "=" + notificationId;
		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				db.delete(TABLE_NOTIFICATIONS, NotificationId + " = ?", new String[] { String.valueOf(notificationId) });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
	}
	public void deleteNotificationOffline(String offline) {
		SQLiteDatabase db = this.getWritableDatabase();


		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS + " where "
				+ IsOffline + "=\"" + offline + "\"";
		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				db.delete(TABLE_NOTIFICATIONS, IsOffline + " = ?", new String[] { String.valueOf(offline) });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
	}

	public void updateTeam(ArrayList<User> team) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < team.size(); i++) {
			User user = team.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_TEAM + " where "
					+ UserId + "=" + user.getId()+ " AND "+CompanyId + "=\"" + user.getCompanyId() + "\"";
			try {
				ContentValues values = new ContentValues();

				values.put(UserId, user.getId());
				values.put(SupervisorId, String.valueOf(user.getSupervisorId()));
				values.put(UserTypeId, String.valueOf(user.getUserTypeId()));
				values.put(DepartmentId, String.valueOf(user.getDepartmentId()));
				values.put(CompanyId, String.valueOf(user.getCompanyId()));
				values.put(WorkSiteId, String.valueOf(user.getWorkSiteId()));
				values.put(ProjectId, String.valueOf(user.getProjectId()));
				values.put(ActivityId, String.valueOf(user.getActivityId()));
				values.put(TaskActivityId, String.valueOf(user.getTaskActivityId()));
				values.put(LoginID, user.getLoginID());
				values.put(SignOutAt, user.getSignOutAt());
				values.put(SignInAt, user.getSignInAt());
				values.put(FirstName, user.getFirstName());
				values.put(LastName, user.getLastName());
				values.put(FullName, user.getFullName());
				values.put(LoginCode, user.getLoginCode());
				values.put(Supervisor, user.getSupervisor());
				values.put(UserType, user.getUserType());
				values.put(Department, user.getDepartment());
				values.put(Company, user.getCompany());
				values.put(Worksite, user.getWorksite());
				values.put(Project, user.getProject());
				values.put(IsSecurityPin, user.getIsSecurityPin());
				values.put(NfcTagId, user.getNfcTagId());
				values.put(Token, user.getToken());
				values.put(ReferenceCount, String.valueOf(user.isReferenceCount()));
				values.put(IsSignedIn, String.valueOf(user.isSignedIn()));
				values.put(IsNightShift, String.valueOf(user.isNightShift()));
				values.put(SignedHours, String.valueOf(user.getSignedHours()));

				//db.update(TABLE_GEOGRAPH, values, UserId + " = ? AND " + DateData + " = ?",new String[] { strUserId,str_dateData });
				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					if(!user.isActive())
						db.delete(TABLE_TEAM, UserId + " = ?", new String[] { String.valueOf(user.getId()) });
					else
						db.update(TABLE_TEAM, values, UserId + " = ? AND " + CompanyId + " = ?", new String[] { String.valueOf(user.getId()),String.valueOf(user.getCompanyId())});
				} else {
					if(user.isActive())
						db.insert(TABLE_TEAM, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	/*
	 * public void markCheckPointAsChecked(String checkPointId, String
	 * reportedTime, SQLiteDatabase db) { // SQLiteDatabase db =
	 * this.getWritableDatabase();
	 * 
	 * try{ ContentValues values = new ContentValues(); values.put(CheckedTime,
	 * reportedTime); values.put(isChecked, "true");
	 * 
	 * 
	 * int a=db.update(TABLE_SCHEDULE, values, CheckPoint_Id + " = ?", new
	 * String[] { String.valueOf(checkPointId) });
	 * 
	 * }catch(Exception e){ e.printStackTrace(); } // db.close(); }
	 */

	public ArrayList<User> getActiveUsers(String companyId) {
		ArrayList<User> users = new ArrayList<User>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ACTIVE_USERS+" where "+CompanyId+ "=\"" + companyId + "\"";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					User user = new User();
					user.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Id))));
					user.setFullName(cursor.getString(cursor.getColumnIndex(FullName)));
					user.setCompanyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CompanyId))));
					users.add(user);
				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return users;
		}
	}

	public ArrayList<TaskEntry> getTaskEnteries(int userId, String date,boolean all,String companyId) {
		ArrayList<TaskEntry> taskEntryList = new ArrayList<TaskEntry>();
		// Select All Query
		String selectQuery=null;
		if(all){
			selectQuery = "SELECT  * FROM " + TABLE_TASK + " where "
					+ UserId + "=" + userId + " AND " + IsOffline + "=\"" + date + "\""+ " AND " + CompanyId + "=\"" + companyId + "\"";
		}
		else {
			 selectQuery = "SELECT  * FROM " + TABLE_TASK + " where "
					+ UserId + "=" + userId + " AND " + TaskDate + "=\"" + date + "\""+ " AND " + CompanyId + "=\"" + companyId + "\"";
		}
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					TaskEntry taskEntry = new TaskEntry();
					taskEntry.setId(Integer.valueOf(cursor.getString(cursor
							.getColumnIndex(Id))));
					taskEntry.setActivityId(Integer.valueOf(cursor
							.getString(cursor.getColumnIndex(ActivityId))));
					taskEntry.setTaskId(Integer.valueOf(cursor.getString(cursor
							.getColumnIndex(TaskId))));
					taskEntry.setUserId(Integer.valueOf(cursor.getString(cursor
							.getColumnIndex(UserId))));
					taskEntry.setTaskName(cursor.getString(cursor
							.getColumnIndex(TaskName)));
					taskEntry.setComments(cursor.getString(cursor
							.getColumnIndex(Comments)));
					taskEntry.setStartTime(cursor.getString(cursor
							.getColumnIndex(StartTime)));
					taskEntry.setCreatedDate(cursor.getString(cursor
							.getColumnIndex(CreatedDate)));
					taskEntry.setEndTime(cursor.getString(cursor
							.getColumnIndex(EndTime)));
					taskEntry.setSelectedDate(cursor.getString(cursor.getColumnIndex(SelectedDate)));
					taskEntry.setToken(cursor.getString(cursor.getColumnIndex(Token)));
					taskEntry.setAttachmentImageFile(cursor.getString(cursor.getColumnIndex(AttachmentImageFile)));

					String str_TimeSpent=cursor.getString(cursor.getColumnIndex(TimeSpent));
					if(str_TimeSpent!=null && !str_TimeSpent.equalsIgnoreCase("null")) {
						taskEntry.setTimeSpent(Double.valueOf(str_TimeSpent));
					}
					String str_SignedIn=cursor.getString(cursor.getColumnIndex(SignedInHours));
					if(str_SignedIn!=null && !str_SignedIn.equalsIgnoreCase("null")) {
						taskEntry.setSignedInHours(Double.valueOf(str_SignedIn));
					}
					taskEntry.setIsoffline(cursor.getString(cursor.getColumnIndex(IsOffline)));
					taskEntry.setUniqueNumber(cursor.getString(cursor.getColumnIndex(UniqueNumber)));
					taskEntry.setCompanyId(cursor.getString(cursor.getColumnIndex(CompanyId)));
					taskEntryList.add(taskEntry);
				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return taskEntryList;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return taskEntryList;
		}
	}

	public ArrayList<User> getTeam(String check, int userId,String  companyId) {
		ArrayList<User> team = new ArrayList<User>();
		// Select All Query
		String selectQuery=null;
		if(check.equalsIgnoreCase("admin")) {
			selectQuery = "SELECT  * FROM " + TABLE_TEAM + " where " + CompanyId + "=\"" + companyId + "\"";
		}else{
			selectQuery = "SELECT  * FROM " + TABLE_TEAM + " where " + SupervisorId + " = " + userId
					+ CompanyId + "=\"" + companyId + "\"";
		}
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					User user = new User();

					user.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(UserId))));
					user.setSupervisorId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(SupervisorId))));
					user.setUserTypeId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(UserTypeId))));
					user.setDepartmentId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DepartmentId))));
					user.setCompanyId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(CompanyId))));
					user.setWorkSiteId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(WorkSiteId))));
					user.setProjectId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ProjectId))));
					user.setActivityId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ActivityId))));
					user.setTaskActivityId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(TaskActivityId))));
					user.setLoginID(cursor.getString(cursor.getColumnIndex(LoginID)));
					user.setSignOutAt(cursor.getString(cursor.getColumnIndex(SignOutAt)));
					user.setSignInAt(cursor.getString(cursor.getColumnIndex(SignInAt)));
					user.setFirstName(cursor.getString(cursor.getColumnIndex(FirstName)));
					user.setLastName(cursor.getString(cursor.getColumnIndex(LastName)));
					user.setFullName(cursor.getString(cursor.getColumnIndex(FullName)));
					user.setLoginCode(cursor.getString(cursor.getColumnIndex(LoginCode)));
					user.setSupervisor(cursor.getString(cursor.getColumnIndex(Supervisor)));
					user.setUserType(cursor.getString(cursor.getColumnIndex(UserType)));
					user.setDepartment(cursor.getString(cursor.getColumnIndex(Department)));
					user.setCompany(cursor.getString(cursor.getColumnIndex(Company)));
					user.setWorksite(cursor.getString(cursor.getColumnIndex(Worksite)));
					user.setProject(cursor.getString(cursor.getColumnIndex(Project)));
					user.setIsSecurityPin(cursor.getString(cursor.getColumnIndex(IsSecurityPin)));
					user.setNfcTagId(cursor.getString(cursor.getColumnIndex(NfcTagId)));
					user.setToken(cursor.getString(cursor.getColumnIndex(Token)));
					user.setReferenceCount(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ReferenceCount))));
					user.setSignedIn(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(IsSignedIn))));
					user.setNightShift(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(IsNightShift))));
					user.setSignedHours(Double.valueOf(cursor.getString(cursor.getColumnIndex(SignedHours))));

					team.add(user);
				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return team;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return team;
		}
	}
	public User getTeamByLoginCode(String loginCode,String nfcCode,String type) {
		//ArrayList<User> team = new ArrayList<User>();
		User user = null;
		String selectQuery=null;
		// Select All Query
		if(type.equalsIgnoreCase("barcode")) {
			 selectQuery = "SELECT  * FROM " + TABLE_TEAM + " where " + LoginCode +"=\"" + loginCode + "\"";
		}
		else{
			 selectQuery = "SELECT  * FROM " + TABLE_TEAM + " where " + NfcTagId + "=\"" + nfcCode + "\"";
		}
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					 user = new User();

					user.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(UserId))));
					user.setSupervisorId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(SupervisorId))));
					user.setUserTypeId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(UserTypeId))));
					user.setDepartmentId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DepartmentId))));
					user.setCompanyId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(CompanyId))));
					user.setWorkSiteId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(WorkSiteId))));
					user.setProjectId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ProjectId))));
					user.setActivityId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ActivityId))));
					user.setTaskActivityId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(TaskActivityId))));
					user.setLoginID(cursor.getString(cursor.getColumnIndex(LoginID)));
					user.setSignOutAt(cursor.getString(cursor.getColumnIndex(SignOutAt)));
					user.setSignInAt(cursor.getString(cursor.getColumnIndex(SignInAt)));
					user.setFirstName(cursor.getString(cursor.getColumnIndex(FirstName)));
					user.setLastName(cursor.getString(cursor.getColumnIndex(LastName)));
					user.setFullName(cursor.getString(cursor.getColumnIndex(FullName)));
					user.setLoginCode(cursor.getString(cursor.getColumnIndex(LoginCode)));
					user.setSupervisor(cursor.getString(cursor.getColumnIndex(Supervisor)));
					user.setUserType(cursor.getString(cursor.getColumnIndex(UserType)));
					user.setDepartment(cursor.getString(cursor.getColumnIndex(Department)));
					user.setCompany(cursor.getString(cursor.getColumnIndex(Company)));
					user.setWorksite(cursor.getString(cursor.getColumnIndex(Worksite)));
					user.setProject(cursor.getString(cursor.getColumnIndex(Project)));
					user.setIsSecurityPin(cursor.getString(cursor.getColumnIndex(IsSecurityPin)));
					user.setNfcTagId(cursor.getString(cursor.getColumnIndex(NfcTagId)));
					user.setToken(cursor.getString(cursor.getColumnIndex(Token)));
					user.setReferenceCount(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ReferenceCount))));
					user.setSignedIn(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(IsSignedIn))));
					user.setNightShift(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(IsNightShift))));
					user.setSignedHours(Double.valueOf(cursor.getString(cursor.getColumnIndex(SignedHours))));

					//team.add(user);
				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return user;
		}
	}
	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	public void updateStatus(int userId,String activityId,String signInAt,String signOutAt,boolean isSignedIn) {
		SQLiteDatabase db = this.getWritableDatabase();
		//for (int i = 0; i < team.size(); i++) {
		//	User user = team.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_TEAM + " where "
					+ UserId + " = " + userId;
			try {
				ContentValues values = new ContentValues();

				values.put(UserId, userId);
				//values.put(SupervisorId, String.valueOf(user.getSupervisorId()));
				//values.put(UserTypeId, String.valueOf(user.getUserTypeId()));
				//values.put(DepartmentId, String.valueOf(user.getDepartmentId()));
				//values.put(CompanyId, String.valueOf(user.getCompanyId()));
				//values.put(WorkSiteId, String.valueOf(user.getWorkSiteId()));
				//values.put(ProjectId, String.valueOf(user.getProjectId()));
				values.put(ActivityId, String.valueOf(activityId));
				//values.put(TaskActivityId,String.valueOf(user.getTaskActivityId()));
				//values.put(LoginID, user.getLoginID());
				values.put(SignOutAt, signOutAt);
				values.put(SignInAt, signInAt);
			//	values.put(FirstName, user.getFirstName());
			//	values.put(LastName, user.getLastName());
			//	values.put(FullName, user.getFullName());
			//	values.put(LoginCode, user.getLoginCode());
			//	values.put(Supervisor, user.getSupervisor());
			//	values.put(UserType, user.getUserType());
			//	values.put(Department, user.getDepartment());
			//	values.put(Company, user.getCompany());
			//	values.put(Worksite, user.getWorksite());
			//	values.put(Project, user.getProject());
			//	values.put(IsSecurityPin, user.getIsSecurityPin());
			//	values.put(NfcTagId, user.getNfcTagId());
			//	values.put(Token, user.getToken());
			//	values.put(ReferenceCount,String.valueOf(user.isReferenceCount()));
				values.put(IsSignedIn, String.valueOf(isSignedIn));
			//	values.put(IsNightShift, String.valueOf(user.isNightShift()));
			//	values.put(SignedHours, String.valueOf(user.getSignedHours()));

				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					//if(!user.isActive())
					//	db.delete(TABLE_TEAM, UserId + " = ?", new String[] { String.valueOf(user.getId()) });
					//else
						db.update(TABLE_TEAM, values, UserId + " = ?", new String[] { String.valueOf(userId) });
			//	} else {
				//	if(user.isActive())
						//db.insert(TABLE_TEAM, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			//}
		}
		db.close();
	}

	public void updateNotificationType(ArrayList<SpinnerData> messages) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < messages.size(); i++) {
			SpinnerData message = messages.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS_TYPE + " where "
					+ MessageId + "=" + message.getId();
			try {
				ContentValues values = new ContentValues();

				values.put(MessageId, String.valueOf(message.getId()));
				values.put(MessageType, message.getName());

				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					db.update(TABLE_NOTIFICATIONS_TYPE, values, MessageId + " = ?",new String[] { String.valueOf(message.getId()) });
				} else {
					db.insert(TABLE_NOTIFICATIONS_TYPE, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}
	public ArrayList<SpinnerData> getNotificationTypeData() {
		ArrayList<SpinnerData> types = new ArrayList<SpinnerData>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS_TYPE;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					SpinnerData type = new SpinnerData();

					type.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(MessageId))));
					type.setName(cursor.getString(cursor.getColumnIndex(MessageType)));
					types.add(type);

				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return types;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return types;
		}
	}


	public void updateProjectTasks(ArrayList<SpinnerData> messages) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < messages.size(); i++) {
			SpinnerData message = messages.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_PROJECT_TASK + " where "
					+ MessageId + "=" + message.getId()+" AND "+ CompanyId + "=" + message.getCompanyId();
			try {
				ContentValues values = new ContentValues();

				values.put(MessageId, String.valueOf(message.getId()));
				values.put(MessageType, message.getName());
				values.put(CompanyId, message.getCompanyId());

				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					//db.update(TABLE_GEOGRAPH, values, UserId + " = ? AND " + DateData + " = ?",new String[] { strUserId,str_dateData });
					db.update(TABLE_PROJECT_TASK, values, MessageId + " = ? AND "+CompanyId + " = ?",new String[] { String.valueOf(message.getId()),String.valueOf(message.getCompanyId()) });
				} else {
					db.insert(TABLE_PROJECT_TASK, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	public ArrayList<SpinnerData> getProjectTasksData(String companyId) {
		ArrayList<SpinnerData> types = new ArrayList<SpinnerData>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PROJECT_TASK+ " where "+	CompanyId +"=\"" + companyId + "\"";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					SpinnerData type = new SpinnerData();
					type.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(MessageId))));
					type.setName(cursor.getString(cursor.getColumnIndex(MessageType)));
					type.setCompanyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CompanyId))));
					types.add(type);

				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return types;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return types;
		}
	}

	//for insert data Geo Graphs
	public void updateGeoGraph(String strUserId,String Alldata, String str_dateData) {
		// Fetch only records with selected Date

		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_GEOGRAPH + " where "
				+ UserId + "=" + strUserId + " AND " + DateData + "=\"" + str_dateData + "\"";

				try {
					ContentValues values = new ContentValues();
					values.put(UserId, strUserId);
					values.put(allData, String.valueOf(Alldata));
					values.put(DateData, String.valueOf(str_dateData));

					cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
					if (cursor.moveToFirst()) {
						/*db.update(TABLE_NAME,
								contentValues,
								NAME + " = ? AND " + LASTNAME + " = ?",
								new String[]{"Manas", "Bajaj"});*/
						db.update(TABLE_GEOGRAPH, values, UserId + " = ? AND " + DateData + " = ?",new String[] { strUserId,str_dateData });
					} else {
						db.insert(TABLE_GEOGRAPH, null, values);
					}


				} catch (Exception e) {
					e.printStackTrace();
				}

			db.close();

	}

	//for insert data Geo Graphs
	public void updateGeoGraphData(String strUserId,String Alldata, String str_sitename) {
		// Fetch only records with selected Date

		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_GEOGRAPH_DATA + " where "
				+ UserId + "=" + strUserId + " AND " + DateData + "=\"" + str_sitename + "\"";

		try {
			ContentValues values = new ContentValues();
			values.put(UserId, strUserId);
			values.put(allData, String.valueOf(Alldata));
			values.put(SiteName, String.valueOf(str_sitename));

			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
						/*db.update(TABLE_NAME,
								contentValues,
								NAME + " = ? AND " + LASTNAME + " = ?",
								new String[]{"Manas", "Bajaj"});*/
				db.update(TABLE_GEOGRAPH, values, UserId + " = ? AND " + DateData + " = ?",new String[] { strUserId,str_sitename });
			} else {
				db.insert(TABLE_GEOGRAPH, null, values);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		db.close();

	}

	//for insert data Geo Graphs
	public void updateGeoGraphData1(String strUserId,String Alldata) {
		// Fetch only records with selected Date

		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_GEOGRAPH_DATA + " where "
				+ UserId +  "=\"" + strUserId + "\"";

		try {
			ContentValues values = new ContentValues();
			values.put(UserId, strUserId);
			values.put(allData, String.valueOf(Alldata));

			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
						/*db.update(TABLE_NAME,
								contentValues,
								NAME + " = ? AND " + LASTNAME + " = ?",
								new String[]{"Manas", "Bajaj"});*/

				//db.update(TABLE_GEOGRAPH_DATA, values, UserId + " = ? AND " + allData + " = ?",new String[] { strUserId,Alldata });
				db.update(TABLE_GEOGRAPH_DATA, values, UserId + " = ?", new String[] { strUserId });

			} else {
				db.insert(TABLE_GEOGRAPH_DATA, null, values);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		db.close();

	}



	//for insert data Geo Graphs
	public ArrayList<UserWorkSite> getGeoGraph(String strUserId) {
		// Fetch only records with selected Date

		ArrayList<UserWorkSite> types = new ArrayList<UserWorkSite>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_GEOGRAPH + " where "
				+ UserId +  "=\"" + strUserId + "\"";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					UserWorkSite type = new UserWorkSite();
					type.setUserId(cursor.getString(cursor.getColumnIndex(UserId)));
					type.setDate(cursor.getString(cursor.getColumnIndex(DateData)));
  				    type.setAllData(cursor.getString(cursor.getColumnIndex(allData)));
					types.add(type);

				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return types;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return types;
		}

}

	//for insert data Geo Graphs
	public UserWorkSiteData getGeoGraphData1(String strUserId) {
		// Fetch only records with selected Date

		UserWorkSiteData userData = new UserWorkSiteData();
		Gson gson = new Gson();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_GEOGRAPH_DATA + " where "
				+ UserId +  "=\"" + strUserId + "\"";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					UserWorkSiteData type = new UserWorkSiteData();
					String userId = (cursor.getString(cursor.getColumnIndex(UserId)));
					String data = (cursor.getString(cursor.getColumnIndex(allData)));
					userData = gson.fromJson(data,UserWorkSiteData.class);

				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return userData;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return userData;
		}

	}




	public void updateUserTask(Activity activity,ArrayList<TaskEntry> arrayTaskEntry) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < arrayTaskEntry.size(); i++) {
			TaskEntry taskEntry = arrayTaskEntry.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_USERTASK + " where "
					+ CreatedDate +  "=\"" + taskEntry.getCreatedDate() + "\"";
			try {
				ContentValues values = new ContentValues();

				values.put(CreatedDate, String.valueOf(taskEntry.getCreatedDate()));
				values.put(TimeSpent, taskEntry.getTimeSpent());
				values.put(CompanyId,  PrefUtils.getStringPreference(activity,PrefUtils.KEY_COMPANY));

				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					db.update(TABLE_USERTASK, values, CreatedDate + " = ?",new String[] { String.valueOf(taskEntry.getCreatedDate()) });
				} else {
					db.insert(TABLE_USERTASK, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	//todo fetch value form database user task graph value
	public ArrayList<TaskEntry> getUserTask(Activity activity) {
		ArrayList<TaskEntry> types = new ArrayList<TaskEntry>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERTASK + " where "
				+ CompanyId +  "=\"" + PrefUtils.getStringPreference(activity,PrefUtils.KEY_COMPANY) + "\"";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					TaskEntry type = new TaskEntry();

					type.setCreatedDate(cursor.getString(cursor.getColumnIndex(CreatedDate)));
					type.setTimeSpent(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TimeSpent))));
					type.setCompanyId(cursor.getString(cursor.getColumnIndex(CompanyId)));
					types.add(type);

				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return types;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return types;
		}
	}

	//TODO STORE DATA FOR TABLE_USER_SIGN_INOUT
	public void updateUserSignInOut(Activity activity,ArrayList<TaskEntry> arrayTaskEntry) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < arrayTaskEntry.size(); i++) {
			TaskEntry taskEntry = arrayTaskEntry.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_USER_SIGN_INOUT + " where "
					+ CreatedDate +  "=\"" + taskEntry.getCreatedDate() + "\"";
			try {
				ContentValues values = new ContentValues();

				values.put(CreatedDate, String.valueOf(taskEntry.getCreatedDate()));
				values.put(SignInTimeSpent, taskEntry.getSignedInHours());
				values.put(SignOutTimeSpent, taskEntry.getSignedOutHours());
				values.put(CompanyId, PrefUtils.getStringPreference(activity,PrefUtils.KEY_COMPANY));
				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					db.update(TABLE_USER_SIGN_INOUT, values, CreatedDate + " = ?",new String[] { String.valueOf(taskEntry.getCreatedDate()) });
				} else {
					db.insert(TABLE_USER_SIGN_INOUT, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	//todo get user graphs sing in and out data
	public ArrayList<TaskEntry> getUserSignInOut(Activity activity) {
		ArrayList<TaskEntry> types = new ArrayList<TaskEntry>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USER_SIGN_INOUT+ " where "
				+ CompanyId +  "=\"" + PrefUtils.getStringPreference(activity,PrefUtils.KEY_COMPANY) + "\"";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					TaskEntry type = new TaskEntry();

					type.setCreatedDate(cursor.getString(cursor.getColumnIndex(CreatedDate)));
					type.setSignedInHours(Double.parseDouble(cursor.getString(cursor.getColumnIndex(SignInTimeSpent))));
					type.setSignedOutHours(Double.parseDouble(cursor.getString(cursor.getColumnIndex(SignOutTimeSpent))));
					type.setCompanyId(cursor.getString(cursor.getColumnIndex(CompanyId)));
					types.add(type);

				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return types;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return types;
		}
	}

	//TODO STORE DATA FOR TABLE_USER_SIGN_INOUT
	public void updateCompany(ArrayList<Company> arrayCompany,String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < arrayCompany.size(); i++) {
			Company company = arrayCompany.get(i);
			String selectQuery = "SELECT  * FROM " + TABLE_COMPANY
					+ " where "
					+ CompanyId + "=\"" + company.getKey() + "\"";
			try {
				ContentValues values = new ContentValues();
				values.put(CompanyId, String.valueOf(company.getKey()));
				values.put(CompanyName, String.valueOf(company.getValue()));
				values.put(UserId, String.valueOf(userId));

				cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					db.update(TABLE_COMPANY, values, CompanyId + " = ?", new String[]{String.valueOf(CompanyId)});
				} else {
					db.insert(TABLE_COMPANY, null, values);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.close();
	}

	//todo get company data
	public ArrayList<Company> getCompany(String  userId) {
		ArrayList<Company> types = new ArrayList<Company>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_COMPANY
				+ " where "
				+ UserId +  "=\"" + userId + "\"";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = (SQLiteCursor) db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					Company type = new Company();
					type.setKey(cursor.getString(cursor.getColumnIndex(CompanyId)));
					type.setValue(cursor.getString(cursor.getColumnIndex(CompanyName)));
					type.setId(cursor.getString(cursor.getColumnIndex(UserId)));
					types.add(type);

				} while (cursor.moveToNext());
			}

			cursor.getWindow().clear();
			cursor.close();
			// close inserting data from database
			db.close();
			// return city list
			return types;
		} catch (Exception e) {
			e.printStackTrace();
			if (cursor != null) {
				cursor.getWindow().clear();
				cursor.close();
			}

			db.close();
			return types;
		}
	}
}