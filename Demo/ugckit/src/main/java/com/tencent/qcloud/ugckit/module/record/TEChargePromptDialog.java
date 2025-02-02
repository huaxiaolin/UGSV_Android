package com.tencent.qcloud.ugckit.module.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.NoCopySpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.tencent.qcloud.ugckit.R;
import com.tencent.qcloud.ugckit.UGCKit;
import com.tencent.qcloud.ugckit.component.dialog.CommonDialog;
import com.tencent.qcloud.ugckit.utils.SharedPreferenceUtils;

/**
 * 用于显示高级美颜收费提示框
 */
public class TEChargePromptDialog {

    private static final String SH_NAME = "TEChargePromptDialog";
    private static final String SH_DISPLAY_PROHIBITED_KEY = "SH_DISPLAY_PROHIBITED_KEY";
    private static int startPosition = 5;
    private static int endPosition = 12;
    private static int en_StartPosition = 39;
    private static int en_EndPosition = 58;
    private static final String WEB_URL = "https://cloud.tencent.com/product/x-magic";

    public static void showTETipDialog(final Activity activity) {
        if (isProhibited()) {
            return;
        }
        SpannableString spannableString = new SpannableString(
                activity.getString(R.string.ugckit_tencent_effect_tip_dialog_content));
        spannableString.setSpan(new NoRefCopyClickableSpan(), isEnglish() ? en_StartPosition : startPosition,
                isEnglish() ? en_EndPosition : endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        new CommonDialog(activity).setCancelable(true)
                .setTitle(activity.getString(R.string.ugckit_tencent_effect_tip_dialog_title))
                .setContent(spannableString)
                .setBtnTxt(activity.getString(R.string.ugckit_tencent_effect_tip_dialog_lefttxt),
                        activity.getString(R.string.ugckit_tencent_effect_tip_dialog_righttxt))
                .setOnClickListener(new CommonDialog.OnCommonDialogBtnClickListener() {
                    @Override
                    public void onLeftBtnClick() {
                        prohibitDisplay();
                    }

                    @Override
                    public void onRightBtnClick() {

                    }
                })
                .show();
    }

    public static void showTEConfirmDialog(final Activity activity) {
        SpannableString spannableString = new SpannableString(
                activity.getString(R.string.ugckit_tencent_effect_tip_dialog_content));
        spannableString.setSpan(new NoRefCopyClickableSpan(), isEnglish() ? en_StartPosition : startPosition,
                isEnglish() ? en_EndPosition : endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        new CommonDialog(activity).setCancelable(true)
                .setTitle(activity.getString(R.string.ugckit_tencent_effect_tip_dialog_title))
                .setContent(spannableString)
                .setBtnTxt(null,
                        activity.getString(R.string.ugckit_tencent_effect_tip_dialog_righttxt))
                .show();


    }


    public static boolean isProhibited() {
        return (Boolean) new SharedPreferenceUtils(UGCKit.getAppContext(), SH_NAME)
                .getSharedPreference(SH_DISPLAY_PROHIBITED_KEY, false);
    }

    public static void prohibitDisplay() {
        new SharedPreferenceUtils(UGCKit.getAppContext(), SH_NAME)
                .put(SH_DISPLAY_PROHIBITED_KEY, true);
    }


    public static class NoRefCopyClickableSpan extends ClickableSpan implements NoCopySpan {

        @Override
        public void onClick(@NonNull View widget) {
            actionToWebViewActivity(widget.getContext(), null, WEB_URL);
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setColor(Color.parseColor("#FF006EFD"));
            textPaint.setUnderlineText(false);
        }
    }

    private static void actionToWebViewActivity(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setAction("com.tencent.liteav.action.webview");
        intent.setPackage(context.getPackageName());
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    private static boolean isEnglish() {
        return UGCKit.getAppContext().getResources().getConfiguration().locale.getCountry().toLowerCase().equals("us");
    }
}
