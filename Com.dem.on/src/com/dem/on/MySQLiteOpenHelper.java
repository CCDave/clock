
package com.dem.on;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Himi
 * @解释 此类我们只需要传建一个构造函数 以及重写两个方法就OK啦、
 * 
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public final static int VERSION = 1;// 版本号
	public final static String TABLE_NAME = "test4DataBasehimi";// 表名
	public final static String ID = "id";// 后面ContentProvider使用
	public final static String TEXT = "text";
	public final static String NAME = "name";
	public final static String TIME = "time";
	public static final String DATABASE_NAME = "test4DataBase.db";

	public MySQLiteOpenHelper(Context context) {
		// 在Android 中创建和打开一个数据库都可以使用openOrCreateDatabase 方法来实现，
		// 因为它会自动去检测是否存在这个数据库，如果存在则打开，不过不存在则创建一个数据库；
		// 创建成功则返回一个 SQLiteDatabase对象，否则抛出异常FileNotFoundException。
		// 下面是来创建一个名为"DATABASE_NAME"的数据库，并返回一个SQLiteDatabase对象 
		
		super(context, DATABASE_NAME, null, VERSION); 
	} 
	@Override
	// 在数据库第一次生成的时候会调用这个方法，一般我们在这个方法里边生成数据库表;
	public void onCreate(SQLiteDatabase db) { 
		
		//String TABLE_NAME = strTableName;		
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
		db.execSQL(str_sql2);
		
		Log.v("Himi", "onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 一般默认情况下，当我们插入 数据库就立即更新
		// 当数据库需要升级的时候，Android 系统会主动的调用这个方法。
		// 一般我们在这个方法里边删除数据表，并建立新的数据表，
		// 当然是否还需要做其他的操作，完全取决于游戏需求。
		Log.v("Himi", "onUpgrade");
	} 
}  