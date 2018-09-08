package com.example.hv12.ejercicio_1_temporizador;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 7/9/18.
 */

public class ContadorAsincrono extends AsyncTask<Integer,Integer,String> {


    boolean pausa = false;
    Context context;
    TextView lblContador;
    Button btn;
    private String VIGILANTE = "VIGILANTE";


    public ContadorAsincrono(Context context, TextView lblContador, Button btn) {
        this.context = context;
        this.lblContador = lblContador;
        this.btn=btn;
    }

    @Override
    protected String doInBackground(Integer... integers) {

        int i = integers[0];

        while (i>=0){
            publishProgress(i);
            i--;
            esperarUnSegundo();
            /** si esta pausado**/
            if(pausa==true){
                synchronized (VIGILANTE){
                    try {
                        /**realiza pausa  en el hilo**/
                        VIGILANTE.wait();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }/**sale del sincronized por lo que ya no hay pausa*/
                    pausa = false;
                }
            }

        }
        return "Finalizado";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        lblContador.setText(values[0]+" Seg");
        super.onProgressUpdate(values);
    }

    private void esperarUnSegundo() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignore) {}
    }

    public boolean esPausa(){
        return pausa;
    }

    @Override
    protected void onPostExecute(String s) {
        btn.setText("Reiniciar");
        super.onPostExecute(s);
    }

    public void pausarContador(){
        pausa = true;
    }
    /** notifica a VIGILANTE en todas sus llamadas con syncronized**/
    public void reanudarContador(){
        pausa=false;
        synchronized (VIGILANTE){
            VIGILANTE.notify();
        }
    }
}
