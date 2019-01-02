package com.filters;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Helper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.SeekBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.filters.utility.TransformImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

import java.util.ResourceBundle;

public class ControlActivity extends AppCompatActivity {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    Toolbar mControlToolbar2;
    ImageView mTickImageView;
    ImageView mCenterImageView;
    TransformImage mTransformImage;

    int mCurrentFilter;

    Uri mSelectedImageUri;

    SeekBar mSeekbar;
    ImageView cancelImageView;

    int mScreenWidth;
    int mScreenHeight;

    Target mApplySingleFilter =new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            int currentFilterValue = mSeekbar.getProgress();

            if(mCurrentFilter == TransformImage.FILTER_BRIGHTNESS) {
                mTransformImage.applyBrightnessSubFilter(currentFilterValue);

                com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS),mTransformImage.getBitmap(TransformImage.FILTER_BRIGHTNESS));

                Picasso.with(ControlActivity.this).invalidate(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS)));
                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).resize(0,mScreenHeight/2).into(mCenterImageView);

            } else if(mCurrentFilter == TransformImage.FILTER_CONTRAST) {
                mTransformImage.applyContrastSubFilter(currentFilterValue);

                com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST),mTransformImage.getBitmap(TransformImage.FILTER_CONTRAST));

                Picasso.with(ControlActivity.this).invalidate(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST)));
                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).resize(0,mScreenHeight/2).into(mCenterImageView);

            } else if(mCurrentFilter == TransformImage.FILTER_VIGNETTE) {
                mTransformImage.applyVignetteSubFilter(currentFilterValue);

                com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE),mTransformImage.getBitmap(TransformImage.FILTER_VIGNETTE));

                Picasso.with(ControlActivity.this).invalidate(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE)));
                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).resize(0,mScreenHeight/2).into(mCenterImageView);

            } else if(mCurrentFilter == TransformImage.FILTER_SATURATION) {
                mTransformImage.applySaturationSubFilter(currentFilterValue);

                com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION),mTransformImage.getBitmap(TransformImage.FILTER_SATURATION));

                Picasso.with(ControlActivity.this).invalidate(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION)));
                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).resize(0,mScreenHeight/2).into(mCenterImageView);

            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    Target msmallTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

             mTransformImage = new TransformImage(ControlActivity.this,bitmap);
            mTransformImage.applyBrightnessSubFilter(TransformImage.DEFAULT_BRIGHTNESS);


            com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS),mTransformImage.getBitmap(TransformImage.FILTER_BRIGHTNESS));
            Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).fit().centerInside().into(mFirstFilterPreviewImageView);

            //

            mTransformImage.applySaturationSubFilter(TransformImage.DEFAULT_SATURATION);

            com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION),mTransformImage.getBitmap(TransformImage.FILTER_SATURATION));
            Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).fit().centerInside().into(mSecondFilterPreviewImageView);
            //
            mTransformImage.applyVignetteSubFilter(TransformImage.DEFAULT_VIGNETTE);

            com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE),mTransformImage.getBitmap(TransformImage.FILTER_VIGNETTE));
            Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).fit().centerInside().into(mThirdFilterPreviewImageView);

            //
            mTransformImage.applyContrastSubFilter(TransformImage.DEFAULT_CONTRAST);

            com.filters.utility.Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST),mTransformImage.getBitmap(TransformImage.FILTER_CONTRAST));
            Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).fit().centerInside().into(mFourthFilterPreviewImageView);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    final static int PICK_IMAGE = 2;
    final static int MY_PERMISSION_REQUEST_STORAGE_PERMISSION = 3;

    ImageView mFirstFilterPreviewImageView;
    ImageView mSecondFilterPreviewImageView;
    ImageView mThirdFilterPreviewImageView;
    ImageView mFourthFilterPreviewImageView;

    private static final String TAG =ControlActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        mControlToolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        mSeekbar = (SeekBar) findViewById(R.id.seekBar2);

        mControlToolbar2.setTitle(getString(R.string.app_name));
        mControlToolbar2.setNavigationIcon(R.drawable.icon);
        mControlToolbar2.setTitleTextColor(getResources().getColor(R.color.colorwhite));

        mFirstFilterPreviewImageView = (ImageView) findViewById(R.id.imageView11);
        mSecondFilterPreviewImageView = (ImageView) findViewById(R.id.imageView12);
        mThirdFilterPreviewImageView = (ImageView) findViewById(R.id.imageView13);
        mFourthFilterPreviewImageView = (ImageView) findViewById(R.id.imageView14);

        mTickImageView = (ImageView) findViewById(R.id.imageView10);
        mTickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this, ImagePreviewActivity.class);
                startActivity(intent);
            }
        });

        mCenterImageView = (ImageView) findViewById(R.id.centerImageView);
        mCenterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();

                if (ContextCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        mFirstFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSeekbar.setMax(TransformImage.MAX_BRIGHTNESS);
                mSeekbar.setProgress(TransformImage.DEFAULT_BRIGHTNESS);

                mCurrentFilter = TransformImage.FILTER_BRIGHTNESS;

                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        mSecondFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSeekbar.setMax(TransformImage.MAX_SATURATION);
                mSeekbar.setProgress(TransformImage.DEFAULT_SATURATION);

                mCurrentFilter = TransformImage.FILTER_SATURATION;

                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).resize(0,mScreenHeight/2).into(mCenterImageView);

            }
        });

        mThirdFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSeekbar.setMax(TransformImage.MAX_VIGNETTE);
                mSeekbar.setProgress(TransformImage.DEFAULT_VIGNETTE);

                mCurrentFilter = TransformImage.FILTER_VIGNETTE;

                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).resize(0,mScreenHeight/2).into(mCenterImageView);

            }
        });

        mFourthFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSeekbar.setMax(TransformImage.MAX_CONTRAST);
                mSeekbar.setProgress(TransformImage.DEFAULT_CONTRAST);

                mCurrentFilter = TransformImage.FILTER_CONTRAST;

                Picasso.with(ControlActivity.this).load(com.filters.utility.Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).resize(0,mScreenHeight/2).into(mCenterImageView);
                
            }
        });

        mTickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(ControlActivity.this).load(mSelectedImageUri).into(mApplySingleFilter);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

    }
    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
                    new MaterialDialog.Builder(ControlActivity.this).title("Permission Granted")
                            .content("Thank you for providing storage permission")
                            .positiveText("Ok").canceledOnTouchOutside(true).show();
                } else {
                    Log.d(TAG,"Permission denied!");
                }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            mSelectedImageUri = data.getData();

            Picasso.with(ControlActivity.this).load(mSelectedImageUri).fit().centerInside().into(mCenterImageView);

            Picasso.with(ControlActivity.this).load(mSelectedImageUri).into(msmallTarget);

        }
    }

    public void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new MaterialDialog.Builder(ControlActivity.this).title(R.string.permission_title)
                        .content(R.string.permission_content)
                        .negativeText(R.string.permission_cancel)
                        .positiveText(R.string.permission_agree_settings)
                        .canceledOnTouchOutside(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),0);
                            }
                        })

                        .show();
            } else {
                ActivityCompat.requestPermissions(ControlActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE_PERMISSION);
            }
            return;
        }

    }

}
