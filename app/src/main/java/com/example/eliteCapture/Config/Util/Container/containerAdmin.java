package com.example.eliteCapture.Config.Util.Container;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class containerAdmin {

    Context c;

    public containerAdmin(Context c) {
        this.c = c;
    }

    public LinearLayout containerV(View v[]){
        LinearLayout c = container();
        c.setOrientation(LinearLayout.VERTICAL);

        if(v.length == 2){
            c.addView(v[0]);
            c.addView(v[1]);
        }else if(v.length == 1){
            c.addView(v[0]);
        }
        return c;
    }

    public LinearLayout container(){
        LinearLayout.LayoutParams lcontainer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lcontainer.setMargins(10, 10, 10, 5);

        LinearLayout container = new LinearLayout(c);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(lcontainer);

        return container;
    }

    public LinearLayout.LayoutParams params(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public View scrollv(LinearLayout l){
        LinearLayout.LayoutParams lscv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ScrollView scv = new ScrollView(c);
        scv.setLayoutParams(lscv);
        scv.addView(l);

        return scv;
    }
}