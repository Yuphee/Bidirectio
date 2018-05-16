package com.zhangyf.bidirectio.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.zhangyf.bidirectio.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhangyf
 * @version 创建时间：2016-3-23 晚上9:51:00
 */
public class MLoadingDialog extends Dialog {
    private static HashMap<Context, MLoadingDialog> dialogs = new HashMap<Context, MLoadingDialog>();

    public synchronized static MLoadingDialog obtainLoadingDialog(Context context) {
        MLoadingDialog dialog = dialogs.get(context);
        if (null == dialog) {
            dialog = new MLoadingDialog(context);
            dialogs.put(context, dialog);
        }
        return dialog;
    }

    public static void showLoadingDialog(Context context) {
        showLoadingDialog(context, "正在加载");
    }

    public static void showLoadingDialog(Context context, String content) {
        if (null == content || content.equals("")) {
            showLoadingDialog(context);
            return;
        }
        MLoadingDialog dialog = MLoadingDialog.obtainLoadingDialog(context);
        dialog.getContent().setText(content);
        dialog.show();

    }

    public static void cancelLoadingDialog(Context context) {
        try {
            MLoadingDialog.obtainLoadingDialog(context).cancel();
            dialogs.remove(context);
        } catch (IllegalArgumentException e) {
        }
    }

    public static void cancelAllLoadingDialog() {
        try {
            Set<Context> set = dialogs.keySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                MLoadingDialog.obtainLoadingDialog((Context) iter.next()).cancel();
                iter.remove();
            }
            dialogs.clear();
        } catch (IllegalArgumentException ex) {
        }

    }

    private TextView content;

    private MLoadingDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);
//        setOwnerActivity((Activity) context);// 设置dialog全屏显示

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.15f; // 设置进度条周边暗度（0.0f ~ 1.0f）
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        this.setCanceledOnTouchOutside(false);
//        this.setContentView(R.layout.loading_dialog);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mloading_dialog, null);
        content = (TextView) view.findViewById(R.id.dialog_content_tv);
        this.setContentView(view);
    }


    private MLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public TextView getContent() {
        return content;
    }

    public void setContent(TextView content) {
        this.content = content;
    }
}
