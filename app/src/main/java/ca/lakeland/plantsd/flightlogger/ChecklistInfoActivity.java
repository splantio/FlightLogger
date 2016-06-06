package ca.lakeland.plantsd.flightlogger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ChecklistInfoActivity extends AppCompatActivity {

    String day;
    String authorName;
    Bitmap sig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_info);

        try {
            DoneChecklist dc = (DoneChecklist) getIntent().getSerializableExtra("DONE_CHECKLIST");
            day = dc.getDate();
            authorName = dc.getAuthor();

            TextView txtTitle = (TextView) findViewById(R.id.txtChkInfoTitle);
            TextView txtAuthor = (TextView) findViewById(R.id.txtChkInfoAuthor);

            txtTitle.setText("Checklist for " + day);
            txtAuthor.setText(authorName);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading checklist data", Toast.LENGTH_SHORT).show();
            finish();
        }



        // get bitmap signature image
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File signatureImg = new File(path, day + authorName + ".png");

            if (signatureImg.exists()) {
                sig = BitmapFactory.decodeFile(signatureImg.getAbsolutePath());
                ImageView imgSignature = (ImageView) findViewById(R.id.imgChkInfoSignature);
                imgSignature.setImageBitmap(sig);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}