package startup.cube;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class LocalFragment extends Fragment {

    private static final int galleryRequestCode = 0;
    private ViewGroup LocalProfileView = null;
    private String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LocalProfileView = (ViewGroup)inflater.inflate(R.layout.activity_local_profile, container, false);
        ImageView changeDPLocal = LocalProfileView.findViewById(R.id.changeDPLocal);
        ImageView localDPView = LocalProfileView.findViewById(R.id.localProfileDPView);

        phone = "8420595895";

        changeDPLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, galleryRequestCode);
            }
        });
        return LocalProfileView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case galleryRequestCode:
                if(null != data){
                    String imageURI = data.getDataString();
                    System.out.println(imageURI);
                    Intent editDPIntent = new Intent(LocalProfileView.getContext(), EditDPActivity.class);
                    Bundle editDPBundle = new Bundle();
                    editDPBundle.putString("phone", phone);
                    editDPBundle.putString("imageURI", imageURI);
                    editDPIntent.putExtras(editDPBundle);
                    startActivity(editDPIntent);
                }
                break;
            default: break;
        }
    }
}
