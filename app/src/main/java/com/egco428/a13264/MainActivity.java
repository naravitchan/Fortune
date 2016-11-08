package com.egco428.a13264;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CommentDataSource dataSource;
    private ArrayAdapter<Comment> arrayAdapter;

    int count=1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, NewfortuneActivity.class);
        startActivityForResult(intent,count);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSource = new CommentDataSource(this);
        dataSource.open();
        List<Comment> values = dataSource.getAllComments();
        ListView listView = (ListView)findViewById(R.id.listView);
        arrayAdapter = new CourseArrayAdapter(this,0,values);
       // ArrayAdapter<Comment> courseArrayAdapter = new CourseArrayAdapter(this,0,values);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,final View view,final int position,
                                    long id) {
                final Comment comment =arrayAdapter.getItem(position);
                dataSource.deleteComment(comment);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.remove(comment);
                        view.setAlpha(1);
                    }
                });
            }
        });
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    class CourseArrayAdapter extends ArrayAdapter<Comment>{
        Context context;
        List<Comment> objects;
        public CourseArrayAdapter(Context context,int resource,List<Comment> objects){
            super(context,resource,objects);
            this.context = context;
            this.objects = objects;
        }
        @Override public View getView(int position, View convertView, ViewGroup parent){
            Comment course = objects.get(position);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list,null);
            TextView txt = (TextView)view.findViewById(R.id.resultlist);
            txt.setText(course.getComment());
            if((course.getComment().equals("Don't Panic"))||(course.getComment().equals("Work Hard"))){
                txt.setTextColor(Color.parseColor("#E38334"));
            }
            else{
                txt.setTextColor(Color.parseColor("#0A29F6"));
            }
            TextView txt2 = (TextView)view.findViewById(R.id.datelist);
            txt2.setText(course.getDate());
            int res = context.getResources().getIdentifier(course.getPicture(),"drawable",context.getPackageName());
            ImageView image = (ImageView)view.findViewById(R.id.imagelist);
            image.setImageResource(res);
            return view;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        dataSource.open();
        List<Comment> values = dataSource.getAllComments();
        ListView listView = (ListView)findViewById(R.id.listView);
        arrayAdapter = new CourseArrayAdapter(this,0,values);
        listView.setAdapter(arrayAdapter);
    }
    @Override
    protected void onPause(){
        super.onPause();
        dataSource.close();
    }


}
