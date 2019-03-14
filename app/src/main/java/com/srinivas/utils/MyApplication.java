package com.srinivas.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ContextThemeWrapper;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.srinivas.biowax.R;


public class MyApplication extends Application {
    private static Context context;
    private static boolean isNew;
    public static AlertDialog dgnw;
    private SharedPreferences sharedPreferences;
    private boolean upgrade;
    private Location currentLocation;

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static boolean isNew() {
        return isNew;
    }

    public static void setAppStatus(boolean isNew) {
        MyApplication.isNew = isNew;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        //ACRA.init(this);
        context = getApplicationContext();
		/*sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		try {
			String versionName = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0).versionName;
			sharedPreferences.edit().putString(MainActivity.KEY_VERSION_NAME, versionName).apply();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}*/
        initImageLoader(getApplicationContext());
    }

    /**
     * Method to find if device is in 3G or not
     *
     * @return true if it is in 3G otherwise false
     */
    public static boolean is3GOr4GConnected() {
        if (isMobileConnected() || isWifiConnected())
            return true;
        else
            return false;
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return ((netInfo != null) && netInfo.isConnected());
    }

    public static boolean isMobileConnected() {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return ((netInfo != null) && netInfo.isConnected());
    }

    /*
     * Network connection dialog
     */

    public static void showDialog(final Activity activity) {
        Builder builder = new Builder(new ContextThemeWrapper(activity, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen));
        builder.setCancelable(false);
        //builder.setIcon(R.drawable.ic_action_no_network);
        builder.setTitle(MyApplication.getAppContext().getResources().getString(R.string.no_network_title));
        builder.setMessage(MyApplication.getAppContext().getResources().getString(R.string.no_network_msg));
        builder.setPositiveButton("Retry", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (MyApplication.is3GOr4GConnected()) {
                    dgnw.dismiss();
                    //startActivity(new Intent(ctx, MoreList.class));
                } else {
                    showDialog(activity);
                }
            }
        });
        dgnw = builder.create();
        dgnw.show();
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

/*	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i("App", "Application Terminated");
	}*/
}
