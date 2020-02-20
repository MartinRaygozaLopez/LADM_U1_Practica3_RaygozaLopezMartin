package com.example.ladm_u1_practica3_raygozalopezmartin

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var vector : Array<Int> = Array(10,{0})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            // PERMISO NO CONCEDIDO, ENTONCES SE SOLICITA
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {
            mensaje("LOS PERMISOS YA FUERON OTORGADOS")
        }

        asignar.setOnClickListener {
            if(valor.text.isEmpty() || posicion.text.isEmpty()){
                mensaje("FAVOR DE LLENAR LOS CAMPOS DE VALOR Y POSICIÓN")
                return@setOnClickListener
            }
            asignarValor(valor.text.toString().toInt(), posicion.text.toString().toInt())
        }

        mostrar.setOnClickListener {
            mostrarLista()
        }

        guardarsd.setOnClickListener {
            guardarArchivoSD()
        }

        leersd.setOnClickListener {
            leerArchivoSD()
        }
    }

    private fun mostrarLista(){
        var datos = ""
        (0..9).forEach {
            if(it == 9){
                datos = datos + vector[it].toString()
            } else {
                datos = datos + vector[it].toString() + ","
            }

        }
        Elista.setText(datos)
    }

    private fun asignarValor(v:Int, p:Int){
        if(p < 0 || p > 9){
            mensaje("INTROZCA UNA POSICIÓN VÁLIDA")
            return
        }
        vector[p] = v
        valor.setText("")
        posicion.setText("")
        mensaje("SE INSERTÓ EL VALOR " + v +" EN EL VECTOR")
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){id,i->}
            .show()
    }

    private fun guardarArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        var data = ""
        (0..9).forEach {
            if(it == 9){
                data = data + vector[it].toString()
            } else {
                data = data + vector[it].toString() + ","
            }

        }

        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nombreG.text.toString() + ".txt")
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("EXITO! se creó el archivo")
            nombreG.setText("")
        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA")
            return
        }

        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nombreL.text.toString() + ".txt")
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()
            var vec = data.split(",")

            (0..9).forEach {
                vector[it] = vec[it].toInt()
            }

            mostrarLista()

            nombreL.setText("")
        }catch (error:IOException){
            mensaje(error.message.toString())
        }
    }

    private fun noSD():Boolean{

        var estado = Environment.getExternalStorageState() //Nos va a regresar si hay una menoria SD insertada en el telefono

        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }

        return false
    }



}
