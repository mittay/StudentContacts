package com.nix.dimablyznyuk.student.contacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.File;

/**
 * Created by Dima Blyznyuk on 15.07.15.
 */
public class GetImageTask extends AsyncTask<String, Void, Bitmap> {

    private ProgressDialog progDialog = null;
    private Context context;
    private static final int WIDTH = 1000;
    private static final int HEIGTH = 1000;

    public GetImageTask(Context activityContext) {
        context = activityContext;
    }


    protected void onPreExecute() {
        progDialog = new ProgressDialog(context);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... imagePath) {
        Bitmap bitmap = null;
        Drawable d = null;
        if (!imagePath[0].equals(ContactEditAddActivity.DEFAULT_IMAGE)
                && new File(imagePath[0]).exists()) {

            bitmap = BitmapFactory.decodeFile(imagePath[0], null);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGTH, true);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_no_photo);
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap v) {
        progDialog.dismiss();
    }
}