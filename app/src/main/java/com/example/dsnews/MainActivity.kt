package com.example.dsnews

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mylibrary.ds.button.DsButton
import com.example.mylibrary.ds.input.DsInput

class MainActivity : AppCompatActivity() {
    private lateinit var input: DsInput
    private lateinit var input2: DsInput
    private lateinit var btn1: DsButton
    private lateinit var btn2: DsButton
    private lateinit var btn3: DsButton
    private lateinit var btn4: DsButton
    private lateinit var btn5: DsButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.text)
        input2 = findViewById(R.id.textz)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)

        setupButtonListeners()

        btn1.apply {
            setDsBackgroundColorResource(R.color.black)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupButtonListeners() {
        btn1.setDsClickListener {
            input.setText("Botão Primary clicado!")
            input.error = "Botão Primary clicado!"
            input2.error = "Botão Primary clicado!"
            Toast.makeText(this, "Primary Button", Toast.LENGTH_SHORT).show()
        }

        btn2.setDsClickListener {
            input.setText("Botão Secondary clicado!")
            Toast.makeText(this, "Secondary Button", Toast.LENGTH_SHORT).show()
        }

        btn3.setDsClickListener {
            input.setText("Botão Outlined clicado!")
            Toast.makeText(this, "Outlined Button", Toast.LENGTH_SHORT).show()
        }

        btn4.setDsClickListener {
            input.setText("Botão Text clicado!")
            Toast.makeText(this, "Text Button", Toast.LENGTH_SHORT).show()
        }

        btn5.setDsClickListener {
            input.setText("Botão Danger clicado!")
            Toast.makeText(this, "Danger Button", Toast.LENGTH_SHORT).show()
        }
    }
}