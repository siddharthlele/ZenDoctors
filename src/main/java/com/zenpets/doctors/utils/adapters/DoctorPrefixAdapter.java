package com.zenpets.doctors.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zenpets.doctors.R;

import java.util.List;

public class DoctorPrefixAdapter extends ArrayAdapter<String> {

    /***** THE ACTIVITY INSTANCE FOR USE IN THE ADAPTER *****/
    private final Activity activity;

    /***** LAYOUT INFLATER TO USE A CUSTOM LAYOUT *****/
    private LayoutInflater inflater = null;

    /***** ARRAY LIST TO GET DATA FROM THE ACTIVITY *****/
    private final List<String> arrServes;

    public DoctorPrefixAdapter(Activity activity, int resource, List<String> arrServes) {
        super(activity, resource);
        this.activity = activity;
        this.arrServes = arrServes;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrServes.size();
    }

    @Override
    public String getItem(int position) {
        return arrServes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        /** A VIEW HOLDER INSTANCE **/
        ViewHolder holder;

        /** CAST THE CONVERT VIEW IN A VIEW INSTANCE **/
        View vi = convertView;

        /** CHECK CONVERT VIEW STATUS **/
        if (convertView == null)	{
            /** CAST THE CONVERT VIEW INTO THE VIEW INSTANCE vi **/
            vi = inflater.inflate(R.layout.custom_spinner_row, parent, false);

            /** INSTANTIATE THE VIEW HOLDER INSTANCE **/
            holder = new ViewHolder();

            /*****	CAST THE LAYOUT ELEMENTS	*****/
            holder.txtValue = (AppCompatTextView) vi.findViewById(R.id.txtValue);
            holder.txtValue.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/HelveticaNeueLTW1G-Cn.otf"));

            /** SET THE TAG TO "vi" **/
            vi.setTag(holder);
        } else {
            /** CAST THE VIEW HOLDER INSTANCE **/
            holder = (ViewHolder) vi.getTag();
        }

        /** SET THE PREFIX TEXT **/
        String strPropertyName = arrServes.get(position);
        if (strPropertyName != null)	{
            holder.txtValue.setText(strPropertyName);
        }

        return vi;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        /** A VIEW HOLDER INSTANCE **/
        ViewHolder holder;

        /** CAST THE CONVERT VIEW IN A VIEW INSTANCE **/
        View vi = convertView;

        /** CHECK CONVERT VIEW STATUS **/
        if (convertView == null)	{
            /** CAST THE CONVERT VIEW INTO THE VIEW INSTANCE vi **/
            vi = inflater.inflate(R.layout.custom_spinner_row, parent, false);

            /** INSTANTIATE THE VIEW HOLDER INSTANCE **/
            holder = new ViewHolder();

            /*****	CAST THE LAYOUT ELEMENTS	*****/
            holder.txtValue = (AppCompatTextView) vi.findViewById(R.id.txtValue);
            holder.txtValue.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/HelveticaNeueLTW1G-Cn.otf"));

            /** SET THE TAG TO "vi" **/
            vi.setTag(holder);
        } else {
            /** CAST THE VIEW HOLDER INSTANCE **/
            holder = (ViewHolder) vi.getTag();
        }

        /** SET THE PREFIX TEXT **/
        String strPropertyName = arrServes.get(position);
        if (strPropertyName != null)	{
            holder.txtValue.setText(strPropertyName);
        }

        return vi;
    }

    private static class ViewHolder	{
        AppCompatTextView txtValue;
    }
}