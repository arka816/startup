package startup.cube;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CreateProfileActivity extends FragmentActivity {

    private ViewPager profilePager;
    private PagerAdapter profilePagerAdapter;
    private static final int NUM_PAGES = 2;
    private LocalFragment localFragment = new LocalFragment();
    private GlobalFragment globalFragment = new GlobalFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_slide);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.statusBarColor));

        profilePager = findViewById(R.id.profilePager);
        profilePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        profilePager.setAdapter(profilePagerAdapter);
    }

    @Override
    public void onBackPressed(){
        if(profilePager.getCurrentItem() == 0){
            super.onBackPressed();
        }
        else {
            profilePager.setCurrentItem(profilePager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0) return localFragment;
            return globalFragment;
        }
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

