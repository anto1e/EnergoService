package com.example.myapplication;

import android.widget.Toast;

public class SaveExcelThread extends Thread{
    public void run() {
        try {
            Variables.exporter.exportToExel();
            Variables.activity.runOnUiThread(() -> {
                Toast.makeText(Variables.activity.getApplicationContext(),"Экспортировано в Excel!",Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
