package com.nix.dimablyznyuk.student.contacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

/**
 * Created by Dima Blyznyuk on 15.07.15.
 */
public class GetImageTask extends AsyncTask<String, Void, Drawable> {

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
    protected Drawable doInBackground(String... params) {

        Drawable d = null;
        if (params[0] != null) {

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(params[0].trim());
//
//            float width = options.outWidth;
//            float height = options.outHeight;
//
//            int inSampleSize = 1;
//            if (height > HEIGTH || width > WIDTH) {
//                if (width > height) {
//                    inSampleSize = Math.round(height / HEIGTH);
//                } else {
//                    inSampleSize = Math.round(width / WIDTH);
//                }
//            }
//            options = new BitmapFactory.Options();
//            options.inSampleSize = inSampleSize;

            Bitmap bitmap = BitmapFactory.decodeFile(params[0], null);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGTH, true);
            d = bitmapToDrawable(bitmap);
        } else {
            d = context.getResources().getDrawable(R.drawable.ic_no_photo);
        }
        return d;
    }

    protected void onPostExecute(Drawable v) {
        progDialog.dismiss();
    }

    public Drawable bitmapToDrawable(Bitmap icon) {
        Drawable d = new BitmapDrawable(icon);
        return d;
    }

}