package com.example.myapplication;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
public class LampsList extends ArrayAdapter {
    private String[] lampNames;     //Названия светильников
    private Integer[] imageid;      //Изображение светильника
    private Activity context;
    public LampsList(Activity context, String[] lampNames, Integer[] imageid) {
        super(context, R.layout.row_lamp, lampNames);
        this.context = context;
        this.lampNames = lampNames;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_lamp, null, true);
        TextView textViewName = (TextView) row.findViewById(R.id.textViewName);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.imageViewLogo);

        textViewName.setText(lampNames[position]);
        imageFlag.setImageResource(imageid[position]);
        return  row;
    }
}


