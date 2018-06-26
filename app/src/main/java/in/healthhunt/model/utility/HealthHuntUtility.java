package in.healthhunt.model.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;

import in.healthhunt.R;
import in.healthhunt.model.beans.Constants;

/**
 * Created by abhishekkumar on 4/22/18.
 */

public class HealthHuntUtility {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "m");
        suffixes.put(1_000_000_000L, "b");
        suffixes.put(1_000_000_000_000L, "t");
        suffixes.put(1_000_000_000_000_000L, "p");
        suffixes.put(1_000_000_000_000_000_000L, "e");
    }

    public static String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        String timeStamp = dateFormat.format(date);
        return timeStamp;

    }

    public static String getUTCTimeStamp() {
        String timeStamp = "";
        Date date = new Date();
        DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeStamp = formatterUTC.format(date);
        return timeStamp;

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date date = Calendar.getInstance().getTime();
//        String timeStamp = dateFormat.format(date);
//        return timeStamp;
    }

    public static String getMD5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertToHyperLink(String str) {
        SpannableString content = new SpannableString(str);
        content.setSpan(new UnderlineSpan(), 0, str.length(), 0);
        return content.toString();
    }

    //Convert dp into device specific pixels
    public static int dpToPx(int dp, Context context)
    {
        if(context != null)
        {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            //float logicalDensity = metrics.density;
            return (int) Math.ceil(dp * displayMetrics.density);
//Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
        return dp;
    }

    //Convert device pixels into device independent units
    public static int pxToDp(int px, Context context)
    {
        if(context != null)
        {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
        return px;
    }

    public static String getDateWithFormat(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Log.i("TAGPARSE", " date " + strDate);
            Date date = format.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            format = new SimpleDateFormat("d MMM yyyy");
            String currentDate = format.format(calendar.getTime());
            Log.i("TAG12222", "cure " + currentDate);
            return currentDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCategoryIcon(String categoryName){

        int res = 0;
        if(categoryName != null){

            if(categoryName.equalsIgnoreCase(Constants.NUTRITION)){
                res = R.mipmap.ic_nutrition_icon;
            }
            else if(categoryName.equalsIgnoreCase(Constants.FITNESS)){
                res = R.mipmap.ic_fitness;
            }
            else if(categoryName.equalsIgnoreCase(Constants.ORGANIC_BEAUTY)){
                res = R.mipmap.ic_organic_beauty;
            }
            else if(categoryName.equalsIgnoreCase(Constants.MENTAL_WELLBEING)){
                res = R.mipmap.ic_mental_well;
            }
            else if(categoryName.equalsIgnoreCase(Constants.LOVE)){
                res = R.mipmap.ic_love_icon;
            }
            else {
                res = R.mipmap.ic_all;
            }
        }
        return res;
    }

    public static int getCategoryColor(String categoryName){

        int color = R.color.hh_green_light2;
        if(categoryName != null){

            if(categoryName.equalsIgnoreCase(Constants.NUTRITION)){
                color = R.color.hh_orange_light;
            }
            else if(categoryName.equalsIgnoreCase(Constants.FITNESS)){
                color = R.color.hh_blue_light;
            }
            else if(categoryName.equalsIgnoreCase(Constants.ORGANIC_BEAUTY)){
                color = R.color.hh_green_light2;
            }
            else if(categoryName.equalsIgnoreCase(Constants.MENTAL_WELLBEING)){
                color = R.color.hh_yello_light;
            }
            else if(categoryName.equalsIgnoreCase(Constants.LOVE)){
                color = R.color.hh_red_light;
            }
        }
        return color;
    }

    public static String addSeparator(String val){
        if(val == null || val.isEmpty()){
            Log.i("TAGUTILITY", " addSeparetor = " + val);
            return val;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        Log.i("TAGUTILITY", " Value = " + val);
        String numberAsString = numberFormat.format(Long.parseLong(val));
        return numberAsString;
    }

    public static String formatNumber(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = /*truncated < 100 && */(truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() &&  networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
