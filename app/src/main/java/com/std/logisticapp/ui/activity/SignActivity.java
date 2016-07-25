package com.std.logisticapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.orhanobut.logger.Logger;
import com.shiki.imgpicker.FGOGallery;
import com.shiki.imgpicker.FGOGalleryUtil;
import com.shiki.imgpicker.FunctionConfig;
import com.shiki.imgpicker.domain.PhotoInfo;
import com.shiki.okttp.OkHttpUtils;
import com.shiki.okttp.callback.SimpleCallback;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.logistic.LogisticApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Maik on 2016/5/27.
 */
public class SignActivity extends BaseActivity {
    private static final String INSTANCE_STATE_PARAM_ORDER = "org.shiki.STATE_PARAM_ORDER_ID";

    @Bind(R.id.iv_card_front)
    SimpleDraweeView ivFront;
    @Bind(R.id.iv_card_back)
    SimpleDraweeView ivBack;
    @Bind(R.id.sign_name)
    SignaturePad signaturePad;
    @Bind(R.id.cb_pass)
    CheckBox cbPass;

    private PhotoInfo frontPhoto;
    private PhotoInfo backPhoto;
    private File signFile;
    private OrderBean mOrder;

    public static Intent getCallingIntent(Context context, OrderBean order) {
        Intent callingIntent = new Intent(context, SignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY, order);
        callingIntent.putExtras(bundle);
        return callingIntent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putParcelable(INSTANCE_STATE_PARAM_ORDER, this.mOrder);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.mOrder = getIntent().getParcelableExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY);
        } else {
            this.mOrder = savedInstanceState.getParcelable(INSTANCE_STATE_PARAM_ORDER);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.iv_card_front)
    public void selectFront() {
        FunctionConfig pickConfig = new FunctionConfig();
        pickConfig.setMutiSelect(false);
        FGOGallery.openGallery(this, pickConfig, new FGOGallery.OnHanlderResultCallback() {

            @Override
            public void onHanlderSuccess(List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    frontPhoto = resultList.get(0);
                    FGOGalleryUtil.displayImage(SignActivity.this, frontPhoto.getPhotoPath(), ivFront, null, 100, 100);
                }

            }

            @Override
            public void onHanlderFailure(String errorMsg) {
                Toast.makeText(SignActivity.this, R.string.photo_list_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.iv_card_back)
    public void selectBack() {
        FunctionConfig pickConfig = new FunctionConfig();
        pickConfig.setMutiSelect(false);
        FGOGallery.openGallery(this, pickConfig, new FGOGallery.OnHanlderResultCallback() {

            @Override
            public void onHanlderSuccess(List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    backPhoto = resultList.get(0);
                    FGOGalleryUtil.displayImage(SignActivity.this, backPhoto.getPhotoPath(), ivBack, null, 100, 100);
                }

            }

            @Override
            public void onHanlderFailure(String errorMsg) {
                Toast.makeText(SignActivity.this, R.string.photo_list_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign:
                saveSignAndSubmit();
                break;
            case R.id.action_clear:
                signaturePad.clear();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSignAndSubmit() {
        if (!cbPass.isChecked()) {
            Toast.makeText(SignActivity.this, getString(R.string.no_validate), Toast.LENGTH_SHORT).show();
            return;
        }
        if (frontPhoto == null || backPhoto == null) {
            Toast.makeText(SignActivity.this, getString(R.string.no_select_img), Toast.LENGTH_SHORT).show();
            return;
        }
        if (signaturePad.isEmpty()) {
            Toast.makeText(SignActivity.this, getString(R.string.no_sign), Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
        signFile = addJpgSignatureToGallery(signatureBitmap);
        if (signFile == null) {
            Toast.makeText(SignActivity.this, getString(R.string.sign_save_error), Toast.LENGTH_SHORT).show();
        } else {
            signSubmit();
        }
    }

    private void signSubmit() {
        OkHttpUtils.post().addParams("isPass",cbPass.isChecked()?"1":"0")
                .addFile("mFiles", mOrder.getDeliveryCode() + "_1.jpg", new File(frontPhoto.getPhotoPath()))
                .addFile("mFiles", mOrder.getDeliveryCode() + "_2.jpg", new File(backPhoto.getPhotoPath()))
                .addFile("mFiles", mOrder.getDeliveryCode() + "_3.jpg", signFile).url(LogisticApi.SIGN_URL + mOrder.getOrdId()).build().execute(new SimpleCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(SignActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultBean response) {
                Toast.makeText(SignActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                if (response.getStatusCode().equals(LogisticApi.SUCCESS_DATA)) {
                    if (signFile.exists())
                        signFile.delete();
                    Intent outcallingIntent = new Intent();
                    outcallingIntent.putExtra(LogisticApi.INTENT_EXTRA_PARAM_DELIVERY, mOrder);
                    setResult(LogisticApi.RESULT_OK, outcallingIntent);
                    finish();
                }
            }
        });
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Logger.e("Directory not created");
        }
        return file;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        SignActivity.this.sendBroadcast(mediaScanIntent);
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private File addJpgSignatureToGallery(Bitmap signature) {
        File photo;
        try {
            photo = new File(getAlbumStorageDir("Signature"), String.format("Signature_%d.jpg", mOrder.getDeliveryCode()));
            Logger.d(photo.getAbsolutePath());
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
        } catch (IOException e) {
            photo = null;
            e.printStackTrace();
        }
        return photo;
    }
}
