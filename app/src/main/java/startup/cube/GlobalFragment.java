package startup.cube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class GlobalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup GlobalProfileView = (ViewGroup)inflater.inflate(R.layout.activity_global_profile, container, false);
        return GlobalProfileView;
    }
}
