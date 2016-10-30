package com.zenpets.doctors.utils.helpers;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zenpets.doctors.utils.models.CountryData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CountryArrayCreator {

    private AssetManager asstMgr;
    private Activity activity;
    private Bitmap flag;

    public CountryArrayCreator(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<CountryData> generateCountryArray()  {

        ArrayList<CountryData> arrCountries = new ArrayList<>();

        /** ADD COUNTRY NAME, FLAG AND CURRENCY CODE THE ARRAYLIST **/
        CountryData d1 = new CountryData();
        d1.setCountryName("Afghanistan");
        flag = PNGConverter("flags/Afghanistan.png");
        d1.setCountryFlag(flag);
        d1.setCurrencySymbol("؋");
        arrCountries.add(d1);

        CountryData d2 = new CountryData();
        d2.setCountryName("Albania");
        flag = PNGConverter("flags/Albania.png");
        d2.setCountryFlag(flag);
        d2.setCurrencySymbol("Lek");
        arrCountries.add(d2);

        CountryData d3 = new CountryData();
        d3.setCountryName("Argentina");
        flag = PNGConverter("flags/Argentina.png");
        d3.setCountryFlag(flag);
        d3.setCurrencySymbol("$");
        arrCountries.add(d3);

        CountryData d4 = new CountryData();
        d4.setCountryName("Aruba");
        flag = PNGConverter("flags/Aruba.png");
        d4.setCountryFlag(flag);
        d4.setCurrencySymbol("ƒ");
        arrCountries.add(d4);

        CountryData d5 = new CountryData();
        d5.setCountryName("Australia");
        flag = PNGConverter("flags/Australia.png");
        d5.setCountryFlag(flag);
        d5.setCurrencySymbol("$");
        arrCountries.add(d5);

        CountryData d6 = new CountryData();
        d6.setCountryName("Austria");
        flag = PNGConverter("flags/Austria.png");
        d6.setCountryFlag(flag);
        d6.setCurrencySymbol("€");
        arrCountries.add(d6);

        CountryData d7 = new CountryData();
        d7.setCountryName("Azerbaijan");
        flag = PNGConverter("flags/Azerbaijan.png");
        d7.setCountryFlag(flag);
        d7.setCurrencySymbol("ман");
        arrCountries.add(d7);

        CountryData d8 = new CountryData();
        d8.setCountryName("Bahamas");
        flag = PNGConverter("flags/Bahamas.png");
        d8.setCountryFlag(flag);
        d8.setCurrencySymbol("$");
        arrCountries.add(d8);

        CountryData d9 = new CountryData();
        d9.setCountryName("Barbados");
        flag = PNGConverter("flags/Barbados.png");
        d9.setCountryFlag(flag);
        d9.setCurrencySymbol("$");
        arrCountries.add(d9);

        CountryData d10 = new CountryData();
        d10.setCountryName("Belarus");
        flag = PNGConverter("flags/Belarus.png");
        d10.setCountryFlag(flag);
        d10.setCurrencySymbol("p.");
        arrCountries.add(d10);

        CountryData d11 = new CountryData();
        d11.setCountryName("Belgium");
        flag = PNGConverter("flags/Belgium.png");
        d11.setCountryFlag(flag);
        d11.setCurrencySymbol("€");
        arrCountries.add(d11);

        CountryData d12 = new CountryData();
        d12.setCountryName("Belize");
        flag = PNGConverter("flags/Belize.png");
        d12.setCountryFlag(flag);
        d12.setCurrencySymbol("BZ$");
        arrCountries.add(d12);

        CountryData d13 = new CountryData();
        d13.setCountryName("Bermuda");
        flag = PNGConverter("flags/Bermuda.png");
        d13.setCountryFlag(flag);
        d13.setCurrencySymbol("$");
        arrCountries.add(d13);

        CountryData d14 = new CountryData();
        d14.setCountryName("Bolivia");
        flag = PNGConverter("flags/Bolivia.png");
        d14.setCountryFlag(flag);
        d14.setCurrencySymbol("$b");
        arrCountries.add(d14);

        CountryData d15 = new CountryData();
        d15.setCountryName("Bosnia and Herzegovina");
        flag = PNGConverter("flags/BosniaAndHerzegovina.png");
        d15.setCountryFlag(flag);
        d15.setCurrencySymbol("KM");
        arrCountries.add(d15);

        CountryData d16 = new CountryData();
        d16.setCountryName("Botswana");
        flag = PNGConverter("flags/Botswana.png");
        d16.setCountryFlag(flag);
        d16.setCurrencySymbol("P");
        arrCountries.add(d16);

        CountryData d17 = new CountryData();
        d17.setCountryName("Brazil");
        flag = PNGConverter("flags/Brazil.png");
        d17.setCountryFlag(flag);
        d17.setCurrencySymbol("R$");
        arrCountries.add(d17);

        CountryData d18 = new CountryData();
        d18.setCountryName("Brunei Darussalam");
        flag = PNGConverter("flags/Brunei.png");
        d18.setCountryFlag(flag);
        d18.setCurrencySymbol("$");
        arrCountries.add(d18);

        CountryData d19 = new CountryData();
        d19.setCountryName("Bulgaria");
        flag = PNGConverter("flags/Bulgaria.png");
        d19.setCountryFlag(flag);
        d19.setCurrencySymbol("лв");
        arrCountries.add(d19);

        CountryData d20 = new CountryData();
        d20.setCountryName("Cambodia");
        flag = PNGConverter("flags/Cambodia.png");
        d20.setCountryFlag(flag);
        d20.setCurrencySymbol("៛");
        arrCountries.add(d20);

        CountryData d21 = new CountryData();
        d21.setCountryName("Canada");
        flag = PNGConverter("flags/Canada.png");
        d21.setCountryFlag(flag);
        d21.setCurrencySymbol("$");
        arrCountries.add(d21);

        CountryData d22 = new CountryData();
        d22.setCountryName("Cayman Islands");
        flag = PNGConverter("flags/CaymanIslands.png");
        d22.setCountryFlag(flag);
        d22.setCurrencySymbol("$");
        arrCountries.add(d22);

        CountryData d23 = new CountryData();
        d23.setCountryName("Chile");
        flag = PNGConverter("flags/Chile.png");
        d23.setCountryFlag(flag);
        d23.setCurrencySymbol("$");
        arrCountries.add(d23);

        CountryData d24 = new CountryData();
        d24.setCountryName("China");
        flag = PNGConverter("flags/China.png");
        d24.setCountryFlag(flag);
        d24.setCurrencySymbol("¥");
        arrCountries.add(d24);

        CountryData d25 = new CountryData();
        d25.setCountryName("Colombia");
        flag = PNGConverter("flags/Colombia.png");
        d25.setCountryFlag(flag);
        d25.setCurrencySymbol("$");
        arrCountries.add(d25);

        CountryData d26 = new CountryData();
        d26.setCountryName("Costa Rica");
        flag = PNGConverter("flags/CostaRica.png");
        d26.setCountryFlag(flag);
        d26.setCurrencySymbol("₡");
        arrCountries.add(d26);

        CountryData d27 = new CountryData();
        d27.setCountryName("Croatia");
        flag = PNGConverter("flags/Croatia.png");
        d27.setCountryFlag(flag);
        d27.setCurrencySymbol("kn");
        arrCountries.add(d27);

        CountryData d28 = new CountryData();
        d28.setCountryName("Cuba");
        flag = PNGConverter("flags/Cuba.png");
        d28.setCountryFlag(flag);
        d28.setCurrencySymbol("₱");
        arrCountries.add(d28);

        CountryData d29 = new CountryData();
        d29.setCountryName("Cyprus");
        flag = PNGConverter("flags/Cyprus.png");
        d29.setCountryFlag(flag);
        d29.setCurrencySymbol("€");
        arrCountries.add(d29);

        CountryData d30 = new CountryData();
        d30.setCountryName("Czech Republic");
        flag = PNGConverter("flags/CzechRepublic.png");
        d30.setCountryFlag(flag);
        d30.setCurrencySymbol("Kč");
        arrCountries.add(d30);

        CountryData d31 = new CountryData();
        d31.setCountryName("Denmark");
        flag = PNGConverter("flags/Denmark.png");
        d31.setCountryFlag(flag);
        d31.setCurrencySymbol("kr");
        arrCountries.add(d31);

        CountryData d32 = new CountryData();
        d32.setCountryName("Dominican Republic");
        flag = PNGConverter("flags/DominicanRepublic.png");
        d32.setCountryFlag(flag);
        d32.setCurrencySymbol("RD$");
        arrCountries.add(d32);

        CountryData d33 = new CountryData();
        d33.setCountryName("Ecuador");
        flag = PNGConverter("flags/Ecuador.png");
        d33.setCountryFlag(flag);
        d33.setCurrencySymbol("$");
        arrCountries.add(d33);

        CountryData d34 = new CountryData();
        d34.setCountryName("Egypt");
        flag = PNGConverter("flags/Egypt.png");
        d34.setCountryFlag(flag);
        d34.setCurrencySymbol("£");
        arrCountries.add(d34);

        CountryData d35 = new CountryData();
        d35.setCountryName("El Salvador");
        flag = PNGConverter("flags/ElSalvador.png");
        d35.setCountryFlag(flag);
        d35.setCurrencySymbol("$");
        arrCountries.add(d35);

        CountryData d36 = new CountryData();
        d36.setCountryName("Estonia");
        flag = PNGConverter("flags/Estonia.png");
        d36.setCountryFlag(flag);
        d36.setCurrencySymbol("€");
        arrCountries.add(d36);

        CountryData d37 = new CountryData();
        d37.setCountryName("Fiji");
        flag = PNGConverter("flags/Fiji.png");
        d37.setCountryFlag(flag);
        d37.setCurrencySymbol("$");
        arrCountries.add(d37);

        CountryData d38 = new CountryData();
        d38.setCountryName("Finland");
        flag = PNGConverter("flags/Finland.png");
        d38.setCountryFlag(flag);
        d38.setCurrencySymbol("€");
        arrCountries.add(d38);

        CountryData d39 = new CountryData();
        d39.setCountryName("France");
        flag = PNGConverter("flags/France.png");
        d39.setCountryFlag(flag);
        d39.setCurrencySymbol("€");
        arrCountries.add(d39);

        CountryData d40 = new CountryData();
        d40.setCountryName("Germany");
        flag = PNGConverter("flags/Germany.png");
        d40.setCountryFlag(flag);
        d40.setCurrencySymbol("€");
        arrCountries.add(d40);

        CountryData d41 = new CountryData();
        d41.setCountryName("Ghana");
        flag = PNGConverter("flags/Ghana.png");
        d41.setCountryFlag(flag);
        d41.setCurrencySymbol("¢");
        arrCountries.add(d41);

        CountryData d42 = new CountryData();
        d42.setCountryName("Gibraltar");
        flag = PNGConverter("flags/Gibraltar.png");
        d42.setCountryFlag(flag);
        d42.setCurrencySymbol("£");
        arrCountries.add(d42);

        CountryData d43 = new CountryData();
        d43.setCountryName("Greece");
        flag = PNGConverter("flags/Greece.png");
        d43.setCountryFlag(flag);
        d43.setCurrencySymbol("€");
        arrCountries.add(d43);

        CountryData d44 = new CountryData();
        d44.setCountryName("Guatemala");
        flag = PNGConverter("flags/Guatemala.png");
        d44.setCountryFlag(flag);
        d44.setCurrencySymbol("Q");
        arrCountries.add(d44);

        CountryData d45 = new CountryData();
        d45.setCountryName("Guernsey");
        flag = PNGConverter("flags/Guernsey.png");
        d45.setCountryFlag(flag);
        d45.setCurrencySymbol("£");
        arrCountries.add(d45);

        CountryData d46 = new CountryData();
        d46.setCountryName("Guyana");
        flag = PNGConverter("flags/Guyana.png");
        d46.setCountryFlag(flag);
        d46.setCurrencySymbol("$");
        arrCountries.add(d46);

        CountryData d47 = new CountryData();
        d47.setCountryName("Honduras");
        flag = PNGConverter("flags/Honduras.png");
        d47.setCountryFlag(flag);
        d47.setCurrencySymbol("L");
        arrCountries.add(d47);

        CountryData d48 = new CountryData();
        d48.setCountryName("Hong Kong");
        flag = PNGConverter("flags/HongKong.png");
        d48.setCountryFlag(flag);
        d48.setCurrencySymbol("$");
        arrCountries.add(d48);

        CountryData d49 = new CountryData();
        d49.setCountryName("Hungary");
        flag = PNGConverter("flags/Hungary.png");
        d49.setCountryFlag(flag);
        d49.setCurrencySymbol("Ft");
        arrCountries.add(d49);

        CountryData d50 = new CountryData();
        d50.setCountryName("Iceland");
        flag = PNGConverter("flags/Iceland.png");
        d50.setCountryFlag(flag);
        d50.setCurrencySymbol("kr");
        arrCountries.add(d50);

        CountryData d51 = new CountryData();
        d51.setCountryName("India");
        flag = PNGConverter("flags/India.png");
        d51.setCountryFlag(flag);
        d51.setCurrencySymbol("₹");
        arrCountries.add(d51);

        CountryData d52 = new CountryData();
        d52.setCountryName("Indonesia");
        flag = PNGConverter("flags/Indonesia.png");
        d52.setCountryFlag(flag);
        d52.setCurrencySymbol("Rp");
        arrCountries.add(d52);

        CountryData d53 = new CountryData();
        d53.setCountryName("Ireland");
        flag = PNGConverter("flags/Ireland.png");
        d53.setCountryFlag(flag);
        d53.setCurrencySymbol("€");
        arrCountries.add(d53);

        CountryData d54 = new CountryData();
        d54.setCountryName("Isle of Man");
        flag = PNGConverter("flags/IsleOfMan.png");
        d54.setCountryFlag(flag);
        d54.setCurrencySymbol("£");
        arrCountries.add(d54);

        CountryData d55 = new CountryData();
        d55.setCountryName("Israel");
        flag = PNGConverter("flags/Israel.png");
        d55.setCountryFlag(flag);
        d55.setCurrencySymbol("₪");
        arrCountries.add(d55);

        CountryData d56 = new CountryData();
        d56.setCountryName("Italy");
        flag = PNGConverter("flags/Italy.png");
        d56.setCountryFlag(flag);
        d56.setCurrencySymbol("€");
        arrCountries.add(d56);

        CountryData d57 = new CountryData();
        d57.setCountryName("Jamaica");
        flag = PNGConverter("flags/Jamaica.png");
        d57.setCountryFlag(flag);
        d57.setCurrencySymbol("J$");
        arrCountries.add(d57);

        CountryData d58 = new CountryData();
        d58.setCountryName("Japan");
        flag = PNGConverter("flags/Japan.png");
        d58.setCountryFlag(flag);
        d58.setCurrencySymbol("¥");
        arrCountries.add(d58);

        CountryData d59 = new CountryData();
        d59.setCountryName("Jersey");
        flag = PNGConverter("flags/Jersey.png");
        d59.setCountryFlag(flag);
        d59.setCurrencySymbol("£");
        arrCountries.add(d59);

        CountryData d60 = new CountryData();
        d60.setCountryName("Kazakhstan");
        flag = PNGConverter("flags/Kazakhstan.png");
        d60.setCountryFlag(flag);
        d60.setCurrencySymbol("лв");
        arrCountries.add(d60);

        CountryData d61 = new CountryData();
        d61.setCountryName("Kenya");
        flag = PNGConverter("flags/Kenya.png");
        d61.setCountryFlag(flag);
        d61.setCurrencySymbol("KSh");
        arrCountries.add(d61);

        CountryData d62 = new CountryData();
        d62.setCountryName("Kuwait");
        flag = PNGConverter("flags/Kuwait.png");
        d62.setCountryFlag(flag);
        d62.setCurrencySymbol("ك");
        arrCountries.add(d62);

        CountryData d63 = new CountryData();
        d63.setCountryName("Kyrgyzstan");
        flag = PNGConverter("flags/Kyrgyzstan.png");
        d63.setCountryFlag(flag);
        d63.setCurrencySymbol("лв");
        arrCountries.add(d63);

        CountryData d64 = new CountryData();
        d64.setCountryName("Laos");
        flag = PNGConverter("flags/Laos.png");
        d64.setCountryFlag(flag);
        d64.setCurrencySymbol("₭");
        arrCountries.add(d64);

        CountryData d65 = new CountryData();
        d65.setCountryName("Latvia");
        flag = PNGConverter("flags/Latvia.png");
        d65.setCountryFlag(flag);
        d65.setCurrencySymbol("Ls");
        arrCountries.add(d65);

        CountryData d66 = new CountryData();
        d66.setCountryName("Lebanon");
        flag = PNGConverter("flags/Lebanon.png");
        d66.setCountryFlag(flag);
        d66.setCurrencySymbol("£");
        arrCountries.add(d66);

        CountryData d67 = new CountryData();
        d67.setCountryName("Liberia");
        flag = PNGConverter("flags/Liberia.png");
        d67.setCountryFlag(flag);
        d67.setCurrencySymbol("$");
        arrCountries.add(d67);

        CountryData d68 = new CountryData();
        d68.setCountryName("Liechtenstein");
        flag = PNGConverter("flags/Liechtenshein.png");
        d68.setCountryFlag(flag);
        d68.setCurrencySymbol("CHF");
        arrCountries.add(d68);

        CountryData d69 = new CountryData();
        d69.setCountryName("Lithuania");
        flag = PNGConverter("flags/Lithuania.png");
        d69.setCountryFlag(flag);
        d69.setCurrencySymbol("Lt");
        arrCountries.add(d69);

        CountryData d70 = new CountryData();
        d70.setCountryName("Luxembourg");
        flag = PNGConverter("flags/Luxembourg.png");
        d70.setCountryFlag(flag);
        d70.setCurrencySymbol("€");
        arrCountries.add(d70);

        CountryData d71 = new CountryData();
        d71.setCountryName("Macedonia");
        flag = PNGConverter("flags/Macedonia.png");
        d71.setCountryFlag(flag);
        d71.setCurrencySymbol("ден");
        arrCountries.add(d71);

        CountryData d72 = new CountryData();
        d72.setCountryName("Malaysia");
        flag = PNGConverter("flags/Malaysia.png");
        d72.setCountryFlag(flag);
        d72.setCurrencySymbol("RM");
        arrCountries.add(d72);

        CountryData d73 = new CountryData();
        d73.setCountryName("Malta");
        flag = PNGConverter("flags/Malta.png");
        d73.setCountryFlag(flag);
        d73.setCurrencySymbol("€");
        arrCountries.add(d73);

        CountryData d74 = new CountryData();
        d74.setCountryName("Mauritius");
        flag = PNGConverter("flags/Mauritius.png");
        d74.setCountryFlag(flag);
        d74.setCurrencySymbol("₨");
        arrCountries.add(d74);

        CountryData d75 = new CountryData();
        d75.setCountryName("Mexico");
        flag = PNGConverter("flags/Mexico.png");
        d75.setCountryFlag(flag);
        d75.setCurrencySymbol("$");
        arrCountries.add(d75);

        CountryData d76 = new CountryData();
        d76.setCountryName("Monaco");
        flag = PNGConverter("flags/Monaco.png");
        d76.setCountryFlag(flag);
        d76.setCurrencySymbol("€");
        arrCountries.add(d76);

        CountryData d77 = new CountryData();
        d77.setCountryName("Mongolia");
        flag = PNGConverter("flags/Mongolia.png");
        d77.setCountryFlag(flag);
        d77.setCurrencySymbol("₮");
        arrCountries.add(d77);

        CountryData d78 = new CountryData();
        d78.setCountryName("Mozambique");
        flag = PNGConverter("flags/Mozambique.png");
        d78.setCountryFlag(flag);
        d78.setCurrencySymbol("MT");
        arrCountries.add(d78);

        CountryData d79 = new CountryData();
        d79.setCountryName("Namibia");
        flag = PNGConverter("flags/Namibia.png");
        d79.setCountryFlag(flag);
        d79.setCurrencySymbol("$");
        arrCountries.add(d79);

        CountryData d80 = new CountryData();
        d80.setCountryName("Nepal");
        flag = PNGConverter("flags/Nepal.png");
        d80.setCountryFlag(flag);
        d80.setCurrencySymbol("₨");
        arrCountries.add(d80);

        CountryData d81 = new CountryData();
        d81.setCountryName("Netherlands");
        flag = PNGConverter("flags/Netherlands.png");
        d81.setCountryFlag(flag);
        d81.setCurrencySymbol("€");
        arrCountries.add(d81);

        CountryData d82 = new CountryData();
        d82.setCountryName("Netherlands Antilles");
        flag = PNGConverter("flags/NetherlandsAntilles.png");
        d82.setCountryFlag(flag);
        d82.setCurrencySymbol("ƒ");
        arrCountries.add(d82);

        CountryData d83 = new CountryData();
        d83.setCountryName("New Zealand");
        flag = PNGConverter("flags/NewZealand.png");
        d83.setCountryFlag(flag);
        d83.setCurrencySymbol("$");
        arrCountries.add(d83);

        CountryData d84 = new CountryData();
        d84.setCountryName("Nicaragua");
        flag = PNGConverter("flags/Nicaragua.png");
        d84.setCountryFlag(flag);
        d84.setCurrencySymbol("C$");
        arrCountries.add(d84);

        CountryData d85 = new CountryData();
        d85.setCountryName("Nigeria");
        flag = PNGConverter("flags/Nigeria.png");
        d85.setCountryFlag(flag);
        d85.setCurrencySymbol("₦");
        arrCountries.add(d85);

        CountryData d86 = new CountryData();
        d86.setCountryName("Northern Ireland");
        flag = PNGConverter("flags/NorthernIreland.png");
        d86.setCountryFlag(flag);
        d86.setCurrencySymbol("£");
        arrCountries.add(d86);

        CountryData d87 = new CountryData();
        d87.setCountryName("Norway");
        flag = PNGConverter("flags/Norway.png");
        d87.setCountryFlag(flag);
        d87.setCurrencySymbol("kr");
        arrCountries.add(d87);

        CountryData d88 = new CountryData();
        d88.setCountryName("Oman");
        flag = PNGConverter("flags/Oman.png");
        d88.setCountryFlag(flag);
        d88.setCurrencySymbol("﷼");
        arrCountries.add(d88);

        CountryData d89 = new CountryData();
        d89.setCountryName("Panama");
        flag = PNGConverter("flags/Panama.png");
        d89.setCountryFlag(flag);
        d89.setCurrencySymbol("B/.");
        arrCountries.add(d89);

        CountryData d90 = new CountryData();
        d90.setCountryName("Paraguay");
        flag = PNGConverter("flags/Paraguay.png");
        d90.setCountryFlag(flag);
        d90.setCurrencySymbol("Gs");
        arrCountries.add(d90);

        CountryData d91 = new CountryData();
        d91.setCountryName("Peru");
        flag = PNGConverter("flags/Peru.png");
        d91.setCountryFlag(flag);
        d91.setCurrencySymbol("S/.");
        arrCountries.add(d91);

        CountryData d92 = new CountryData();
        d92.setCountryName("Philippines");
        flag = PNGConverter("flags/Philippines.png");
        d92.setCountryFlag(flag);
        d92.setCurrencySymbol("₱");
        arrCountries.add(d92);

        CountryData d93 = new CountryData();
        d93.setCountryName("Poland");
        flag = PNGConverter("flags/Poland.png");
        d93.setCountryFlag(flag);
        d93.setCurrencySymbol("zł");
        arrCountries.add(d93);

        CountryData d94 = new CountryData();
        d94.setCountryName("Portugal");
        flag = PNGConverter("flags/Portugal.png");
        d94.setCountryFlag(flag);
        d94.setCurrencySymbol("€");
        arrCountries.add(d94);

        CountryData d95 = new CountryData();
        d95.setCountryName("Qatar");
        flag = PNGConverter("flags/Qatar.png");
        d95.setCountryFlag(flag);
        d95.setCurrencySymbol("﷼");
        arrCountries.add(d95);

        CountryData d96 = new CountryData();
        d96.setCountryName("Romania");
        flag = PNGConverter("flags/Romania.png");
        d96.setCountryFlag(flag);
        d96.setCurrencySymbol("lei");
        arrCountries.add(d96);

        CountryData d97 = new CountryData();
        d97.setCountryName("Russia");
        flag = PNGConverter("flags/RussianFederation.png");
        d97.setCountryFlag(flag);
        d97.setCurrencySymbol("руб");
        arrCountries.add(d97);

        CountryData d98 = new CountryData();
        d98.setCountryName("Saudi Arabia");
        flag = PNGConverter("flags/SaudiArabia.png");
        d98.setCountryFlag(flag);
        d98.setCurrencySymbol("﷼");
        arrCountries.add(d98);

        CountryData d99 = new CountryData();
        d99.setCountryName("Scotland");
        flag = PNGConverter("flags/Scotland.png");
        d99.setCountryFlag(flag);
        d99.setCurrencySymbol("£");
        arrCountries.add(d99);

        CountryData d100 = new CountryData();
        d100.setCountryName("Serbia");
        flag = PNGConverter("flags/Serbia.png");
        d100.setCountryFlag(flag);
        d100.setCurrencySymbol("Дин.");
        arrCountries.add(d100);

        CountryData d101 = new CountryData();
        d101.setCountryName("Seychelles");
        flag = PNGConverter("flags/Seychelles.png");
        d101.setCountryFlag(flag);
        d101.setCurrencySymbol("₨");
        arrCountries.add(d101);

        CountryData d102 = new CountryData();
        d102.setCountryName("Singapore");
        flag = PNGConverter("flags/Singapore.png");
        d102.setCountryFlag(flag);
        d102.setCurrencySymbol("$");
        arrCountries.add(d102);

        CountryData d103 = new CountryData();
        d103.setCountryName("Slovenia");
        flag = PNGConverter("flags/Slovenia.png");
        d103.setCountryFlag(flag);
        d103.setCurrencySymbol("€");
        arrCountries.add(d103);

        CountryData d104 = new CountryData();
        d104.setCountryName("Solomon Islands");
        flag = PNGConverter("flags/SolomonIslands.png");
        d104.setCountryFlag(flag);
        d104.setCurrencySymbol("$");
        arrCountries.add(d104);

        CountryData d105 = new CountryData();
        d105.setCountryName("Somalia");
        flag = PNGConverter("flags/Somalia.png");
        d105.setCountryFlag(flag);
        d105.setCurrencySymbol("S");
        arrCountries.add(d105);

        CountryData d106 = new CountryData();
        d106.setCountryName("South Africa");
        flag = PNGConverter("flags/SouthAfrica.png");
        d106.setCountryFlag(flag);
        d106.setCurrencySymbol("R");
        arrCountries.add(d106);

        CountryData d107 = new CountryData();
        d107.setCountryName("South Korea");
        flag = PNGConverter("flags/SouthKorea.png");
        d107.setCountryFlag(flag);
        d107.setCurrencySymbol("₩");
        arrCountries.add(d107);

        CountryData d108 = new CountryData();
        d108.setCountryName("Spain");
        flag = PNGConverter("flags/Spain.png");
        d108.setCountryFlag(flag);
        d108.setCurrencySymbol("€");
        arrCountries.add(d108);

        CountryData d109 = new CountryData();
        d109.setCountryName("Sri Lanka");
        flag = PNGConverter("flags/SriLanka.png");
        d109.setCountryFlag(flag);
        d109.setCurrencySymbol("රු");
        arrCountries.add(d109);

        CountryData d110 = new CountryData();
        d110.setCountryName("Suriname");
        flag = PNGConverter("flags/Suriname.png");
        d110.setCountryFlag(flag);
        d110.setCurrencySymbol("$");
        arrCountries.add(d110);

        CountryData d111 = new CountryData();
        d111.setCountryName("Sweden");
        flag = PNGConverter("flags/Sweden.png");
        d111.setCountryFlag(flag);
        d111.setCurrencySymbol("kr");
        arrCountries.add(d111);

        CountryData d112 = new CountryData();
        d112.setCountryName("Switzerland");
        flag = PNGConverter("flags/Switzerland.png");
        d112.setCountryFlag(flag);
        d112.setCurrencySymbol("CHF");
        arrCountries.add(d112);

        CountryData d113 = new CountryData();
        d113.setCountryName("Taiwan");
        flag = PNGConverter("flags/Taiwan.png");
        d113.setCountryFlag(flag);
        d113.setCurrencySymbol("NT$");
        arrCountries.add(d113);

        CountryData d114 = new CountryData();
        d114.setCountryName("Thailand");
        flag = PNGConverter("flags/Thailand.png");
        d114.setCountryFlag(flag);
        d114.setCurrencySymbol("$");
        arrCountries.add(d114);

        CountryData d115 = new CountryData();
        d115.setCountryName("Trinidad and Tobago");
        flag = PNGConverter("flags/TrinidadAndTobago.png");
        d115.setCountryFlag(flag);
        d115.setCurrencySymbol("TT$");
        arrCountries.add(d115);

        CountryData d116 = new CountryData();
        d116.setCountryName("Turkey");
        flag = PNGConverter("flags/Turkey.png");
        d116.setCountryFlag(flag);
        d116.setCurrencySymbol("₺");
        arrCountries.add(d116);

        CountryData d117 = new CountryData();
        d117.setCountryName("Tuvalu");
        flag = PNGConverter("flags/Tuvalu.png");
        d117.setCountryFlag(flag);
        d117.setCurrencySymbol("$");
        arrCountries.add(d117);

        CountryData d118 = new CountryData();
        d118.setCountryName("Ukraine");
        flag = PNGConverter("flags/Ukraine.png");
        d118.setCountryFlag(flag);
        d118.setCurrencySymbol("₴");
        arrCountries.add(d118);

        CountryData d119 = new CountryData();
        d119.setCountryName("United Arab Emirates");
        flag = PNGConverter("flags/UnitedArabEmirates.png");
        d119.setCountryFlag(flag);
        d119.setCurrencySymbol("د.إ");
        arrCountries.add(d119);

        CountryData d120 = new CountryData();
        d120.setCountryName("United Kingdom");
        flag = PNGConverter("flags/UnitedKingdom.png");
        d120.setCountryFlag(flag);
        d120.setCurrencySymbol("£");
        arrCountries.add(d120);

        CountryData d121 = new CountryData();
        d121.setCountryName("United States of America");
        flag = PNGConverter("flags/UnitedStatesOfAmerica.png");
        d121.setCountryFlag(flag);
        d121.setCurrencySymbol("$");
        arrCountries.add(d121);

        CountryData d122 = new CountryData();
        d122.setCountryName("Uruguay");
        flag = PNGConverter("flags/Uruguay.png");
        d122.setCountryFlag(flag);
        d122.setCurrencySymbol("$U");
        arrCountries.add(d122);

        CountryData d123 = new CountryData();
        d123.setCountryName("Uzbekistan");
        flag = PNGConverter("flags/Uzbekistan.png");
        d123.setCountryFlag(flag);
        d123.setCurrencySymbol("лв");
        arrCountries.add(d123);

        CountryData d124 = new CountryData();
        d124.setCountryName("Vatican City");
        flag = PNGConverter("flags/VaticanCity.png");
        d124.setCountryFlag(flag);
        d124.setCurrencySymbol("€");
        arrCountries.add(d124);

        CountryData d125 = new CountryData();
        d125.setCountryName("Venezuela");
        flag = PNGConverter("flags/Venezuela.png");
        d125.setCountryFlag(flag);
        d125.setCurrencySymbol("Bs");
        arrCountries.add(d125);

        CountryData d126 = new CountryData();
        d126.setCountryName("Vietnam");
        flag = PNGConverter("flags/Vietnam.png");
        d126.setCountryFlag(flag);
        d126.setCurrencySymbol("₫");
        arrCountries.add(d126);

        CountryData d127 = new CountryData();
        d127.setCountryName("Wales");
        flag = PNGConverter("flags/Wales.png");
        d127.setCountryFlag(flag);
        d127.setCurrencySymbol("£");
        arrCountries.add(d127);

        CountryData d128 = new CountryData();
        d128.setCountryName("Yemen");
        flag = PNGConverter("flags/Yemen.png");
        d128.setCountryFlag(flag);
        d128.setCurrencySymbol("﷼");
        arrCountries.add(d128);

        CountryData d129 = new CountryData();
        d129.setCountryName("Zimbabwe");
        flag = PNGConverter("flags/Zimbabwe.png");
        d129.setCountryFlag(flag);
        d129.setCurrencySymbol("Z$");
        arrCountries.add(d129);

        return arrCountries;
    }

    /***** CONVERTING PNG IMAGES INTO byte[] *****/
    private Bitmap PNGConverter(String imgPath) {
        asstMgr = activity.getAssets();
        Bitmap bmpFlag;
        try {
            InputStream inStream = asstMgr.open(imgPath);
            bmpFlag = BitmapFactory.decodeStream(inStream);
            return bmpFlag;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}