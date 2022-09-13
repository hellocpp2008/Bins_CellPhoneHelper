package com.bins.biffhelps.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bins.biffhelps.R;
import com.bins.biffhelps.adapter.ContactAdapter;
import com.bins.biffhelps.model.ContactModel;
import com.bins.biffhelps.service.ContactManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // 使用ToolBar控件替代ActionBar控件，需要在java代码中使用setSupportActionBar()方法
        setSupportActionBar(toolbar);

        // ===DrawerLayout、NavigationView===
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 先使用 setDisplayHomeAsUpEnabled() 方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);// 然后调用 setHomeAsUpIndicator() 方法来设置一个导航按钮图标
        }
        NavigationView navView = findViewById(R.id.nav_view);// 获取 NavigationView 滑动布局
        navView.setCheckedItem(R.id.nav_friends);// 默认选中项
        // 设置一个菜单项选中事件的监听器，当用户点击了任意菜单项时，就会回调到onNavigationItemSelected() 方法中。
        navView.setNavigationItemSelectedListener(item -> {
            Log.i("MainActivity.onCreate(..)", item.getItemId() + "");
            // TODO 需要写的逻辑
            if (item.getTitle().equals("通讯录备份")) {
                List<ContactModel> contactModels = ContactManager.readContacts(this);
                // Toast.makeText(this, GsonUtil.obj2Json(contactModels), Toast.LENGTH_LONG).show();
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
                recyclerView.setLayoutManager(layoutManager);
                ContactAdapter adapter = new ContactAdapter(contactModels);
                recyclerView.setAdapter(adapter);
            }
            mDrawerLayout.closeDrawers();// 关闭当前打开的抽屉视图（即滑动控件），即点击一下滑动窗口里的按钮，滑动窗口就隐藏掉。
            return true;
        });
        // 刷新
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);// 调用setColorSchemeResources() 方法来设置下拉刷新进度条的颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
            }
        });

        // ====悬浮按钮FloatingActionButton====
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // 接着这里又调用了一个setAction()方法来设置一个动作，从而让Snackbar不仅仅是一个提示，而是可以和用户进行交互的。
            Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo", v -> Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show())
                    .show();
        });
    }

    /**
     * 在 onCreateOptionsMenu() 方法中加载了toolbar.xml这个菜单文件，然后在 onOptionsItemSelected() 方法中处理各个按钮的点击事件。
     *有【发-
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * 处理各个按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

}