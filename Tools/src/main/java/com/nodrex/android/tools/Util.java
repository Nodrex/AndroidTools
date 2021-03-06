package com.nodrex.android.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


/**
 * This class contains useful methods and utilities for Android.
 * @author Nodar Tchumbadze
 * @since 2014
 * @version 2.1
 */
public class Util {

    private static boolean disableLogs;
    private static boolean disableToasts;
    /**
     * 2g internet connection value.
     */
    public static final int G2 = 2;
    /**
     * 3g internet connection value.
     */
    public static final int G3 = 3;
    /**
     * 4g internet connection value.
     */
    public static final int G4 = 4;
    /**
     * value to identify wifi.
     */
    public static final int WIFI = 5;
    /**
     * value to identify ethernet.
     */
    public static final int ETHERNET = 6;
    public static final int UNKNOWN = -1;
    public static final String LOG_TAG = "Tools";

    private static ConnectivityManager connectivityManager;
    private static TelephonyManager telephonyManager;
    private static ClipboardManager clipboardManager;
    private static NotificationManager notification;
    private static PowerManager powerManager;
    private static ActivityManager activityManager;
    private static InputMethodManager keyboard;
    private static SharedPreferences sharedPref;
    private static Vibrator vibrator;

    private static String [] globalIpDetectorLinks = {
            "https://api.ipify.org",
            "http://checkip.amazonaws.com/",
            "http://icanhazip.com/",
            "http://www.trackip.net/ip",
            "http://myexternalip.com/raw",
            "http://ipecho.net/plain",
            "http://bot.whatismyipaddress.com/"
    };

    private Util() {}

    /**
     * Disables all logs.
     */
    public static void disableLogs() {
        disableLogs = true;
    }

    /**
     * Disables all toasts.
     */
    public static void disableToasts() {
        disableToasts = true;
    }

    /**
     * @param activity
     * @return TelephonyManager for future use.
     */
    public static TelephonyManager getTelephonyManager(Activity activity) {
        if (telephonyManager != null) return telephonyManager;
        if (activity == null) return null;
        telephonyManager = (TelephonyManager) activity.getSystemService(Activity.TELEPHONY_SERVICE);
        return telephonyManager;
    }

    /**
     * @param activity
     * @return ClipboardManager for future use.
     */
    public static ClipboardManager getClipboardManager(Activity activity) {
        if(clipboardManager != null) return clipboardManager;
        if(activity == null)return null;
        clipboardManager = (ClipboardManager) activity.getSystemService(Activity.CLIPBOARD_SERVICE);
        return clipboardManager;
    }

    /**
     * @param activity
     * @return ConnectivityManager for future use.
     */
    public static ConnectivityManager getConnectivityManager(Activity activity) {
        if (connectivityManager != null) return connectivityManager;
        if (activity == null) return null;
        connectivityManager = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        return connectivityManager;
    }

    /**
     * @param activity
     * @return PowerManager for future use.
     */
    public static PowerManager getPowerManager(Activity activity) {
        if (powerManager != null) return powerManager;
        if (activity == null) return null;
        powerManager = (PowerManager) activity.getSystemService(Activity.POWER_SERVICE);
        return powerManager;
    }

    /**
     * @param activity
     * @return ActivityManager  for future use.
     */
    public static ActivityManager getActivityManager(Activity activity) {
        if (activityManager != null) return activityManager;
        if (activity == null) return null;
        activityManager = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
        return activityManager;
    }

    /**
     * @param activity
     * @return SharedPreferences for future use.
     */
    public static SharedPreferences getSharedPref(Activity activity) {
        if (sharedPref != null) return sharedPref;
        if (activity == null) return null;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPref;
    }

    /**
     * @param activity
     * @return InputMethodManager for future use.
     */
    public static InputMethodManager getKeyboard(Activity activity) {
        if (keyboard != null) return keyboard;
        if (activity == null) return null;
        keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return keyboard;
    }

    /**
     * @param activity
     * @return Vibrator for future use.
     */
    public static Vibrator getVibrator(Activity activity) {
        if (vibrator != null) return vibrator;
        if (activity == null) return null;
        vibrator = (Vibrator) activity.getSystemService(Activity.VIBRATOR_SERVICE);
        return vibrator;
    }

    /**
     * @param activity
     * @return NotificationManager for future use.
     */
    public static NotificationManager getNotification(Activity activity) {
        if (notification != null) return notification;
        if (activity == null) return null;
        notification = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        return notification;
    }

    /**
     * @param activity to create vibrator if this one does not exists.
     * @return true if vibrator is supported, false otherwise.
     */
    public static boolean isVibrateSupported(Activity activity) {
        getVibrator(activity);
        return vibrator != null && vibrator.hasVibrator();
    }

    /**
     * Sets portrait orientation.
     * @param activity
     */
    public static void setPortraitOrientation(Activity activity) {
        if(activity != null) activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Sets landscape orientation.
     * @param activity
     */
    public static void setLandscapeOrientation(Activity activity) {
        if(activity != null) activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Sets unspecified orientation.
     * @param activity
     */
    public static void setUnspecifiedOrientation(Activity activity) {
        if(activity != null) activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * Shows toast with given text.
     * </br>If you want to disable all toasts without commenting them, just call disableToasts() method.
     * @param activity
     * @param text
     */
    public static void toast(Activity activity, String text) {
        if (disableToasts) return;
        if (activity == null || text == null) return;
        Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        if (toast != null) toast.show();
    }

    /**
     * Keeps screen active if on is true and screen will be off after some time of inactivity if on is false.
     * @param activity
     * @param on true to keep, false not to keep.
     */
    public static void keepScreenOn(Activity activity, boolean on) {
        if (activity == null) return;
        Window window = activity.getWindow();
        if (window == null) return;
        int flag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        if (on) window.addFlags(flag);
        else window.clearFlags(flag);
    }

    /**
     * Copy Text into clipboard.
     * @param activity
     * @param toastText
     * @param copyText
     */
    public static void copyTextToClipboard(Activity activity, String toastText,String copyText){
        getClipboardManager(activity);
        if(clipboardManager == null) return;
        ClipData clip = ClipData.newPlainText(toastText, copyText);
        clipboardManager.setPrimaryClip(clip);
    }

    /**
     * @param activity
     * @return Resources.
     */
    public static Resources getResources(Activity activity) {
        return activity == null ? null : activity.getResources();
    }

    /**
     * @param activity
     * @param id
     * @return string value from resources by id, more concretely from strings.xml
     */
    public static String getStrFromRes(Activity activity, int id) {
        Resources resources = getResources(activity);
        return resources == null ? null : resources.getString(id);
    }

    /**
     * @param activity
     * @param id
     * @return color form resources by id, more concretely from colors.xml
     */
    public static int getColorFromRes(Activity activity, int id) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ContextCompat.getColor(activity, id);
            }
        } catch (Exception e) {}
        return getColorFromResOldStyle(activity, id);
    }

    @SuppressWarnings("deprecation")
    private static int getColorFromResOldStyle(Activity activity, int id) {
        try {
            Resources resources = getResources(activity);
            if(resources != null) return resources.getColor(id);
        } catch (Exception e) {}
        return -1;
    }

    /**
     * @param activity
     * @param id
     * @return Drawable value from resources, more concretely from drawable folder.
     */
    public static Drawable getDrawableFromRes(Activity activity, int id) {
        Resources resources = getResources(activity);
        if (resources == null) return null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                return getDrawableFromResNewStyle(activity, resources, id);
            }
        } catch (Exception e) {}
        return getDrawableFromResOldStyle(resources, id);
    }

    @SuppressLint("NewApi")
    private static Drawable getDrawableFromResNewStyle(Activity activity, Resources resources, int id) {
        return resources.getDrawable(id, activity.getTheme());
    }

    @SuppressWarnings("deprecation")
    private static Drawable getDrawableFromResOldStyle(Resources resources, int id) {
        return resources.getDrawable(id);
    }

    /**
     * @param activity
     * @param id
     * @return Bitmap form resources by id, more concretely from drawable folder.
     */
    public static Bitmap getBitmapFromRes(Activity activity, int id) {
        Resources resources = getResources(activity);
        return resources == null ? null : BitmapFactory.decodeResource(resources, id);
    }

    /**
     * @param activity
     * @param id
     * @return int from resources by id, more concretely from dimens.xml file.
     */
    public static int getIntFromRes(Activity activity, int id) {
        Resources resources = getResources(activity);
        return resources == null ? -1 : resources.getInteger(id);
    }

    /**
     * @param activity
     * @param id
     * @return dimension form resources by id, more concretely from dimens.xml file.
     */
    public static float getDimensionFromRes(Activity activity, int id) {
        Resources resources = getResources(activity);
        return resources == null ? -1 : resources.getDimension(id);
    }

    //TODO should be tested
    /**
     * <img src="../../../../../doc/formattedStr.PNG" /></br></br>
     * This is strings.xml content where u , means underline and b means bold.
     * @param activity
     * @param id
     * @return formatted String from resources, more concretely from strings.xml which is in values folder.
     */
    public static String getFormattedStrFromRes(Activity activity, int id) {
        String str = getStrFromRes(activity, id);
        if (str == null) return null;
        Spanned formattedText = Html.fromHtml(str);
        if(formattedText == null) return str;
        return formattedText.toString();
    }

    /**
     * @param activity
     * @return true if app is in landscape mode, false otherwise.
     */
    public static boolean isLandscapeMode(Activity activity) {
        Resources resources = getResources(activity);
        if (resources == null) return false;
        Configuration configuration = resources.getConfiguration();
        return configuration != null && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * @param activity
     * @return true if app is in portrait mode, false otherwise.
     */
    public static boolean isPortraitMode(Activity activity) {
        Resources resources = getResources(activity);
        if (resources == null) return false;
        Configuration configuration = resources.getConfiguration();
        return configuration != null && configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Initiates language for app.
     * @param activity
     * @param langPostFix for example: "en"
     */
    public static void initLang(Activity activity, String langPostFix) {
        try {
            Resources res = getResources(activity);
            if(res == null) return;
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = new Locale(langPostFix);
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {}
    }

    /**
     * Sets font to TextView.
     * @param activity
     * @param textView
     * @param fullFontName "fontName.ttf" if you will put font under assets folder,</br>
     * or "some_folder/fontName.ttf" when  some_folder is located under assets folder.
     */
    public static void setFont(Activity activity, TextView textView, String fullFontName) {
        if (textView == null || activity == null || fullFontName == null) return;
        AssetManager assets = activity.getAssets();
        if (assets == null) return;
        Typeface typeFace = Typeface.createFromAsset(assets, fullFontName);
        if (typeFace != null) textView.setTypeface(typeFace);
    }

    /**
     * Underlines text
     * @param textView
     */
    public static void underlineText(TextView textView) {
        if(textView != null) textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param activity
     * @param background
     * @param logo
     * @deprecated
     */
    public static void setUpActionBar(Activity activity, Drawable background, Drawable logo) {
        if (activity == null) return;
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(background);
        actionBar.setLogo(logo);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param activity
     * @param background
     * @param logoId of drawable res.
     * @deprecated
     */
    public static void setUpActionBar(Activity activity, Drawable background, int logoId) {
        if (activity == null) return;
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(background);
        actionBar.setLogo(logoId);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param activity
     * @param backgroundColorId R.color.someColor
     * @param logoId of drawable res.
     * @deprecated
     */
    public static void setUpActionBar(Activity activity, int backgroundColorId, int logoId) {
        if (activity == null) return;
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(new ColorDrawable(getColorFromRes(activity, backgroundColorId)));
        actionBar.setLogo(logoId);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param activity
     * @param backgroundColorId of colors.xml which is located under values folder.
     * @param logo
     * @deprecated
     */
    public static void setUpActionBar(Activity activity, int backgroundColorId, Drawable logo) {
        if (activity == null) return;
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(new ColorDrawable(getColorFromRes(activity, backgroundColorId)));
        actionBar.setLogo(logo);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param activity
     * @param backgroundColorCode "#FFFFFF"
     * @param logo
     * @deprecated
     */
    public static void setUpActionBar(Activity activity, String backgroundColorCode, Drawable logo) {
        if (activity == null) return;
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(backgroundColorCode)));
        actionBar.setLogo(logo);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param activity
     * @param backgroundColorCode "#FFFFFF"
     * @param logoId of drawable res.
     * @deprecated
     */
    public static void setUpActionBar(Activity activity, String backgroundColorCode, int logoId) {
        if (activity == null) return;
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(backgroundColorCode)));
        actionBar.setLogo(logoId);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param actionBar activity.getActionBar()
     * @param backgroundColorCode android.graphics.Color.WHITE
     * @param logoId of drawable res.
     * @deprecated
     */
    public static void setUpActionBar(ActionBar actionBar, int backgroundColorCode, int logoId) {
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(new ColorDrawable(backgroundColorCode));
        actionBar.setLogo(logoId);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * <img src="../../../../../doc/actionBar.PNG" /></br></br>
     * Sets up action bar background and icon.
     * @param actionBar activity.getActionBar()
     * @param backgroundColorCode android.graphics.Color.WHITE
     * @param logo
     * @deprecated
     */
    public static void setUpActionBar(ActionBar actionBar, int backgroundColorCode, Drawable logo) {
        if (actionBar == null) return;
        actionBar.setBackgroundDrawable(new ColorDrawable(backgroundColorCode));
        actionBar.setLogo(logo);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Creates new dialog without title and with transparent background,<br>configures additional options according to parameters.
     * @param activity to create dialog.
     * @param clearDim true means that activity will not get darker after dialog show.
     * @param fullScreen true means that this window will not be cropped by status bar if this one is located at the top of screen.
     * @param CancelOnOutsideTouch true means that it will be dismissed on outside touch of dialog.
     * @param layoutId to set content view.
     * @return newly created and configured Dialog.
     */
    public static Dialog create(Activity activity, boolean clearDim, boolean fullScreen, boolean CancelOnOutsideTouch, int layoutId) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            if(clearDim) window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            if(fullScreen) window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        dialog.setContentView(layoutId);
        dialog.setCanceledOnTouchOutside(CancelOnOutsideTouch);
        return dialog;
    }

    /**
     * Creates new dialog without title, with transparent background and custom animation.</br></br>
     * <img src="../../../../../doc/DialogAnimation.PNG" /></br></br>
     * Where this style is written in styles.xml file , located under values folder.</br>
     * fade_in_anim_for_example is actual animation located in anim folder and is used to animate dialog show.</br>
     * Second fade_out animation is used to animate dialog hide.</br></br>
     * @param activity to create dialog.
     * @param clearDim true means that activity will not get darker after dialog show.
     * @param fullScreen true means that this window will not be cropped by status bar if this one is located at the top of screen.
     * @param CancelOnOutsideTouch true means that it will be dismissed on outside touch of dialog.
     * @param layoutId to set content view.
     * @param animName R.style.yourDialogAnimName
     * @return newly created and configured Dialog.
     */
    public static Dialog create(Activity activity, boolean clearDim, boolean fullScreen, boolean CancelOnOutsideTouch, int layoutId, int animName) {
        Dialog dialog = create(activity, clearDim, fullScreen, CancelOnOutsideTouch, layoutId);
        setAnimation(dialog, animName);
        return dialog;
    }

    /**
     * Creates new dialog without title and with transparent background,<br>configures additional options according to parameters.
     * @param activity to create dialog.
     * @param dimAmount 1.0 means totally black and 0.0 means no dim.
     * @param fullScreen true means that this window will not be cropped by status bar if this one is located at the top of screen.
     * @param CancelOnOutsideTouch true means that it will be dismissed on outside touch of dialog.
     * @param layoutId to set content view.
     * @return newly created and configured Dialog.
     */
    public static Dialog create(Activity activity, float dimAmount, boolean fullScreen, boolean CancelOnOutsideTouch, int layoutId) {
        Dialog dialog = create(activity, false, fullScreen, CancelOnOutsideTouch, layoutId);
        Window window = dialog.getWindow();
        if (window == null) return dialog;
        LayoutParams attributes = window.getAttributes();
        if(attributes != null) attributes.dimAmount = dimAmount;
        return dialog;
    }

    /**
     * Creates new dialog without title, with transparent background and custom animation.</br></br>
     * <img src="../../../../../doc/DialogAnimation.PNG" /></br></br>
     * Where this style is written in styles.xml file , located under values folder.</br>
     * fade_in_anim_for_example is actual animation located in anim folder and is used to animate dialog show.</br>
     * Second fade_out animation is used to animate dialog hide.</br></br>
     * @param activity to create dialog.
     * @param dimAmount 1.0 means totally black and 0.0 means no dim.
     * @param fullScreen true means that this window will not be cropped by status bar if this one is located at the top of screen.
     * @param CancelOnOutsideTouch true means that it will be dismissed on outside touch of dialog.
     * @param layoutId to set content view.
     * @param animName R.style.yourDialogAnimName
     * @return newly created and configured Dialog.
     */
    public static Dialog create(Activity activity, float dimAmount, boolean fullScreen, boolean CancelOnOutsideTouch, int layoutId, int animName) {
        Dialog dialog = create(activity, dimAmount, fullScreen, CancelOnOutsideTouch, layoutId);
        setAnimation(dialog, animName);
        return dialog;
    }

    /**
     * Sets gravity to dialog.
     * </br>Shows dialog at the bottom , top or center of the screen, according to gravity.
     * @param dialog
     * @param gravity android.view.Gravity.TOP,
     * </br>android.view.Gravity.BOTTOM,</br>android.view.Gravity.CENTER,
     * </br>android.view.Gravity.CENTER_VERTICAL,</br> ...
     */
    public static void setGravity(Dialog dialog, int gravity) {
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = gravity;
    }

    /**
     * Sets animation to dialog.
     * <img src="../../../../../doc/DialogAnimation.PNG" /></br></br>
     * Where this style is written in styles.xml file , located under values folder.</br>
     * fade_in_anim_for_example is actual animation located in anim folder and is used to animate dialog show.</br>
     * Second fade_out animation is used to animate dialog hide.</br></br>
     * @param dialog
     * @param animName R.style.yourDialogAnimName
     */
    public static void setAnimation(Dialog dialog, int animName) {
        Window window = dialog.getWindow();
        if (window == null) return;
        LayoutParams attributes = window.getAttributes();
        if(attributes != null) attributes.windowAnimations = animName;
    }

    /**
     * Dismiss and release all resources of dialog.
     * @param dialog
     */
    public static Dialog clearDialog(Dialog dialog) {
        if(dialog == null) return null;
        try {
            dialog.dismiss();
        } catch (Exception e) {}
        return null;
    }

    /**
     * Converts dp to pixel.
     * @param activity
     * @param dp
     * @return pixel.
     */
    public static float dpToPixel(Activity activity, float dp) {
        Resources resources = getResources(activity);
        if (resources == null) return 0;
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics == null ? 0 : dp * metrics.density;
    }

    /**
     * Converts pixel to dp.
     * @param activity
     * @param px
     * @return dp.
     */
    public static float pixelToDp(Activity activity, float px) {
        Resources resources = getResources(activity);
        if (resources == null) return 0;
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics == null ? 0 : px / metrics.density;
    }

    public static float getDensity(Activity activity){
        Resources resources = getResources(activity);
        if(resources == null) return 0;
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics == null ? 0 : displayMetrics.density;
    }

    /**
     * Sets them , layout and title for activity.
     * @param activity
     * @param style R.style.AppTheme
     * @param layout R.layout.activity_main
     * @param title
     * @deprecated set them and title on each activity or on app in manifest file.
     */
    public static void setup(Activity activity, int style, int layout, String title) {
        activity.setTheme(style);
        activity.setContentView(layout);
        activity.setTitle(title);
    }

    /**
     * Sets full screen and hides software navigation buttons if that one exists.
     * </br>Is recommended to use in onWindowFocusChanged method of Activity.
     * </br>(or call after all dialog dismiss)
     * @param activity
     */
    public static void hideNavigationButtons(Activity activity) {
        if (activity == null) return;
        Window window = activity.getWindow();
        if (window == null) return;
        View decorView = window.getDecorView();
        if (decorView == null) return;
        switch (Build.VERSION.SDK_INT){
            case Build.VERSION_CODES.KITKAT : KitkatSystemUiVisibility(decorView); break;
            case Build.VERSION_CODES.JELLY_BEAN : jellyBeanSystemUiVisibility(decorView); break;
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH : iceCreamSandwichSystemUiVisibility(decorView,window); break;
        }
    }

    private static void iceCreamSandwichSystemUiVisibility(View decorView, Window window) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
    }

    @SuppressLint("InlinedApi")
    private static void KitkatSystemUiVisibility(View decorView) {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @SuppressLint("InlinedApi")
    private static void jellyBeanSystemUiVisibility(View decorView) {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * Hides status bar.
     * @param activity
     */
    public static void hideStatusBar(Activity activity) {
        if (activity == null) return;
        Window window = activity.getWindow();
        if (window == null) return;
        WindowManager.LayoutParams attrs = window.getAttributes();
        if (attrs == null) return;
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);
    }

    /**
     * Detects if device has hardware buttons.
     * @param activity
     * @return true if device has hardware buttons, otherwise false.
     */
    public static boolean hasHardwareButtons(Activity activity) {
        try {
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            boolean home = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
            boolean apps = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_APP_SWITCH);
            boolean menu = false;
            if (activity != null) menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            return back || home || apps || menu;
        } catch (Exception e) {}
        return false;
    }

    /**
     * Sets EditText input type to plain text password and also makes password visible.
     * @param editText
     */
    public static void showPassword(EditText editText) {
        if(editText != null) editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    /**
     * Sets EditText input type to plain text password and also makes password invisible.
     * @param editText
     */
    public static void hidePassword(EditText editText) {
        if (editText != null) editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    /**
     * @param activity
     * @return true if network is connected, false otherwise.
     */
    public static boolean isNetworkConnected(Activity activity) {
        getConnectivityManager(activity);
        if (connectivityManager == null) return false;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    /**
     * @param activity
     * @return true if connection type is wifi, false otherwise.
     */
    public static boolean isConnectionTypeWifi(Activity activity) {
		/*
		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return (wifiNetwork != null && wifiNetwork.isConnected());
		*/
        //TODO should be tested
        getConnectivityManager(activity);
        if (connectivityManager == null) return false;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && (info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    //TODO should be tested
    /**
     * @param activity
     * @return true if connection type is ethernet, false otherwise.
     */
    public static boolean isConnectionTypeEthernet(Activity activity) {
		/*
		NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		return (networkInfo != null && networkInfo.isConnected());*/

        getConnectivityManager(activity);
        if (connectivityManager == null) return false;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && (info.isConnected() && info.getType() == ConnectivityManager.TYPE_ETHERNET);
    }

    /**
     * @param activity
     * @return connection type: G2 or G3 or G4 or WIFI or ETHERNET or UNKNOWN.
     */
    public static int getConnectionType(Activity activity) {
        if (activity == null) return UNKNOWN;
        if (isConnectionTypeWifi(activity)) return WIFI;
        if (isConnectionTypeEthernet(activity)) return ETHERNET;
        getTelephonyManager(activity);
        if (telephonyManager == null) return UNKNOWN;
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return G2;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return G3;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return G4;
            default:
                return UNKNOWN;
        }
    }

    /**
     * Converts connection type in to string
     * @param connectionType
     * @return
     */
    public static String convertToStr(int connectionType){
        switch (connectionType){
            case ETHERNET: return "Ethernet";
            case WIFI: return "Wifi";
            case G2: return "2g";
            case G3: return "3g";
            case G4: return "4g";
            default: return  "Unknown";
        }
    }

    /**
     * @return local ip address.
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            //log(ex.toString());
        }
        return null;
    }

    /**
     * Activity method onRequestPermissionsResult should be overridden in given activity to handle location permission if this one is not written in manifest file.<br>
     * In onRequestPermissionsResult returned permissions will be only one permission: Manifest.permission.ACCESS_FINE_LOCATION and returned requestCode will be same requestCode which you will gave here</br>
     * and of course last return parameter will be 0 if permission was granted(PackageManager.PERMISSION_GRANTED) or -1 if permission was denied (PackageManager.PERMISSION_DENIED).<br>
     * If location permission is written in manifest file then onRequestPermissionsResult overridden is not necessary.<br>
     * App life cycle should be handled by you just recommendation. Seriously handle App life cycle if ACCESS_FINE_LOCATION is not written in manifest and you ar going to show permission dialog.<br>
     * @param activity
     * @param requestCode what ever you want if permission.ACCESS_FINE_LOCATION is written in manifest, or number to return in onRequestPermissionsResult if this permission is not written in manifest.
     * @param showPermissionDialog if permission.ACCESS_FINE_LOCATION is not written in manifest and you do not want to show permission dialog give false, if you want to show permission dialog give true. If permission is written in manifest you can send wat ever you want.
     * @return latitude:longitude or null
     */
    public static String getLocation(Activity activity, int requestCode,boolean showPermissionDialog) {
        if(activity == null) return null;
        String locationStr = null;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return null;
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;
        if (isNetworkEnabled) {
            location = reqLocUpdatesAndGetLastLoc(activity,requestCode,showPermissionDialog, locationManager, LocationManager.NETWORK_PROVIDER);
        } else if (isGPSEnabled) {
            location = reqLocUpdatesAndGetLastLoc(activity,requestCode,showPermissionDialog,locationManager, LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            locationStr = latitude + ":" + longitude;
        }
        return locationStr;
    }

    /**
     * Request location updates and get last known location.
     * @param locationManager
     * @param provider
     * @return Last known location.
     */
    private static Location reqLocUpdatesAndGetLastLoc(Activity activity,int requestCode,boolean showPermissionDialog, LocationManager locationManager, String provider) {
        LocationListener locationListener = new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
            public void onLocationChanged(Location location) {}
        };
        long updates = 60000;// 1 minute
        float distance = 10;// meter
        int permissionState = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if ( permissionState != PackageManager.PERMISSION_GRANTED ) {
            if(showPermissionDialog){
                String [] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(activity,permissions,requestCode);
            }
            return null;
        }
        locationManager.requestLocationUpdates(provider, updates, distance, locationListener);
        return locationManager.getLastKnownLocation(provider);
    }

    /**
     * @param activity
     * @return width:height
     */
    public static String getResolution(Activity activity) {
        if(activity == null) return null;
        WindowManager windowManager = activity.getWindowManager();
        if(windowManager == null) return null;
        Display display = windowManager.getDefaultDisplay();
        if(display == null) return null;
        Point size = new Point();
        display.getSize(size);
        return size.x + ":" + size.y;
    }

    /**
     * @param activity
     * @return PackageInfo for future use.
     */
    public static PackageInfo getPackageInfo(Activity activity){
        if(activity == null) return null;
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager == null) return null;
        String packageName = activity.getPackageName();
        try {
            return packageName == null ? null : packageManager.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {}
        return null;
    }

    /**
     * @param activity
     * @return App version name which is represented in AndroidManifest.xml, under
     *         manifest tag, value of versionName.
     */
    public static String getAppVersionName(Activity activity) {
        PackageInfo packageInfo = getPackageInfo(activity);
        return packageInfo == null ? null : packageInfo.versionName;
    }

    /**
     * @param activity
     * @return App version code which is represented in AndroidManifest.xml, under
     *         manifest tag, value of versionCode.
     */
    public static int getAppVersionCode(Activity activity) {
        PackageInfo packageInfo = getPackageInfo(activity);
        return packageInfo == null ? 0 : packageInfo.versionCode;
    }

    /**
     * @return name of device.
     */
    public static String getDeviceName() {
        String manufacturer = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        String ret = "";
        if (manufacturer != null) ret += (manufacturer + " ");
        if (model != null) ret += (model + " ");
        return ret.trim();
    }

    /**
     * @return os version.
     */
    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * @param activity
     * @return Mac address.
     */
    public static String getMacAddress(Activity activity) {
        if(activity == null) return null;
        WifiManager manager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        if(manager == null) return null;
        WifiInfo info = manager.getConnectionInfo();
        return info == null ? null : info.getMacAddress();
    }

    /**
     * @param activity
     * @return wifi name.
     */
    public static String getWifiSsid(Activity activity) {
        if(activity == null) return null;
        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager == null) return null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo == null ? null : wifiInfo.getSSID();
    }

    /**
     * @param activity
     * @return wifi name (ssid).
     */
    public static String getWifiName(Activity activity){
        return getWifiSsid(activity);
    }

    /**
     * @param activity
     * @return Device id.
     */
    public static String getDeviceId(Activity activity) {
        if(activity == null)return null;
        return Secure.getString(activity.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * @param activity
     * @return Display size in Point.
     */
    public static Point getDisplayPoint(Activity activity) {
        if(activity == null)return null;
        WindowManager windowManager = activity.getWindowManager();
        if(windowManager == null) return null;
        Display display = windowManager.getDefaultDisplay();
        if(display == null) return null;
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * @return cpu name.
     */
    public static String getChipset() {
        return android.os.Build.HARDWARE;
    }

    /**
     * @return cpu name.
     */
    public static String getCpuName() {
        return getChipset();
    }

    /**
     * Removes value from SharedPreferences.
     * @param activity
     * @param key
     */
    public static void remove(Activity activity,String key){
        if(key == null) return;
        if(sharedPref == null) getSharedPref(activity);
        //sharedPref.edit().remove(key).commit();
        sharedPref.edit().remove(key).apply();
    }

    /**
     * Adds boolean value to SharedPreferences.
     * @param activity
     * @param key
     * @param value
     */
    public static void add(Activity activity,String key, boolean value){
        if(key == null) return;
        if(sharedPref == null) getSharedPref(activity);
        //sharedPref.edit().putBoolean(key, value).commit();
        sharedPref.edit().putBoolean(key, value).apply();
    }

    /**
     * Adds int value to SharedPreferences.
     * @param activity
     * @param key
     * @param value
     */
    public static void add(Activity activity,String key, int value){
        if(key == null) return;
        if(sharedPref == null) getSharedPref(activity);
        //sharedPref.edit().putInt(key, value).commit();
        sharedPref.edit().putInt(key, value).apply();
    }

    /**
     * Adds String value to SharedPreferences.
     * @param activity
     * @param key
     * @param value
     */
    public static void add(Activity activity,String key, String value){
        if(key == null) return;
        if(sharedPref == null) getSharedPref(activity);
        //sharedPref.edit().putString(key, value).commit();
        sharedPref.edit().putString(key, value).apply();
    }

    /**
     * Adds long value to SharedPreferences.
     * @param activity
     * @param key
     * @param value
     */
    public static void add(Activity activity,String key, long value){
        if(key == null) return;
        if(sharedPref == null) getSharedPref(activity);
        //sharedPref.edit().putLong(key, value).commit();
        sharedPref.edit().putLong(key, value).apply();
    }

    /**
     * @param activity
     * @param key
     * @param def default value which should be returned if value was not found on given key.
     * @return boolean value from SharedPreferences.
     */
    public static boolean get(Activity activity,String key, boolean def){
        if(key == null) return def;
        if(sharedPref == null) getSharedPref(activity);
        return sharedPref.getBoolean(key, def);
    }

    /**
     * @param activity
     * @param key
     * @param def default value which should be returned if value was not found on given key.
     * @return int value from SharedPreferences.
     */
    public static int get(Activity activity,String key, int def){
        if(key == null) return def;
        if(sharedPref == null) getSharedPref(activity);
        return sharedPref.getInt(key, def);
    }

    /**
     * @param activity
     * @param key
     * @param def default value which should be returned if value was not found on given key.
     * @return String value from SharedPreferences.
     */
    public static String get(Activity activity,String key, String def){
        if(key == null) return def;
        if(sharedPref == null) getSharedPref(activity);
        return sharedPref.getString(key, def);
    }

    /**
     * @param activity
     * @param key
     * @param def default value which should be returned if value was not found on given key.
     * @return long value from SharedPreferences.
     */
    public static long get(Activity activity,String key, long def){
        if(key == null) return def;
        if(sharedPref == null) getSharedPref(activity);
        return sharedPref.getLong(key, def);
    }

    /**
     * @param activity
     * @return true if is phone, or phablet
     */
    public static boolean isPhone(Activity activity){
        return activity != null && 0 == getIntFromRes(activity, R.integer.deviceSize);
    }

    /**
     * @param activity
     * @return true if is 7 inch tablet, or 8 inch tablet.
     */
    public static boolean isTablet7(Activity activity){
        return activity != null && 1 == getIntFromRes(activity, R.integer.deviceSize);
    }

    /**
     * @param activity
     * @return true if is 10 inch tablet.
     */
    public static boolean isTablet10(Activity activity){
        return activity != null && 2 == getIntFromRes(activity, R.integer.deviceSize);
    }

    /**
     * Detects if app is running on TV Device.
     * @param activity
     * @return true if device uses xlarge size or has feature TELEVISION or LEANBACK. false otherwise.
     */
    public static boolean isTV(Activity activity){
        if(activity == null) return false;
        int tmp = getIntFromRes(activity, R.integer.deviceSize);
        boolean size = (tmp == 2);
        PackageManager packageManager = activity.getPackageManager();
        if(packageManager == null) return size;
        boolean television = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEVISION);
        boolean leanback = packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK);
        return (television || leanback || size);
    }

    /**
     * Use <b>"Tools"</b> as tag for information logging.</br>
     * </br>If you want to disable all logs without commenting them, just call disableLogs() method.
     * @param text
     */
    public static void log(String text) {
        if(disableLogs)return;
        try {
            Log.d(LOG_TAG, text);
        } catch (Exception e) {}
    }

    /**
     * Removes year field from DatePicker view.
     * @param activity
     * @param datePicker which filed should be deleted.
     */
    public static void removeYearField(Activity activity,DatePicker datePicker) {
        try {
            int yearSpinnerId = getResources(activity).getIdentifier("year", "id", "android");
            if (yearSpinnerId == 0) return;
            View yearSpinner = datePicker.findViewById(yearSpinnerId);
            if (yearSpinner != null) yearSpinner.setVisibility(View.GONE);
        } catch (Exception e) {}
    }

    /**
     * Changes month language of DatePicker view.
     * This method changes only once and if you want language to be changed after date selection, you should set {@link DateChangedListener}.<br></br>
     * Also this method should be called after {@link DatePicker#init(int, int, int, OnDateChangedListener)} method to change language properly.
     * @param activity
     * @param datePicker datePicker which month language should be changed.
     * @param arrayId R.array.long_months_values for example.
     */
    public static void changeMonthLanguage(Activity activity,DatePicker datePicker, int arrayId){
        try {
            Resources resources = getResources(activity);
            String [] months = resources.getStringArray(arrayId);
            int monthSpinnerId = resources.getIdentifier("month", "id", "android");
            if (monthSpinnerId == 0)return;
            NumberPicker monthSpinner = (NumberPicker) datePicker.findViewById(monthSpinnerId);
            if (monthSpinner != null) monthSpinner.setDisplayedValues(months);
        } catch (Exception e) {}
    }

    /**
     * Calls changeMonthLanguage method when date is change to set chosen language again,</br>
     * because after date change this one is change by android DataPicker Api,</b>
     * so we need to set our month language again.
     */
    public static class DateChangedListener implements OnDateChangedListener{
        Activity activity;
        int arrayId;
        public DateChangedListener(Activity activity, int arrayId) {
            this.activity = activity;
            this.arrayId = arrayId;
        }

        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            changeMonthLanguage(activity,datePicker,arrayId);
        }
    }

    /**
     * Shows software keyboard for EditText.<br><br/>
     * This method should be called after requestFocus of given editText.
     * @param activity to initiate keyboard if this one does not exists.
     * @param editText for hom to show keyboard.
     */
    public static void showKeyboard(Activity activity, EditText editText){
        if(activity == null || editText == null) return;
        InputMethodManager keyboard = Util.getKeyboard(activity);
        if(keyboard == null) return;
        keyboard.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Hides software keyboard for dialog.
     * @param window dialog.getWindow() for example.
     */
    public static void hideKeyboard(Window window){
        if(window != null) window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Hides software keyboard for editText.
     * @param activity
     * @param WindowToken editText.getWindowToken() for example.
     */
    public static void hideKeyboard(Activity activity, IBinder WindowToken){
        if(WindowToken == null) return;
        getKeyboard(activity);
        if(keyboard != null) keyboard.hideSoftInputFromWindow(WindowToken, 0);
    }

    /**
     * Plays notification sound.
     * @param activity
     */
    public static void playNotificationSound(Activity activity){
        if(activity == null) return;
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if(notification == null) return;
        Ringtone ringtone = RingtoneManager.getRingtone(activity, notification);
        if(ringtone != null) ringtone.play();
    }

    /**
     * Sends GoogleAnalytics data
     * @param activity
     * @param analyticsId
     * @param screenName
     */
    public static void sendAnalyticsData(Activity activity,String analyticsId,String screenName) {
        try {
            Tracker tracker = GoogleAnalytics.getInstance(activity).newTracker(analyticsId);
            tracker.enableAdvertisingIdCollection(true);
            tracker.setSessionTimeout(60);
            tracker.setScreenName(screenName);
            //tracker.send(new HitBuilders.AppViewBuilder().build());
            tracker.send(new HitBuilders.ScreenViewBuilder().build());//TODO should be tested
        } catch (Exception e) { log(e.toString()); }
    }

    /**
     * @param activity to create powerManager if it is not created.
     * @return true if device is locked, false otherwise.
     */
    public static boolean isDeviceLocked(Activity activity){
        try {
            getPowerManager(activity);
            if(powerManager == null) return false;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH){
                return isDeviceLockedNewStyle(powerManager);
            }
            return isDeviceLockedOldStyle(powerManager);
        } catch (Exception e) {
            log("isDeviceLocked: Exception: " + e.toString());
        }
        return false;
    }

    @SuppressLint("NewApi")
    private static boolean isDeviceLockedNewStyle(PowerManager powerManager){
        return !powerManager.isInteractive();
    }

    @SuppressWarnings("deprecation")
    private static boolean isDeviceLockedOldStyle(PowerManager powerManager){
        return !powerManager.isScreenOn();
    }

    /**
     * @param activity
     * @return true if android device has call capabilities, false otherwise.
     */
    public static boolean canCall(Activity activity){
        if(activity == null) return false;
        PackageManager pm = activity.getPackageManager();
        return pm != null && pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    /**
     * Opens current app page in play store.
     * @param activity
     */
    public static void openPlayStore(Activity activity){
        if(activity == null) return;
        String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Opens given app page in play store.
     * @param activity
     * @param appPackageName to open given app in play store. activity.getPackageName().
     */
    public static void openPlayStore(Activity activity,String appPackageName){
        if(activity == null || appPackageName == null) return;
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Checks if given service class is running or not.
     * @param serviceClass should be subclass of {@link android.app.Service}
     * @return true if given service is running, false otherwise.
     */
    public static boolean isServiceRunning(Activity activity, Class<?> serviceClass) {
        if(serviceClass == null) return false;
        String myServiceName = serviceClass.getName();
        if(myServiceName == null) return false;
        getActivityManager(activity);
        if(activityManager == null) return false;
        List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        if(services == null || services.size() <= 0) return false;
        log("Number of running services: " + services.size());
        for (RunningServiceInfo service : services) {
            if(service == null) continue;
            if(service.service == null)continue;
            String serviceName = service.service.getClassName();
            if(serviceName ==null) continue;
            if (myServiceName.equals(serviceName)) return true;
        }
        return false;
    }

    /**
     * This method should be called asynchronously (from non ui thread) because this call
     * requires downloading data from internet.
     * @return global ip address
     */
    public static String getGlobalIpAddress(){
        if(globalIpDetectorLinks == null || globalIpDetectorLinks.length <= 0 ) {
            Util.log("globalIpDetectorLinks is null or empty!");
            return null;
        }
        for (String link: globalIpDetectorLinks) {
            try {
                return getPublicIpAddress(link);
            } catch (Exception e) {
                log("There was some problem while trying to retrieve global ip address: " + e.toString());
            }
        }
        log("Global ip can not be retrieved");
        return null;
    }

    private static String getPublicIpAddress(String link) throws Exception {
        Util.log("Trying to get global ip address from: " + link);
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(2));
        InputStream inputStream = url.openStream();
        Scanner s = new Scanner(inputStream, Helper.UTF8).useDelimiter("\\A");
        return s.next();
    }

    /**
     * Closes appliaction
     * @param activity
     */
    public static void closeApp(Activity activity){
        try {
            if(activity != null) activity.finishAffinity();
        }catch (Exception e){
            log("There was some problem, while trying to call finishAffinity: " + e.toString());
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    /**
     * Restarts application after 100 millisecond delay.
     * @param activity
     * @param activityClass
     */
    public static void restartApp(Activity activity,Class activityClass){
        Intent mStartActivity = new Intent(activity, activityClass);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(activity, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    /**
     * Restarts application
     * @param activity
     * @param activityClass
     * @param delay after which app should be started
     */
    public static void restartApp(Activity activity,Class activityClass, int delay){
        Intent mStartActivity = new Intent(activity, activityClass);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(activity, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + delay, mPendingIntent);
        System.exit(0);
    }



	/*	public static void showNotification(Activity activity, int iconId, String title, String text, Class open) {
	if(activity == null) return;
	NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
	builder = builder.setSmallIcon(iconId);
	builder = builder.setContentTitle(title);
	builder = builder.setContentText(text);

	Intent resultIntent = new Intent(activity, /*MainActivity.class*//*open);

	PendingIntent resultPendingIntent = PendingIntent.getActivity(
					instance,
					0,
					resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

	builder.setContentIntent(resultPendingIntent);

	Intent intent = new Intent(NotificationDismiss.DISMISS);
	PendingIntent deletePendingIntent = PendingIntent.getBroadcast(instance, 0, intent, 0);
	builder.setDeleteIntent(deletePendingIntent);

	Notification note = builder.build();

	notificationManager.notify(Constants.NOTIFICATION_EXIT, note);
}
*/


    //TODO should be tested
		/*
		public static void add(SharedPreferences sharedPref,String key, Object value){
			To Save

		     Editor prefsEditor = mPrefs.edit();
		     Gson gson = new Gson();
		     String json = gson.toJson(MyObject);
		     prefsEditor.putString("MyObject", json);
		     prefsEditor.commit();
		To Retreive

		    Gson gson = new Gson();
		    String json = mPrefs.getString("MyObject", "");
		    MyObject obj = gson.fromJson(json, MyObject.class);
		}
		*/

}
