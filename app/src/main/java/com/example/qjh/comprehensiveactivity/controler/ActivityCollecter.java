package com.example.qjh.comprehensiveactivity.controler;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollecter   {
    public static List<Activity> activities=new ArrayList<>();

    public static  void AddActivity(Activity activity)
    {
        activities.add(activity);
    }
    public static  void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }

    public static void FinishALL()
    {
         for(Activity activity:activities)
            {
                if(!activity.isFinishing())
                {
                    activity.finish();    //结束acticity
                }
            }

        activities.clear();
    }



}
