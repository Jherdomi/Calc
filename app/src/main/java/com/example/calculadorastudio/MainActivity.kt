package com.example.calculadorastudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    var resultadoTextView: TextView?=null;
    var memoria = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        resultadoTextView=findViewById(R.id.resultadoTextView)
    }

    fun calcular(view : View){

        var boton=view as Button
        var textoBoton=boton.text.toString()
        var concatenar=resultadoTextView?.text.toString()+textoBoton
        var concatenarSinCeros=quitarCerosIzquirda(concatenar)

        if(textoBoton=="="){
            var resultado=0.0
            try {

                resultado=eval(resultadoTextView?.text.toString())
                resultadoTextView?.text=resultado.toString()
            }catch (e:Exception){
                resultadoTextView?.text="Syntax Error"
            }
            resultadoTextView?.text="0"
        }else if(textoBoton=="B"){
            val length = resultadoTextView!!.length()
            if(length > 0)
                resultadoTextView!!.text = resultadoTextView!!.text.subSequence(0, length - 1)
        }else if(textoBoton=="G"){
            memoria = resultadoTextView?.text.toString()

            if(memoria == "+" || memoria == "-" || memoria == "/" || memoria == "*"){
                memoria = "0"
            }
        }else if(textoBoton=="M"){
            resultadoTextView?.text = resultadoTextView?.text.toString() + memoria
        }else{
            resultadoTextView?.text=concatenarSinCeros
        }
    }



    fun quitarCerosIzquirda(str : String):String{
        var i=0

        while (i<str.length && str[i]=='0')i++


        val sb=StringBuffer(str)
        sb.replace(0,i,"")
        return sb.toString()
    }



    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("ERROR")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm()
                    else if (eat('-'.toInt())) x -= parseTerm()
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor()
                    else if (eat('/'.toInt())) x /= parseFactor()
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor()
                if (eat('-'.toInt())) return -parseFactor()
                var x: Double
                val startPos = pos



                if (eat('('.toInt())) {
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) {
                    while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("ERROR")
                }
                return x
            }
        }.parse()
    }


}

