package com.example.sqlite_todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Sb .db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 데이터 베이스가 생성이 될 때 호출
        // 데이터베이스 -> 테이블 -> 컬럼(가로) -> 값(세로)
        db.execSQL(" CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    //SElECT 문 (할일 목록들을 조회)
    public ArrayList<TodoItem> getTodoList() {
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        if (cursor.getCount() != 0) {
            //조회온 데이터가 있을때 내부수행

            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String writeDate = cursor.getString(cursor.getColumnIndexOrThrow("writeDate"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setTitle(title);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDate);
                todoItems.add(todoItem);

            }
        }
        cursor.close();

        return todoItems;
    }

    //INSERT문 (할일 목록을 넣는다.)
    public  void InsertTodo(String _title, String _content, String _writeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Todolist (title, c, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "');");
    }

    // UPDATE문 (할일 목록을 수정한다.)
    public void UpdateTodo(String _title, String _content, String _writeDate, String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" UPDATE Todolist SET title='" + _title + "', content='" + _content + "' , writeDate='" + _writeDate + "' WHERE writeDate='" + _beforeDate + "');");
    }

    //DELETE문 (할일 목록을 제거한다.)
    public void deleteTodo(String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" DELETE FROM Todolist WHERE writeDate = '" + _beforeDate + "');");
    }

}
