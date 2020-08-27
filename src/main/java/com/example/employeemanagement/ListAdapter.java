package com.example.employeemanagement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends BaseAdapter {
Context context;
//List<Employee> list;
    List<Employee> employees;
    private EmployeeOperations employeeOps;
public ListAdapter(Context context,List employees){
    this.context = context;
    this.employees = employees;
}
    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int i) {
        return employees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
      View view1= LayoutInflater.from(context).inflate(R.layout.listlayout,viewGroup,false);



        de.hdodenhof.circleimageview.CircleImageView imageView = view1.findViewById(R.id.profile_image);
        TextView textView = view1.findViewById(R.id.tv);

       int count=0;
        while (count<employees.size()) {
           // Log.d("Count Is",count+"");
            Employee employee = new Employee();
            imageView.setImageBitmap(employees.get(i).getImage());
            textView.setText(employees.get(i).tostring());
           count++;
        }
        return view1;

        //return null;

    }


}
