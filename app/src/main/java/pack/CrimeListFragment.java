package pack;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.test2_1.CrimePagerActivity;
import com.test2_1.R;

import java.util.ArrayList;

/**
 * Created by 昊天 on 2016/5/20.
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private Button button;
    private static CrimeAdapter crimeAdapter;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 因为Fragment 的onCreateOptionsMenu方法是由FragmentManager负责调用的
         * 因此，当Activity接收到来自操作系统的onCreateOptionsMenu方法回调请求时
         * 必须通知FragmentManager
         * 其管理的fragment应接收onCreateOptionsMenu方法的调用指令
         * 采用以下方法
         */
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);
        //Button实例化
        button= (Button) getActivity().findViewById(R.id.activity_main_add_btn);

        //先获取CrimeLab单例，在获取其中的Crime列表
        mCrimes=CrimeLab.get(getActivity()).getCrimes();
        crimeAdapter=new CrimeAdapter(mCrimes);
        setListAdapter(crimeAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CrimeLab.addCrime(new Crime());
//                crimeAdapter.notifyDataSetChanged();
                Crime crime=new Crime();
                CrimeLab.addCrime(crime);
                Intent intent=new Intent(getActivity(),CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getmID());
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=super.onCreateView(inflater,container,savedInstanceState);
        ListView listView= (ListView) view.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(listView);
        }else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            //设置监听器，实现接口回调的各方法
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {}

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater=mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter= (CrimeAdapter) getListAdapter();
                            CrimeLab crimeLab=CrimeLab.get(getActivity());
                            for (int i=adapter.getCount()-1;i>=0;i--){
                                if (getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                        default:return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        return view;
    }

    //实例化生成一个上下文菜单选项
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        /**
         * 首先获取与CrimeListActivity关联的MenuInflater
         * 然后调用Inflater方法，传入菜单资源ID和ContextMenu实例
         * 用菜单资源中的定义的菜单实例项填充菜单实例
         * 完成上下文菜单的创建
         */
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }
    //监听上下文菜单选项选择事件

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position=info.position;
        CrimeAdapter adapter= (CrimeAdapter) getListAdapter();
        Crime crime=adapter.getItem(position);
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    //响应菜单选项选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:{
                Crime crime=new Crime();
                CrimeLab.addCrime(crime);
                Intent intent=new Intent(getActivity(),CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getmID());
                startActivityForResult(intent,0);
                return true;
            }

            default:return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime crime= (Crime) getListAdapter().getItem(position);
        //切换到CrimeActivity
        Intent i=new Intent(getActivity(),CrimePagerActivity.class);
        //附带信息，显示相应item的详情页
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getmID());
//        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.toString());
        startActivity(i);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
        //上面刚刚用的是notifyAll()   出错了，不知道为什么
    }
    //实例化生成fragment_crime_list.xml中定义的菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /**
         * 为遵守约定，调用了超类的onCreateOptionsMenu方法
         * 以便把任何超类的选项菜单功能都在子方法中也获得应用
         * 但是事实上，超类的这个方法什么都没做=.=
         */
        super.onCreateOptionsMenu(menu, inflater);
        //下面将我们定义的菜单项目填充到Menu实例中
        inflater.inflate(R.menu.fragment_crime_list,menu);
    }

    //onPause时保存

    @Override
    public void onPause() {
        CrimeLab.get(getActivity()).saveCrimes();
        super.onPause();
    }

    public static void reflashAdapter(){
        crimeAdapter.notifyDataSetChanged();
    }

    private class CrimeAdapter extends ArrayAdapter<Crime>{
        public CrimeAdapter(ArrayList<Crime> crimes){
            super(getActivity(),0,crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * 首先检测传入的视图对象是否是复用对象
             * 如果不是，则从定制的布局里产生一个新的视图对象
             */
            if(convertView==null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }
            /**
             * 配置这个view和crime匹配
             * 获取列表中当前position的Crime对象
             * 引用视图对象的各个组件
             * 并以Crime的数据信息对应配置视图对象
             */
            Crime crime=getItem(position);
            TextView textView_Title= (TextView) convertView.findViewById(R.id.list_item_crime_title_tv);
            TextView textView_Date= (TextView) convertView.findViewById(R.id.list_item_crime_date_tv);
            CheckBox checkBox_solved= (CheckBox) convertView.findViewById(R.id.list_item_crime_solved_checkBox);
            textView_Title.setText(crime.getmTitle());
            textView_Date.setText(crime.getmDate().toString());
            checkBox_solved.setChecked(crime.ismSolved());
            return convertView;
        }
    }

}
