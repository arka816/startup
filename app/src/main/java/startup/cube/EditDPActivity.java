package startup.cube;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static startup.cube.WebService.updateDP;

import java.io.IOException;

public class EditDPActivity extends AppCompatActivity {
    int lastMaskTouchX = 0, lastMaskTouchY = 0;
    String moveMaskDir = "";
    int maskSize;
    float imageWidth = 0;
    float imageHeight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dp);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.statusBarColorSecondary));

        final ImageView editImageView = findViewById(R.id.editImageView);
        final View editMask = findViewById(R.id.editMask);
        final ImageView editOkay = findViewById(R.id.editDPTick);
        final String imageURI = getIntent().getExtras().getString("imageURI");
        final String phone = getIntent().getExtras().getString("phone");

        final View ltcp = findViewById(R.id.ltcp);
        final View rtcp = findViewById(R.id.rtcp);
        final View lbcp = findViewById(R.id.lbcp);
        final View rbcp = findViewById(R.id.rbcp);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int densityDPI = metrics.densityDpi;
        final int screenHeight = metrics.heightPixels - densityDPI /2;
        final int screenWidth = metrics.widthPixels;

        ViewGroup.LayoutParams maskParams = editMask.getLayoutParams();


        float imageAR;
        float screenAR = ((float) screenWidth)/ ((float) screenHeight);

        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imageURI));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bm != null){
            imageAR = ((float) bm.getWidth())/ ((float) bm.getHeight());
            if(imageAR > screenAR){
                moveMaskDir = "horizontal";
                imageWidth = screenWidth;
                imageHeight = screenWidth/imageAR;
                maskParams.height = (int)(imageHeight * 420 /densityDPI);
                maskParams.width = (int) (imageHeight * 420 /densityDPI);
                maskSize = (int) imageHeight;
            }
            else{
                moveMaskDir = "vertical";
                imageHeight = screenHeight;
                imageWidth = screenHeight * imageAR;
                maskParams.height = (int)(imageWidth * 420 /densityDPI);
                maskParams.width = (int) (imageWidth * 420 /densityDPI);
                maskSize = (int) imageWidth;
            }
        }
        final Bitmap original = Bitmap.createScaledBitmap(bm, ((int) imageWidth * 420 /densityDPI), ((int) imageHeight * 420 /densityDPI), false);
        editImageView.setImageBitmap(original);

        final int rawImageWidth = (int) (imageWidth * 420 /densityDPI);
        final int rawImageHeight = (int) (imageHeight * 420 /densityDPI);


        editMask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                maskSize = editMask.getWidth();
                System.out.println("Mask Touch Triggered in direction " + moveMaskDir);
                float dx, dy;
                switch(event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        lastMaskTouchX = (int)event.getRawX();
                        lastMaskTouchY = (int)event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        dx = event.getRawX() - lastMaskTouchX;
                        dy = event.getRawY() - lastMaskTouchY;
                        lastMaskTouchX = (int) event.getRawX();
                        lastMaskTouchY = (int) event.getRawY();
                        if(moveMaskDir.equals("horizontal")){
                            float val = editMask.getX() + dx;
                            if(val >= 0 && val <= (editImageView.getX() + rawImageWidth - maskSize)) editMask.setX(val);
                        }
                        else if(moveMaskDir.equals("vertical")){
                            float val = editMask.getY() + dy;
                            System.out.println(val);
                            if(val >= 0 && val <= (editImageView.getY() + rawImageHeight - maskSize)) editMask.setY(val);
                        }
                        break;
                }
                return true;
            }
        });

        editOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap finalDP = Bitmap.createBitmap(original, (int)(editMask.getX() - editImageView.getX()), (int)(editMask.getY() - editImageView.getY()), maskSize, maskSize);
                editImageView.setImageBitmap(finalDP);
                editMask.setVisibility(View.INVISIBLE);

                updateDP(finalDP, getApplicationContext(), phone);
            }
        });
    }
}
