//Projeto: TCC
//Version 0.1

package com.rafael.tcc

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    //Declaração de variáveis globais
    private var email: String? = null
    private var senha: String? = null

    //Elementos da interface
    private var tv_forgot_password: TextView? = null
    private var txt_email: TextView? = null
    private var txt_senha: TextView? = null
    private var btn_login: TextView? = null
    private var btn_criar_conta: TextView? = null
    private var mProgressBar: ProgressDialog? = null


    //Referências ao Firabase
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inicializar()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColorTo(R.color.colorPrimary)
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun Window.setStatusBarColorTo(color: Int){
        this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = ContextCompat.getColor(baseContext, color)
    }

    fun Intent.change(){
        startActivity(this)
        finish()

    }

    private fun inicializar(){
        tv_forgot_password = findViewById(R.id.tv_forgot_password) as TextView
        txt_email = findViewById(R.id.txt_email) as EditText
        txt_senha = findViewById(R.id.txt_senha) as EditText
        btn_login = findViewById(R.id.btn_login) as Button
        btn_criar_conta = findViewById(R.id.btn_criar_conta) as Button
        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()

        tv_forgot_password!!.setOnClickListener(){ startActivity(Intent(this@LoginActivity, EsqueceSenhaActivity::class.java))
        }

        btn_criar_conta!!.setOnClickListener(){ startActivity(Intent(this@LoginActivity, CriarContaActivity::class.java))
        }

        btn_login!!.setOnClickListener(){loginUser()}

    }

    private fun loginUser() {
        email = txt_email?.text.toString()
        senha = txt_senha?.text.toString()

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)){
            mProgressBar!!.setMessage("Verificando o usuário")
            mProgressBar!!.show()

            Log.d(TAG, "Login do usuário")

            //Verificando o usuário e senha
            mAuth!!.signInWithEmailAndPassword(email!!,senha!!).addOnCompleteListener(this){task ->
                mProgressBar!!.hide()

                //Verificando o sucesso ou falha do login e atualizando a interface do usuário
                if(task.isSuccessful){
                    Log.d(TAG, "Logado com sucesso")
                    updateUi()
                }else{
                    Log.e(TAG, "Usuário ou senha inválido", task.exception)
                    Toast.makeText(this@LoginActivity, "Erro de autenticação, verifique as informações", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this@LoginActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }


    }

    private fun updateUi() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}

