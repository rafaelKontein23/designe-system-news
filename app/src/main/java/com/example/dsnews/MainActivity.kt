package com.example.dsnews

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dsnews.databinding.ActivityMainBinding
import com.example.mylibrary.ds.text.DsText

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupButtonListeners()
        setupTitle()

        binding.btn1.setDsBackgroundColorResource(R.color.black)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setToolbarTitle("Meu App", DsText.TextStyle.DESCRIPTION)
            setBackButton(show = true) {
                finish()
            }
        }
    }

    private fun setupTitle() {
        binding.titulo.setColoredText(
            "Ainda não tem conta? " to Color.GRAY,
            "Cadastrar" to Color.BLUE
        )
    }

    private fun setupButtonListeners() {
        binding.btn1.setDsClickListener {
            binding.text.setText("Botão Primary clicado!")
            binding.text.error = "Botão Primary clicado!"
            binding.textz.error = "Botão Primary clicado!"
            Toast.makeText(this, "Primary Button", Toast.LENGTH_SHORT).show()
        }

        binding.btn2.setDsClickListener {
            binding.text.setText("Botão Secondary clicado!")
            Toast.makeText(this, "Secondary Button", Toast.LENGTH_SHORT).show()
        }

        binding.btn3.setDsClickListener {
            binding.text.setText("Botão Outlined clicado!")
            Toast.makeText(this, "Outlined Button", Toast.LENGTH_SHORT).show()
        }

        binding.btn4.setDsClickListener {
            binding.text.setText("Botão Text clicado!")
            Toast.makeText(this, "Text Button", Toast.LENGTH_SHORT).show()
        }

        binding.btn5.setDsClickListener {
            binding.text.setText("Botão Danger clicado!")
            Toast.makeText(this, "Danger Button", Toast.LENGTH_SHORT).show()
        }
    }
}
