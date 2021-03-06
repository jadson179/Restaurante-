package com.example.restaurante.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.restaurante.models.vo.Cardapio;
import com.example.restaurante.models.vo.ItemCardapio;

import java.util.ArrayList;

public class DatabaseSqlite extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "restaurante";
    private static final String schema = "create table if not exists cardapio (id integer primary key autoincrement, nome varchar(255), descricao varchar(255),categoria varchar(255), preco real, isGluten numeric,calorias real,image varchar(255) )";

    public DatabaseSqlite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static SQLiteDatabase getDBInstance(Activity activity){
        DatabaseSqlite dao = new DatabaseSqlite(activity.getBaseContext());
        return dao.getWritableDatabase();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(schema);
    }


    public void dropDatabase(Activity activity) {
        activity.deleteDatabase(this.DATABASE_NAME);
    }


    public Cardapio cardapio(Activity activity) {
        Cursor cursor  = DatabaseSqlite.getDBInstance(activity).rawQuery("select id,nome,descricao,categoria,preco,isGluten,calorias,image from cardapio",null);
        ArrayList<ItemCardapio> listitemCardapio =new ArrayList<ItemCardapio>();

        while (cursor.moveToNext()){
            Integer id=cursor.getInt(0);
            String nome=cursor.getString(1);
            String descricao=cursor.getString(2);
            String categoria=cursor.getString(3);
            String preco=cursor.getString(4);
            boolean isGluten= Boolean.parseBoolean(cursor.getString(5));
            double calorias=cursor.getDouble(6);
            String image=cursor.getString(7);
            ItemCardapio itemCardapio = new ItemCardapio(id,nome,descricao,categoria,preco,isGluten,calorias,image);

            listitemCardapio.add(itemCardapio);
        }
        return new Cardapio(listitemCardapio);
    }

    public static void deleteAll(Activity activity) {
        getDBInstance(activity).execSQL("delete from cardapio");
    }
    public static long insert(Activity activity, ItemCardapio itemCardapio){
        ContentValues values = new ContentValues();
        values.put("nome",itemCardapio.getNome());
        values.put("descricao",itemCardapio.getDescricao());
        values.put("categoria",itemCardapio.getCategoria());
        values.put("preco",itemCardapio.getPreco());
        values.put("isGluten",itemCardapio.isGluten());
        values.put("calorias",itemCardapio.getCalorias());
        values.put("image",itemCardapio.getImage());

        return getDBInstance(activity).insert("cardapio",null,values);
    }

}
