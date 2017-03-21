package sjing.cmu.edu.project4android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.util.Base64;

public class MainActivity extends AppCompatActivity {

    public static final String URL =
            "http://10.0.2.2:8080/Project4Task1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity ma = this;
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                String searchTerm = ((EditText) findViewById(R.id.search)).getText().toString();
                GetCourse getCourse = new GetCourse();
                getCourse.search(searchTerm, ma);
            }
        });

    }

    /**
     * @param encodedString the encodedString
     * referenced from http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     */
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void pictureReady(String[] result) {
        Log.d("TAG", "pictureReady: ");
        TextView courseName = (TextView) findViewById(R.id.course_name);
        TextView courseDes = (TextView) findViewById(R.id.description);
        ImageView pictureView = (ImageView) findViewById(R.id.coursePicture);
        TextView searchView = (EditText) findViewById(R.id.search);

        if (result != null) {
            //set text result[0]=name to courseName
            courseName.setText(result[0].toString());
            //set text result[2]=description to courseDes
            courseDes.setText(result[2].toString());
            //set text result[1]=bitmap to pictureView
            pictureView.setImageBitmap(StringToBitMap(result[1]));
            searchView.setText("");
            pictureView.invalidate();

            //if there is no result
        } else {
            //set courseName "No result"
            courseName.setText("No result");
            searchView.setText("");
            pictureView.invalidate();
        }
    }
}


