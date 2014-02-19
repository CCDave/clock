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
				+ WAY
				+ " text);";
		sqlRecord.execSQL(str_sql2);
		Log.i("DataBase", "执行了CreateDataTable");
	}
	public void DeleteDataTable(String strTableName){
		String str_sql = "DROP TABLE " + strTableName;
		if (sqlRecord != null){
			sqlRecord.execSQL(str_sql);
		}
		
	}
	public void InsertData(ContentValues cv){
		
		sqlRecord.insert(MySQLiteOpenHelper.TABLE_NAME, null, cv);
	}
	public void DeleteData(String tableName, int ID){//TODO 添加key = id 删除
		sqlRecord.delete(tableName, MySQLiteOpenHelper.ID + "="+ ID, null);
	}
	public void UpdateTable(){
		
	}
	public Cursor FindData(String strTableName, int ID){
		String str_sql = "SELECT * FROM " 
				+  strTableName
				+ " ORDER BY ID DESC";
		//此处获取了数据
		Cursor  cur = sqlRecord.rawQuery(str_sql, null);
		return cur;
	}
	public void UpdateData(String strTableName, int ID, ContentValues cv){
		sqlRecord.update(strTableName, cv, "id " + "=" + ID, null);
	}
	public void EnumDataBase(String strTableName){
		
		Log.i("MyGesture", "onScroll");  
		Cursor cur = sqlRecord.rawQuery("SELECT * FROM "
				+ strTableName, null);
		if (cur != null) {
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
				
			}
		}
	}
	
}
