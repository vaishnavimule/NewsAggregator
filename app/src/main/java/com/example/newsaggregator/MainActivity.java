package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private StoryAdapter storyAdapter;
    private ViewPager2 viewPager;

    private String selectedCategory;
    private Source selectedSource;

    private ArrayList<Story> stories = new ArrayList<>();
    private List<Source> filteredSources = new ArrayList<>();
    private ArrayList<Source> sources = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> colors = new ArrayList<>();

    private HashMap<String, ArrayList<Source>> sourceData = new HashMap<>();
    private ArrayList<Source> sourceList = new ArrayList<>();


    protected Map<String, String> colorCategories = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);
        drawerList.setOnItemClickListener((parent, view, position, id) -> topicSelected(position));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer,R.string.close_drawer);
        storyAdapter = new StoryAdapter(this, stories);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(storyAdapter);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            // Drawer logic
            if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            ArrayList<Source> clist = sourceData.get(item.getTitle().toString());
            sources.clear();

            sourceData.put("All",sourceList);

            if (clist != null) {
                sources.addAll(clist);
            }

            if(clist!=null)
                setTitle(item.getTitle().toString()+ " (" + clist.size() + ")");
            else
                setTitle(item.getTitle().toString());

        filterSources();
        return super.onOptionsItemSelected(item);
    }

    private void filterSources() {
        filteredSources = sources.stream()
                .filter(s -> selectedCategory == null || s.getCategory().equals(selectedCategory))
                .collect(Collectors.toList());

        if (filteredSources.size() == 0) {
            showNoSourcesDialog();
        }
        drawerList.setAdapter(new SourceAdapter(this, R.layout.drawer_list_item,
                filteredSources.toArray(new Source[0])));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void showNoSourcesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No sources available");
        builder.setMessage(
                "No sources exist matching the specified Topic, Language and/or Country");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updatingData(ArrayList<Source> listIn){
        this.sources = listIn;

        for (Source s : listIn) {
            if (!sourceData.containsKey(s.getCategory())) {
                sourceData.put(s.getCategory(), new ArrayList<>());
            }
            ArrayList<Source> slist = sourceData.get(s.getCategory());
            if (slist != null) {
                slist.add(s);
            }
        }
            sourceData.put("All", listIn);

            ArrayList<String> tempList = new ArrayList<>(sourceData.keySet());
            Collections.sort(tempList);
            for (String str : tempList)
                menu.add(str);

            sourceList.addAll(listIn);
            if(sourceList.size()> 0)
                setTitle("News Gateway"+ " (" + sourceList.size() + ") ");
            else
                setTitle("News Gateway");

            drawerList.setAdapter(new SourceAdapter(this, R.layout.drawer_list_item,
                    sourceList.toArray(new Source[0])));

            filteredSources = sources.stream()
                .filter(s -> selectedCategory == null || s.getCategory().equals(selectedCategory))
                .collect(Collectors.toList());

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
    }

    private void mapCategoriesAndColors() {
        for (int i = 0; i < this.categories.size(); i++) {
            String color;
            if (colors != null && (colors.size()) > i) {
                color = colors.get(i);
            } else {
                color = "#000000";  // Default color is black
            }
            colorCategories.put(this.categories.get(i), color);
        }
    }

    public void loadData(){
        if (hasNetworkConnection(this)) {
            NewsSourceVolley.downloadSource(this);

        } else {
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasNetworkConnection(Activity activity) {
        ConnectivityManager connectivityManager =
                activity.getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void fetchingArticlesSuccess(ArrayList<Story> articles) {
        this.stories.clear();
        this.stories.addAll(articles);
        storyAdapter.notifyDataSetChanged();

        if (this.stories.isEmpty()) {
            Toast.makeText(this, String.format(Locale.getDefault(), getString(R.string.no_articles),
                    selectedSource.getName()), Toast.LENGTH_SHORT).show();
            return;
        }
        viewPager.setBackground(null);
        viewPager.setCurrentItem(0);

        setTitle(selectedSource.getName());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void topicSelected(int position) {
        selectedSource = filteredSources.get(position);
        if (this.selectedSource != null) {
            NewsArticleVolley.downloadStory(this,selectedSource.getId());
            drawerLayout.closeDrawer(drawerList);
        }
    }

    public void receiveColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public void updatingSourcesFailed() {
        Toast.makeText(this, getString(R.string.error_sources), Toast.LENGTH_SHORT).show();
    }

    public void fetchingArticlesFailed() {
        Toast.makeText(this, getString(R.string.error_articles), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(getString(R.string.selected_category), selectedCategory);

        outState.putStringArrayList(getString(R.string.categories), categories);
        outState.putParcelableArrayList(getString(R.string.sources), sources);

        outState.putParcelable(getString(R.string.selected_source), selectedSource);
        outState.putParcelableArrayList(getString(R.string.articles), stories);
        outState.putInt(getString(R.string.current_article), viewPager.getCurrentItem());

        outState.putStringArrayList(getString(R.string.colors), colors);

        super.onSaveInstanceState(outState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedCategory = savedInstanceState.getString(getString(R.string.selected_category));

        categories = savedInstanceState.getStringArrayList(getString(R.string.categories));
        sources = savedInstanceState.getParcelableArrayList(getString(R.string.sources));

        colors = savedInstanceState.getStringArrayList(getString(R.string.colors));
        mapCategoriesAndColors();

        stories.addAll(savedInstanceState.getParcelableArrayList(getString(R.string.articles)));

        if (!stories.isEmpty()) {
            storyAdapter.notifyDataSetChanged();
            viewPager.setBackground(null);
            viewPager
                    .setCurrentItem(savedInstanceState.getInt(getString(R.string.current_article)));
        }
        selectedSource = savedInstanceState.getParcelable(getString(R.string.selected_source));

        filterSources();

        if (selectedSource != null) {
            setTitle(selectedSource.getName());
        }

    }
}