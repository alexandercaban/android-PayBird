package com.cristiancollazos.paybird;

import android.app.Application;

public class ApplicationSingleton extends Application {

    private static ApplicationSingleton objInstance;

    public static ApplicationSingleton getInstance() {
        if (objInstance == null) {
            objInstance = new ApplicationSingleton();
        }

        return objInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objInstance = this;
    }

}
