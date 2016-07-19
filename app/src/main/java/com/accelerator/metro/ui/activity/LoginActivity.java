package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.accelerator.metro.Config;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.User;
import com.accelerator.metro.contract.LoginContract;
import com.accelerator.metro.presenter.LoginPresenter;
import com.accelerator.metro.utils.CipherUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseDialogActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_account_inputLayout)
    TextInputLayout accountLayout;
    @Bind(R.id.login_password_inputLayout)
    TextInputLayout passwordLayout;
    @Bind(R.id.login_btn)
    Button btnLogin;
    @Bind(R.id.login_tv_register)
    TextView tvRegister;

    private LoginPresenter presenter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.login);
        setSupportActionBar(toolbar);
        presenter = new LoginPresenter(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences spf=getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String userName=spf.getString(Config.USER_NAME,"");
        EditText account = accountLayout.getEditText();

        if (account != null) {
            account.setText(userName);
            account.setSelection(userName.length());
        }

    }

    @OnClick(R.id.login_tv_register)
    public void tvRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @OnClick(R.id.login_btn)
    public void loginClick(View view) {
        //登录按钮监听

        EditText account = accountLayout.getEditText();
        EditText password = passwordLayout.getEditText();

        userName = account != null ? account.getText().toString().trim() : null;
        String userPassword = password != null ? password.getText().toString() : null;


        if (!checkNotNull(userName)) {
            Snackbar.make(view, R.string.login_not_empty_account, Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        Pattern p = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
        Matcher m = p.matcher(userName);

        if (!m.find()) {
            Snackbar.make(view, R.string.login_not_phone, Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!checkNotNull(userPassword)) {
            Snackbar.make(view, R.string.login_not_empty_password, Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        setDialogMsg(R.string.login_start);
        dialog.show();

        String newPwd=CipherUtil.base64Encode(userName,userPassword);

        presenter.login(userName, newPwd);

    }

    private boolean checkNotNull(String str) {
        return !TextUtils.isEmpty(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_login_forgot_password:
                Toast.makeText(this, "正在开发...", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSucceed(User values) {
        Log.e(TAG, values.toString());

        User.ElseInfoBean info=values.getElse_info();

        SharedPreferences spf=getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=spf.edit();

        editor.putString(Config.USER_NAME,userName);
        editor.putString(Config.USER_ID,info.getUser_id());
        editor.putString(Config.USER_SESSION,info.getSession_id());

        editor.apply();

        finish();

    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        dialog.dismiss();
    }

    @Override
    public void onCompleted() {
        dialog.dismiss();
    }
}
