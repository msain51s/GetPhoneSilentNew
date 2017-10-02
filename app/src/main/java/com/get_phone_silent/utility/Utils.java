package com.get_phone_silent.utility;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 9/26/2017.
 */

public class Utils {

    public static void showValidationPopup(View view,String message){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static void showCommonAlertDialog(Context context,String title,String message,int type){
            SweetAlertDialog dialog=new SweetAlertDialog(context,type);
            dialog.setTitleText(title);
            dialog.setContentText(message);
            dialog.setCancelable(false);

                dialog.setConfirmText("Ok");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });

            dialog.show();
        }
}
