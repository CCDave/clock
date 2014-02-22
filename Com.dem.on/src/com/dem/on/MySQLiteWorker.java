package com.dem.on;
import com.dem.on.MySQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MySQLiteWorker {
	private MySQLiteOpenHelper sqlHelpRecord = null; 
	private SQLiteDatabase sqlRecord = null; 	
	
	public MySQLiteWorker(Context context){
		sqlHelpRecord = new MySQLiteOpenHelper(context);// 实例一个数据库辅助器
		sqlRecord = sqlHelpRecord.getWritableDatabase();
	}
	public void CreateDataBase(){
		
	}
	public void CreateDataTable(String strTableName){
		String TABLE_NAME = strTableName;
		String ID = "id";
		String NAME = "name";
		String RECORD_DIR = "record_dir";
		String PICTURE_DIR = "picture_dir";
		String TIME = "time";
		String WAY = "way";
		String BEIZHU = "beizhu";
		String ZHENDONG = "zhendong";
		String CHONGFU = "chongfu";
		String LINGSHEN_DIR = "lingshen_dir";
		String CLOCKTIME = "clocktime";
		String USERNAME = "username";
		String UPLOADTIME = "uploadtime";
		String HEAD_DIR = "head_dir";
		String USE_TIMES = "use_time";
		
		String str_sql2 = "CREATE TABLE " 
				+ TABLE_NAME 
				+ "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ NAME
				+ " text,"
				+ RECORD_DIR
				+ " text,"
				+ PICTURE_DIR
				+ " text,"
				+ TIME
				+ " text,"
				+ BEIZHU
				+ " text,"
				+ ZHENDONG
				+ " text,"
				+ CHONGFU
				+ " text,"
				+ LINGSHEN_DIR
				+ " text,"
				+ CLOCKTIME
				+ " text,"
				+ USERNAME
				+ " text,"
				+ UPLOADTIME
				+ " text,"
				+ HEAD_DIR
				+ " text,"
				+ USE_TIMES
				+ " text,"
				+ WAY
				+ " text);";
		try {
			sqlRecord.execSQL(str_sql2);
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("提示:数据库存在", "TABLE_NAME");
		}
		
		
	}
	public void DeleteDataTable(String strTableName){
		String str_sql = "DROP TABLE " + strTableName;
		if (sqlRecord != null){
			sqlRecord.execSQL(str_sql);
		}
		
	}
	public void InsertData(String strtablename, ContentValues cv){
		
		sqlRecord.insert(strtablename, null, cv);
	}
	public void DeleteData(String tableName, int ID){//TODO 添加key = id 删除
		sqlRecord.delete(tableName, MySQLiteOpenHelper.ID + "="+ ID, null);
	}
	public void UpdateTable(){
		
	}
	public Cursor FindData(String strTableName, int ID){
		Log.i("测试数据", ""+ID);
		Cursor cur = sqlRecord.rawQuery("SELECT * FROM "
				+ strTableName, null);
		if (cur.getCount() != 0) {
			while (cur.moveToNext()) {
				if (Integer.parseInt(cur.getString(0)) == ID){
					Log.i("DataBaseItem0", cur.getString(0));
					Log.i("DataBaseItem1", cur.getString(1));
					Log.i("DataBaseItem2", cur.getString(2));
					Log.i("DataBaseItem3", cur.getString(3));
					Log.i("DataBaseItem4", cur.getString(4));
					Log.i("DataBaseItem5", cur.getString(5));
					Log.i("DataBaseItem6", cur.getString(6));
					Log.i("DataBaseItem7", cur.getString(7));
					Log.i("DataBaseItem8", cur.getString(8));
					Log.i("DataBaseItem9", cur.getString(9));
					Log.i("DataBaseItem10", cur.getString(10));
					Log.i("DataBaseItem11", cur.getString(11));
					Log.i("DataBaseItem12", cur.getString(12));
					return cur;
				}	
			}
		}
		return cur;
	}
	public void UpdateData(String strTableName, int ID, ContentValues cv){
		sqlRecord.update(strTableName, cv, "id " + "=" + ID, null);
	}
	
	public SQLiteDatabase getDataBase(){
		return sqlRecord;
	}
	
	public void EnumDataBase(String strTableName){
		
		Log.i("MyGesture", "onScroll");  
		Cursor cur = sqlRecord.rawQuery("SELECT * FROM "
				+ strTableName, null);
		if (cur.getCount() != 0) {
			//String name;
			//String time;
			
			while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
				//name = cur.getString(0); 
				// 参数0 指的是列的下标,这里的0指的是id列
				//time = cur.getString(1);
				// 这里的0相对于当前应该是咱们的text列了
				Log.i("DataBaseItem0", cur.getString(0));
				Log.i("DataBaseItem1", cur.getString(1));
				Log.i("DataBaseItem2", cur.getString(2));
				Log.i("DataBaseItem3", cur.getString(3));
				Log.i("DataBaseItem4", cur.getString(4));
				Log.i("DataBaseItem5", cur.getString(5));
				Log.i("DataBaseItem6", cur.getString(6));
				Log.i("DataBaseItem7", cur.getString(7));
				Log.i("DataBaseItem8", cur.getString(8));
				Log.i("DataBaseItem9", cur.getString(9));
				Log.i("DataBaseItem10", cur.getString(10));
				Log.i("DataBaseItem11", cur.getString(11));
				Log.i("DataBaseItem12", cur.getString(12));
				
			}
		}
	}
	
}
