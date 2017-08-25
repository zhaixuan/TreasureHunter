package com.zhuoxin.treasurehunter.treasurehunter.user.register;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;
import com.zhuoxin.treasurehunter.treasurehunter.commons.RegexUtils;
import com.zhuoxin.treasurehunter.treasurehunter.custom.AlertDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dionysus on 2017/8/25.
 */

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.et_Confirm)
    EditText mEtConfirm;
    @BindView(R.id.btn_Register)
    Button mBtnRegister;
    private ActivityUtils mActivityUtils;
    private String comfirm;
    private String passWord;
    private String userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.register);
        }
        //给输入框添加监听
        mEtUsername.addTextChangedListener(mWatcher);
        mEtPassword.addTextChangedListener(mWatcher);
        mEtConfirm.addTextChangedListener(mWatcher);
    }

    private TextWatcher mWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            userName = mEtUsername.getText().toString().trim();
            passWord = mEtPassword.getText().toString().trim();
            comfirm = mEtConfirm.getText().toString().trim();

            boolean canClick = !TextUtils.isEmpty(userName)
                    &&!TextUtils.isEmpty(passWord)
                    &&!TextUtils.isEmpty(comfirm);
            mBtnRegister.setEnabled(canClick);
        }
    };

    @Override//设置箭头返回按钮
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_Register)
    public void onViewClicked() {
        if (RegexUtils.verifyUsername(userName) != RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment mFragment = AlertDialogFragment.getInstance(getString(R.string.username_error), getString(R.string.username_rules));
            mFragment.show(getSupportFragmentManager(),"username");
            return;
        }
        if (RegexUtils.verifyPassword(passWord) != RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment mFragment = AlertDialogFragment.getInstance(getString(R.string.password_error), getString(R.string.password_rules));
            mFragment.show(getSupportFragmentManager(),"password");
            return;
        }
        if (!TextUtils.equals(passWord,comfirm)){
            AlertDialogFragment mFragment = AlertDialogFragment.getInstance("注册失败", "两次输入的密码不一致！");
            mFragment.show(getSupportFragmentManager(),"comfirm");
            return;
        }
        // TODO: 2017/8/25
        mActivityUtils.showToast("注册成功");
    }
}
