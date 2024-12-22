package com.example.bugcatyyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MenuAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)
    }


    fun MantenimientoProducto(view: View){
        startActivity(Intent(this, MantenimientoActivity::class.java))
    }


}