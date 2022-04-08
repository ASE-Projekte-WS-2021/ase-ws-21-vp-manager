package com.example.vpmanager.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.EditSwipeableDatesAdapter;


//Creates a Callback for the given drag and swipe allowance. These values serve as defaults
public class EditSwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {


    private EditSwipeableDatesAdapter mAdapter;
    private Drawable icon;
    private final ColorDrawable background;


    public EditSwipeToDeleteCallback(EditSwipeableDatesAdapter adapter) {
        super(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        mAdapter = adapter;
        icon = ContextCompat.getDrawable(mAdapter.mContext, R.drawable.ic_baseline_delete_24);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        int bound1 = 110;
        int bound2 = itemView.getWidth() - 110;
        int backgroundCornerOffset = 1;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();


        if (dX > 0) { // Swiping to the right

            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());


            background.draw(c);
            if (itemView.getLeft() + ((int) dX) + backgroundCornerOffset > bound1) {
                icon.draw(c);
            }

        } else if (dX < 0) { // Swiping to the left

            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

            background.draw(c);
            if (itemView.getRight() + ((int) dX) - backgroundCornerOffset < bound2) {
                icon.draw(c);
            }

        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
            icon.setBounds(0, 0, 0, 0);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
