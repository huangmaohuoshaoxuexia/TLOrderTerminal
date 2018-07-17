package tl.com.tlsl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tl.com.tlsl.R;

/**
 * Created by admin on 2017/10/20.
 */

public class TerminalListAdapter extends BaseAdapter{
    private LayoutInflater mlayoutInflater;
    private Context mContext;
    private ArrayList<String> mList;
    public TerminalListAdapter(Context context,ArrayList<String> list){
        this.mContext = context;
        mlayoutInflater = LayoutInflater.from(mContext);
        this.mList = list;
    }
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if(convertView==null){
            convertView = mlayoutInflater.inflate(R.layout.terminal_search_list_item, null);
            mHolder = new ViewHolder();
            mHolder.textView = convertView.findViewById(R.id.terminal_item);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        TextView textView;
    }
}
