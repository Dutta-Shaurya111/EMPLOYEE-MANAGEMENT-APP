package com.example.employeemanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ViewAllEmployees extends AppCompatActivity {
    private EmployeeOperations employeeOps;
    private List<Employee> employees;
    private ListView listView;
    private Spinner spinner;
    private ArrayList<String> arrayList;
    private Button sort;
    private ArrayAdapter<String> adapter;
    private EditText search_ed;
    private ImageView search_img;
    private  ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_employees);

        search_ed=findViewById(R.id.search_ed);
        search_img=findViewById(R.id.search_img);

        listView=findViewById(R.id.list);
        sort=findViewById(R.id.sort_btn);

        spinner = findViewById(R.id.sort_by);
        arrayList = new ArrayList<>();
        for(int i=0;i<6;i++){
        String[] arr = getResources().getStringArray(R.array.sort_array);
            arrayList.add(arr[i]);
        }

        employeeOps = new EmployeeOperations(this);
        employeeOps.open();
        employees = employeeOps.getAllEmployees();
        employeeOps.close();
            listAdapter=new ListAdapter(this,employees);
            listView.setAdapter(listAdapter);

            adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            // sorting spinner array alphabetically
        Collections.sort(arrayList, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }

        });
        adapter.notifyDataSetChanged();

            /*
            ArrayAdapter<Employee> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, employees);
            listView.setAdapter(adapter);
*/
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });

        sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinner.getSelectedItemPosition() == 4) {
                        AlphabeticSort(employees, listAdapter);
                    }

                    if (spinner.getSelectedItemPosition() == 2) {
                        SortById(employees, listAdapter);
                    }
                    if (spinner.getSelectedItemPosition() == 3) {
                        SortByDate(employees, listAdapter);
                    }
                    if(spinner.getSelectedItemPosition() == 0){

                    }
                }

            });

            search_ed.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void afterTextChanged(Editable str) {
                    filter(str.toString());
                }
            });
        }

        private void filter(String s){
        List<Employee> filteredList = new ArrayList<>();
        //employees.clear();
        for(Employee emp : employees){
            if (emp.getFirstname().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(emp);
            }

        }
        employees = filteredList;
        listAdapter.notifyDataSetChanged();
        }
        /*
        private void filterList(List<Employee> filteredList){
        employees = filteredList;
        notify();
        }
        */

        // Sorting based on Name (Alphabetically)
        private void AlphabeticSort(List<Employee> list,ListAdapter adapter){
          Collections.sort(list, new Comparator<Employee>() {
              @Override
              public int compare(Employee employee1, Employee employee2) {
                  return (employee1.getFirstname()).compareTo(employee2.getFirstname());
              }
          });
          listAdapter.notifyDataSetChanged();
        }

    // Sorting based on Id
    private void SortById(List<Employee> list,ListAdapter adapter){
        Collections.sort(list, new Comparator<Employee>() {
            @Override
            public int compare(Employee employee1, Employee employee2) {
                return String.valueOf(employee1.getEmpId()).compareTo(String.valueOf(employee2.getEmpId()));
            }
        });
        listAdapter.notifyDataSetChanged();
    }

    // Sorting based on HireDate
    private void SortByDate(List<Employee> list,ListAdapter adapter){
        Collections.sort(list, new Comparator<Employee>() {
            @Override
            public int compare(Employee employee1, Employee employee2) {
                return employee1.getHiredate().compareTo(employee2.getHiredate());
            }
        });
        listAdapter.notifyDataSetChanged();
    }



        private void showMessage(String message){
        Toast.makeText(this,message+"",Toast.LENGTH_SHORT).show();
        }

    }
