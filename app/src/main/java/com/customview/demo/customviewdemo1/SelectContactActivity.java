package com.customview.demo.customviewdemo1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectContactActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mName;
    private EditText mMobile;
    private Button   mSelectContact;
    private Button   mSelect_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        initView();
    }

    private void initView() {
        mName = (EditText) findViewById(R.id.et_name);
        mMobile = (EditText) findViewById(R.id.et_mobile);
        mSelectContact = (Button) findViewById(R.id.btn_select);
        mSelect_all = (Button) findViewById(R.id.btn_select_all);
        mSelectContact.setOnClickListener(this);
        mSelect_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select:
                checkPermisson();

                break;
            case R.id.btn_select_all:
                ArrayList<HashMap<String, String>> list = selectAllContacts();
                JSONArray jsonArray = new JSONArray();
                try {
                    for (HashMap<String, String> map : list) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", entry.getKey());
                            jsonObject.put("mobileNo", entry.getValue());
                            jsonArray.put(jsonObject);
                        }
                    }

                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("linkmans", jsonArray);
                    Log.d("xxx", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void checkPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},10086);
        }else {
            selectContacts();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10086:
                if(grantResults.length>0){
                    selectContacts();
                }
                break;
            default:
                break;
        }
    }

    private ArrayList<HashMap<String, String>> selectAllContacts() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取id和name字段
        String[] projection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        //创建集合存储联系人和电话号码
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            //首先判断当前联系人是否有手机号码，如果有就获取并存储
            HashMap<String, String> map = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            //获取联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //获取数据库id
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取手机号码
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            String number = null;
            if (phoneCursor != null) {
                if (phoneCursor.moveToFirst()) {
                    number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    sb.append(number).append(",");
                }
            }
            phoneCursor.close();
            map.put(name, number);
            list.add(map);
        }
        cursor.close();
        return list;
    }

    private void selectContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                getContacts(data);
                break;
            default:
                break;
        }
    }

    private void getContacts(Intent data) {
        if (data == null) {
            return;
        }
        Uri contactData = data.getData();
        if (contactData == null) {
            return;
        }

        //姓名和电话
        String name = null;
        String number = "111";
        boolean hasPhone = false;
        //查询联系人
        Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if ("1".equalsIgnoreCase(hasNumber)) {
                hasPhone = true;
            }
            //如果此用户有存了手机号
            if (hasPhone) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
                //多个号码取第一个
                if (phones.moveToLast()) {
                    number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d("xxx", "number==" + number);
                }
                phones.close();
            }
            cursor.close();
            mName.setText(name);
            mMobile.setText(number);
        }
    }
}
