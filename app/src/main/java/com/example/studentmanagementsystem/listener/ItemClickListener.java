/*
ItemClickListener class for clicking the items of the recycler view
 */
package com.example.studentmanagementsystem.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

public class ItemClickListener implements RecyclerView.OnItemTouchListener {

    private ClickListener clickListener;

    public ItemClickListener(Context context,final RecyclerView recyclerView,final ClickListener clickListener){
        this.clickListener=clickListener;
    }
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){


        View child=recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
        if(child!=null&& clickListener!=null){
            clickListener.onClick(child,recyclerView.getChildAdapterPosition(child));
        }}
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    //interface clickListener for clicking the item
    public interface ClickListener{
        void onClick(View view,int position);
    }
}
