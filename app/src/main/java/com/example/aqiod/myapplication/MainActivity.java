package com.example.aqiod.myapplication;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button button_login;
    private Button button_reg;

    private TextView yanZheng;
    private EditText idenField;
    private Button button_reg_reg;

    private Button selectFile;

    private static final int REQUESTCODE_FROM_ACTIVITY = 1000;

    private EditText username;
    private EditText password;
    private EditText email;
    private Button back;

    private void initReg() {
        yanZheng = (TextView) findViewById(R.id.yanZheng);
        idenField = (EditText) findViewById(R.id.identityField);
        button_reg_reg = (Button) findViewById(R.id.reg_btn_reg);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                initMain();
            }
        });
    }

    private void setReg() {
        setContentView(R.layout.register);
        initReg();
        yanZheng.setText(Utils.IdentifyCode());
        yanZheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yanZheng.setText(Utils.IdentifyCode());
            }
        });

        button_reg_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ident = idenField.getText().toString();
                String identValue = yanZheng.getText().toString();
                if (ident.equals(identValue)) {
                    Toast.makeText(MainActivity.this, "验证码正确！", Toast.LENGTH_LONG).show();
                    String user = username.getText().toString();
                    String pass = password.getText().toString();
                    String mail = email.getText().toString();
                    StringBuilder url = new StringBuilder();
                    url.append("http://addfile.sevendegree.date:8080/user/register.do?username=");
                    url.append(user);
                    url.append("&password=");
                    url.append(pass);
                    if (mail != null && !mail.equals("") && !mail.equals(" ")) {
                        url.append("&email=");
                        url.append(mail);
                    }
                    CallUrl callUrl = new CallUrl(url.toStrigir
                    callUrl.start();
                    try {
                        callUrl.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String json = callUrl.getResult();
                    if (Utils.getStatus(json) == 0) {
                        Toast.makeText(MainActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                        setContentView(R.layout.activity_main);
                    } else {
                        Toast.makeText(MainActivity.this, json, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "验证码错误！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 设置选取文件的逻辑，分成一个函数是为了代码和思路更清晰
     * @param button
     */
    private void selectFile(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), REQUESTCODE_FROM_ACTIVITY);
                intent.setType("image/*");
            }
        });
    }

    private File file = null;

    private TextView urlText;

    private Button jump;

    private EditText userField;
    private EditText passField;
    

    private class CallUrl extends Thread {
        private String result = null;

        private String url;

        public CallUrl(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            result = URLGetUtil.accessUrl(url);
        }

        public String getResult() {
            return result;
        }
    }


    /**
     * 提交login的get请求，请求地址硬编码了不太好，这里只用一次就图方便了
     * 还有login是get的方式也是图方便，把后端的代码从post改成了post和get都可以的形式
     */
    private boolean canLogin() {
        String username = userField.getText().toString();
        String password = passField.getText().toString();

        final StringBuilder url = new StringBuilder();
        url.append("http://addfile.sevendegree.date:8080/user/login.do?username=");
        url.append(username);
        url.append("&password=");
        url.append(password);
        CallUrl t = new CallUrl(url.toString());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Utils.getStatus(t.getResult()) == 0;
    }

    private void setLogin() {
        setContentView(R.layout.login);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                initMain();
            }
        });
        selectFile = (Button) findViewById(R.id.select_file);
        selectFile(selectFile);
    }

    private void initMain() {
        userField = (EditText) findViewById(R.id.userField);
        passField = (EditText) findViewById(R.id.passField);
        button_login = (Button) findViewById(R.id.login);
        button_reg = (Button) findViewById(R.id.register);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canLogin())
                    setLogin();
                else {
                    Toast.makeText(MainActivity.this, "用户名不存在或密码错误！", Toast.LENGTH_LONG).show();
                }
            }
        });
        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReg();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,    Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    121);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMain();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 121) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) return;
        }
        Toast.makeText(MainActivity.this, "获取权限失败，请手动赋予存储权限！", Toast.LENGTH_LONG).show();
    }
    /**
     **得到选择的文件后的回调，处理上传和网址显示和跳转
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_FROM_ACTIVITY) {

            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String string = uri.toString();
            String a[];
            //判断文件是否在sd卡中
            if (string.contains(String.valueOf(Environment.getExternalStorageDirectory()))) {
                //对Uri进行切割
                a = string.split(String.valueOf(Environment.getExternalStorageDirectory()));
                //获取到file
                file = new File(Environment.getExternalStorageDirectory(), a[1]);
            } else if (string.contains(String.valueOf(Environment.getDataDirectory()))) { //判断文件是否在手机内存中
                //对Uri进行切割
                a = string.split(String.valueOf(Environment.getDataDirectory()));
                //获取到file
                file = new File(Environment.getDataDirectory(), a[1]);
            } else {
                //出现其他没有考虑到的情况
                Toast.makeText(this, "文件路径解析失败！", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            //得到文件并上传
            final StringBuilder result = new StringBuilder();

//            Toast.makeText(this,"file null?" + (file == null), Toast.LENGTH_SHORT).show();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String res = UploadUtil.uploadFile(file, "http://addfile.sevendegree.date:8080/file/upload.do");
                    result.append(res);
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, UploadUtil.s, Toast.LENGTH_SHORT).show();
            final ResponseObject responseObject = Utils.parseJson(result.toString());
            if (responseObject != null && responseObject.getCode() == 0) {
                urlText = (TextView) findViewById(R.id.urlText);
                urlText.setText(responseObject.getUrl());
                urlText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(urlText.getText());
                        Toast.makeText(MainActivity.this, "网址已复制", Toast.LENGTH_SHORT).show();
                    }
                });
                jump = (Button) findViewById(R.id.jump);
                jump.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(responseObject.getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
