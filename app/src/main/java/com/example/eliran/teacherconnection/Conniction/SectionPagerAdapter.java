package com.example.eliran.teacherconnection.Conniction;

import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.eliran.teacherconnection.DetailFrag;

class SectionPagerAdapter extends FragmentPagerAdapter {


    public MyConnectionDetailFrag myFrag;
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                RequestFragment requestFragment=new RequestFragment();
                return  requestFragment;
            case 1:
                SentFrag sentFrag=new SentFrag();
                return  sentFrag;
            case 2:
                FriendsFrag friendsFrag=new FriendsFrag();
                return friendsFrag;
           /* case 3 :
                MyConnectionDetailFrag myConnectionDetailFrag=new MyConnectionDetailFrag();
                myFrag=myConnectionDetailFrag;
                return myConnectionDetailFrag;*/

            default:return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    public  CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "REQUESTS";
            case 1:
                return "SENT";
            case 2:
                return "CONNECTIONS";
            default: return null;
        }
    }
}
