package com.example.bugcatyyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class CatalogoActivity : AppCompatActivity() {

    lateinit var navegacion: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        navegacion = findViewById(R.id.menuNavegacion)
        navegacion.setOnNavigationItemSelectedListener { item->

            when(item.itemId){
                R.id.itemFragment1 -> {

                    supportFragmentManager.commit {
                        replace<CatalogoFragment>(R.id.fragmentContainer)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.itemFragment2 -> {
                    supportFragmentManager.commit {
                        replace<CarritoFragment>(R.id.fragmentContainer)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.itemFragment3 -> {
                    supportFragmentManager.commit {
                        replace<UsuarioFragment>(R.id.fragmentContainer)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }

        false
    }

        val catalogoFragment = CatalogoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, catalogoFragment)
            .addToBackStack(null)
            .commit()

    }

}

