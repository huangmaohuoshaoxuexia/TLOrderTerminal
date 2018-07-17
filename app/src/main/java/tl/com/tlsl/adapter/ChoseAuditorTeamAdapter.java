package tl.com.tlsl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tl.com.tlsl.R;

/**
 * Created by admin on 2017/10/20.
 */

public class ChoseAuditorTeamAdapter extends BaseAdapter{
    private LayoutInflater mlayoutInflater;
    private Context mContext;
    private ArrayList mList;
    public ChoseAuditorTeamAdapter(Context context, ArrayList list){
        this.mContext = context;
        mlayoutInflater = LayoutInflater.from(mContext);
        this.mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
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
            convertView = mlayoutInflater.inflate(R.layout.select_auditor_item, null);
            mHolder = new ViewHolder();
            mHolder.textView = convertView.findViewById(R.id.team_text);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.textView.setText(mList.get(position).toString());
        return convertView;
    }
    class ViewHolder{
        TextView textView;
    }
}
