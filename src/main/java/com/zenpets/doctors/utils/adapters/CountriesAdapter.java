package com.zenpets.doctors.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.models.CountryData;

import java.util.ArrayList;

public class CountriesAdapter extends ArrayAdapter<CountryData> {

    /***** THE ACTIVITY INSTANCE FOR USE IN THE ADAPTER *****/
    private final Activity activity;

    /***** LAYOUT INFLATER TO USE A CUSTOM LAYOUT *****/
    private LayoutInflater inflater = null;

    /***** ARRAY LIST TO GET DATA FROM THE ACTIVITY *****/
    private final ArrayList<CountryData> arrCountries;

    public CountriesAdapter(Activity activity, ArrayList<CountryData> arrCountries) {
        super(activity, R.layout.states_row);

        /** CAST THE ACTIVITY FROM THE METHOD TO THE LOCAL ACTIVITY INSTANCE **/
        this.activity = activity;

        /** CAST THE CONTENTS OF THE ARRAY LIST IN THE METHOD TO THE LOCAL INSTANCE **/
        this.arrCountries = arrCountries;

        /** INSTANTIATE THE LAYOUT INFLATER **/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrCountries.size();
    }

    @Override
    public CountryData getItem(int position) {
        return arrCountries.get(position);
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
            vi = inflater.inflate(R.layout.countries_dropdown_row, parent, false);

            /** INSTANTIATE THE VIEW HOLDER INSTANCE **/
            holder = new ViewHolder();

            /*****	CAST THE LAYOUT ELEMENTS	*****/
            holder.txtCountryName = (AppCompatTextView) vi.findViewById(R.id.txtCountryName);
            holder.txtCountryName.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/HelveticaNeueLTW1G-Cn.otf"));
            holder.txtCurrencySymbol = (AppCompatTextView) vi.findViewById(R.id.txtCurrencySymbol);

            /** SET THE TAG TO "vi" **/
            vi.setTag(holder);
        } else {
            /** CAST THE VIEW HOLDER INSTANCE **/
            holder = (ViewHolder) vi.getTag();
        }

        /** SET THE COUNTRY NAME **/
        if (arrCountries.get(position).getCountryName() != null)    {
            holder.txtCountryName.setText(arrCountries.get(position).getCountryName());
            Bitmap bmpFlag = arrCountries.get(position).getCountryFlag();
            Drawable d = new BitmapDrawable(activity.getResources(), bmpFlag);
            holder.txtCountryName.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        }

        /** SET THE CURRENCY SYMBOL **/
        if (arrCountries.get(position).getCurrencySymbol() != null) {
            holder.txtCurrencySymbol.setText(arrCountries.get(position).getCurrencySymbol());
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
            vi = inflater.inflate(R.layout.countries_row, parent, false);

            /** INSTANTIATE THE VIEW HOLDER INSTANCE **/
            holder = new ViewHolder();

            /*****	CAST THE LAYOUT ELEMENTS	*****/
            holder.txtCountryName = (AppCompatTextView) vi.findViewById(R.id.txtCountryName);
            holder.txtCountryName.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/HelveticaNeueLTW1G-Cn.otf"));
            holder.txtCurrencySymbol = (AppCompatTextView) vi.findViewById(R.id.txtCurrencySymbol);

            /** SET THE TAG TO "vi" **/
            vi.setTag(holder);
        } else {
            /** CAST THE VIEW HOLDER INSTANCE **/
            holder = (ViewHolder) vi.getTag();
        }

        /** SET THE COUNTRY NAME **/
        if (arrCountries.get(position).getCountryName() != null)    {
            holder.txtCountryName.setText(arrCountries.get(position).getCountryName());
            Bitmap bmpFlag = arrCountries.get(position).getCountryFlag();
            Drawable d = new BitmapDrawable(activity.getResources(), bmpFlag);
            holder.txtCountryName.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        }

        /** SET THE CURRENCY SYMBOL **/
        if (arrCountries.get(position).getCurrencySymbol() != null) {
            holder.txtCurrencySymbol.setText(arrCountries.get(position).getCurrencySymbol());
        }

        return vi;
    }

    private static class ViewHolder	{
        AppCompatTextView txtCountryName;
        AppCompatTextView txtCurrencySymbol;
    }
}