package co.newsmusik.newsmusik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import android.text.Html;

public class ContentDetail extends Activity {

    ImageButton imageButton;
    private static final String TAG_PICTURE = "extra_fields_search";
    private static final String TAG_INTROTEXT = "introtext";
    private static final String TAG_IMAGECREDITS = "image_credits";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_selection);
        Intent in = getIntent();
        String responsivePic = "<style>img{display: inline; height: auto; max-width: 100%;}</style>";

        String picture = in.getStringExtra(TAG_PICTURE);
        String detailContent = in.getStringExtra(TAG_INTROTEXT);
        String imageCredits = in.getStringExtra(TAG_IMAGECREDITS);

        WebView lbldetailContent = (WebView) findViewById(R.id.detailContent);
        lbldetailContent.loadData(detailContent + responsivePic, "text/html; charset=UTF-8;", null);

        ImageView pic = (ImageView) findViewById(R.id.imageContent);

        Glide
                .with(this)
                .load(picture)
                .asBitmap()
                .into(pic);

        TextView imgCredits = (TextView) findViewById(R.id.imageCredit);
        imgCredits.setText(imageCredits);
        imgCredits.setTypeface(null, Typeface.ITALIC);

        imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

