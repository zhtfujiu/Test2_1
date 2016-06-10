package pack;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import save_to_rom.CrimeIntentJSONSerializer;

/**
 * Created by 昊天 on 2016/5/20.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private static ArrayList<Crime> mCrimes;//空的，用来保存Crime对象的ArrayList

    private static final String TAG="CrimeLab";
    private static final String FILENAME="crimes.json";
    private CrimeIntentJSONSerializer mSerializer;
    private CrimeLab(Context mAppContext){
        this.mAppContext=mAppContext;
        mSerializer=new CrimeIntentJSONSerializer(mAppContext,FILENAME);
//        mCrimes=new ArrayList<Crime>();
        try {
            mCrimes=mSerializer.LoadCrimes();

        } catch (IOException e) {
            mCrimes=new ArrayList<Crime>();
            Log.e(TAG,"Error loading crimes:",e);
        }
        //暂时往数组列表里存入20个Crime对象

//        for (int i=0;i<20;i++){
//            Crime c=new Crime();
//            c.setmTitle("Crime # "+i);
//            c.setmSolved(i%3==0);//每隔三个选一个
//            mCrimes.add(c);
//        }
    }

    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }
    public boolean saveCrimes(){
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"Crimes saved to file");
            return true;
        } catch (IOException e) {
            Log.d(TAG,"Error saving crimes:",e);
            return false;
        }
    }

    public static void addCrime(Crime crime){
        mCrimes.add(crime);
    }

    public static CrimeLab get(Context context){
        if (sCrimeLab==null){
            sCrimeLab=new CrimeLab(context.getApplicationContext());
        }//Anytime，只要是应用层面的单例，就应该一直使用application context
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }
    public Crime getCrime(UUID uuid){
        for (int i=0;i<mCrimes.size();i++){
            if (mCrimes.get(i).getmID().equals(uuid)){
                return mCrimes.get(i);
            }
        }
        /**
         * 上面这个循环是我自己的思路设计的
         * 下面的注释内代码是书上的
         * 列表元素对象:列表 代表遍历
         */
        /*for (Crime c:mCrimes){
            if (c.getmID().equals(uuid)){
                return c;
            }
        }*/
        return null;
    }
}
