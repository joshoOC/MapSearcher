package com.angiie.mapsearcher

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Ubicaciones(context: Context) {

    var miContexto: Context? = null
    var manejador: SQLiteConnection? = null
    var gestor: SQLiteDatabase? = null

    init {
        miContexto = context
        manejador = SQLiteConnection(context, "bdLugares", null, 1)
    }

    fun openBD() {
        gestor = manejador?.writableDatabase
    }

    fun registrarLugar(nombreLugar: String, latitud: Double, longitud: Double) {
        var valores = ContentValues()
        valores.put("nombreLugar", nombreLugar)
        valores.put("latitud", latitud)
        valores.put("longitud", longitud)

        gestor?.insert("lugares", null, valores)

    }

    fun consultarLugar(nombre: String): String {
        var unLugar = ""
        var query = "SELECT*FROM lugares WHERE nombreLugar = ?;"
        var parametros = arrayOf("$nombre")
        var cursor: Cursor? = gestor?.rawQuery(query, parametros)
        if (cursor!!.moveToFirst()) {
            unLugar = "${cursor.getDouble(2)}," +
                    "${cursor.getDouble(3)}"
        } else {
            return ""
        }
        return unLugar
    }

    inner class SQLiteConnection(
        context: Context,
        name: String,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
    ) : SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(db: SQLiteDatabase?) {
            var query: String =
                "CREATE TABLE lugares(id Integer primary key autoincrement, nombreLugar text, latitud number, longitud number);"
            db?.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            var query: String = "DROP TABLE IF EXISTS lugares"
            db?.execSQL(query)
            query =
                "CREATE TABLE lugares(id Integer primary key autoincrement, nombreLugar text, latitud number, longitud number);"
            db?.execSQL(query)
        }
    }


}