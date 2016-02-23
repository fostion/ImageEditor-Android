package com.xinlan.imageeditlibrary.editimage.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.model.StickerBean;
import com.xinlan.imageeditlibrary.editimage.utils.Matrix3;
import com.xinlan.imageeditlibrary.editimage.view.LabelTextView;
import com.xinlan.imageeditlibrary.editimage.view.StickerItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 贴图分类fragment
 *
 * @author panyi
 */
public class TextFragment extends Fragment {
    public static final String TAG = TextFragment.class.getName();
    public static final String STICKER_FOLDER = "stickers";

    private View mainView;
    private EditImageActivity activity;
    private LabelTextView mLableTextView;// 贴图显示控件

    private Button btn;

    private LoadStickersTask mLoadStickersTask;
    private List<StickerBean> stickerBeanList = new ArrayList<StickerBean>();

    public static TextFragment newInstance(EditImageActivity activity) {
        TextFragment fragment = new TextFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_text,
                null);
        this.mLableTextView = activity.mTextPanel;

        btn = (Button) mainView.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStickerItem("stickers/dongwu/1dongwu1.png.svntmp");
            }
        });
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //导入贴图数据
    private void loadStickersData() {
        if (mLoadStickersTask != null) {
            mLoadStickersTask.cancel(true);
        }
        mLoadStickersTask = new LoadStickersTask();
        mLoadStickersTask.execute(1);
    }


    /**
     * 导入贴图数据
     */
    private final class LoadStickersTask extends AsyncTask<Integer, Void, Void> {
        private Dialog loadDialog;

        public LoadStickersTask() {
            super();
            loadDialog = BaseActivity.getLoadingDialog(getActivity(), "正在载入贴图数据...", false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            stickerBeanList.clear();
            AssetManager assetManager = getActivity().getAssets();
            try {
                String[] lists = assetManager.list(STICKER_FOLDER);
                for (String parentPath : lists) {

                }//end for each
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadDialog.dismiss();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadDialog.dismiss();
        }
    }//end inner class

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadStickersTask != null) {
            mLoadStickersTask.cancel(true);
        }
    }

    /**
     * 从Assert文件夹中读取位图数据
     *
     * @return
     */
    private Bitmap drawToBitmap(String text) {

        TextView tempTextView = new TextView(activity);
        tempTextView.setText("哈哈这是测试");
        tempTextView.setBackgroundResource(R.drawable.icon_biaoqian);
        tempTextView.setTextSize(10f);
        tempTextView.setTextColor(Color.WHITE);
        tempTextView.setGravity(Gravity.CENTER_VERTICAL);
        tempTextView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        if (tempTextView.getWidth() == 0 || tempTextView.getHeight() == 0) {//若是布局没有显示出来，先自己计算长宽
            int measuredWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int measuredHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            // validate view.measurewidth and view.measureheight
            tempTextView.measure(measuredWidth, measuredHeight);
            tempTextView.layout(0, 0, tempTextView.getMeasuredWidth(), tempTextView.getMeasuredHeight());
        }
        tempTextView.setDrawingCacheEnabled(true);
        Bitmap bitmap = tempTextView.getDrawingCache();

        if (bitmap == null) return null;
        final Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

        try {
            //截图将图片截取出来
            cv.drawBitmap(bitmap, null, dst, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //清除
            tempTextView.setDrawingCacheEnabled(false);
        }
        return newBitmap;
    }

    public LabelTextView getmLableTextView() {
        return mLableTextView;
    }

    public void setmLableTextView(LabelTextView mLableTextView) {
        this.mLableTextView = mLableTextView;
    }

    /**
     * 选择贴图加入到页面中
     *
     * @param path
     */
    public void selectedStickerItem(String path) {
        mLableTextView.addBitImage(drawToBitmap("123456"));
    }

    /**
     * 返回主菜单页面
     *
     * @author panyi
     */
    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end inner class

    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(0);
        mLableTextView.setVisibility(View.GONE);
        activity.bannerFlipper.showPrevious();
    }

    /**
     * 保存贴图任务
     * @author panyi
     */
    private final class SaveTextTask extends
            AsyncTask<Bitmap, Void, Bitmap> {
        private Dialog dialog;

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            // System.out.println("保存贴图!");
            Matrix touchMatrix = activity.mainImage.getImageViewMatrix();

            Bitmap resultBit = Bitmap.createBitmap(params[0]).copy(
                    Bitmap.Config.RGB_565, true);
            Canvas canvas = new Canvas(resultBit);

            float[] data = new float[9];
            touchMatrix.getValues(data);// 底部图片变化记录矩阵原始数据
            Matrix3 cal = new Matrix3(data);// 辅助矩阵计算类
            Matrix3 inverseMatrix = cal.inverseMatrix();// 计算逆矩阵
            Matrix m = new Matrix();
            m.setValues(inverseMatrix.getValues());

            LinkedHashMap<Integer, StickerItem> addItems = mLableTextView.getBank();
            for (Integer id : addItems.keySet()) {
                StickerItem item = addItems.get(id);
                item.matrix.postConcat(m);// 乘以底部图片变化矩阵
                canvas.drawBitmap(item.bitmap, item.matrix, null);
            }// end for
            saveBitmap(resultBit, activity.saveFilePath);
            return resultBit;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mLableTextView.clear();
            activity.changeMainBitmap(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = BaseActivity.getLoadingDialog(getActivity(), "图片合成保存中...",
                    false);
            dialog.show();
        }
    }// end inner class

    /**
     * 保存贴图层 合成一张图片
     */
    public void saveTextSticker() {
        // System.out.println("保存 合成图片");
        SaveTextTask task = new SaveTextTask();
        task.execute(activity.mainBitmap);
    }

    /**
     * 保存Bitmap图片到指定文件
     *
     * @param bm
     */
    public static void saveBitmap(Bitmap bm, String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println("保存文件--->" + f.getAbsolutePath());
    }

}// end class
